package com.tatv.baseapp.utils.ui;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchUtils {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private long lastTime = 0;


    @SuppressLint("ClickableViewAccessibility")
    public void setOnTouchListener(View view, View rootView, WindowManager windowManager, WindowManager.LayoutParams wmParams, OnTouchListener listener){
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTime = System.currentTimeMillis();
                    //remember the initial position.
                    initialX = wmParams.x;
                    initialY = wmParams.y;
                    //get the touch location
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    if(wmParams.x < initialX + 30 && wmParams.x > initialX - 30 && wmParams.y < initialY + 30 && wmParams.y > initialY - 30){
                        if (System.currentTimeMillis() - lastTime > 1000){
                            //on longclick
                            if(listener != null) listener.onLongTouch();
                        }else {
                            //on click
                            if(listener != null) listener.onTouch();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    //Calculate the X and Y coordinates of the view.
                    int px = initialX + (int) (event.getRawX() - initialTouchX);
                    int py = initialY + (int) (event.getRawY() - initialTouchY);

                    wmParams.x = px;
                    wmParams.y = py;
                    if(listener != null) listener.onMove(wmParams.x, wmParams.y);
                    //Update the layout with new X & Y coordinate
                    windowManager.updateViewLayout(rootView, wmParams);
                    return true;
            }
            return false;
        });
    }



    public interface OnTouchListener{
        void onTouch();
        void onLongTouch();
        void onMove(int dx,int dy);
    }
}
