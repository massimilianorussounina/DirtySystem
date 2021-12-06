package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;

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
    protected Body chassis,wheelsSx,wheelsDx;

    BulldozerPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef chassisDef = new BodyDef();
        chassisDef.setType(BodyType.dynamicBody);
        DynamicPositionComponent dinamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
        chassisDef.setPosition(dinamicPositionComponent.coordinate_x, dinamicPositionComponent.coordinate_y);
        GameWorld gameWorld = gameObject.gameWorld;
        chassis = gameWorld.world.createBody(chassisDef);
        chassis.setUserData(this);
        PolygonShape ch = new PolygonShape();
        BulldozerDrawableComponent bulldozerDrawableComponent = (BulldozerDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        ch.setAsBox(bulldozerDrawableComponent.width/2 , bulldozerDrawableComponent.height/2);
       // chassis.createFixture(ch, 0);


        BodyDef wheelsSxdef = new BodyDef();
        wheelsSxdef.setType(BodyType.dynamicBody);
        wheelsSxdef.setPosition((dinamicPositionComponent.coordinate_x-(bulldozerDrawableComponent.width/2))+0.5f, dinamicPositionComponent.coordinate_y-(bulldozerDrawableComponent.height/2));
        wheelsSx = gameWorld.world.createBody(wheelsSxdef);
        wheelsSx.setUserData(this);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.5f);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circleShape);
        fixturedef.setFriction(0.5f);
        fixturedef.setRestitution(0.1f);
        fixturedef.setDensity(2f);
        wheelsSx.createFixture(fixturedef);

        BodyDef wheelsDxdef = new BodyDef();
        wheelsDxdef.setType(BodyType.dynamicBody);
        wheelsDxdef.setPosition((dinamicPositionComponent.coordinate_x+(bulldozerDrawableComponent.width/2))-0.5f, dinamicPositionComponent.coordinate_y-(bulldozerDrawableComponent.height/2));
        wheelsDx = gameWorld.world.createBody(wheelsSxdef);
        wheelsDx.setUserData(this);
        CircleShape circleShapeDx = new CircleShape();
        circleShape.setRadius(0.5f);
        FixtureDef fixturedefDx = new FixtureDef();
        fixturedefDx.setShape(circleShapeDx);
        fixturedefDx.setFriction(0.5f);
        fixturedefDx.setRestitution(0.1f);
        fixturedefDx.setDensity(2f);
        wheelsDx.createFixture(fixturedef);

        chassisDef.delete();
        ch.delete();
        wheelsDxdef.delete();
        wheelsSxdef.delete();
        wheelsSx.delete();
        wheelsDx.delete();
        fixturedef.delete();
        fixturedefDx.delete();

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.setBodyA(chassis);
        jointDef.setBodyB(wheelsSx);
        jointDef.setLocalAnchorA(-bulldozerDrawableComponent.width/2+0.5f, -bulldozerDrawableComponent.height/2);
        jointDef.setLocalAnchorB(0, 0);
        jointDef.setEnableMotor(true);
        jointDef.setMotorSpeed(0);
        jointDef.setMaxMotorTorque(8f);
        RevoluteJoint joint = (RevoluteJoint) gameWorld.world.createJoint(jointDef);
        jointDef.delete();

        RevoluteJointDef jointDefDx = new RevoluteJointDef();
        jointDef.setBodyA(chassis);
        jointDef.setBodyB(wheelsDx);
        jointDefDx.setLocalAnchorA(+bulldozerDrawableComponent.width/2-0.5f, -bulldozerDrawableComponent.height/2);
        jointDefDx.setLocalAnchorB(0, 0);
        jointDefDx.setEnableMotor(true);
        jointDefDx.setMotorSpeed(0);
        jointDefDx.setMaxMotorTorque(8f);
        RevoluteJoint joint2 = (RevoluteJoint) gameWorld.world.createJoint(jointDef);
        jointDef.delete();


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

