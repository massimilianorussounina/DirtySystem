package com.example.dirtsystemec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.fpl.liquidfun.BodyType;

public class GameObject extends Entity{

    protected String name;
    protected GameWorld gameWorld;


    GameObject(GameWorld gameWorld){
        super();
        this.gameWorld = gameWorld;
    }
    GameObject(GameWorld gameWorld,String name){
        super();
        this.name= name;
        this.gameWorld = gameWorld;
    }

    public static void createBarrel(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectBarrel = new GameObject(gameWorld);

        DynamicPositionComponent dynamicPositionComponent = new DynamicPositionComponent("barrel",coordinateX,coordinateY,gameObjectBarrel);
        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("barrel",gameObjectBarrel,0.8f,0.8f,R.drawable.barrel,0,0,187,184);
        CirclePhysicsComponent circlePhysicsComponent = new CirclePhysicsComponent("barrel",gameObjectBarrel,BodyType.dynamicBody,coordinateX,coordinateY,bitmapDrawableComponent.width/2f,bitmapDrawableComponent.height/2f,0.5f,0.1f,1f);


        gameObjectBarrel.addComponent(dynamicPositionComponent);
        gameObjectBarrel.addComponent(bitmapDrawableComponent);
        gameObjectBarrel.addComponent(circlePhysicsComponent);


        gameWorld.addGameObject(gameObjectBarrel);
    }


    public static void createIncinerator(float coordinateX, float coordinateY,float fireCoordinateX, float fireCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectIncinerator = new GameObject(gameWorld);

        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("incinerator",gameObjectIncinerator,2.5f,2.7f,R.drawable.incinerator,0,0,37,54);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("incinerator",coordinateX,coordinateY,gameObjectIncinerator);
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("incinerator",gameObjectIncinerator,BodyType.staticBody,coordinateX,coordinateY, bitmapDrawableComponent.width, bitmapDrawableComponent.height, 2f);

        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.fire_spritesheet,bitmapFactory );
        SpriteDrawableComponent spriteDrawableComponent = new SpriteDrawableComponent("incinerator",gameObjectIncinerator,new FireSprite(gameWorld,new Spritesheet(bitmap,8),fireCoordinateX,fireCoordinateY,
                2.5f,2.0f,49,75,200,8));


