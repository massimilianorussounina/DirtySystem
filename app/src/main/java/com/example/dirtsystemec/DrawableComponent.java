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

    BulldozerDrawableComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        GameWorld gameWorld= gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width=2.4f;
        this.height=Math.round(((width* 79.545)/100f)*100f)/100f;
        box1_x=width;
        box1_y=Math.round(((height*34.285f)/100f)*100f)/100f;
        box2_x=Math.round(((width*52.272f)/100f)*100f)/100f;
        box2_y=Math.round(((height*42.857f)/100f)*100f)/100f;
        box3_x=Math.round(((width*9.09f)/100f)*100f)/100f;
        box3_y=Math.round(((height*8.571f)/100f)*100f)/100f;
        screenBox1_x= gameWorld.toPixelsXLength(box1_x)/2;
        screenBox1_y= gameWorld.toPixelsYLength(box1_y)/2;
        screenBox2_x= gameWorld.toPixelsXLength(box2_x)/2;
        screenBox2_y= gameWorld.toPixelsYLength(box2_y)/2;
        screenBox3_x= gameWorld.toPixelsXLength(box3_x)/2;
        screenBox3_y= gameWorld.toPixelsYLength(box3_y)/2;
        this.screen_semi_width = gameWorld.toPixelsXLength(width)/2;
        this.screen_semi_height = gameWorld.toPixelsYLength(height)/2;

        int green = (int)(255*Math.random());
        int color = Color.argb(200, 255, green, 0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        coordinate_y1= gameWorld.toPixelsYLength( Math.round(((height*31.428f)/100f)*100f)/100f);
        coordinate_x1=gameWorld.toPixelsXLength(Math.round(( (width*11.363f)/100f)*100f)/100f);
        coordinate_y2 =gameWorld.toPixelsYLength(Math.round(( (height*25.714f)/100f)*100f)/100f);
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
            width=(Math.round((bulldozerDrawableComponent.width*11.363f)*100f)/100f)/100f;
            GameWorld gameWorld= gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);
            radius = gameWorld.toPixelsYLength(width);
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            canvas.drawCircle(coordinate_x,coordinate_y,radius,paint);
            canvas.restore();
        }
    }

    class DrapperDrawableComponent extends DrawableComponent{
        public DrapperDrawableComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent) {
            super();
            this.owner = gameObject;
            GameWorld gameWorld = gameObject.gameWorld;
            this.canvas = new Canvas(gameWorld.buffer);

            this.width = Math.round(((bulldozerDrawableComponent.width*11.363f)/100f)*100f)/100f;
            this.height = Math.round(((bulldozerDrawableComponent.width*34.090f)/100f)*100f)/100f;
            this.screen_semi_width = gameWorld.toPixelsXLength(width)/2;
            this.screen_semi_height = gameWorld.toPixelsYLength(height)/2;
            int green = (int)(255*Math.random());
            int color = Color.argb(200, 255, green, 0);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void draw(Bitmap buffer, float coordinate_x, float coordinate_y, float angle) {
            canvas.save();
            canvas.rotate((float) Math.toDegrees(angle), coordinate_x, coordinate_y);
            canvas.drawRect(coordinate_x- screen_semi_width ,coordinate_y-screen_semi_height, coordinate_x + screen_semi_width, coordinate_y +screen_semi_height, paint);
            canvas.restore();
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




class ObstacleDrawableComponent extends DrawableComponent {
    private float screen_x1;
    private float screen_x2;
    private float screen_x3;
    private float screen_y1;
    private float screen_y2;
    private float screen_y3;

    ObstacleDrawableComponent(GameObject gameObject,int i){
        super();
        this.owner = gameObject;
        GameWorld gameWorld= gameObject.gameWorld;
        this.canvas = new Canvas(gameWorld.buffer);
        this.width=4f;
        this.height=2f;
        if(i == 1){
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
       /* this.screen_x1 = gameWorld.toPixelsXLength(positionComponent.x1_local);
        this.screen_x2 = gameWorld.toPixelsXLength(positionComponent.x2_local);
        this.screen_x3 = gameWorld.toPixelsXLength(positionComponent.x3_local);
        this.screen_y1 = gameWorld.toPixelsYLength(positionComponent.y1_local);
        this.screen_y2 = gameWorld.toPixelsYLength(positionComponent.y2_local);
        this.screen_y3 = gameWorld.toPixelsYLength(positionComponent.y3_local);*/
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
        /*
        path.reset();
        path.moveTo(coordinate_x+screen_x1, coordinate_y+screen_y1);
        path.lineTo(coordinate_x+screen_x2, coordinate_y+screen_y2);
        path.lineTo(coordinate_x+screen_x3, coordinate_y+screen_y3);
        path.lineTo(coordinate_x+screen_x1, coordinate_y+screen_y1);
        canvas.drawPath(path, paint);*/
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

    SeaDrawableComponent(GameObject gameObject,float sea_coordinate_x, float sea_coordinate_y){
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


