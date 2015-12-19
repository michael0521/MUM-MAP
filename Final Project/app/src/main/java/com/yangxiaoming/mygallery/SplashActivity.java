package com.yangxiaoming.mygallery;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by yangxiaoming on 12/17/2015.
 */
public class SplashActivity extends RootActivity{

    private String address;
    private int PORT = 0;
    private ProgressBar progressBar;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0x111){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button nextBtn = (Button)findViewById(R.id.next);
        Button resetBtn = (Button)findViewById(R.id.reset);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        EditText editText = (EditText)findViewById(R.id.address);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView warningTextView = (TextView) findViewById(R.id.warning);
                warningTextView.setText("");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addrTextView = (EditText) findViewById(R.id.address);
                addrTextView.setText("");
                TextView warningTextView = (TextView) findViewById(R.id.warning);
                warningTextView.setText("");
                progressBar.setVisibility(View.INVISIBLE);

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText addrTextView = (EditText) findViewById(R.id.address);
                address = addrTextView.getText().toString();
                boolean valid = new IPAddressValidator().validate(address);

                progressBar.setVisibility(View.VISIBLE);

                if (valid == true) {
                    Log.d("IPAddress is valid : ", address);
                    Boolean isServerAlive = false;
                    try {
                        // get() can make code block here, that means AsychTask become unreal Async
                        isServerAlive = new TestConnection().execute(address).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(isServerAlive == true){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("address", address);
                        startActivity(intent);
                    }else {
                        TextView warningTextView = (TextView) findViewById(R.id.warning);
                        warningTextView.setText("The address is not reachable, please check again!");
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Log.d("IPAddress is invalid : ", address);
                    TextView warningTextView = (TextView) findViewById(R.id.warning);
                    warningTextView.setText("The IP format is incorrect, please check again!");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private class TestConnection extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            FTPClient ftp = new FTPClient();
            try {

                int reply;
                if (PORT > 0) {
                    ftp.connect(address, PORT);
                } else {
                    ftp.connect(address);

                }
                System.out.println("Connected to " + address + " on " + (PORT>0 ? PORT : ftp.getDefaultPort()));

                // After connection attempt, you should check the reply code to verify
                // success.
                reply = ftp.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)){
                    ftp.disconnect();
                    System.err.println("FTP server refused connection.");
                    return false;
                }else {
                    handler.sendEmptyMessage(0x111);
                    return true;

                }
            }
            catch (IOException e){
                if (ftp.isConnected()){
                    try{
                        ftp.disconnect();
                    }
                    catch (IOException f){
                        // do nothing
                    }
                }
                System.err.println("Could not connect to server.");
                e.printStackTrace();

            }
            return false;
        }
    }

}

class IPAddressValidator{

    private Pattern pattern;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public IPAddressValidator(){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    public boolean validate(final String ip){
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
