package com.example.dirtsystemec;

import android.graphics.Canvas;

public class FireSprite {

    private int currentAntimation;
    private long lastTimeStamp;
    private float coordinate_x;
    private float coordinate_y;
    private static float screen_semi_width, screen_semi_height;
    private Spritesheet spritesheet;
    private final Canvas canvas;


    public FireSprite(GameWorld gw, Spritesheet spritesheet, float x, float y){
        this.canvas = new Canvas(gw.buffer);
        this.spritesheet = spritesheet;
        lastTimeStamp = System.currentTimeMillis();
        currentAntimation = 0;
        this.coordinate_x = gw.toPixelsX(x);
        this.coordinate_y = gw.toPixelsY(y);
        this.screen_semi_width = gw.toPixelsXLength(2.5f)/2;
        this.screen_semi_height = gw.toPixelsYLength(2f)/2;
        spritesheet.setFrameSize(49,75,screen_semi_width,screen_semi_height);
        for(int i = 0;i<8;i++){
            spritesheet.setAnimation(i,200);
        }
    }


    public void draw(long currentTimeStamp){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if(currentTimeStamp-lastTimeStamp > spritesheet.getDelay()[currentAntimation]){
            if(currentAntimation >= spritesheet.getDelay().length-1){
                currentAntimation = 0;
            }else{
                currentAntimation = currentAntimation+1;
            }
            lastTimeStamp = currentTimeStamp;
        }
        spritesheet.drawAnimation(canvas,currentAntimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }
}
