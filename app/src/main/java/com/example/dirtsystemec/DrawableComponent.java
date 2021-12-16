package com.example.dirtsystemec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.fpl.liquidfun.CircleShape;

public abstract class DrawableComponent  extends Component{

    protected final Rect src = new Rect();
    protected final RectF dest = new RectF();
    protected Bitmap bitmap;
    protected Canvas canvas;
    protected Paint paint = new Paint();
    protected Path path = new Path();
    protected float width, height,density;
    protected float screenSemiWidth, screenSemiHeight;


    @Override
    public ComponentType type(){
        return ComponentType.Drawable;
    }

    public abstract void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle);

    public void draw(Bitmap buffer){
        PhysicsComponent physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.Physics);
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

class GroundDrawableComponent extends DrawableComponent {


    GroundDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        this.width = 9.0f;
        this.height = 4.0f;
        this.bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.ground, bitmapFactory);
        this.screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        this.screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        src.set(0, 0, 200, 82);
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


class BulldozerDrawableComponent extends DrawableComponent {

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




class IncineratorDrawableComponent extends DrawableComponent {

    private final FireSprite fireSprite;
    IncineratorDrawableComponent(GameObject gameObject,float fire_coordinate_x, float fire_coordinate_y){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 2.5f;
        this.height = 2.0f;
        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        Bitmap bitmapFire = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.fire_spritesheet, o);
        fireSprite = new FireSprite(gameWorld,new Spritesheet(bitmapFire,8),fire_coordinate_x,fire_coordinate_y);
        BitmapFactory.Options b = new BitmapFactory.Options();
        b.inScaled = false;
        bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.incinerator, b);
        src.set(0, 0, 37, 54);
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
        fireSprite.draw(System.currentTimeMillis());
    }
}

class ScoreBarDrawableComponent extends DrawableComponent {

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
}








class TowerDrawableComponent extends DrawableComponent {

    TowerDrawableComponent(GameObject gameObject,BridgePosition bridgePosition){
        super();
        this.owner = gameObject;
        GameWorld gameWorld= gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 4f;
        this.height = 2f;
        if(bridgePosition == BridgePosition.RIGHT){
            BitmapFactory.Options b = new BitmapFactory.Options();
            b.inScaled = false;
            bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.right_bridge, b);
        }else{
            BitmapFactory.Options b = new BitmapFactory.Options();
            b.inScaled = false;
            bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.left_bridge, b);
        }

        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        int color = Color.argb(250, 133, 133, 131);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        src.set(0, 0, 423, 208);
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


class BarrelDrawableComponent extends DrawableComponent {

    BarrelDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 0.8f;
        this.height = 0.8f;
        this.density = 100.0f;
        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        CircleShape box = new CircleShape();
        box.setRadius(width/2);
        BitmapFactory.Options b = new BitmapFactory.Options();
        b.inScaled = false;
        bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.barrel, b);
        src.set(0, 0, 187, 184);
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

class SeaDrawableComponent extends DrawableComponent {

    private SeaSprite seaSprite;

    public SeaDrawableComponent(GameObject gameObject,float sea_coordinate_x, float sea_coordinate_y){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 5.3f;
        this.height = 0.1f;
        screenSemiWidth = gameWorld.toPixelsXLength(width)/2;
        screenSemiHeight = gameWorld.toPixelsYLength(height)/2;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        Bitmap bitmapSea = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.sea, o);
        seaSprite = new SeaSprite(gameWorld,new Spritesheet(bitmapSea,18),sea_coordinate_x,sea_coordinate_y);
    }


    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
       seaSprite.draw(System.currentTimeMillis());
    }
}

class BridgeDrawableComponent extends DrawableComponent{
    float width,height;
    public BridgeDrawableComponent (GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 2.4f;
        this.height = 0.2f;
        this.screenSemiWidth = gameWorld.toPixelsXLength(this.width)/2;
        this.screenSemiHeight = gameWorld.toPixelsYLength(this.height)/2;
        int color = Color.argb(255, 32, 32, 32);
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


