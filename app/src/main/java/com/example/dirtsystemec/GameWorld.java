package com.example.dirtsystemec;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Sound;
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
    private volatile boolean verifyAction = false;
    private long timeOfLastSound = 0;
    List<GameObject> listBarrel;
    PhysicsComponent bulldozer;
    World world;
    protected GameObject gameObjectBulldozer;
    final Box physicalSize, screenSize, currentView;
    private MyContactListener contactListener;
    private TouchConsumer touchConsumer;
    private TouchHandler touchHandler;
    private int numFps;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    final Activity activity;
    private Bitmap bitmap;
    private final RectF dest = new RectF();
    boolean flag=false;
    float speed=7f;
    // Arguments are in physical simulation units.
    public GameWorld(Box physicalSize, Box screenSize, Activity theActivity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.activity = theActivity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World( 0.0f, 0.0f);  // gravity vector
        this.currentView = physicalSize;
        this.numFps = 0;
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

    public synchronized void update(float elapsedTime) {
        float positionYBulldozer;
        GameObject gameObjectBulldozer= null;
        // advance the physics simulation
        numFps++;

        /*    Inizio Fase AI   */

        positionYBulldozer = bulldozer.body.getPositionY();
        int direction = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
        if(positionYBulldozer > 19.9f && direction == 1){
            System.out.println(positionYBulldozer);
            rotationBulldozer(-8f,positionYBulldozer,this,-1,activity);
            verifyAction = false;
        }
        else if(positionYBulldozer < - 19.9f && direction == -1){
            rotationBulldozer(-8f,positionYBulldozer,this,1,activity);
            verifyAction = false;
        }


        if(numFps == 60 ){
            if(verifyAction && listBarrel.size() == 0){
                verifyAction = false;
            }
            if(!verifyAction){
                for(GameObject gameObject: objects) {
                    if (gameObject.name.equals("bulldozer")) {
                        gameObjectBulldozer = gameObject;
                        break;
                    }
                }
                if(gameObjectBulldozer != null){
                    List<Component> components = gameObjectBulldozer.getComponent(ComponentType.AI);
                    if(components != null){
                        for (Component component: components) {
                            Action action = ((FsmAIComponent) component).fsm.stepAndGetAction(this);
                            if(action != null){
                                if(action.equals(Action.burned)){
                                    int result = searchBarrel();
                                    System.out.println("ti muovi verso "+ result);
                                    burnedBarrel(result,this,activity);
                                    verifyAction = true;
                                }else if(action.equals(Action.waited)){
                                    System.out.println("vai al centro");
                                    moveToCenter(this,activity);
                                }
                            }
                        }
                    }
                }
            }
            numFps = 0;
        }



        /*                      */



        /* Simulazione Fisica */
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);

       /*                     */

        /* Fase Collisioni    */


        /*                  */

        //accelerationAnddeceleration();


        handleCollisions(contactListener.getCollisions());

        for (Input.TouchEvent event: touchHandler.getTouchEvents())
            touchConsumer.consumeTouchEvent(event);
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

        if(obj.name!=null && obj.name.equals("bulldozer")) {
            gameObjectBulldozer = obj;
            objects.add(obj);

            for (Component psh : obj.getComponent(ComponentType.Physics)) {
                if (((PhysicsComponent)psh).name.equals("chassis")){
                    bulldozer=(PhysicsComponent)psh;
                }
            }
        }else if(obj.name!=null && obj.name.equals("barrel")){
            objects.add(0,obj);
        }else{
            objects.add(obj);
        }

    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
            handleDeleteBarrel(event);
            handleAddBarrel(event);
            handleSoundCollisions(event);
        }

    }

    private void handleSoundCollisions(Collision event){
        Sound sound = CollisionSounds.getSound(((GameObject)event.a.owner).name, ((GameObject)event.b.owner).name);
        if (sound!=null) {
            long currentTime = System.nanoTime();
            if (currentTime - timeOfLastSound > 500_000_000) {
                timeOfLastSound = currentTime;
                sound.play(0.5f);
            }
        }
    }


    public void rotationBulldozer(float coordinateX, float coordinateY, GameWorld gameWorld, int direction, Activity context){
        List<Component> componentsAi = null;
        List<Component> componentsPhysics = null;
        GameObject gameObjectBulldozer = null;

        for(GameObject gameObject: objects){
            if(gameObject.name.equals("bulldozer")){
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if(gameObjectBulldozer != null){
            componentsAi = gameObjectBulldozer.getComponent(ComponentType.AI);
            componentsPhysics = gameObjectBulldozer.getComponent(ComponentType.Physics);
            objects.remove(gameObjectBulldozer);

            if(componentsPhysics != null){
                for (Component component : componentsPhysics){
                    world.destroyBody(((PhysicsComponent)component).body);
                    ((PhysicsComponent)component).body.setUserData(null);
                    ((PhysicsComponent)component).body.delete();
                    ((PhysicsComponent)component).body = null;
                }
            }
            GameObject.createBulldozer(coordinateX,coordinateY,gameWorld,direction,context,componentsAi);
            verifyAction = false;
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
            objects.remove((GameObject) event.b.owner);
            listBarrel.remove((GameObject) event.b.owner);
            world.destroyBody(event.b.body);
            event.b.body.setUserData(null);
            event.b.body.delete();
            event.b.body = null;
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("incinerator")){
            objects.remove((GameObject) event.a.owner);
            listBarrel.remove((GameObject) event.a.owner);
            world.destroyBody(event.a.body);
            event.a.body.setUserData(null);
            event.a.body.delete();
            event.a.body = null;
        }
    }

    public void handleAddBarrel(Collision event){
        if(event.a.name.equals("ground")  && event.b.name.equals("barrel")){
            if(!listBarrel.contains((GameObject)event.b.owner)){
                listBarrel.add((GameObject)event.b.owner);
            }
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("ground")){
            if(!listBarrel.contains((GameObject)event.b.owner)){
                listBarrel.add((GameObject)event.a.owner);
            }
        }
    }


    private void accelerationAndDeceleration(){
        if(bulldozer.body.getAngle()>=1.6f || bulldozer.body.getAngle() < 1.4f) {
            int invert = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
            if (invert == -1 && bulldozer.body.getAngle() < 1.4f) {
                flag=true;
                for (Component c : bulldozer.owner.getComponent(ComponentType.Joint)) {
                    if (c instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                            ((RevoluteJointComponent) c).joint.setMaxMotorTorque(0f);
                    }
                }

            }
            else if (invert == +1 && bulldozer.body.getAngle() >= 1.6f) {
                flag=true;
                for (Component c : bulldozer.owner.getComponent(ComponentType.Joint)) {
                    if (c instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                            ((RevoluteJointComponent) c).joint.setMaxMotorTorque(0f);
                    }
                }
            }
            else if(flag) {
                flag=false;
                for (Component c : bulldozer.owner.getComponent(ComponentType.Joint)) {
                    if (c instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                            ((RevoluteJointComponent) c).joint.setMaxMotorTorque(100f);
                    }

                }
            }

            if (speed >= 5) {
                speed = speed - 0.05f;
            }

            for (Component c : bulldozer.owner.getComponent(ComponentType.Joint)) {
                if (c instanceof RevoluteJointComponent) {
                    if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                        ((RevoluteJointComponent) c).joint.setMotorSpeed(invert * speed);
                }
            }
        }

        else{
            if(speed<=10){
                speed=speed+0.05f;
            }
            int invert = ((DynamicPositionComponent)bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
            for(Component c:bulldozer.owner.getComponent(ComponentType.Joint)){
                if(c instanceof RevoluteJointComponent) {
                    if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                        ((RevoluteJointComponent) c).joint.setMotorSpeed(invert*speed);
                         ((RevoluteJointComponent) c).joint.setMaxMotorTorque(100f);

                }
            }

        }
    }

    protected int searchBarrel(){
        int contRight = 0;
        int contLeft = 0;
        float positionYBulldozer = bulldozer.body.getPositionY();
        if(listBarrel.size() == 0){
            return 0;
        }else{
            for (GameObject g: listBarrel) {
                List<Component> positionComponents = g.getComponent(ComponentType.Position);
                PositionComponent positionComponent = (PositionComponent) positionComponents.get(0);
                    if(positionYBulldozer > positionComponent.getCoordinateY() ){
                        contLeft = contLeft+1;

                    }else{
                        contRight= contRight+1;
                    }
            }
            System.out.println("left = "+contLeft);
            System.out.println("right = "+contRight);

            if(contLeft > contRight){
                return -1;
            }else{
                return 1;
            }
        }
    }



    protected void burnedBarrel(int direction,GameWorld gameWorld,Activity context){
        int invert = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
        float angle =bulldozer.body.getAngle();
        float positionYBulldozer = bulldozer.body.getPositionY();
        if(invert != direction){
            float positionXBulldozer = bulldozer.body.getPositionX();
            rotationBulldozer(positionXBulldozer,positionYBulldozer,gameWorld,direction,context);
        }

        for(Component c:bulldozer.owner.getComponent(ComponentType.Joint)){
            if(c instanceof RevoluteJointComponent) {
                if (((RevoluteJointComponent) c).joint.isMotorEnabled())
                    ((RevoluteJointComponent) c).joint.setMotorSpeed(direction * speed);
                ((RevoluteJointComponent) c).joint.setMaxMotorTorque(50f);
            }
        }
    }

    protected void moveToCenter(GameWorld gameWorld, Activity context){
        float positionYBulldozer = bulldozer.body.getPositionY();
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) (gameObjectBulldozer).getComponent(ComponentType.Position).get(0);
        int direction = (dynamicPositionComponent.direction);


        if(direction == -1){
            if(positionYBulldozer >= direction *(0.5f) && positionYBulldozer < (1.8f)){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) componentBulldozer).joint.isMotorEnabled())
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(direction * speed);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                    }
                }
            }else{
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) componentBulldozer).joint.isMotorEnabled())
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(direction * speed);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(50f);
                    }
                }
            }
        }else{
            if(positionYBulldozer <= direction *(3f) && positionYBulldozer > (-2f)){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) componentBulldozer).joint.isMotorEnabled())
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(direction * speed);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                    }
                }
            }else{
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        if (((RevoluteJointComponent) componentBulldozer).joint.isMotorEnabled())
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(direction * speed);
                        ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(50f);
                    }
                }
            }
        }
    }













}