        gameObjectIncinerator.addComponent(spriteDrawableComponent);
        gameObjectIncinerator.addComponent(staticPositionComponent);
        gameObjectIncinerator.addComponent(bitmapDrawableComponent);
        gameObjectIncinerator.addComponent(polygonPhysicsComponent);
        gameWorld.addGameObject(gameObjectIncinerator);
    }


    public static void createSea(float coordinateX, float coordinateY,float seaCoordinateX, float seaCoordinateY,GameWorld gameWorld) {

        GameObject gameObjectSea = new GameObject(gameWorld);

        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("sea",coordinateX,coordinateY,gameObjectSea);

        BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
        bitmapFactory.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(gameWorld.activity.getResources(), R.drawable.sea,bitmapFactory );
        SpriteDrawableComponent spriteDrawableComponent = new SpriteDrawableComponent("sea",gameObjectSea,new SeaSprite(gameWorld,new Spritesheet(bitmap,15),seaCoordinateX,seaCoordinateY,
                5.3f,2.0f,500,108,500,15));
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("sea",gameObjectSea,BodyType.staticBody,coordinateX,coordinateY,spriteDrawableComponent.width, spriteDrawableComponent.height,2f);

        gameObjectSea.addComponent(staticPositionComponent);
        gameObjectSea.addComponent(spriteDrawableComponent);
        gameObjectSea.addComponent(polygonPhysicsComponent);


        gameWorld.addGameObject(gameObjectSea);
    }


    public static void createGround(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectGround = new GameObject(gameWorld);
        StaticPositionComponent staticPositionComponent = new StaticPositionComponent("ground",coordinateX,coordinateY,gameObjectGround);
        BitmapDrawableComponent bitmapDrawableComponent = new BitmapDrawableComponent("ground",gameObjectGround,9.0f,4.0f,R.drawable.ground,0,0,200,82);
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("ground",gameObjectGround,BodyType.staticBody,coordinateX,coordinateY, bitmapDrawableComponent.width/2, bitmapDrawableComponent.height/2, 1f,0.1f,0.1f);


        gameObjectGround.addComponent(staticPositionComponent);
        gameObjectGround.addComponent(bitmapDrawableComponent);
        gameObjectGround.addComponent(polygonPhysicsComponent);

        gameWorld.addGameObject(gameObjectGround);
    }


    public static void createBridge(float bridgeCoordinateX, float bridgeCoordinateY,
                                    float towerCoordinateXRight, float towerCoordinateYRight,
                                    float towerBodyCoordinateX1Right, float towerBodyCoordinateY1Right,
                                    float towerBodyCoordinateX2Right, float towerBodyCoordinateY2Right,
                                    float towerBodyCoordinateX3Right, float towerBodyCoordinateY3Right,
                                    float towerCoordinateXLeft, float towerCoordinateYLeft,
                                    float towerBodyCoordinateX1Left, float towerBodyCoordinateY1Left,
                                    float towerBodyCoordinateX2Left, float towerBodyCoordinateY2Left,
                                    float towerBodyCoordinateX3Left, float towerBodyCoordinateY3Left,
                                    GameWorld gameWorld){


        /* Costruzione torre right */

        GameObject gameObjectTowerRight = new GameObject(gameWorld);
        TrianglePositionComponent trianglePositionComponentRight = new TrianglePositionComponent("torreRight",towerCoordinateXRight,towerCoordinateYRight,towerBodyCoordinateX1Right,towerBodyCoordinateY1Right,
                towerBodyCoordinateX2Right,towerBodyCoordinateY2Right,towerBodyCoordinateX3Right,towerBodyCoordinateY3Right,gameObjectTowerRight);
        BitmapDrawableComponent bitmapDrawableComponentRight = new BitmapDrawableComponent("torreRight",gameObjectTowerRight,4.0f,2.0f,R.drawable.right_bridge,0,0,423,208);
        PolygonPhysicsComponent polygonPhysicsComponentRight = new PolygonPhysicsComponent("torreRight",gameObjectTowerRight, BodyType.staticBody,towerCoordinateXRight,towerCoordinateYRight,
                towerBodyCoordinateX1Right,towerBodyCoordinateY1Right,towerBodyCoordinateX2Right,towerBodyCoordinateY2Right,towerBodyCoordinateX3Right,towerBodyCoordinateY3Right,
                1f,0.4f,10f);

        gameObjectTowerRight.addComponent(trianglePositionComponentRight);
        gameObjectTowerRight.addComponent(bitmapDrawableComponentRight);
        gameObjectTowerRight.addComponent(polygonPhysicsComponentRight);
        gameWorld.addGameObject(gameObjectTowerRight);




        /* Costruzione torre left */

        GameObject gameObjectTowerLeft = new GameObject(gameWorld);
        TrianglePositionComponent trianglePositionComponentLeft = new TrianglePositionComponent("torreLeft",towerCoordinateXLeft,towerCoordinateYLeft,towerBodyCoordinateX1Left,towerBodyCoordinateY1Left,
                towerBodyCoordinateX2Left,towerBodyCoordinateY2Left,towerBodyCoordinateX3Left,towerBodyCoordinateY3Left,gameObjectTowerLeft);
        BitmapDrawableComponent bitmapDrawableComponentLeft = new BitmapDrawableComponent("torreLeft",gameObjectTowerLeft,4.0f,2.0f,R.drawable.left_bridge,0,0,423,208);
        PolygonPhysicsComponent polygonPhysicsComponentLeft = new PolygonPhysicsComponent("torreLeft",gameObjectTowerLeft, BodyType.staticBody,towerCoordinateXLeft,towerCoordinateYLeft,
                towerBodyCoordinateX1Left,towerBodyCoordinateY1Left,towerBodyCoordinateX2Left,towerBodyCoordinateY2Left,towerBodyCoordinateX3Left,towerBodyCoordinateY3Left,
                1f,0.4f,10f);
        gameObjectTowerLeft.addComponent(trianglePositionComponentLeft);
        gameObjectTowerLeft.addComponent(bitmapDrawableComponentLeft);
        gameObjectTowerLeft.addComponent(polygonPhysicsComponentLeft);
        gameWorld.addGameObject(gameObjectTowerLeft);





        /* Costruzione ponte */

        GameObject gameObjectBridge = new GameObject(gameWorld);
        DynamicPositionComponent dynamicPositionComponent = new DynamicPositionComponent("bridge",bridgeCoordinateX,bridgeCoordinateY,gameObjectBridge);
        RectDrawableComponent rectDrawableComponent = new RectDrawableComponent("bridge",gameObjectBridge,4.8f,0.2f, Color.argb(255, 32, 32, 32));
        PolygonPhysicsComponent polygonPhysicsComponent = new PolygonPhysicsComponent("bridge",gameObjectBridge,BodyType.staticBody,bridgeCoordinateX,bridgeCoordinateY,rectDrawableComponent.width/2, rectDrawableComponent.height/2,4f,0.4f,0.4f);
        gameObjectBridge.addComponent(dynamicPositionComponent);
        gameObjectBridge.addComponent(rectDrawableComponent);
        gameObjectBridge.addComponent(polygonPhysicsComponent);
        gameWorld.addGameObject(gameObjectBridge);

    }



    public static void createEnclosure(float coordinateXMax, float coordinateXMin,float coordinateYMax,float coordinateYMin,GameWorld gameWorld){
        GameObject gameObjectEnclosure = new GameObject(gameWorld);
        PolygonPhysicsComponent polygonPhysicsComponentTop = new PolygonPhysicsComponent("top",gameObjectEnclosure,BodyType.staticBody,
                (coordinateXMin + (coordinateXMax - coordinateXMin)/2), coordinateYMin,(coordinateXMax - coordinateXMin),0.1f, 0,1);


        PolygonPhysicsComponent polygonPhysicsComponentBottom =  new PolygonPhysicsComponent("bot",gameObjectEnclosure,BodyType.staticBody,
                (coordinateXMin + (coordinateXMax - coordinateXMin)/2), coordinateYMax,(coordinateXMax - coordinateXMin),0.1f, 0,1);



        PolygonPhysicsComponent polygonPhysicsComponentLeft = new PolygonPhysicsComponent("left",gameObjectEnclosure,BodyType.staticBody,
                coordinateXMin, (coordinateYMin + (coordinateYMax - coordinateYMin)/2),0.1f,(coordinateYMax - coordinateYMin), 0,1);


        PolygonPhysicsComponent polygonPhysicsComponentRight = new PolygonPhysicsComponent("right",gameObjectEnclosure,BodyType.staticBody,
                coordinateXMax, (coordinateYMin + (coordinateYMax - coordinateYMin)/2),0.1f,(coordinateYMax - coordinateYMin), 0,1);

        gameObjectEnclosure.addComponent(polygonPhysicsComponentTop);
        gameObjectEnclosure.addComponent(polygonPhysicsComponentBottom);
        gameObjectEnclosure.addComponent(polygonPhysicsComponentLeft);
        gameObjectEnclosure.addComponent(polygonPhysicsComponentRight);
        gameWorld.addGameObject(gameObjectEnclosure);
    }




    public static void createBulldozer(float coordinateX, float coordinateY,GameWorld gameWorld,int invert){
        float width=2.8f;
        GameObject gameObjectBulldozer = new GameObject(gameWorld,"bulldozer");

        PolygonPhysicsComponent polygonPhysicsComponentChassis = new PolygonPhysicsComponent("chassis",gameObjectBulldozer,BodyType.dynamicBody
        ,coordinateX,coordinateY,(width/2),proportionalToBulldozer(1.2f,width)/2,4f,0f,1f);

        gameObjectBulldozer.addComponent(polygonPhysicsComponentChassis);

        PolygonPhysicsComponent polygonPhysicsComponentCabin = new PolygonPhysicsComponent("cabin",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,(proportionalToBulldozer(2.3f,width))/2,proportionalToBulldozer(1.5f,width)/2,2f,0.1f,1f);

        gameObjectBulldozer.addComponent(polygonPhysicsComponentCabin);


        RevoluteJointComponent revoluteJointComponentBulldozer =  new RevoluteJointComponent(gameObjectBulldozer,polygonPhysicsComponentChassis.body,
                polygonPhysicsComponentCabin.body,(invert*-proportionalToBulldozer(0.5f,width)),-proportionalToBulldozer(1.4f,width),0,0,0,
                0,0);
        gameObjectBulldozer.addComponent(revoluteJointComponentBulldozer);

        RectDrawableComponent rectDrawableComponentChassis = new RectDrawableComponent("chassis",gameObjectBulldozer,width,proportionalToBulldozer(1.2f,width), Color.argb(50, 32, 32, 32));
        RectDrawableComponent rectDrawableComponentCabin = new RectDrawableComponent("cabin",gameObjectBulldozer,proportionalToBulldozer(2.3f,width),proportionalToBulldozer(1.5f,width), Color.argb(255, 32, 32, 32));


        gameObjectBulldozer.addComponent(rectDrawableComponentCabin);
        gameObjectBulldozer.addComponent(rectDrawableComponentChassis);



        PolygonPhysicsComponent polygonPhysicsComponentDamperOne = new PolygonPhysicsComponent("damperOne",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(1.5f,width)/2,4f,0.1f,0.5f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentDamperOne);

        PrismaticJointComponet prismaticJointComponet = new PrismaticJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperOne.body,-width/2+proportionalToBulldozer(0.5f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0,1,false,false,0,polygonPhysicsComponentDamperOne.body.getMass()*8.5f );

        gameObjectBulldozer.addComponent(prismaticJointComponet);

        DistanceJointComponet distanceJointComponet = new DistanceJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperOne.body,-width/2+proportionalToBulldozer(0.5f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0.7f,4f,-proportionalToBulldozer(0.2f,width));
        gameObjectBulldozer.addComponent(distanceJointComponet);

        RectDrawableComponent rectDrawableComponentDamperOne = new RectDrawableComponent("damperOne",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(1.5f,width),Color.argb(255,4,255,21));
        gameObjectBulldozer.addComponent(rectDrawableComponentDamperOne);



        PolygonPhysicsComponent polygonPhysicsComponentDamperTwo = new PolygonPhysicsComponent("damperTwo",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(1.5f,width)/2,4f,0.1f,0.5f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentDamperTwo);

        PrismaticJointComponet prismaticJointComponentDamperTwo = new PrismaticJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperTwo.body,+width/2-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0,0.5f,false,true,0,polygonPhysicsComponentDamperTwo.body.getMass()*8.5f );

        gameObjectBulldozer.addComponent(prismaticJointComponentDamperTwo);

        DistanceJointComponet distanceJointComponentDamperTwo = new DistanceJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperTwo.body,+width/2-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0.7f,4f,-proportionalToBulldozer(0.2f,width));
        gameObjectBulldozer.addComponent(distanceJointComponentDamperTwo);

        RectDrawableComponent rectDrawableComponentDamper2 = new RectDrawableComponent("damperTwo",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(1.5f,width),Color.argb(255,4,255,21));
        gameObjectBulldozer.addComponent(rectDrawableComponentDamper2);



        PolygonPhysicsComponent polygonPhysicsComponentDamperThree = new PolygonPhysicsComponent("damperThree",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(1.5f,width)/2,4f,0.1f,0.5f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentDamperThree);

        PrismaticJointComponet prismaticJointComponentThree = new PrismaticJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperThree.body,+width/2-(proportionalToBulldozer(0.5f,width)*2)- proportionalToBulldozer(0.6f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0,1,false,false,0,polygonPhysicsComponentDamperThree.body.getMass()*8.5f );

        gameObjectBulldozer.addComponent(prismaticJointComponentThree);

        DistanceJointComponet distanceJointComponentThree = new DistanceJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperThree.body,+width/2-(proportionalToBulldozer(0.5f,width)*2)- proportionalToBulldozer(0.6f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0.7f,4f,-proportionalToBulldozer(0.2f,width));
        gameObjectBulldozer.addComponent(distanceJointComponentThree);

        RectDrawableComponent rectDrawableComponentDamperThree = new RectDrawableComponent("damperThree",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(1.5f,width),Color.argb(255,4,255,21));
        gameObjectBulldozer.addComponent(rectDrawableComponentDamperThree);


        PolygonPhysicsComponent polygonPhysicsComponentDamperFour = new PolygonPhysicsComponent("damperFour",gameObjectBulldozer,BodyType.dynamicBody
                ,coordinateX,coordinateY,proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(1.5f,width)/2,4f,0.1f,0.5f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentDamperFour);

        PrismaticJointComponet prismaticJointComponentFour = new PrismaticJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperFour.body,-width/2+(proportionalToBulldozer(0.5f,width)*2)+ proportionalToBulldozer(0.6f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0,1,false,false,0,polygonPhysicsComponentDamperFour.body.getMass()*8.5f );

        gameObjectBulldozer.addComponent(prismaticJointComponentFour);

        DistanceJointComponet distanceJointComponentFour = new DistanceJointComponet(gameObjectBulldozer, polygonPhysicsComponentChassis.body,polygonPhysicsComponentDamperFour.body,-width/2+(proportionalToBulldozer(0.5f,width)*2)+ proportionalToBulldozer(0.6f,width),proportionalToBulldozer(3.5f,width)/2+proportionalToBulldozer(0.3f,width),0,
                proportionalToBulldozer(0.9f,width),0.7f,4f,-proportionalToBulldozer(0.2f,width));
        gameObjectBulldozer.addComponent(distanceJointComponentFour);

        RectDrawableComponent rectDrawableComponentDamperFour = new RectDrawableComponent("damperFour",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(1.5f,width),Color.argb(255,4,255,21));
        gameObjectBulldozer.addComponent(rectDrawableComponentDamperFour);




        CirclePhysicsComponent circlePhysicsComponentWheelOne = new CirclePhysicsComponent("wheelOne",gameObjectBulldozer,BodyType.dynamicBody,coordinateX -(proportionalToBulldozer(3.5f,width)/2),coordinateY+(width/2)-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),4,0.1f,1f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentWheelOne);

        RevoluteJointComponent revoluteJointComponentOne = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentDamperOne.body, circlePhysicsComponentWheelOne.body,0,proportionalToBulldozer(0.7f,width),0,0,100f,true,invert*10f);
        gameObjectBulldozer.addComponent(revoluteJointComponentOne);

        CircleDrawableComponent circleDrawableComponentOne= new CircleDrawableComponent("wheelOne",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(255,255,0,0));
        gameObjectBulldozer.addComponent(circleDrawableComponentOne);

        CirclePhysicsComponent circlePhysicsComponentWheelTwo = new CirclePhysicsComponent("wheelTwo",gameObjectBulldozer,BodyType.dynamicBody,coordinateX -(proportionalToBulldozer(3.5f,width)/2),coordinateY+(width/2)-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),4,0.1f,1f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentWheelTwo);

        RevoluteJointComponent revoluteJointComponentTwo = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentDamperTwo.body, circlePhysicsComponentWheelTwo.body,0,proportionalToBulldozer(0.7f,width),0,0,100f,true,invert*10f);
        gameObjectBulldozer.addComponent(revoluteJointComponentTwo);

        CircleDrawableComponent circleDrawableComponentTwo = new CircleDrawableComponent("wheelTwo",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(255,255,0,0));
        gameObjectBulldozer.addComponent(circleDrawableComponentTwo);

        CirclePhysicsComponent circlePhysicsComponentWheelThree = new CirclePhysicsComponent("wheelThree",gameObjectBulldozer,BodyType.dynamicBody,coordinateX -(proportionalToBulldozer(3.5f,width)/2),coordinateY+(width/2)-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),4,0.1f,1f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentWheelThree);

        RevoluteJointComponent revoluteJointComponentThree = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentDamperThree.body, circlePhysicsComponentWheelThree.body,0,proportionalToBulldozer(0.7f,width),0,0,100f,true,invert*10f);
        gameObjectBulldozer.addComponent(revoluteJointComponentThree);

        CircleDrawableComponent circleDrawableComponentThree = new CircleDrawableComponent("wheelThree",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(255,255,0,0));
        gameObjectBulldozer.addComponent(circleDrawableComponentThree);

        CirclePhysicsComponent circlePhysicsComponentWheelFour = new CirclePhysicsComponent("wheelFour",gameObjectBulldozer,BodyType.dynamicBody,coordinateX -(proportionalToBulldozer(3.5f,width)/2),coordinateY+(width/2)-proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),proportionalToBulldozer(0.5f,width),4,0.1f,1f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentWheelFour);

        RevoluteJointComponent revoluteJointComponent4 = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentDamperFour.body, circlePhysicsComponentWheelFour.body,0,proportionalToBulldozer(0.7f,width),0,0,100f,true,invert*10f);
        gameObjectBulldozer.addComponent(revoluteJointComponent4);

        CircleDrawableComponent circleDrawableComponentFour = new CircleDrawableComponent("wheelFour",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(255,255,0,0));
        gameObjectBulldozer.addComponent(circleDrawableComponentFour);





        PolygonPhysicsComponent polygonPhysicsComponentShovel1 = new PolygonPhysicsComponent("shovel1",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(1f,width)/2,proportionalToBulldozer(0.5f,width)/2,4f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentShovel1);

        if (invert==1){
            RevoluteJointComponent revoluteJointComponent7 = new RevoluteJointComponent(gameObjectBulldozer,
                    polygonPhysicsComponentChassis.body,
                    polygonPhysicsComponentShovel1.body,
                    invert*(width-proportionalToBulldozer(2,width))+invert*0.05f,
                    -proportionalToBulldozer(1.1f,width),
                    invert*(-proportionalToBulldozer(0.2f,width)),
                    -proportionalToBulldozer(0.6f,width),0,0,-1.5708f);
            gameObjectBulldozer.addComponent(revoluteJointComponent7);
        }
        else{
            RevoluteJointComponent revoluteJointComponent7 = new RevoluteJointComponent(gameObjectBulldozer,
                    polygonPhysicsComponentChassis.body,
                    polygonPhysicsComponentShovel1.body,
                    invert*(width-proportionalToBulldozer(2,width))+invert*0.05f,
                    -proportionalToBulldozer(0.5f,width),
                    invert*(-proportionalToBulldozer(0.2f,width)),
                    0,0,1.5708f,0);
            gameObjectBulldozer.addComponent(revoluteJointComponent7);
        }

        RectDrawableComponent rectDrawableComponent8= new RectDrawableComponent("shovel1",gameObjectBulldozer,proportionalToBulldozer(1f,width),proportionalToBulldozer(0.5f,width),Color.argb(255,50,55,50));
        gameObjectBulldozer.addComponent(rectDrawableComponent8);

        PolygonPhysicsComponent polygonPhysicsComponentShovel2 = new PolygonPhysicsComponent("shovel2",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(0.5f,width)/2,proportionalToBulldozer(2.6f,width)/2,4f);
        gameObjectBulldozer.addComponent(polygonPhysicsComponentShovel2);

        RevoluteJointComponent revoluteJointComponent8 = new RevoluteJointComponent(gameObjectBulldozer, polygonPhysicsComponentShovel1.body,polygonPhysicsComponentShovel2.body,0,0,invert*-proportionalToBulldozer(0.5f,width),-proportionalToBulldozer(1f,width),0,0,0);
        gameObjectBulldozer.addComponent(revoluteJointComponent8);

        RectDrawableComponent rectDrawableComponent9= new RectDrawableComponent("shovel2",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),proportionalToBulldozer(2.6f,width),Color.argb(255,50,55,50));
        gameObjectBulldozer.addComponent(rectDrawableComponent9);

        CirclePhysicsComponent circlePhysicsComponentShovel = new CirclePhysicsComponent("wheelshovel",gameObjectBulldozer,BodyType.dynamicBody,coordinateX,coordinateY+invert*proportionalToBulldozer(3f,width),proportionalToBulldozer(1f,width)/2,0,1,0.1f,4f);
        gameObjectBulldozer.addComponent(circlePhysicsComponentShovel);

        RevoluteJointComponent revoluteJointComponentShovel = new RevoluteJointComponent(gameObjectBulldozer,polygonPhysicsComponentShovel2.body,circlePhysicsComponentShovel.body,0,proportionalToBulldozer(1.3f,width),0,0,0,true,0);
        gameObjectBulldozer.addComponent(revoluteJointComponentShovel);

        CircleDrawableComponent circleDrawambleComponentShovel = new CircleDrawableComponent("wheelshovel",gameObjectBulldozer,proportionalToBulldozer(0.5f,width),Color.argb(55,0,0,255));
        gameObjectBulldozer.addComponent(circleDrawambleComponentShovel);






        gameWorld.addGameObject(gameObjectBulldozer);


    }

    public static  void createOilStain (float coordinateX, float coordinateY,GameWorld gameWorld){
        GameObject gameObjectOilStain = new GameObject(gameWorld);
        PolygonPhysicsComponent polygonPhysicsComponentOilStain = new PolygonPhysicsComponent("oilstain",gameObjectOilStain,BodyType.staticBody,coordinateX,coordinateY,0.5f,0.1f,4f,0.05f,0f);
        gameObjectOilStain.addComponent(polygonPhysicsComponentOilStain);
        RectDrawableComponent rectDrawableComponentOilStain = new RectDrawableComponent("oilstain",gameObjectOilStain,1,0.1f,Color.argb(255,0,0,0));
        gameObjectOilStain.addComponent(rectDrawableComponentOilStain);
        gameWorld.addGameObject(gameObjectOilStain);

    }

    public static float proportionalToBulldozer(float number,float constant){
        float percent;
        percent = (number * 100) / 4.4f;
        return Math.round(((constant*percent) / 100f) * 100f) / 100f;
    }





   /* public static void createBulldozer1(float coordinateX, float coordinateY,GameWorld gameWorld,int invert) {

        GameObject gameObjectBulldozer = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectWheelSxOne =  new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectWheelDxOne =  new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectWheelSxTwo =  new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectWheelDxTwo =  new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectDamperOne = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectDamperTwo = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectDamperThree = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectDamperFour = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectShovel = new GameObject(gameWorld,"bulldozer");
        GameObject gameObjectWheelShovel = new GameObject(gameWorld,"bulldozer");


        gameObjectBulldozer.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectBulldozer));
        BulldozerDrawableComponent bulldozerDrawableComponent = new BulldozerDrawableComponent(gameObjectBulldozer,invert);
        gameObjectBulldozer.addComponent(bulldozerDrawableComponent);
        BulldozerPhysicsComponent bulldozerPhysicsComponent = new BulldozerPhysicsComponent(gameObjectBulldozer);
        gameObjectBulldozer.addComponent(bulldozerPhysicsComponent);




        gameObjectShovel.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectShovel));
        BulldozerDrawableComponent.ShovelDrawableComponent shovelDrawableComponent=bulldozerDrawableComponent.new ShovelDrawableComponent(gameObjectShovel,bulldozerDrawableComponent);
        BulldozerPhysicsComponent.ShovelPhysicsComponent shovelPhysicsComponent = bulldozerPhysicsComponent.new ShovelPhysicsComponent(gameObjectShovel,bulldozerPhysicsComponent);
        gameObjectShovel.addComponent(shovelDrawableComponent);
        gameObjectShovel.addComponent(shovelPhysicsComponent);
        gameWorld.addGameObject(gameObjectShovel);



        gameObjectDamperOne.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectDamperOne));
        gameObjectDamperOne.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObjectBulldozer));
        gameObjectDamperOne.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamperOne,bulldozerDrawableComponent,bulldozerPhysicsComponent,1));
        gameWorld.addGameObject(gameObjectDamperOne);

        gameObjectDamperTwo.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectDamperTwo));
        gameObjectDamperTwo.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObjectBulldozer));
        gameObjectDamperTwo.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamperTwo,bulldozerDrawableComponent,bulldozerPhysicsComponent,2));
        gameWorld.addGameObject(gameObjectDamperTwo);

        gameObjectWheelSxOne.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectWheelSxOne));
        gameObjectWheelSxOne.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSxOne,bulldozerDrawableComponent));
        gameObjectWheelSxOne.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSxOne,bulldozerDrawableComponent,1,gameObjectDamperOne));
        gameWorld.addGameObject(gameObjectWheelSxOne);


        gameObjectWheelDxOne.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectWheelDxOne));
        gameObjectWheelDxOne.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDxOne,bulldozerDrawableComponent));
        gameObjectWheelDxOne.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDxOne,bulldozerDrawableComponent,2,gameObjectDamperTwo));
        gameWorld.addGameObject(gameObjectWheelDxOne);

        gameObjectDamperThree.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectDamperThree));
        gameObjectDamperThree.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObjectBulldozer));
        gameObjectDamperThree.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamperThree,bulldozerDrawableComponent,bulldozerPhysicsComponent,3));
        gameWorld.addGameObject(gameObjectDamperThree);

        gameObjectDamperFour.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectDamperFour));
        gameObjectDamperFour.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObjectBulldozer));
        gameObjectDamperFour.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamperFour,bulldozerDrawableComponent,bulldozerPhysicsComponent,4));
        gameWorld.addGameObject(gameObjectDamperFour);


        gameObjectWheelDxTwo.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectWheelDxTwo));
        gameObjectWheelDxTwo.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDxTwo,bulldozerDrawableComponent));
        gameObjectWheelDxTwo.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDxTwo,bulldozerDrawableComponent,3,gameObjectDamperThree));
        gameWorld.addGameObject(gameObjectWheelDxTwo);

        gameObjectWheelSxTwo.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectWheelSxTwo));
        gameObjectWheelSxTwo.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSxTwo,bulldozerDrawableComponent));
        gameObjectWheelSxTwo.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSxTwo,bulldozerDrawableComponent,4,gameObjectDamperFour));
        gameWorld.addGameObject(gameObjectWheelSxTwo);

        gameObjectWheelShovel.addComponent(shovelDrawableComponent.new WheelShovelDrawableComponent(gameObjectWheelShovel));
        gameObjectWheelShovel.addComponent(shovelPhysicsComponent.new WheelShovelPhysicsComponent(gameObjectWheelShovel));
        gameWorld.addGameObject(gameObjectWheelShovel);

       gameWorld.addGameObject(gameObjectBulldozer);




    public static void createScoreBar(float scoreBarCoordinateX, float scoreBarCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectScoreBar = new GameObject(gameWorld);
        gameObjectScoreBar.addComponent(new StaticPositionComponent(scoreBarCoordinateX,scoreBarCoordinateY,gameObjectScoreBar));
        gameObjectScoreBar.addComponent(new ScoreBarDrawableComponent(gameObjectScoreBar));
        gameWorld.addGameObject(gameObjectScoreBar);
    }*/
}
