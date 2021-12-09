package com.example.dirtsystemec;

public class GameObject extends Entity{

    protected String name;
    protected GameWorld gameWorld;

    GameObject(GameWorld gameWorld){
        super();
        this.gameWorld = gameWorld;
    }

    public static GameObject createGround(float coordinate_x, float coordinate_y,GameWorld gameWorld) {

        GameObject gameObject = new GameObject(gameWorld);
        gameObject.addComponent(new StaticPositionComponent(coordinate_x,coordinate_y,gameObject));
        gameObject.addComponent(new GroundDrawableComponent(gameObject));
        gameObject.addComponent(new GroundPhysicsComponent(gameObject));
        return gameObject;
    }

    public static GameObject createObstacle( int i,float coordinate_x,float coordinate_y,float x1_local, float y1_local,float x2_local, float y2_local,float x3_local, float y3_local,GameWorld gameWorld){

        GameObject gameObject = new GameObject(gameWorld);
        gameObject.addComponent(new TrianglePositionComponet(coordinate_x,coordinate_y,x1_local,y1_local,x2_local,y2_local,x3_local,y3_local,gameObject));
        gameObject.addComponent(new ObstacleDrawableComponent(gameObject,i));
        gameObject.addComponent(new ObstaclePhysicsComponent(gameObject));
        return gameObject;

    }


    public static GameObject createIncinerator(float coordinate_x, float coordinate_y,float fire_coordinate_x, float fire_coordinate_y,GameWorld gameWorld) {

        GameObject gameObject = new GameObject(gameWorld);
        gameObject.addComponent(new StaticPositionComponent(coordinate_x,coordinate_y,gameObject));
        gameObject.addComponent(new IncineratorDrawableComponent(gameObject,fire_coordinate_x,fire_coordinate_y));
        gameObject.addComponent(new IncineratorPhysicsComponent(gameObject));
        return gameObject;
    }

    public static GameObject createBarrel(float coordinate_x, float coordinate_y,GameWorld gameWorld) {

        GameObject gameObject = new GameObject(gameWorld);
        gameObject.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObject));
        gameObject.addComponent(new BarrelDrawableComponent(gameObject));
        gameObject.addComponent(new BarrelPhysicsComponent(gameObject));
        return gameObject;
    }


    public static GameObject createSea(float coordinate_x, float coordinate_y,float sea_coordinate_x, float sea_coordinate_y,GameWorld gameWorld) {

        GameObject gameObject = new GameObject(gameWorld);
        gameObject.addComponent(new StaticPositionComponent(coordinate_x,coordinate_y,gameObject));
        gameObject.addComponent(new SeaDrawableComponent(gameObject,sea_coordinate_x,sea_coordinate_y));
        gameObject.addComponent(new SeaPhysicsComponent(gameObject));
        return gameObject;
    }

    public static GameObject createBulldozer(float coordinate_x, float coordinate_y,GameWorld gameWorld) {

        GameObject gameObject = new GameObject(gameWorld);
        GameObject gameObjectWheelSx =  new GameObject(gameWorld);
        GameObject gameObjectWheelDx =  new GameObject(gameWorld);
        GameObject gameObjectWheelSx2 =  new GameObject(gameWorld);
        GameObject gameObjectWheelDx2 =  new GameObject(gameWorld);
        GameObject gameObjectDrapper1 = new GameObject(gameWorld);
        GameObject gameObjectDrapper2 = new GameObject(gameWorld);
        GameObject gameObjectDrapper3 = new GameObject(gameWorld);
        GameObject gameObjectDrapper4 = new GameObject(gameWorld);


        gameObject.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObject));
        BulldozerDrawableComponent bulldozerDrawableComponent = new BulldozerDrawableComponent(gameObject);
        gameObject.addComponent(bulldozerDrawableComponent);
        BulldozerPhysicsComponent bulldozerPhysicsComponent = new BulldozerPhysicsComponent(gameObject);
        gameObject.addComponent(bulldozerPhysicsComponent);



        gameObjectDrapper1.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDrapper1));
        gameObjectDrapper1.addComponent(bulldozerDrawableComponent.new DrapperDrawableComponent(gameObject,bulldozerDrawableComponent));
        gameObjectDrapper1.addComponent(bulldozerPhysicsComponent.new DrapperPhysicsComponent(gameObjectDrapper1,bulldozerDrawableComponent,bulldozerPhysicsComponent,1));
        gameWorld.addGameObject(gameObjectDrapper1);

      gameObjectDrapper2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDrapper2));
        gameObjectDrapper2.addComponent(bulldozerDrawableComponent.new DrapperDrawableComponent(gameObject,bulldozerDrawableComponent));
        gameObjectDrapper2.addComponent(bulldozerPhysicsComponent.new DrapperPhysicsComponent(gameObjectDrapper2,bulldozerDrawableComponent,bulldozerPhysicsComponent,2));
        gameWorld.addGameObject(gameObjectDrapper2);

        gameObjectWheelSx.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelSx));
        gameObjectWheelSx.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSx,bulldozerDrawableComponent));
        gameObjectWheelSx.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSx,bulldozerDrawableComponent,bulldozerPhysicsComponent,1,gameObjectDrapper1));
        gameWorld.addGameObject(gameObjectWheelSx);


        gameObjectWheelDx.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelDx));
        gameObjectWheelDx.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDx,bulldozerDrawableComponent));
        gameObjectWheelDx.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDx,bulldozerDrawableComponent,bulldozerPhysicsComponent,2,gameObjectDrapper2));
        gameWorld.addGameObject(gameObjectWheelDx);

       gameObjectDrapper3.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDrapper3));
        gameObjectDrapper3.addComponent(bulldozerDrawableComponent.new DrapperDrawableComponent(gameObject,bulldozerDrawableComponent));
        gameObjectDrapper3.addComponent(bulldozerPhysicsComponent.new DrapperPhysicsComponent(gameObjectDrapper3,bulldozerDrawableComponent,bulldozerPhysicsComponent,3));
        gameWorld.addGameObject(gameObjectDrapper3);

        gameObjectDrapper4.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDrapper4));
        gameObjectDrapper4.addComponent(bulldozerDrawableComponent.new DrapperDrawableComponent(gameObject,bulldozerDrawableComponent));
        gameObjectDrapper4.addComponent(bulldozerPhysicsComponent.new DrapperPhysicsComponent(gameObjectDrapper4,bulldozerDrawableComponent,bulldozerPhysicsComponent,4));
        gameWorld.addGameObject(gameObjectDrapper4);




        gameObjectWheelDx2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelDx2));
        gameObjectWheelDx2.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDx2,bulldozerDrawableComponent));
        gameObjectWheelDx2.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDx2,bulldozerDrawableComponent,bulldozerPhysicsComponent,3,gameObjectDrapper3));
        gameWorld.addGameObject(gameObjectWheelDx2);

        gameObjectWheelSx2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelSx2));
        gameObjectWheelSx2.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSx2,bulldozerDrawableComponent));
        gameObjectWheelSx2.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSx2,bulldozerDrawableComponent,bulldozerPhysicsComponent,4,gameObjectDrapper4));
        gameWorld.addGameObject(gameObjectWheelSx2);






        return gameObject;
    }




}
