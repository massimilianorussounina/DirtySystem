package com.example.dirtsystemec;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class GameWorld {
    public final int[] maxScore = {500,800,1000,1500,2500,3500,5000,7500,11000,15000};
    private int numberBarrel = 5;
    private long score = 0,lastScore=0;
    private long timeZeroBarrel;
    private int level=1;
    final static float bufferWidth = 1080, bufferHeight = 1920;    // actual pixels
    Bitmap buffer;
    private final Canvas canvas;
    //private BulldozerPhysicsComponent bulldozer;
    private boolean buttonTrash = true, gameOverFlag=false;
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
    private HandlerUI handlerUI;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    final Activity activity;
    private Bitmap bitmap;
    private final RectF dest = new RectF();
    boolean flagCollisionBarrel = false;
    float speed=7f;
    // Arguments are in physical simulation units.
    private GameObject gameOver;
    private TextDrawableComponent timerTex,numberBarrelText,textScore;
    private long startTime,currentTime,maxTime=300000,timerPause=0,timeResume=0;



    public GameWorld(Box physicalSize, Box screenSize, Activity theActivity,HandlerUI handlerUI) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.activity = theActivity;
        this.handlerUI=handlerUI;
        this.buffer = Bitmap.createBitmap((int)bufferWidth,(int) bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World( 0.0f, 0.0f);  // gravity vector
        this.currentView = physicalSize;
        this.numFps = 0;
        this.startTime= System.currentTimeMillis();
        // stored to prevent GC
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        touchConsumer = new TouchConsumer(this);

        this.objects = new ArrayList<>();
        this.listBarrel = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        Rect src = new Rect();
        src.set(0,0,(int)bufferHeight,1080);
    }

    public synchronized void update(float elapsedTime) {
        float positionYBulldozer;

        GameObject gameObjectBulldozer = null;
        numFps++;

        /*    Inizio Fase AI   */
        if(!gameOverFlag) {
            positionYBulldozer = bulldozer.body.getPositionY();
            int direction = ((DynamicPositionComponent) bulldozer.owner.getComponent(ComponentType.Position).get(0)).direction;
            if (positionYBulldozer > 19.9f && direction == 1) {
                System.out.println(positionYBulldozer);
                rotationBulldozer(-8f, positionYBulldozer, this, -1, activity);
                verifyAction = false;
            } else if (positionYBulldozer < -19.9f && direction == -1) {
                rotationBulldozer(-8f, positionYBulldozer, this, 1, activity);
                verifyAction = false;
            }


            if (numFps == 10) {
                if (timeResume != 0 && timerPause != 0) {
                    startTime = startTime + (timeResume - timerPause);
                    timeResume = 0;
                    timerPause = 0;
                }
                if (currentTime >= 0) {
                    currentTime = maxTime - (System.currentTimeMillis() - startTime);
                    timerTex.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(currentTime),
                            TimeUnit.MILLISECONDS.toSeconds(currentTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTime))
                    ));
                } else if (!gameOverFlag) {
                    deleteBulldozer();
                    gameOverFlag = true;
                    gameOver = GameObject.createTextGameOver(0, -7.5f, this, "GAME OVER");
                }

                if (!gameOverFlag)
                    score = (long) (score + (listBarrel.size() * 1.5f));
                if (score >= maxScore[level]) {
                    level = level + 1;
                    if (level > 10) {
                        deleteBulldozer();
                        gameOverFlag = true;
                        gameOver = GameObject.createTextGameOver(0, -7.5f, this, "WIN");

                    }
                    speed = speed + (level * 0.20f);
                    numberBarrel = numberBarrel + (5 * level);
                }
                if ((score - lastScore) > (maxScore[level] / 10)) {
                    numberBarrel = numberBarrel + 1;
                    Log.i("lastscore", ": " + (score - lastScore));
                    lastScore = score;
                    Log.i("agg barrel", ": " + (100 * level));

                }
                if (System.currentTimeMillis() - timeZeroBarrel >= 3000 && numberBarrel == 0) {
                    numberBarrel = numberBarrel + level;
                }
                numberBarrelText.setText(String.format("%02d", numberBarrel));
                textScore.setText("Score: " + score);
            }

            if (listBarrel.size() == 0) {
                verifyAction = false;
            }

            if (numFps == 10) {

                if (!verifyAction) {
                    for (GameObject gameObject : objects) {
                        if (gameObject.name.equals("bulldozer")) {
                            gameObjectBulldozer = gameObject;
                            break;
                        }
                    }
                    if (gameObjectBulldozer != null) {
                        List<Component> components = gameObjectBulldozer.getComponent(ComponentType.AI);
                        if (components != null) {
                            for (Component component : components) {
                                Action action = ((FsmAIComponent) component).fsm.stepAndGetAction(this);
                                if (action != null) {
                                    if (action.equals(Action.burned)) {
                                        int result = searchBarrel();
                                        burnedBarrel(result, this, activity);
                                        verifyAction = true;
                                    } else if (action.equals(Action.waited)) {
                                        moveToCenter(this, activity);
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
            /* update Timer */

            for (Input.TouchEvent event : touchHandler.getTouchEvents())
                touchConsumer.consumeTouchEvent(event);
        }
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

    public synchronized void addGameObject(GameObject obj) {

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
        }else if (obj.name!= null && obj.name.equals("timer")){
            timerTex=(TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            objects.add(obj);
        }
        else if (obj.name!= null && obj.name.equals("numberBarrel")){
            numberBarrelText=(TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            objects.add(obj);
        }
        else if(obj.name!= null && obj.name.equals("textscore")){
            textScore=(TextDrawableComponent)obj.getComponent(ComponentType.Drawable).get(0);
            objects.add(obj);
        }
        else
        {
            objects.add(obj);
        }

    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {

            if (event.a.name.equals("barrel")) {

                if (!listBarrel.contains((GameObject) event.a.owner)) {
                    flagCollisionBarrel = false;
                    listBarrel.add((GameObject) event.a.owner);
                    handleSoundCollisions(event);
                    if (event.b.name.equals("incinerator")) handleDeleteBarrel(event);
                } else if (event.b.name.equals("incinerator")) {
                    handleSoundCollisions(event);
                    handleDeleteBarrel(event);
                } else {
                    handleSoundCollisions(event);
                }
            } else if (event.b.name.equals("barrel")) {
                if (!listBarrel.contains((GameObject) event.b.owner)) {
                    flagCollisionBarrel = false;
                    listBarrel.add((GameObject) event.b.owner);
                    handleSoundCollisions(event);
                    if (event.a.name.equals("incinerator")) handleDeleteBarrel(event);
                } else if (event.a.name.equals("incinerator")) {
                    handleSoundCollisions(event);
                    handleDeleteBarrel(event);
                } else {
                    handleSoundCollisions(event);
                }
            }
        }
    }

    private void handleSoundCollisions(Collision event){
        Sound sound = CollisionSounds.getSound(((GameObject)event.a.owner).name, ((GameObject)event.b.owner).name);

        if (sound!=null) {
            sound.play(MainActivity.volumeSoundEffect);
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

    private void deleteBulldozer() {
        List<Component> componentsAi = null;
        List<Component> componentsPhysics = null;
        GameObject gameObjectBulldozer = null;

        for (GameObject gameObject : objects) {
            if (gameObject.name.equals("bulldozer")) {
                gameObjectBulldozer = gameObject;
                break;
            }
        }

        if (gameObjectBulldozer != null) {
            componentsAi = gameObjectBulldozer.getComponent(ComponentType.AI);
            componentsPhysics = gameObjectBulldozer.getComponent(ComponentType.Physics);
            objects.remove(gameObjectBulldozer);

            if (componentsPhysics != null) {
                for (Component component : componentsPhysics) {
                    world.destroyBody(((PhysicsComponent) component).body);
                    ((PhysicsComponent) component).body.setUserData(null);
                    ((PhysicsComponent) component).body.delete();
                    ((PhysicsComponent) component).body = null;
                }
            }
        }
    }

    // Conversions between screen coordinates and physical coordinates

    public float toMetersX(float x) { return currentView.xmin + x * (currentView.width/screenSize.width); }
    public float toMetersY(float y) { Log.d("Metric Conversion View",""+ canvas.getClipBounds().bottom);
    float metricY,offsetY;
    metricY=(currentView.ymin + (y * (currentView.height/ screenSize.height)));
    offsetY=Math.abs(metricY*((screenSize.height/bufferHeight)/100));
    if(metricY >=0)
        offsetY=offsetY*-1;
    return metricY;}


    public float toPixelsX(float x) { return (x-currentView.xmin)/currentView.width*bufferWidth; }
    public float toPixelsY(float y) {return ((y-currentView.ymin)/currentView.height)*bufferHeight;}


    public float toPixelsXLength(float x) {
        return x/currentView.width*bufferWidth;
    }
    public float toPixelsYLength(float y) {
        return y/currentView.height*bufferHeight;
    }
    public synchronized void setGravity(float x, float y) {
        world.setGravity(x, y);
    }

    @Override
    public void finalize() {
        world.delete();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void eventTouch(float coordinateX, float coordinateY){
        if(!gameOverFlag) {
            if ((coordinateX <= 13.5f && coordinateX >= 9f) && (coordinateY >= 23f && coordinateY <= 26)) {
                handlerUI.sendEmptyMessage(0);
            }else if(!flagCollisionBarrel && ((coordinateY>=-22 && coordinateY <=21.6))){
                if (numberBarrel == 1)
                    timeZeroBarrel = System.currentTimeMillis();
                if (numberBarrel > 0) {
                    flagCollisionBarrel = true;
                    GameObject.createBarrel(13f, coordinateY, this);
                    numberBarrel = numberBarrel - 1;
                    numberBarrelText.setText(String.format("%02d", numberBarrel));
                }
            }
        }
        else{
            ArrayList<GameObject> temp = new ArrayList<GameObject>();
            for(GameObject obj: objects){
                if(!obj.name.equals("barrel"))
                    temp.add(obj);
            }
            objects=temp;
            GameObject.createBulldozer(-6.6f,0,this,-1,activity,null);
            score=0;
            startTime= System.currentTimeMillis();
            currentTime= maxTime - (System.currentTimeMillis() - startTime);
            numberBarrel=5;
            objects.remove(gameOver);
            level=1;
            gameOverFlag=false;
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
            if(listBarrel.size() == 0){
                verifyAction = false;
            }
        }else if(event.a.name.equals("barrel")  && event.b.name.equals("incinerator")){
            objects.remove((GameObject) event.a.owner);
            listBarrel.remove((GameObject) event.a.owner);
            world.destroyBody(event.a.body);
            event.a.body.setUserData(null);
            event.a.body.delete();
            event.a.body = null;
            if(listBarrel.size() == 0){
                verifyAction = false;
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
                ((RevoluteJointComponent) c).joint.setMaxMotorTorque(20f);
            }
        }
    }

    protected void moveToCenter(GameWorld gameWorld, Activity context){
        float positionYBulldozer = bulldozer.body.getPositionY();
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) (gameObjectBulldozer).getComponent(ComponentType.Position).get(0);
        int direction = (dynamicPositionComponent.direction);

        if(direction == -1){
            if(positionYBulldozer >= -0.5f && positionYBulldozer < 0.1f){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                    }
                }
            }else{
                if (positionYBulldozer < -0.5f) {
                    rotationBulldozer(-8f, positionYBulldozer, this, 1, activity);
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(20f);;
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(20f);
                        }
                    }
                }
            }
        }else{
            if(positionYBulldozer >= -0.1f && positionYBulldozer < (3f)){
                for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                    if(componentBulldozer instanceof RevoluteJointComponent) {
                        ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(0f);
                    }
                }
            }else{
                if (positionYBulldozer > 3) {
                    rotationBulldozer(-8f, positionYBulldozer, this, -1, activity);
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(-speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(20f);
                        }
                    }
                }else{
                    for(Component componentBulldozer :gameObjectBulldozer.getComponent(ComponentType.Joint)){
                        if(componentBulldozer instanceof RevoluteJointComponent) {
                            ((RevoluteJointComponent) componentBulldozer).joint.setMotorSpeed(speed);
                            ((RevoluteJointComponent) componentBulldozer).joint.setMaxMotorTorque(20f);
                        }
                    }
                }
            }
        }
    }


    public void setTimerPause(long time){
        this.timerPause=time;
    }

    public void setTimeResume(long time){
        this.timeResume=time;
    }


    public long getScore() {
        return score;
    }
}
