package com.example.dirtsystemec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawableComponent  extends Component{

    protected String name;
    protected final Rect src = new Rect();
    protected final RectF dest = new RectF();
    protected Bitmap bitmap;
    protected Canvas canvas;
    protected Paint paint = new Paint();
    protected float width, height,density;
    protected float screenSemiWidth, screenSemiHeight;

    DrawableComponent(String name){
        this.name = name;
    }

    @Override
    public ComponentType type(){
        return ComponentType.Drawable;
    }

    public abstract void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle);

    public void draw(Bitmap buffer){
        ArrayList physicsComponents = (ArrayList) owner.getComponent(ComponentType.Physics);
        PhysicsComponent physicsComponent = null;

        for (Object c: physicsComponents) {
            physicsComponent = (PhysicsComponent) c;
            if(physicsComponent.name.compareTo(this.name) == 0)break;
        }

        if (physicsComponent != null) {
            float coordinate_x = physicsComponent.getBodyPositionX(),
                    coordinate_y = physicsComponent.getBodyPositionY(),
                    angle = physicsComponent.getBodyAngle();
            GameWorld gameWorld = ((GameObject) owner).gameWorld;
            Box view = gameWorld.currentView;
            if (coordinate_x > view.xmin && coordinate_x < view.xmax &&
                    coordinate_y > view.ymin && coordinate_y < view.ymax) {
                // Screen position
                float screen_x = gameWorld.toPixelsX(coordinate_x),
                        screen_y = gameWorld.toPixelsY(coordinate_y);
                this.draw(buffer, screen_x, screen_y, angle);
            }

        } else {
            this.draw(buffer, 0, 0, 0);
        }
    }
}




