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


    public static void createBridge(float bridgeCoordinateX,float bridgeCoordinateY,
                                    float towerCoordinateX, float towerCoordinateY,
                                    float towerBodyCoordinateX1, float towerBodyCoordinateY1,
                                    float towerBodyCoordinateX2, float towerBodyCoordinateY2,
                                    float towerBodyCoordinateX3, float towerBodyCoordinateY3,
                                    BridgePosition bridgePosition, GameWorld gameWorld){

        GameObject gameObjectTower = new GameObject(gameWorld);
        gameObjectTower.addComponent(new TrianglePositionComponet(towerCoordinateX,towerCoordinateY,towerBodyCoordinateX1,towerBodyCoordinateY1,
                                towerBodyCoordinateX2,towerBodyCoordinateY2,towerBodyCoordinateX3,towerBodyCoordinateY3,gameObjectTower));
        gameObjectTower.addComponent(new TowerDrawableComponent(gameObjectTower,bridgePosition));
        TowerPhysicsComponent towerPhysicsComponent = new TowerPhysicsComponent(gameObjectTower);
        gameObjectTower.addComponent(towerPhysicsComponent);
        gameWorld.addGameObject(gameObjectTower);


        GameObject gameObjectBridge = new GameObject(gameWorld);
        gameObjectBridge.addComponent(new DynamicPositionComponent(bridgeCoordinateX,bridgeCoordinateY,gameObjectBridge));
        gameObjectBridge.addComponent(new BridgeDrawableComponet(gameObjectBridge));
        gameObjectBridge.addComponent(new BridgePhysicsComponent(gameObjectBridge,bridgePosition,towerPhysicsComponent));
        gameWorld.addGameObject(gameObjectBridge);

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
        GameObject gameObjectDamper1 = new GameObject(gameWorld);
        GameObject gameObjectDamper2 = new GameObject(gameWorld);
        GameObject gameObjectDamper3 = new GameObject(gameWorld);
        GameObject gameObjectDamper4 = new GameObject(gameWorld);
        GameObject gameObjectShovel = new GameObject(gameWorld);
        GameObject gameObjectWheelShovel = new GameObject(gameWorld);



        gameObject.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObject));
        BulldozerDrawableComponent bulldozerDrawableComponent = new BulldozerDrawableComponent(gameObject);
        gameObject.addComponent(bulldozerDrawableComponent);
        BulldozerPhysicsComponent bulldozerPhysicsComponent = new BulldozerPhysicsComponent(gameObject);
        gameObject.addComponent(bulldozerPhysicsComponent);



        gameObjectDamper1.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDamper1));
        gameObjectDamper1.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObject));
        gameObjectDamper1.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamper1,bulldozerDrawableComponent,bulldozerPhysicsComponent,1));
        gameWorld.addGameObject(gameObjectDamper1);

        gameObjectDamper2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDamper2));
        gameObjectDamper2.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObject));
        gameObjectDamper2.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamper2,bulldozerDrawableComponent,bulldozerPhysicsComponent,2));
        gameWorld.addGameObject(gameObjectDamper2);

        gameObjectWheelSx.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelSx));
        gameObjectWheelSx.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSx,bulldozerDrawableComponent));
        gameObjectWheelSx.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSx,bulldozerDrawableComponent,bulldozerPhysicsComponent,1,gameObjectDamper1));
        gameWorld.addGameObject(gameObjectWheelSx);


        gameObjectWheelDx.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelDx));
        gameObjectWheelDx.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDx,bulldozerDrawableComponent));
        gameObjectWheelDx.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDx,bulldozerDrawableComponent,bulldozerPhysicsComponent,2,gameObjectDamper2));
        gameWorld.addGameObject(gameObjectWheelDx);

        gameObjectDamper3.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDamper3));
        gameObjectDamper3.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObject));
        gameObjectDamper3.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamper3,bulldozerDrawableComponent,bulldozerPhysicsComponent,3));
        gameWorld.addGameObject(gameObjectDamper3);

        gameObjectDamper4.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectDamper4));
        gameObjectDamper4.addComponent(bulldozerDrawableComponent.new DamperDrawableComponent(gameObject));
        gameObjectDamper4.addComponent(bulldozerPhysicsComponent.new DamperPhysicsComponent(gameObjectDamper4,bulldozerDrawableComponent,bulldozerPhysicsComponent,4));
        gameWorld.addGameObject(gameObjectDamper4);




        gameObjectWheelDx2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelDx2));
        gameObjectWheelDx2.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelDx2,bulldozerDrawableComponent));
        gameObjectWheelDx2.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelDx2,bulldozerDrawableComponent,bulldozerPhysicsComponent,3,gameObjectDamper3));
        gameWorld.addGameObject(gameObjectWheelDx2);

        gameObjectWheelSx2.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectWheelSx2));
        gameObjectWheelSx2.addComponent(bulldozerDrawableComponent.new WheelDrawableComponent(gameObjectWheelSx2,bulldozerDrawableComponent));
        gameObjectWheelSx2.addComponent(bulldozerPhysicsComponent.new WheelPhysicsComponent(gameObjectWheelSx2,bulldozerDrawableComponent,bulldozerPhysicsComponent,4,gameObjectDamper4));
        gameWorld.addGameObject(gameObjectWheelSx2);

        gameObjectShovel.addComponent(new DynamicPositionComponent(coordinate_x,coordinate_y,gameObjectShovel));
        BulldozerDrawableComponent.ShovelDrawableComponent shovelDrawableComponent=bulldozerDrawableComponent.new ShovelDrawableComponent(gameObjectShovel,bulldozerDrawableComponent);
        BulldozerPhysicsComponent.ShovelPhysicsComponent shovelPhysicsComponent = bulldozerPhysicsComponent.new ShovelPhysicsComponent(gameObjectShovel,bulldozerDrawableComponent,bulldozerPhysicsComponent);
        gameObjectShovel.addComponent(shovelDrawableComponent);
        gameObjectShovel.addComponent(shovelPhysicsComponent);
        gameWorld.addGameObject(gameObjectShovel);

        gameObjectWheelShovel.addComponent(shovelDrawableComponent.new WheelShovelDrawableComponent(gameObjectWheelShovel));
        gameObjectWheelShovel.addComponent(shovelPhysicsComponent.new WheelShovelPhysicsComponent(gameObjectWheelShovel));
        gameWorld.addGameObject(gameObjectWheelShovel);






        return gameObject;
    }




}
