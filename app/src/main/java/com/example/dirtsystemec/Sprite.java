package com.example.dirtsystemec;

import android.graphics.Canvas;
import android.util.Log;

public abstract class Sprite {

    protected int currentAnimation;
    protected long lastValue;
    protected final float coordinate_x;
    protected final float coordinate_y;
    protected final Spritesheet spritesheet;
    protected final Canvas canvas;


    public Sprite(GameWorld gw, Spritesheet spritesheet, float coordinateX, float coordinateY,float width, float height, int widthFrame,
                  int heightFrame,int delay,int numberOfAnimations,long lastValue){
        this.canvas = new Canvas(gw.buffer);
        this.spritesheet = spritesheet;
        this.lastValue = System.currentTimeMillis();
        this.currentAnimation = 0;
        this.coordinate_x = gw.toPixelsX(coordinateX);
        this.coordinate_y = gw.toPixelsY(coordinateY);
        float screen_semi_width = gw.toPixelsXLength(width) / 2;
        float screen_semi_height = gw.toPixelsYLength(height) / 2;
        spritesheet.setFrameSize(widthFrame,heightFrame, screen_semi_width, screen_semi_height);
        for(int i = 0;i<numberOfAnimations;i++){
            spritesheet.setAnimation(i,delay);
        }
    }

    public abstract void draw(long currentTimeStamp);

}


class FireSprite extends Sprite {


    public FireSprite(GameWorld gameWorld, Spritesheet spritesheet, float coordinateX, float coordinateY, float width, float height, int widthFrame,
                      int heightFrame, int delay, int numberOfAnimations) {
        super(gameWorld, spritesheet, coordinateX, coordinateY, width, height, widthFrame, heightFrame, delay, numberOfAnimations,System.currentTimeMillis());
    }


    @Override
    public void draw(long currentValue) {
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if (currentValue - lastValue > spritesheet.getDelay()[currentAnimation]) {
            if (currentAnimation >= spritesheet.getDelay().length - 1) {
                currentAnimation = 0;
            } else {
                currentAnimation = currentAnimation + 1;
            }
            lastValue = currentValue;
        }
        spritesheet.drawAnimation(canvas, currentAnimation, 0, coordinate_x, coordinate_y);
        canvas.restore();
    }
}

    class SeaSprite extends Sprite{


        public SeaSprite(GameWorld gameWorld, Spritesheet spritesheet, float coordinateX, float coordinateY,float width, float height, int widthFrame,
                          int heightFrame,int delay,int numberOfAnimations){
            super(gameWorld,spritesheet,coordinateX,coordinateY,width,height,widthFrame,heightFrame,delay,numberOfAnimations,System.currentTimeMillis());
        }


        @Override
        public void draw(long currentValue){
            canvas.save();
            canvas.rotate(90, coordinate_x, coordinate_y);
            if(currentValue-lastValue > spritesheet.getDelay()[currentAnimation]){
                if(currentAnimation >= spritesheet.getDelay().length-1){
                    currentAnimation = 0;
                }else{
                    currentAnimation = currentAnimation + 1;
                }
                lastValue = currentValue;
            }
            spritesheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
            canvas.restore();
        }


}

class ScoreSprite extends Sprite{


    public ScoreSprite(GameWorld gameWorld, Spritesheet spritesheet, float coordinateX, float coordinateY,float width, float height, int widthFrame,
                     int heightFrame,int delay,int numberOfAnimations){
        super(gameWorld,spritesheet,coordinateX,coordinateY,width,height,widthFrame,heightFrame,delay,numberOfAnimations,0);
    }


    @Override
    public void draw(long currentValue){
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        long percent = 0;
        if(currentValue != 0){
           percent = (100*Math.abs(currentValue-lastValue))/currentValue;

        }

        if(percent >= spritesheet.getDelay()[currentAnimation]){
            if(currentAnimation >= spritesheet.getDelay().length-1){
                currentAnimation = 0;

            }else{
                currentAnimation = currentAnimation + 1;
            }
            lastValue = currentValue;
        }
        spritesheet.drawAnimation(canvas,currentAnimation,0,coordinate_x,coordinate_y);
        canvas.restore();
    }


}


