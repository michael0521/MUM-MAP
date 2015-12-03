package com.yangxiaoming.yogaflying;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;


public class MainActivity extends AppCompatActivity {

    private GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //gets rid of the title at the top of the app
        super.onCreate(savedInstanceState);
        //FrameLayout frameLayout = new FrameLayout(this);

        gv = new GameView(this);
        //frameLayout.addView(gv);
        setContentView(gv);

        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gv.setRacketX((int) (event.getX()));

                gv.invalidate();

                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gv.killThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gv.onDestroy();
    }
}