/*class BulldozerDrawableComponent extends DrawableComponent {

    private final float boxOneX;
    private final float boxOneY;
    private final float boxTwoX;
    private final float boxTwoY;
    private final float boxThreeX;
    private final float boxThreeY;

    private final float screenBoxOneX;
    private final float screenBoxOneY;
    private final float screenBoxTwoX;
    private final float screenBoxTwoY;
    private final float screenBoxThreeX;
    private final float screenBoxThreeY;
    private final float coordinateOneX;
    private final float coordinateOneY;
    private float coordinateTwoX;
    private final float coordinateTwoY;
    static final float width = 2.8f; //2.8f
    int invert;

    public float getBoxOneX() {
        return boxOneX;
    }

    public float getBoxOneY() {
        return boxOneY;
    }

    public float getBoxTwoX() {
        return boxTwoX;
    }

    public float getBoxTwoY() {
        return boxTwoY;
    }

    public float getBoxThreeX() {
        return boxThreeX;
    }

    public float getBoxThreeY() {
        return boxThreeY;
    }

    BulldozerDrawableComponent(GameObject gameObject, int invert){
        super();
        this.owner = gameObject;
        this.invert=invert;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.height = proportionalToBulldozer(3.5f);
        this.boxOneX = width;
        this.boxOneY = proportionalToBulldozer(1.2f);
        this.boxTwoX = invert * proportionalToBulldozer(2.3f);
        this.boxTwoY = proportionalToBulldozer(1.5f);
        this.boxThreeX = invert * proportionalToBulldozer(0.4f);
        this.boxThreeY = proportionalToBulldozer(0.3f);
        this.screenBoxOneX = gameWorld.toPixelsXLength(boxOneX)/2;
        this.screenBoxOneY = gameWorld.toPixelsYLength(boxOneY)/2;
        this.screenBoxTwoX = gameWorld.toPixelsXLength(boxTwoX)/2;
        this.screenBoxTwoY = gameWorld.toPixelsYLength(boxTwoY)/2;
        this.screenBoxThreeX = gameWorld.toPixelsXLength(boxThreeX)/2;
        this.screenBoxThreeY = gameWorld.toPixelsYLength(boxThreeY)/2;
        this.screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        this.screenSemiHeight = gameWorld.toPixelsYLength(this.height)/2;

        int green = (int)(255*Math.random());
        int color = Color.argb(200, 255, green, 0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        coordinateOneY = gameWorld.toPixelsYLength( proportionalToBulldozer(1.1f));
        coordinateOneX = invert * gameWorld.toPixelsXLength(proportionalToBulldozer(0.5f));
        coordinateTwoY = gameWorld.toPixelsYLength(proportionalToBulldozer(0.9f));
    }

    public static float proportionalToBulldozer(float number){
        float percent;
        percent = (number * 100) / 4.4f;
        return Math.round(((width*percent) / 100f) * 100f) / 100f;
    }

    @Override
    public void draw(Bitmap buffer, float coordinateX, float coordinateY, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinateX, coordinateY);
        canvas.drawRect(coordinateX - screenBoxOneX ,coordinateY - screenBoxOneY, coordinateX + screenBoxOneX, coordinateY + screenBoxOneY, paint);
        coordinateY = coordinateY - coordinateOneY;
        coordinateX = coordinateX - coordinateOneX;
        canvas.drawRect(coordinateX - screenBoxTwoX , coordinateY - screenBoxTwoY, coordinateX + screenBoxTwoX, coordinateY + screenBoxTwoY, paint);
        coordinateY = coordinateY - coordinateTwoY;
        canvas.drawRect(coordinateX - screenBoxThreeX , coordinateY - screenBoxThreeY , coordinateX + screenBoxThreeX , coordinateY + screenBoxThreeY, paint);
        canvas.restore();
    }


    class WheelDrawableComponent extends DrawableComponent{

        float radius;

        public WheelDrawableComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent) {
            super();
            this.owner = gameObject;
            this.width = proportionalToBulldozer(0.5f);
            GameWorld gameWorld= gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);
            this.radius = gameWorld.toPixelsYLength(this.width);
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            canvas.drawCircle(coordinate_x,coordinate_y,this.radius,this.paint);
            canvas.restore();
        }
    }

    class DamperDrawableComponent extends DrawableComponent{
        public DamperDrawableComponent(GameObject gameObject) {
            super();
            this.owner = gameObject;
            GameWorld gameWorld = gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);
            this.width = proportionalToBulldozer(0.5f);
            this.height = proportionalToBulldozer(1.5f);
            this.screenSemiWidth = gameWorld.toPixelsXLength(this.width)/2;
            this.screenSemiHeight = gameWorld.toPixelsYLength(this.height)/2;
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            canvas.drawRect(coordinate_x- this.screenSemiWidth ,coordinate_y- this.screenSemiHeight, coordinate_x + this.screenSemiWidth, coordinate_y + this.screenSemiHeight, this.paint);
            canvas.restore();
        }
    }

    class ShovelDrawableComponent extends DrawableComponent{
        private final float coordinateOneX;
        private final float coordinateOneY;
        private final float screenBoxOneX;
        private final float screenBoxOneY;
        private final float screenBoxTwoX;
        private final float screenBoxTwoY;

        public ShovelDrawableComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent) {
            super();
            this.owner = gameObject;
            GameWorld gameWorld = gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);
            float boxOneX = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            float boxOneY = BulldozerDrawableComponent.proportionalToBulldozer(1.6f);
            float boxTwoX = invert * BulldozerDrawableComponent.proportionalToBulldozer(1f);
            float boxTwoY = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            this.screenBoxOneX = gameWorld.toPixelsXLength(boxOneX)/2;
            this.screenBoxOneY = gameWorld.toPixelsYLength(boxOneY)/2;
            this.screenBoxTwoX = gameWorld.toPixelsXLength(boxTwoX)/2;
            this.screenBoxTwoY = gameWorld.toPixelsYLength(boxTwoY)/2;
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.coordinateOneY = -gameWorld.toPixelsYLength(BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
            this.coordinateOneX = invert*-gameWorld.toPixelsXLength(BulldozerDrawableComponent.proportionalToBulldozer(0.5f));
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            paint.setColor(Color.argb(200,255,0,0));
            canvas.drawRect(coordinate_x- screenBoxOneX ,coordinate_y-screenBoxOneY, coordinate_x + screenBoxOneX, coordinate_y +screenBoxOneY, paint);
            paint.setColor(Color.argb(200,0,255,0));
            coordinate_x = coordinate_x+coordinateOneX;
            coordinate_y = coordinate_y+coordinateOneY;
            canvas.drawRect(coordinate_x- screenBoxTwoX ,coordinate_y - screenBoxTwoY, coordinate_x + screenBoxTwoX, coordinate_y +screenBoxTwoY, paint);
            paint.setColor(Color.argb(200,0,0,255));
            canvas.restore();
        }
        class WheelShovelDrawableComponent extends DrawableComponent {
            float radius;

            public WheelShovelDrawableComponent(GameObject gameObject) {
                super();
                this.owner = gameObject;
                width = BulldozerDrawableComponent.proportionalToBulldozer(0.3f);
                GameWorld gameWorld = gameObject.gameWorld;
                this.canvas = new Canvas(gameWorld.buffer);
                radius = gameWorld.toPixelsYLength(width);
                int green = (int) (255 * Math.random());
                int color = Color.argb(200, 255, green, 0);
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }

            @Override
            public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
                canvas.save();
                canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
                canvas.drawCircle(coordinate_x, coordinate_y, radius, paint);
                canvas.restore();
            }
        }
    }

}


/*class ScoreBarDrawableComponent extends DrawableComponent {

    private final ScoreBarSprite scoreBarSprite;
    ScoreBarDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmapFire = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.scorebar, bitmapFactory);
        float scoreBarCoordinateX =  ((StaticPositionComponent)gameObject.getComponent(ComponentType.Position)).coordinateX;
        float scoreBarCoordinateY =  ((StaticPositionComponent)gameObject.getComponent(ComponentType.Position)).coordinateY;
        scoreBarSprite = new ScoreBarSprite(gameWorld,new Spritesheet(bitmapFire,10),scoreBarCoordinateX,scoreBarCoordinateY);
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        scoreBarSprite.draw(System.currentTimeMillis());
    }
}*/




