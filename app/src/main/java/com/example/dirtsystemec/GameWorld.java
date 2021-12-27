package com.example.dirtsystemec;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
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
    private boolean buttonTrash = true;
    // Simulation
    List<GameObject> objects;

    List<GameObject> listBarrel;
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
        this.listBarrel = new ArrayList<>();
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

        for (Input.TouchEvent event: touchHandler.getTouchEvents())
            touchConsumer.consumeTouchEvent(event);
    }

    public synchronized void render() {
        canvas.drawARGB(100,126,193,243);

        for(GameObject gameObject: listBarrel){
            List<Component> components = gameObject.getComponent(ComponentType.Drawable);
            if(components != null){
                for (Component component: components) {
                    ((DrawableComponent) component).draw(buffer);
                }
            }
        }

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

        if(obj.name!=null && obj.name.equals("bulldozer")) {
            objects.add(obj);
            for (Component psh : obj.getComponent(ComponentType.Physics)) {
                if (((PhysicsComponent)psh).name.equals("chassis")){
                    bulldozer=(PhysicsComponent)psh;
                }
            }
        }else if(obj.name!=null && obj.name.equals("barrel")){
            listBarrel.add(obj);
        }else{
            objects.add(obj);
        }

    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
            handleDeleteBarrel(event);
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

    public void eventButton(float coordinateX, float coordinateY){
        int index = 0;

        if(buttonTrash) {
            if ((coordinateX <= 12.5f && coordinateX >= 10f) && (coordinateY >= -23.8f && coordinateY <= -21.8f)) {
                buttonTrash = false;
                for (GameObject g : objects) {
                    if (g.name.equals("buttonTrash")) {
                        break;
                    } else {
                        index++;
                    }
                }
                if (index <= objects.size()) {
                    objects.remove(index);
                    GameObject.createButtonTrash(11.5f, -22.8f, this, buttonTrash);
                }
            }
        }else{
            buttonTrash = true;
            for (GameObject g: objects) {
                if(g.name.equals("buttonTrash")){
                    break;
                }else{
                    index++;
                }
            }
            if(index <= objects.size()) {
                objects.remove(index);
                GameObject.createButtonTrash(11.5f,-22.8f,this,buttonTrash);
            }
            GameObject.createBarrel(13f,coordinateY,this);
        }

    }


    public void handleDeleteBarrel(Collision event){
        if(event.a.name.equals("incinerator")  && event.b.name.equals("barrel")){
            listBarrel.remove((GameObject) event.b.owner);
            world.destroyBody(event.b.body);
            event.b.body.setUserData(null);
            event.b.body.delete();
            event.b.body=null;
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("incinerator")){
            listBarrel.remove((GameObject) event.a.owner);
            world.destroyBody(event.a.body);
            event.a.body.setUserData(null);
            event.a.body.delete();
            event.a.body=null;
        }
    }

}
