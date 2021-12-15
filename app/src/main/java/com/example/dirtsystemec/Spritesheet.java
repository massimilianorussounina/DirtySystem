package com.example.dirtsystemec;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Spritesheet {
    private Bitmap spriteSheet;
    private int spriteWidth, spriteHeight;
    private int[] start,delay;
    private final Rect src = new Rect();
    private final RectF dest = new RectF();
    private float screen_semi_width, screen_semi_height;
    private int numberOfAnimations;


    public Spritesheet(Bitmap spritesheet, int numberOfAnimations){
        spriteSheet=spritesheet;
        this.numberOfAnimations= numberOfAnimations;
        delay = new int[numberOfAnimations];
        start = new int[numberOfAnimations];
    }

    public void setFrameSize(int width, int height,float screen_semi_width,float screen_semi_height){
        spriteHeight=height;
        spriteWidth=width;
        this.screen_semi_width = screen_semi_width;
        this.screen_semi_height = screen_semi_height;
    }
    public void setAnimation(int animation,int delay){
        this.delay[animation]=delay;

    }

    public void drawFrame(Canvas canvas, int frame, float x, float y){

    }

    public void drawAnimation(Canvas canvas,int animation, int step, float x,float y){
        if(animation == 0){
            src.set(0, 0, spriteWidth, spriteHeight);
        }else{
            src.left = animation * spriteWidth;
            src.right = src.left+spriteWidth;
        }
        dest.left = x - screen_semi_width;
        dest.bottom = y + screen_semi_height;
        dest.right = x + screen_semi_width;
        dest.top = y - screen_semi_height;
        canvas.drawBitmap(spriteSheet, src, dest, null);
    }


    public int[] getDelay() {
        return delay;
    }

    public int getNumberOfAnimations() {
        return numberOfAnimations;
    }
}
