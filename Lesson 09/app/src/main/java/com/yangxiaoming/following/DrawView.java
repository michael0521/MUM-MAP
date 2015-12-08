package com.yangxiaoming.following;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by yangxiaoming on 12/7/2015.
 */
public class DrawView extends View {

    private Bitmap Maharishi;
    private float currentX = 40;
    private float currentY = 50;

    private final int maharishiWidth = 100;
    private final int maharishiHeight = 125;

   // public DrawView(Context context, AttributeSet set) {
     //   super(context, set);
   // }

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Maharishi = BitmapFactory.decodeResource(getResources(), R.drawable.maharishi_mahesh_yogi);
        Maharishi = Bitmap.createScaledBitmap(Maharishi, maharishiWidth, maharishiHeight, false);
        canvas.drawBitmap(Maharishi, currentX, currentY, null);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.currentX = event.getX();
        this.currentY = event.getY();
        this.invalidate();
        return true;

    }


}
