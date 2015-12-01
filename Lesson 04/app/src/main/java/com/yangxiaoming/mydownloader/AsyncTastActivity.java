package com.yangxiaoming.mydownloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AsyncTastActivity extends Activity {

    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_tast);
        show = (TextView)findViewById(R.id.show);
    }

    public void download(View source)throws MalformedURLException{
        DownTask task = new DownTask(this);
        EditText editText = (EditText) findViewById(R.id.url);
        String url = editText.getText().toString();
        task.execute(new URL(url));
    }

    public void clear(View source)throws MalformedURLException{
        EditText editText = (EditText) findViewById(R.id.url);
        TextView textView = (TextView) findViewById(R.id.show);
        textView.setText("");
        editText.setText("");
    }

    class DownTask extends AsyncTask<URL, Integer, String>{
        ProgressDialog pdialog;
        int hasRead = 0;
        Context mContext;
        public DownTask(Context ctx){
            mContext = ctx;
        }

        @Override
        protected String doInBackground(URL... params) {
            StringBuilder sb = new StringBuilder();
            try{
                URLConnection conn = params[0].openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while((line = br.readLine()) != null){
                    sb.append(line + "\n");
                    hasRead++;
                    publishProgress(hasRead);
                }
                return sb.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result){
            show.setText(result);
            pdialog.dismiss();
        }

        @Override
        protected void onPreExecute(){
            pdialog = new ProgressDialog(mContext);
            pdialog.setTitle("Running...");
            pdialog.setMessage("Downloading...");
            pdialog.setCancelable(false);
            pdialog.setMax(202);
            pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pdialog.setIndeterminate(false);
            pdialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            show.setText(values[0] + " have been read!");
            pdialog.setProgress(values[0]);
        }
    }
}
