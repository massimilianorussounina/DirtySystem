package com.example.dirtsystemec;

import android.graphics.Canvas;

public class SeaSprite {

    private int currentAnimation;
    private long lastTimeStamp;
    private final float coordinate_x;
    private final float coordinate_y;
    private final Spritesheet spritesheet;
    private final Canvas canvas;


    public SeaSprite(GameWorld gw, Spritesheet spritesheet, float x, float y){
        this.canvas = new Canvas(gw.buffer);
        this.spritesheet = spritesheet;
        this.lastTimeStamp = System.currentTimeMillis();
        this.currentAnimation = 0;
        this.coordinate_x = gw.toPixelsX(x);
        this.coordinate_y = gw.toPixelsY(y);
        float screen_semi_width = gw.toPixelsXLength(5.3f) / 2;
        float screen_semi_height = gw.toPixelsYLength(2f) / 2;
        spritesheet.setFrameSize(500,108, screen_semi_width, screen_semi_height);
        for(int i = 0;i<spritesheet.getNumberOfAnimations();i++){
            spritesheet.setAnimation(i,500);
        }
    }

    public void draw(long currentTimeStamp){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if(currentTimeStamp-lastTimeStamp > spritesheet.getDelay()[currentAnimation]){
            if(currentAnimation >= spritesheet.getDelay().length-1){
                currentAnimation = 0;
            }else{
                currentAnimation = currentAnimation + 1;
            }
            lastTimeStamp = currentTimeStamp;
        }
        spritesheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }

}