class RectDrawableComponent extends DrawableComponent{


    public RectDrawableComponent (String name,GameObject gameObject, float width, float height, int color){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = width;
        this.height = height;
        this.screenSemiWidth = gameWorld.toPixelsXLength(this.width)/2;
        this.screenSemiHeight = gameWorld.toPixelsYLength(this.height)/2;
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    public void draw(Bitmap buffer, float coordinateX, float coordinateY, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinateX, coordinateY);
        canvas.drawRect(coordinateX- this.screenSemiWidth ,coordinateY- this.screenSemiHeight, coordinateX + this.screenSemiWidth, coordinateY + this.screenSemiHeight, this.paint);
        canvas.restore();
    }
}

class CircleDrawambleComponent extends  DrawableComponent{
    float radius;
    public CircleDrawambleComponent (String name,GameObject gameObject, float radius, int color){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.radius=gameWorld.toPixelsYLength(radius);
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);


    }

    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        canvas.drawCircle(coordinate_x,coordinate_y,this.radius,this.paint);
        canvas.restore();
    }
}



class BitmapDrawableComponent extends DrawableComponent {

    BitmapDrawableComponent(String name,GameObject gameObject, float width, float height,
                            float density, int drawable, int left, int top, int right,
                            int bottom){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = width;
        this.height = height;
        this.density = density;
        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(),drawable, bitmapFactory);
        src.set(left, top, right, bottom);
    }

    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        dest.left = coordinate_x - screenSemiWidth;
        dest.bottom = coordinate_y + screenSemiHeight;
        dest.right = coordinate_x + screenSemiWidth;
        dest.top = coordinate_y - screenSemiHeight;
        canvas.drawBitmap(bitmap, src, dest, null);
        canvas.restore();
    }
}



class SpriteDrawableComponent extends DrawableComponent {

    private final Sprite sprite;

    public SpriteDrawableComponent(String name,GameObject gameObject,Sprite sprite){
        super(name);
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.sprite = sprite;
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        sprite.draw(System.currentTimeMillis());
    }
}



