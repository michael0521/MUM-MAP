package com.yangxiaoming.following;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


/**
 * Created by yangxiaoming on 12/7/2015.
 */
public class DrawView extends View {

    private Bitmap Maharishi;
    private Bitmap rose;
    private float currentX = 40;
    private float currentY = 50;
    private int roseX;
    private int roseY;

    private final int maharishiWidth = 100;
    private final int maharishiHeight = 125;
    private final int roseWidth = 25;
    private final int roseHeight = 50;

    private boolean newRose = true;


    private int score = 0;

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

   // public DrawView(Context context, AttributeSet set) {
     //   super(context, set);
   // }

    public DrawView(Context context) {
        super(context);

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(newRose == true){
            roseX = new Random().nextInt(getWidth()-maharishiWidth*2) + maharishiWidth;
            roseY = new Random().nextInt(getHeight()-maharishiHeight*2) + maharishiHeight;
        }

        Maharishi = BitmapFactory.decodeResource(getResources(), R.drawable.maharishi_mahesh_yogi);
        rose = BitmapFactory.decodeResource(getResources(), R.drawable.rose);
        Maharishi = Bitmap.createScaledBitmap(Maharishi, maharishiWidth, maharishiHeight, false);
        rose = Bitmap.createScaledBitmap(rose, roseWidth, roseHeight, false);
        canvas.drawBitmap(Maharishi, currentX, currentY, null);

        canvas.drawBitmap(rose, roseX, roseY, null);
        if(currentX < roseX && currentX + maharishiWidth > roseX + roseWidth
                &&
                currentY < roseY && currentY + maharishiHeight > roseY + roseHeight){
                score ++;
            newRose = true;
        }else {
            newRose = false;
        }
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setTextSize(40);
        canvas.drawText("Your score is: " + Integer.toString(score), 20, 40, p);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.currentX = event.getX();
        this.currentY = event.getY();
        this.invalidate();
        return true;

    }


}
