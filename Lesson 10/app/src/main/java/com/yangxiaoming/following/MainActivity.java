package com.yangxiaoming.following;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawView(this);
        setContentView(dv);


    }

    @Override
    protected void onPause(){
        SharedPreferences prefs = getSharedPreferences("Following", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int tempscore = dv.getScore();
        Log.d("OnPause", "onPause: " + Integer.toString(tempscore));
        editor.putInt("RoseAmount", tempscore);

        editor.commit();

        super.onPause();

    }

    @Override
    protected void onResume(){
        SharedPreferences prefs = getSharedPreferences("Following", MODE_PRIVATE);
        int retrievedHighScore = prefs.getInt("RoseAmount", 0);

        dv.setScore(retrievedHighScore);
        Log.d("OnResume", "onResume: " + Integer.toString(retrievedHighScore));
        super.onResume();
    }
}
