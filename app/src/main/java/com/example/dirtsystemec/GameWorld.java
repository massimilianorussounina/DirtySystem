package com.example.dirtsystemec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;



public class GameWorld {

    final static int bufferWidth = 1080, bufferHeight = 1920;    // actual pixels
    Bitmap buffer;
    private final Canvas canvas;
    //private BulldozerPhysicsComponent bulldozer;

    // Simulation
    List<GameObject> objects;
    List<GameObject> listBulldozer;

    World world;
    final Box physicalSize, screenSize, currentView;
    private MyContactListener contactListener;
    private TouchConsumer touchConsumer;
    private TouchHandler touchHandler;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    final Activity activity;
    private Bitmap bitmap;
    private final RectF dest = new RectF();

    // Arguments are in physical simulation units.
    public GameWorld(Box physicalSize, Box screenSize, Activity theActivity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.activity = theActivity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World( 0.0f, 0.0f);  // gravity vector
        this.currentView = physicalSize;

        // stored to prevent GC
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        touchConsumer = new TouchConsumer(this);

        this.objects = new ArrayList<>();
        this.listBulldozer = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        Rect src = new Rect();
        src.set(0,0,1920,1080);
    }

    public synchronized void update(float elapsedTime)
    {
        float y;
        // advance the physics simulation
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        /*y=bulldozer.body.getPositionY();
        if(y >= 20.5f){
            deleteBulldozer();
            GameObject.createBulldozer(-7.5f,y-0.5f,this,-1);
            //creare il nuovo con -1
        }
        else if(y <=-20f){
            deleteBulldozer();
            GameObject.createBulldozer(-7.5f,y+0.5f,this,1);
            //creare il nuovo con 1
        }*/



        //handleCollisions(contactListener.getCollisions());

        // Handle touch events
       //for (Input.TouchEvent event: touchHandler.getTouchEvents())
         //    touchConsumer.consumeTouchEvent(event);
    }

    public synchronized void render() {
        canvas.drawARGB(100,126,193,243);

        for(GameObject gameObject: objects){
            List<Component> components = gameObject.getComponent(ComponentType.Drawable);
            if(components != null){
                for (Component component: components) {
                    ((DrawableComponent) component).draw(buffer);
                }
            }
        }

    }

    public synchronized void addGameObject(GameObject obj)
    {
        objects.add(obj);
       /* if(obj.getComponent(ComponentType.Physics) instanceof BulldozerPhysicsComponent ){
            //this.bulldozer=  (BulldozerPhysicsComponent) obj.getComponent(ComponentType.Physics);
        }
        if(obj.name != null && obj.name.compareTo("bulldozer")==0){
            this.listBulldozer.add(obj);
        }*/
    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
           /* Sound sound = CollisionSounds.getSound(event.a.getClass(), event.b.getClass());
            if (sound!=null) {
                long currentTime = System.nanoTime();
                if (currentTime - timeOfLastSound > 500_000_000) {
                    timeOfLastSound = currentTime;
                    sound.play(0.7f);
                }
            }*/
        }

    }

    public void deleteBulldozer(){
        for (GameObject obj:listBulldozer){
            objects.remove(obj);
            if(((PhysicsComponent)obj.getComponent(ComponentType.Physics)).body==null) {
                Log.i("body", "oh nooooooo");
            }
            world.destroyBody(((PhysicsComponent)obj.getComponent(ComponentType.Physics)).body);
           ((PhysicsComponent)obj.getComponent(ComponentType.Physics)).body.setUserData(null);
            ((PhysicsComponent)obj.getComponent(ComponentType.Physics)).body.delete();
            ((PhysicsComponent)obj.getComponent(ComponentType.Physics)).body=null;

        }
        listBulldozer = new ArrayList<>();
    }

    // Conversions between screen coordinates and physical coordinates

    public float toMetersX(float x) { return currentView.xmin + x * (currentView.width/screenSize.width); }
    public float toMetersY(float y) { return currentView.ymin + y * (currentView.height/screenSize.height); }

    public float toPixelsX(float x) { return (x-currentView.xmin)/currentView.width*bufferWidth; }
    public float toPixelsY(float y) { return (y-currentView.ymin)/currentView.height*bufferHeight; }

    public float toPixelsXLength(float x)
    {
        return x/currentView.width*bufferWidth;
    }
    public float toPixelsYLength(float y)
    {
        return y/currentView.height*bufferHeight;
    }

    public synchronized void setGravity(float x, float y)
    {
        world.setGravity(x, y);
    }

    @Override
    public void finalize()
    {
        world.delete();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

}
