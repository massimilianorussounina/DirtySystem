package com.example.dirtsystemec;

import android.graphics.Canvas;

public abstract class Sprite {

    protected int currentAnimation;
    protected long lastTimeStamp;
    protected final float coordinate_x;
    protected final float coordinate_y;
    protected final Spritesheet spritesheet;
    protected final Canvas canvas;


    public Sprite(GameWorld gw, Spritesheet spritesheet, float coordinateX, float coordinateY,float width, float height, int widthFrame,
                  int heightFrame,int delay,int numberOfAnimations){
        this.canvas = new Canvas(gw.buffer);
        this.spritesheet = spritesheet;
        lastTimeStamp = System.currentTimeMillis();
        currentAnimation = 0;
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
        super(gameWorld, spritesheet, coordinateX, coordinateY, width, height, widthFrame, heightFrame, delay, numberOfAnimations);
    }


    @Override
    public void draw(long currentTimeStamp) {
        canvas.save();
        canvas.rotate(90, coordinate_x, coordinate_y);
        if (currentTimeStamp - lastTimeStamp > spritesheet.getDelay()[currentAnimation]) {
            if (currentAnimation >= spritesheet.getDelay().length - 1) {
                currentAnimation = 0;
            } else {
                currentAnimation = currentAnimation + 1;
            }
            lastTimeStamp = currentTimeStamp;
        }
        spritesheet.drawAnimation(canvas, currentAnimation, 0, coordinate_x, coordinate_y);
        canvas.restore();
    }
}

    class SeaSprite extends Sprite{


        public SeaSprite(GameWorld gameWorld, Spritesheet spritesheet, float coordinateX, float coordinateY,float width, float height, int widthFrame,
                          int heightFrame,int delay,int numberOfAnimations){
            super(gameWorld,spritesheet,coordinateX,coordinateY,width,height,widthFrame,heightFrame,delay,numberOfAnimations);
        }


        @Override
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




