package com.example.dirtsystemec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
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
    PhysicsComponent bulldozer;
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
        y=bulldozer.body.getPositionY();
        if(y >= 20.5f){
            deleteBulldozer();
            GameObject.createBulldozer(-7.5f,y-0.5f,this,-1);
            //creare il nuovo con -1
        }
        else if(y <=-20f){
            deleteBulldozer();
            GameObject.createBulldozer(-7.5f,y+0.5f,this,1);
            //creare il nuovo con 1
        }



        handleCollisions(contactListener.getCollisions());

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
        if(obj.name!=null && obj.name.equals("bulldozer")) {
            for (Component psh : obj.getComponent(ComponentType.Physics)) {
                if (((PhysicsComponent)psh).name.equals("chassis")){
                    bulldozer=(PhysicsComponent)psh;
                }
            }
        }

    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
           if(event.a.name.equals("wheelOne") || event.a.name.equals("wheelTwo")|| event.a.name.equals("wheelThree")|| event.a.name.equals("wheelFour")){
               if(event.b.name.equals("oilstain")){
                   for(Component component: event.a.owner.getComponent(ComponentType.Joint)) {
                       if (component instanceof RevoluteJointComponent) {
                           RevoluteJointComponent revoluteJointComponent = (RevoluteJointComponent) component;
                           if (revoluteJointComponent.bodyOne.equals(event.a.body) || revoluteJointComponent.bodyTwo.equals(event.a.body)) {
                               if(revoluteJointComponent.joint instanceof RevoluteJoint) {
                                   if(revoluteJointComponent.joint.getMotorSpeed()!=2f)
                                       revoluteJointComponent.joint.setMotorSpeed(2f);
                               }
                           }
                       }
                   }

               }
           } else
           if(event.b.name.equals("wheelOne") || event.b.name.equals("wheelTwo")|| event.b.name.equals("wheelThree")|| event.b.name.equals("wheelFour")){
               if(event.a.name.equals("oilstain")){
                   for(Component component: event.b.owner.getComponent(ComponentType.Joint)){
                       if (component instanceof RevoluteJointComponent) {
                           RevoluteJointComponent revoluteJointComponent = (RevoluteJointComponent) component;
                           if (revoluteJointComponent.bodyOne.equals(event.b.body) || revoluteJointComponent.bodyTwo.equals(event.b.body)) {
                               if(revoluteJointComponent.joint instanceof RevoluteJoint) {
                                   if(revoluteJointComponent.joint.getMotorSpeed()!=2f)
                                       revoluteJointComponent.joint.setMotorSpeed(2f);
                               }
                           }
                       }
                   }

               }
           }
        }

    }

    public void deleteBulldozer(){

        objects.remove(bulldozer.owner);
        for (Component obj:bulldozer.owner.getComponent(ComponentType.Physics)){
            world.destroyBody(((PhysicsComponent)obj).body);
            ((PhysicsComponent)obj).body.setUserData(null);
            ((PhysicsComponent)obj).body.delete();
            ((PhysicsComponent)obj).body=null;
        }

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
