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
    protected static final float THICKNESS = 5;
    protected Paint paint = new Paint();
    protected Path path = new Path();
    protected float xmin, xmax, ymin, ymax;
    protected float screen_xmin, screen_xmax, screen_ymin, screen_ymax;
    protected float width, height,density;
    protected float screen_semi_width, screen_semi_height;


    @Override
    public ComponentType type(){
        return ComponentType.Drawable;
    }

    public abstract void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle);

    public boolean draw(Bitmap buffer){
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
                return true;
            } else{

                return false;
            }

        } else {
            this.draw(buffer, 0, 0, 0);
            return true;
        }
    }
}

class GroundDrawableComponent extends DrawableComponent {


    GroundDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        this.width = 9.0f;
        this.height = 4.0f;
        this.bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.ground, o);
        this.xmin = xmin; this.xmax = xmax; this.ymin = ymin; this.ymax = ymax;
        this.screen_xmin = gameWorld.toPixelsX(xmin+THICKNESS);
        this.screen_xmax = gameWorld.toPixelsX(xmax-THICKNESS);
        this.screen_ymin = gameWorld.toPixelsY(ymin+THICKNESS);
        this.screen_ymax = gameWorld.toPixelsY(ymax-THICKNESS);
        this.screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        this.screen_semi_height = gameWorld.toPixelsYLength(height)/2;
        src.set(0, 0, 200, 82);
    }



    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        dest.left = coordinate_x - screen_semi_width;
        dest.bottom = coordinate_y + screen_semi_height;
        dest.right = coordinate_x + screen_semi_width;
        dest.top = coordinate_y - screen_semi_height;
        canvas.drawBitmap(bitmap, src, dest, null);
        canvas.restore();
    }
}


class BulldozerDrawableComponent extends DrawableComponent {


    float box1_x,box1_y,box2_x,box2_y,box3_x,box3_y;
    float screenBox1_x,screenBox1_y,screenBox2_x,screenBox2_y,screenBox3_x,screenBox3_y;
    float coordinate_x1,coordinate_y1,coordinate_x2,coordinate_y2;
    static final float width = 2.8f; //2.8f
    int invert;

    BulldozerDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        invert = -1;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.height = proportionalToBulldozer(3.5f);
        this.box1_x = this.width;
        this.box1_y = proportionalToBulldozer(1.2f);
        this.box2_x = invert * proportionalToBulldozer(2.3f);
        this.box2_y = proportionalToBulldozer(1.5f);
        this.box3_x = invert * proportionalToBulldozer(0.4f);
        this.box3_y = proportionalToBulldozer(0.3f);
        this.screenBox1_x = gameWorld.toPixelsXLength(this.box1_x)/2;
        this.screenBox1_y = gameWorld.toPixelsYLength(this.box1_y)/2;
        this.screenBox2_x = gameWorld.toPixelsXLength(this.box2_x)/2;
        this.screenBox2_y = gameWorld.toPixelsYLength(this.box2_y)/2;
        this.screenBox3_x = gameWorld.toPixelsXLength(this.box3_x)/2;
        this.screenBox3_y = gameWorld.toPixelsYLength(this.box3_y)/2;
        this.screen_semi_width = gameWorld.toPixelsXLength(this.width)/2;
        this.screen_semi_height = gameWorld.toPixelsYLength(this.height)/2;

        int green = (int)(255*Math.random());
        int color = Color.argb(200, 255, green, 0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        coordinate_y1 = gameWorld.toPixelsYLength( proportionalToBulldozer(1.1f));
        coordinate_x1 = invert * gameWorld.toPixelsXLength(proportionalToBulldozer(0.5f));
        coordinate_y2 = gameWorld.toPixelsYLength(proportionalToBulldozer(0.9f));
    }

