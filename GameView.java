package com.shimla.game;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceView;
public class GameView extends SurfaceView implements Runnable {
    Thread thread; boolean playing; Paint paint; int screenX, screenY;
    float scootyX, scootyY, speed = 8; int score=0; boolean isJump=false; float jumpV=0;
    Rect road; int[] obstacleX = {800, 1500, 2200}; int[] obstacleType={0,1,0};
    public GameView(Context c) {
        super(c);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenX = dm.widthPixels; screenY = dm.heightPixels;
        scootyX = 150; scootyY = screenY - 350;
        paint = new Paint(); road = new Rect(0, screenY-250, screenX, screenY);
    }
    public void run() {
        while(playing) { update(); draw(); sleep(); }
    }
    void update() {
        for(int i=0;i<obstacleX.length;i++){ obstacleX[i]-=speed; if(obstacleX[i]<-100){obstacleX[i]=screenX+ (int)(Math.random()*800); score+=10; obstacleType[i]=(int)(Math.random()*2);} }
        if(isJump){ scootyY+=jumpV; jumpV+=2; if(scootyY>=screenY-350){scootyY=screenY-350; isJump=false; jumpV=0;} }
        speed = 8 + score/100f;
    }
    void draw() {
        if(!getHolder().getSurface().isValid()) return;
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.rgb(135,206,235));
        paint.setColor(Color.rgb(34,139,34)); canvas.drawRect(0,screenY-400,screenX,screenY-250,paint);
        paint.setColor(Color.DKGRAY); canvas.drawRect(road, paint);
        paint.setColor(Color.WHITE); for(int i=0;i<screenX;i+=120) canvas.drawRect(i, screenY-135, i+60, screenY-125, paint);
        paint.setColor(Color.RED); for(int i=0;i<3;i++){ if(obstacleType[i]==0) canvas.drawRect(obstacleX[i], screenY-320, obstacleX[i]+60, screenY-250, paint); else {paint.setColor(Color.YELLOW); canvas.drawCircle(obstacleX[i]+30, screenY-280, 35, paint); paint.setColor(Color.RED);} }
        paint.setColor(Color.BLUE); canvas.drawRoundRect(scootyX, scootyY, scootyX+120, scootyY+80, 20,20, paint);
        paint.setColor(Color.BLACK); canvas.drawCircle(scootyX+25, scootyY+80, 18, paint); canvas.drawCircle(scootyX+95, scootyY+80, 18, paint);
        paint.setColor(Color.WHITE); paint.setTextSize(60); canvas.drawText("Score: "+score, 30, 80, paint);
        paint.setTextSize(35); canvas.drawText("Shimla Scooty Game", screenX/2-180, 80, paint);
        getHolder().unlockCanvasAndPost(canvas);
    }
    void sleep(){ try{Thread.sleep(17);}catch(Exception e){} }
    public boolean onTouchEvent(MotionEvent e){ if(e.getAction()==MotionEvent.ACTION_DOWN &&!isJump){ isJump=true; jumpV=-32; } return true; }
    public void pause(){ playing=false; try{thread.join();}catch(Exception e){} }
    public void resume(){ playing=true; thread=new Thread(this); thread.start(); }
    static class DisplayMetrics extends android.util.DisplayMetrics{}
}
