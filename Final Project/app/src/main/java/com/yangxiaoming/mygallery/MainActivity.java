package com.yangxiaoming.mygallery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class MainActivity extends Activity {

    private Button cameraBtn;
    private Button browseBtn;
    private Button sendBtn;
    private ImageView myImageView01;
    private ProgressBar progressBar;
    private int status = 0;
    private Uri uri;

    private static String SERVER_IP = "";
    private static int PORT = 21;   //default port

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0x112) {
                progressBar.setProgress(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SERVER_IP = getIntent().getStringExtra("address");
        myImageView01 = (ImageView) findViewById(R.id.myImageView01);
        progressBar = (ProgressBar) findViewById(R.id.sendingProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        cameraBtn = (Button) findViewById(R.id.camera);
        browseBtn = (Button) findViewById(R.id.browse);
        sendBtn = (Button) findViewById(R.id.send);


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                changeProgress(0);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        browseBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                changeProgress(0);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        changeProgress(0);
                        while (status < 90) {
                            try {
                                changeProgress(status++);
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();

                new Connection().execute(uri);
                Toast.makeText(getApplicationContext(), "The picture has been sent to your PC!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void changeProgress(int s) {
        status = s;
        handler.sendEmptyMessage(0x112);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            Log.d("uri is ", uri.toString());
            ContentResolver cr = this.getContentResolver();
            InputStream is = null;
            try {
                is = cr.openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            myImageView01.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class Connection extends AsyncTask<Uri, Object, Object> {

        @Override
        protected Object doInBackground(Uri... params) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getApplicationContext().getContentResolver().query(params[0],
                    proj, // Which columns to return
                    null, // WHERE clause; which rows to return (all rows)
                    null, // WHERE clause selection arguments (none)
                    null); // Order-by clause (ascending by name)
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            String filePath = cursor.getString(column_index);
            Log.d("picture from", filePath);
            startFTP(filePath);
            return null;
        }

        public boolean startFTP(String filePath) {

            try {
                String userId = "";
                String password = "";
                String remoteDirectory = "D:/test";

                //new ftp client
                FTPClient ftp = new FTPClient();
                //try to connect
                ftp.connect(SERVER_IP);
                //login to server
                if (!ftp.login(userId, password)) {
                    ftp.logout();
                    return false;
                }
                int reply = ftp.getReplyCode();
                //FTPReply stores a set of constants for FTP reply codes.
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return false;
                }

                //enter passive mode
                ftp.enterLocalPassiveMode();
                //get system name
                Log.d("Remote system is ", ftp.getSystemType());
                //change current directory, but may not work, depends on server implementation
                ftp.changeWorkingDirectory(remoteDirectory);
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                Log.d("Current directory is ", ftp.printWorkingDirectory());

                //get input stream
                InputStream fis;
                //fis = new FileInputStream(localDirectory + "/" + fileToFTP);
                fis = new FileInputStream(filePath);
                //store the file in the remote server
                ftp.storeFile(new File(filePath).getName(), fis);
                //close the stream
                fis.close();

                ftp.logout();
                ftp.disconnect();

            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            changeProgress(100);

            return true;
        }
    }
}