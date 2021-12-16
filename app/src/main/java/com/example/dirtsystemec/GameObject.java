package com.example.dirtsystemec;

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

    public static void createGround(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectGround = new GameObject(gameWorld);
        gameObjectGround.addComponent(new StaticPositionComponent(coordinateX,coordinateY,gameObjectGround));
        gameObjectGround.addComponent(new GroundDrawableComponent(gameObjectGround));
        gameObjectGround.addComponent(new GroundPhysicsComponent(gameObjectGround));
        gameWorld.addGameObject(gameObjectGround);
    }


    public static void createBridge(float bridgeCoordinateX,float bridgeCoordinateY,
                                    float towerCoordinateX, float towerCoordinateY,
                                    float towerBodyCoordinateX1, float towerBodyCoordinateY1,
                                    float towerBodyCoordinateX2, float towerBodyCoordinateY2,
                                    float towerBodyCoordinateX3, float towerBodyCoordinateY3,
                                    BridgePosition bridgePosition, GameWorld gameWorld){

        /* Costruzione delle torri */
        GameObject gameObjectTower = new GameObject(gameWorld);
        gameObjectTower.addComponent(new TrianglePositionComponent(towerCoordinateX,towerCoordinateY,towerBodyCoordinateX1,towerBodyCoordinateY1,
                                towerBodyCoordinateX2,towerBodyCoordinateY2,towerBodyCoordinateX3,towerBodyCoordinateY3,gameObjectTower));
        gameObjectTower.addComponent(new TowerDrawableComponent(gameObjectTower,bridgePosition));
        TowerPhysicsComponent towerPhysicsComponent = new TowerPhysicsComponent(gameObjectTower);
        gameObjectTower.addComponent(towerPhysicsComponent);
        gameWorld.addGameObject(gameObjectTower);


        /* Costruzione delle ponte */

        GameObject gameObjectBridge = new GameObject(gameWorld);
        gameObjectBridge.addComponent(new DynamicPositionComponent(bridgeCoordinateX,bridgeCoordinateY,gameObjectBridge));
        gameObjectBridge.addComponent(new BridgeDrawableComponent(gameObjectBridge));
        gameObjectBridge.addComponent(new BridgePhysicsComponent(gameObjectBridge,bridgePosition,towerPhysicsComponent));
        gameWorld.addGameObject(gameObjectBridge);
    }


    public static void createIncinerator(float coordinateX, float coordinateY,float fireCoordinateX, float fireCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectIncinerator = new GameObject(gameWorld);
        gameObjectIncinerator.addComponent(new StaticPositionComponent(coordinateX,coordinateY,gameObjectIncinerator));
        gameObjectIncinerator.addComponent(new IncineratorDrawableComponent(gameObjectIncinerator,fireCoordinateX,fireCoordinateY));
        gameObjectIncinerator.addComponent(new IncineratorPhysicsComponent(gameObjectIncinerator));
        gameWorld.addGameObject(gameObjectIncinerator);
    }

    public static void createBarrel(float coordinateX, float coordinateY,GameWorld gameWorld) {
        GameObject gameObjectBarrel = new GameObject(gameWorld);
        gameObjectBarrel.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectBarrel));
        gameObjectBarrel.addComponent(new BarrelDrawableComponent(gameObjectBarrel));
        gameObjectBarrel.addComponent(new BarrelPhysicsComponent(gameObjectBarrel));
        gameWorld.addGameObject(gameObjectBarrel);
    }

    public static void createSea(float coordinateX, float coordinateY,float seaCoordinateX, float seaCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectSea = new GameObject(gameWorld);
        gameObjectSea.addComponent(new StaticPositionComponent(coordinateX,coordinateY,gameObjectSea));
        gameObjectSea.addComponent(new SeaDrawableComponent(gameObjectSea,seaCoordinateX,seaCoordinateY));
        gameObjectSea.addComponent(new SeaPhysicsComponent(gameObjectSea));
        gameWorld.addGameObject(gameObjectSea);
    }

    public static void createBulldozer(float coordinateX, float coordinateY,GameWorld gameWorld,int invert) {

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


        /* Costruzione pala caricatrice */

        gameObjectShovel.addComponent(new DynamicPositionComponent(coordinateX,coordinateY,gameObjectShovel));
        BulldozerDrawableComponent.ShovelDrawableComponent shovelDrawableComponent=bulldozerDrawableComponent.new ShovelDrawableComponent(gameObjectShovel,bulldozerDrawableComponent);
        BulldozerPhysicsComponent.ShovelPhysicsComponent shovelPhysicsComponent = bulldozerPhysicsComponent.new ShovelPhysicsComponent(gameObjectShovel,bulldozerPhysicsComponent);
        gameObjectShovel.addComponent(shovelDrawableComponent);
        gameObjectShovel.addComponent(shovelPhysicsComponent);
        gameWorld.addGameObject(gameObjectShovel);


        /* Costruzione ammortizzatori */

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


        /* Costruzione ruote */

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
    }

    public static void createEnclosure(float coordinateXMax, float coordinateXMin,float coordinateYMax,float coordinateYMin,GameWorld gameWorld){
        GameObject gameObjectEnclosure = new GameObject(gameWorld);
        gameObjectEnclosure.addComponent(new EnclosurePhysicsComponent(gameObjectEnclosure,coordinateXMax,coordinateXMin,coordinateYMax,coordinateYMin));
        gameWorld.addGameObject(gameObjectEnclosure);
    }

    public static void createScoreBar(float scoreBarCoordinateX, float scoreBarCoordinateY,GameWorld gameWorld) {
        GameObject gameObjectScoreBar = new GameObject(gameWorld);
        gameObjectScoreBar.addComponent(new StaticPositionComponent(scoreBarCoordinateX,scoreBarCoordinateY,gameObjectScoreBar));
        gameObjectScoreBar.addComponent(new ScoreBarDrawableComponent(gameObjectScoreBar));
        gameWorld.addGameObject(gameObjectScoreBar);
    }


}
