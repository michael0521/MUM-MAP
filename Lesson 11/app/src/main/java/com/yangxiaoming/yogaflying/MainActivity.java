package com.yangxiaoming.yogaflying;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private GameView gv;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //gets rid of the title at the top of the app
        super.onCreate(savedInstanceState);
/*
        AlertDialog.Builder alertDialogBuilder  = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        System.out.println("Yes in dialog");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

*/
        FrameLayout frameLayout = new FrameLayout(this);

        gv = new GameView(this);
        frameLayout.addView(gv);

        setContentView(frameLayout);

        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getY() > gv.getRacketY() - 100) {
                    gv.setRacketX((int) (event.getX()));
                }else{
                    gv.setTouchX((int) event.getX());
                    gv.setTouchY((int) event.getY());
                }

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
