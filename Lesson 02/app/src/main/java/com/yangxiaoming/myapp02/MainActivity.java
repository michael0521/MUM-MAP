package com.yangxiaoming.myapp02;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        layout.setLayoutParams(params);



        TextView textView1 = new TextView(this);
        textView1.setBackgroundColor(Color.YELLOW);
        textView1.setText("My First App");
        textView1.setTextSize(20);
        textView1.setTextColor(Color.BLUE);
        textView1.setGravity(Gravity.CENTER);



        TextView textView2 = new TextView(this);
        textView2.setBackgroundColor(Color.YELLOW);
        textView2.setText("Welcome to my first super cool App!");
        textView2.setTextSize(20);
        textView2.setTextColor(Color.BLUE);
        textView2.setHeight(100);
        textView2.setGravity(Gravity.LEFT);


        TextView textView3 = new TextView(this);
        textView3.setBackgroundColor(Color.YELLOW);
        textView3.setText("Bye!");
        textView3.setTextSize(20);
        textView3.setTextColor(Color.BLUE);
        textView3.setGravity(Gravity.RIGHT);




        textView1.setLayoutParams(params);
        textView2.setLayoutParams(params);
        textView3.setLayoutParams(params);


        layout.addView(textView1);
        layout.addView(textView2);
        layout.addView(textView3);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        /*
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
