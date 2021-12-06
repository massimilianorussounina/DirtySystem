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
        gameObject.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObject));
        gameObject.addComponent(new BulldozerDrawableComponent(gameObject));
        gameObject.addComponent(new BulldozerPhysicsComponent(gameObject));
        return gameObject;
    }


}