    public static float proportionalToBulldozer(float x){
        float perc;
        perc= (x * 100) / 4.4f;
        return Math.round(((width*perc) / 100f) * 100f) / 100f;
    }
    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        canvas.drawRect(coordinate_x- screenBox1_x ,coordinate_y-screenBox1_y, coordinate_x + screenBox1_x, coordinate_y +screenBox1_y, paint);
        coordinate_y=coordinate_y- coordinate_y1;
        coordinate_x=coordinate_x- coordinate_x1;
        canvas.drawRect(coordinate_x-screenBox2_x , coordinate_y-screenBox2_y, coordinate_x + screenBox2_x, coordinate_y +screenBox2_y, paint);
        coordinate_y=coordinate_y- coordinate_y2;
        canvas.drawRect(coordinate_x-screenBox3_x , coordinate_y-screenBox3_y , coordinate_x + screenBox3_x , coordinate_y +screenBox3_y, paint);
        canvas.restore();
        //canvas.drawCircle((coordinate_x- screen_semi_width)+radius,coordinate_y- screen_semi_height,radius,paint);
        //canvas.drawCircle((coordinate_x+ screen_semi_width)-radius,coordinate_y- screen_semi_height,radius,paint);

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
            this.screen_semi_width = gameWorld.toPixelsXLength(this.width)/2;
            this.screen_semi_height = gameWorld.toPixelsYLength(this.height)/2;
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            canvas.drawRect(coordinate_x- this.screen_semi_width ,coordinate_y- this.screen_semi_height, coordinate_x + this.screen_semi_width, coordinate_y + this.screen_semi_height, this.paint);
            canvas.restore();
        }
    }

    class ShovelDrawableComponent extends DrawableComponent{
       private float box_1_x,box_1_y,box_2_x,box_2_y;
       private float coordinate_x1,coordinate_y1,coordinate_x2,coordinate_y2;
       private float screenBox1_x,screenBox1_y,screenBox2_x,screenBox2_y,screenBox3_x,screenBox3_y;
        public ShovelDrawableComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent) {
            super();
            this.owner = gameObject;
            GameWorld gameWorld = gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);
            this.box_1_x = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            this.box_1_y = BulldozerDrawableComponent.proportionalToBulldozer(1.6f);
            this.box_2_x = invert * BulldozerDrawableComponent.proportionalToBulldozer(1f);
            this.box_2_y = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            this.screenBox1_x = gameWorld.toPixelsXLength(box_1_x)/2;
            this.screenBox1_y = gameWorld.toPixelsYLength(box_1_y)/2;
            this.screenBox2_x = gameWorld.toPixelsXLength(box_2_x)/2;
            this.screenBox2_y = gameWorld.toPixelsYLength(box_2_y)/2;
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            this.paint.setColor(color);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.coordinate_y1 = -gameWorld.toPixelsYLength(BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
            this.coordinate_x1 = invert*-gameWorld.toPixelsXLength(BulldozerDrawableComponent.proportionalToBulldozer(0.5f));
            this.coordinate_x2 = -gameWorld.toPixelsXLength(BulldozerDrawableComponent.proportionalToBulldozer(0.25f));
            this.coordinate_y2 = -gameWorld.toPixelsYLength(BulldozerDrawableComponent.proportionalToBulldozer(1f));
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            paint.setColor(Color.argb(200,255,0,0));
            canvas.drawRect(coordinate_x- screenBox1_x ,coordinate_y-screenBox1_y, coordinate_x + screenBox1_x, coordinate_y +screenBox1_y, paint);
            paint.setColor(Color.argb(200,0,255,0));
            coordinate_x=coordinate_x+coordinate_x1;
            coordinate_y=coordinate_y+coordinate_y1;
            canvas.drawRect(coordinate_x- screenBox2_x ,coordinate_y-screenBox2_y, coordinate_x + screenBox2_x, coordinate_y +screenBox2_y, paint);
            paint.setColor(Color.argb(200,0,0,255));
            coordinate_x=coordinate_x-coordinate_x1;
            coordinate_y=(coordinate_y-coordinate_y)-1f;

           /* coordinate_x=coordinate_x+coordinate_x2;
            coordinate_y=coordinate_y+coordinate_y2;
            canvas.drawRect(coordinate_x- screenBox3_x ,coordinate_y-screenBox3_y, coordinate_x + screenBox3_x, coordinate_y +screenBox3_y, paint);*/
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

    private FireSprite fireSprite;
    IncineratorDrawableComponent(GameObject gameObject,float fire_coordinate_x, float fire_coordinate_y){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 2.5f;
        this.height = 2.0f;
        screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        screen_semi_height = gameWorld.toPixelsYLength(height)/2;
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
        dest.left = coordinate_x - screen_semi_width;
        dest.bottom = coordinate_y + screen_semi_height;
        dest.right = coordinate_x + screen_semi_width;
        dest.top = coordinate_y - screen_semi_height;
        canvas.drawBitmap(bitmap, src, dest, null);
        canvas.restore();
        fireSprite.draw(System.currentTimeMillis());
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

        PositionComponent positionComponent = (TrianglePositionComponet) owner.getComponent(ComponentType.Position);
        screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        screen_semi_height = gameWorld.toPixelsYLength(height)/2;
        int color = Color.argb(250, 133, 133, 131);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        src.set(0, 0, 423, 208);
    }



    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        dest.left = coordinate_x - screen_semi_width;
        dest.bottom = coordinate_y + screen_semi_height;
        dest.right = coordinate_x + screen_semi_width;
        dest.top = coordinate_y - screen_semi_height;
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
        screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        screen_semi_height = gameWorld.toPixelsYLength(height)/2;
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
        dest.left = coordinate_x - screen_semi_width;
        dest.bottom = coordinate_y + screen_semi_height;
        dest.right = coordinate_x + screen_semi_width;
        dest.top = coordinate_y - screen_semi_height;
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
        screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        screen_semi_height = gameWorld.toPixelsYLength(height)/2;
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

class BridgeDrawableComponet extends DrawableComponent{
    float width,height;
    public BridgeDrawableComponet (GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld = gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width = 2.4f;
        this.height = 0.4f;
        this.screen_semi_width = gameWorld.toPixelsXLength(this.width)/2;
        this.screen_semi_height = gameWorld.toPixelsYLength(this.height)/2;
        int green = (int)(255*Math.random());
        int color = Color.argb(200, 255, green, 0);
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
        canvas.drawRect(coordinate_x- this.screen_semi_width ,coordinate_y- this.screen_semi_height, coordinate_x + this.screen_semi_width, coordinate_y + this.screen_semi_height, this.paint);
        canvas.restore();
    }
}


