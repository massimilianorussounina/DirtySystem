package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class PhysicsComponent  extends Component{

    protected Body body;

    @Override
    public ComponentType type(){
        return ComponentType.Physics;
    }

    public float getBodyPositionX() {
        return body.getPositionX();
    }

    public float getBodyPositionY() {
        return body.getPositionY();
    }

    public float getBodyAngle(){
        return body.getAngle();
    }
}


class GroundPhysicsComponent extends PhysicsComponent{


    GroundPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bdef = new BodyDef();
        bdef.setAngle(1.5708f);
        bdef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bdef.setPosition(staticPositionComponent.coordinate_x, staticPositionComponent.coordinate_y);

        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bdef);
        body.setUserData(this);
        PolygonShape box = new PolygonShape();
        GroundDrawableComponent groundDrawableComponent = (GroundDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(groundDrawableComponent.width/2 , groundDrawableComponent.height/2);
        body.createFixture(box, 0);
        bdef.delete();
        box.delete();
    }

}


class BulldozerPhysicsComponent extends PhysicsComponent{


    BulldozerPhysicsComponent(GameObject go){
        super();
        this.owner = go;
    }
}


class ObstaclePhysicsComponent extends PhysicsComponent{


    ObstaclePhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bdef = new BodyDef();
        PositionComponent positionComponent= (TrianglePositionComponet) owner.getComponent(ComponentType.Position);
        bdef.setPosition(positionComponent.coordinate_x, positionComponent.coordinate_y);
        bdef.setType(BodyType.staticBody);
        bdef.setAngle(1.5708f);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bdef);
        body.setUserData(this);
        PolygonShape triangle = new PolygonShape();
        triangle.setAsTriangle(positionComponent.x1_local, positionComponent.y1_local, positionComponent.x2_local, positionComponent.y2_local, positionComponent.x3_local, positionComponent.y3_local);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(triangle);
        fixturedef.setFriction(0.1f);
        fixturedef.setRestitution(0.4f);
        fixturedef.setDensity(0.5f);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bdef.delete();
        triangle.delete();
    }
}

class IncineratorPhysicsComponent extends PhysicsComponent{


    IncineratorPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bdef = new BodyDef();
        bdef.setAngle(1.5708f);
        bdef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bdef.setPosition(staticPositionComponent.coordinate_x, staticPositionComponent.coordinate_y);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bdef);
        this.body.setUserData(this);
        PolygonShape box = new PolygonShape();
        IncineratorDrawableComponent incineratorDrawableComponent = (IncineratorDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(incineratorDrawableComponent.width/2 , incineratorDrawableComponent.height/2);
        body.createFixture(box, 0);
        bdef.delete();
        box.delete();
    }
}

class BarrelPhysicsComponent extends PhysicsComponent{


    BarrelPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bdef = new BodyDef();
        bdef.setAngle(1.5708f);
        bdef.setType(BodyType.dynamicBody);
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
        bdef.setPosition(dynamicPositionComponent.coordinate_x, dynamicPositionComponent.coordinate_y);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bdef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        BarrelDrawableComponent barrelDrawableComponent = (BarrelDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(barrelDrawableComponent.width/2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circleShape);
        fixturedef.setFriction(0.5f);
        fixturedef.setRestitution(0.1f);
        fixturedef.setDensity(barrelDrawableComponent.density);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bdef.delete();
        circleShape.delete();
    }
}


class SeaPhysicsComponent extends PhysicsComponent{


    SeaPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bdef = new BodyDef();
        bdef.setAngle(1.5708f);
        bdef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bdef.setPosition(staticPositionComponent.coordinate_x, staticPositionComponent.coordinate_y);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bdef);
        this.body.setUserData(this);
        PolygonShape box = new PolygonShape();
        SeaDrawableComponent seaDrawableComponent = (SeaDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(seaDrawableComponent.width/2 , seaDrawableComponent.height/2);
        body.createFixture(box, 0);
        bdef.delete();
        box.delete();
    }
}