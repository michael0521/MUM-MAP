package com.yangxiaoming.yogaflying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


/**
 * Created by yangxiaoming on 12/2/2015.
 */
public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private Bitmap Maharishi;
    private Bitmap sky;
    private Bitmap gameover;
    private GameThread gthread = null;

    private MediaPlayer music;

    private int ySpeed = 10;
    Random rand = new Random();
    private double xyRate = rand.nextDouble() - 0.5;
    private double xSpeed = (int) (ySpeed * xyRate * 2);
    // horizontal position (graphic is 205 pixels wide thus initialize right edge of graphic fall to left screen edge)
    private int maharishiX = rand.nextInt(200) + 10;
    private int maharishiY = rand.nextInt(10) + 10; // vertical position

    private final int maharishiWidth = 100;
    private final int maharishiHeight = 125;

    private int racketY;
    private int racketX = rand.nextInt(200);
    private final int racketHeight = 20;
    private final int racketWidth = 150;

    private boolean isLose = false;

    public void setRacketX(int racketX) {
        this.racketX = racketX;
    }

    public boolean isLose() {
        return isLose;
    }

    public GameView(Context context) {
        super(context);
        music = MediaPlayer.create(getContext(), R.raw.die);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Maharishi = BitmapFactory.decodeResource(getResources(), R.drawable.maharishi_mahesh_yogi);
                Maharishi = Bitmap.createScaledBitmap(Maharishi, maharishiWidth, maharishiHeight, false);
                sky = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
                gameover = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
                makeThread();

                gthread.setRunning(true);
                gthread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        racketY = getHeight() - maharishiHeight;
        //canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawBitmap(sky, 0, 0, paint);
        canvas.drawRect(racketX, racketY, racketX + racketWidth, racketY + racketHeight, paint);

        if (maharishiX <= 0 || maharishiX >= getWidth() - maharishiWidth) {
            xSpeed = -xSpeed;
        }
        if (maharishiY >= racketY + racketHeight + maharishiHeight && (maharishiX < racketX || maharishiX > racketX + racketWidth)) {
            isLose = true;
            /*no idea why not work
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(40);

            canvas.drawBitmap(gameover, 200, 200, paint);
            */
            music.start();
            killThread();

        }
        if (maharishiY <= 0
                ||
                (maharishiX > racketX && maharishiX <= racketX + racketWidth && maharishiY >= racketY - maharishiHeight)
                ||
                maharishiX + maharishiWidth > racketX && maharishiX + maharishiWidth < racketX + racketWidth && maharishiY + maharishiHeight == racketY) {

            ySpeed = -ySpeed;
        }

        maharishiX += xSpeed;
        maharishiY += ySpeed;


        canvas.drawBitmap(Maharishi, maharishiX, maharishiY, null);

    }

    public void makeThread() {
        gthread = new GameThread(this);

    }

    public void killThread() {
        boolean retry = true;
        gthread.setRunning(false);
        while (retry) {
            try {
                gthread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void onDestroy() {
        Maharishi.recycle();
        music.release();
        Maharishi = null;
        System.gc();
    }
}