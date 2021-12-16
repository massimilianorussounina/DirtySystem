package com.example.dirtsystemec;


import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.PrismaticJointDef;
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
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bodyDef.setPosition(staticPositionComponent.coordinateX, staticPositionComponent.coordinateY);

        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape box = new PolygonShape();
        GroundDrawableComponent groundDrawableComponent = (GroundDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(groundDrawableComponent.width/2 , groundDrawableComponent.height/2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.1f);
        fixturedef.setRestitution(0.4f);
        fixturedef.setDensity(0.5f);
        body.createFixture(fixturedef);
        bodyDef.delete();
        box.delete();
    }

}


class BulldozerPhysicsComponent extends PhysicsComponent{
    float boxOneX,boxOneY,boxTwoX,boxTwoY,boxThreeX,boxThreeY;
    float width,height,radius;
    int invert;

    BulldozerPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(BodyType.dynamicBody);
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
        bodyDef.setPosition(dynamicPositionComponent.coordinateX, dynamicPositionComponent.coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);

        body.setSleepingAllowed(false);
        PolygonShape chassisShape = new PolygonShape();
        PolygonShape cabinShape = new PolygonShape();
        PolygonShape lightShape = new PolygonShape();
        BulldozerDrawableComponent bulldozerDrawableComponent = (BulldozerDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        invert=bulldozerDrawableComponent.invert;
        this.width = BulldozerDrawableComponent.width;
        this.height = bulldozerDrawableComponent.height;
        this.radius = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
        this.boxOneX = bulldozerDrawableComponent.getBoxOneX();
        this.boxOneY = bulldozerDrawableComponent.getBoxOneY();
        this.boxTwoX = bulldozerDrawableComponent.getBoxTwoX();
        this.boxTwoY = bulldozerDrawableComponent.getBoxTwoY();
        this.boxThreeX = bulldozerDrawableComponent.getBoxThreeX();
        this.boxThreeY = bulldozerDrawableComponent.getBoxThreeY();
        chassisShape.setAsBox(boxOneX/2,boxOneY/2);
        cabinShape.setAsBox(invert * boxTwoX/2,boxTwoY/2,invert * (-BulldozerDrawableComponent.proportionalToBulldozer(0.5f)),(-BulldozerDrawableComponent.proportionalToBulldozer(1.2f)),0);
        lightShape.setAsBox(invert*boxThreeX/2,boxThreeY/2,invert* (-BulldozerDrawableComponent.proportionalToBulldozer(0.5f)),(-BulldozerDrawableComponent.proportionalToBulldozer(2.5f)),0);

        FixtureDef fixtureCabindef = new FixtureDef();
        fixtureCabindef.setShape(cabinShape);
        fixtureCabindef.setDensity(4f);
        FixtureDef fixtureLightdef = new FixtureDef();
        fixtureLightdef.setShape(lightShape);
        fixtureLightdef.setDensity(4f);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(chassisShape);
        fixturedef.setDensity(4f);
        body.createFixture(fixturedef);
        body.createFixture(fixtureCabindef);
        body.createFixture(fixtureLightdef);
        fixtureLightdef.delete();
        fixtureCabindef.delete();
        fixturedef.delete();
        lightShape.delete();
        chassisShape.delete();
        bodyDef.delete();
        cabinShape.delete();
        chassisShape.delete();

    }


    class WheelPhysicsComponent extends PhysicsComponent{
        GameWorld gameWorld;
        float radius;

        public WheelPhysicsComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent,int count,GameObject gameObjectDrapper) {
          super();
          this.owner = gameObject;
          this.radius= BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
          BodyDef wheelDef = new BodyDef();
          wheelDef.setAngle(1.5708f);
          wheelDef.setType(BodyType.dynamicBody);
          DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
          BulldozerPhysicsComponent.DamperPhysicsComponent drapperPhysicsComponent = (BulldozerPhysicsComponent.DamperPhysicsComponent) gameObjectDrapper.getComponent(ComponentType.Physics);

           switch (count) {
               case 1:
                   wheelDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY + (BulldozerDrawableComponent.width / 2) - radius);

                   break;
               case 2:
                   wheelDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY - (BulldozerDrawableComponent.width / 2) + radius);
                   break;
               case 3:
                   wheelDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY - (BulldozerDrawableComponent.width / 2) + (radius * 2) + BulldozerDrawableComponent.proportionalToBulldozer(1.3f));
                   break;
               case 4:
                   wheelDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY - (BulldozerDrawableComponent.width / 2) - (radius * 2) - BulldozerDrawableComponent.proportionalToBulldozer(1.3f));
                   break;

               default:
                   try {
                       throw new Exception("Error Wheel Position");
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
           }
          gameWorld = gameObject.gameWorld;
          this.body = gameWorld.world.createBody(wheelDef);
          this.body.setUserData(this);
          body.setSleepingAllowed(false);

          CircleShape circleShape = new CircleShape();
          circleShape.setRadius(radius);
          FixtureDef fixturedef = new FixtureDef();
          fixturedef.setShape(circleShape);
          fixturedef.setFriction(1f);
          fixturedef.setRestitution(0.1f);
          fixturedef.setDensity(4f);
          body.createFixture(fixturedef);

          wheelDef.delete();
          circleShape.delete();


          switch (count){
              case 1:
                  createJoint(drapperPhysicsComponent.body,body,0, BulldozerDrawableComponent.proportionalToBulldozer(0.4f),0,0);
                  break;
              case 2:
                  createJoint(drapperPhysicsComponent.body,body,0, BulldozerDrawableComponent.proportionalToBulldozer(0.4f),0,0);
                  break;
              case 3:
                  createJoint(drapperPhysicsComponent.body,body,0, BulldozerDrawableComponent.proportionalToBulldozer(0.4f),0,0);
                  break;
              case 4:
                  createJoint(drapperPhysicsComponent.body,body,0, BulldozerDrawableComponent.proportionalToBulldozer(0.4f),0,0);
                  break;

              default:
                  try {
                      throw new Exception("Error Wheel Joint");
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
          }

        }

       private void createJoint(Body a, Body b, float localOneX, float localOneY, float localTwoX, float localTwoY){
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(localOneX,localOneY);
            jointDef.setLocalAnchorB(localTwoX,localTwoY);
            jointDef.setEnableMotor(true);
            jointDef.setMotorSpeed(invert * 3f);
            jointDef.setMaxMotorTorque(20f);
            gameWorld.world.createJoint(jointDef);
            jointDef.delete();
       }

    }


    class DamperPhysicsComponent extends PhysicsComponent{
        GameWorld gameWorld;
        float width,height;
        BulldozerDrawableComponent bulldozerDrawableComponent;

        public DamperPhysicsComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent,BulldozerPhysicsComponent bulldozerPhysicsComponent,int count){
            super();
            this.owner = gameObject;
            this.width = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            this.height =  BulldozerDrawableComponent.proportionalToBulldozer(1.5f);
            this.bulldozerDrawableComponent=bulldozerDrawableComponent;
            radius = BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
            BodyDef damperBoyDef = new BodyDef();
            damperBoyDef.setType(BodyType.dynamicBody);
            damperBoyDef.setAngle(1.5708f);
            DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);

            switch (count) {
                case 1:
                    damperBoyDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY - (BulldozerDrawableComponent.width / 2) + radius);
                    break;
                case 2:
                    damperBoyDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY + (BulldozerDrawableComponent.width / 2) - radius);
                    break;
                case 3:
                    damperBoyDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY - (BulldozerDrawableComponent.width / 2) + (bulldozerPhysicsComponent.radius * 2) + BulldozerDrawableComponent.proportionalToBulldozer(0.6f));
                    break;
                case 4:
                    damperBoyDef.setPosition((dynamicPositionComponent.coordinateX - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinateY + (BulldozerDrawableComponent.width / 2) - (bulldozerPhysicsComponent.radius * 2) -  BulldozerDrawableComponent.proportionalToBulldozer(0.6f));
                    break;

                default:
                    try {
                        throw new Exception("Error Damper Position");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            gameWorld = gameObject.gameWorld;
            this.body = gameWorld.world.createBody(damperBoyDef);
            this.body.setUserData(this);
            body.setSleepingAllowed(false);


            PolygonShape shapeDamper = new PolygonShape();
            shapeDamper.setAsBox(width/2,height/2);
            FixtureDef fixtureDrupperdef = new FixtureDef();
            fixtureDrupperdef.setShape(shapeDamper);
            fixtureDrupperdef.setDensity(4f);
            body.createFixture(fixtureDrupperdef);
            shapeDamper.delete();
            fixtureDrupperdef.delete();
            damperBoyDef.delete();


            switch (count){
                 case 1:
                    createJoint(bulldozerPhysicsComponent.body,body,- BulldozerDrawableComponent.width /2+bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2+BulldozerDrawableComponent.proportionalToBulldozer(0.3f),0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                 case 2:
                    createJoint(bulldozerPhysicsComponent.body,body,+ BulldozerDrawableComponent.width /2-bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2+BulldozerDrawableComponent.proportionalToBulldozer(0.3f),0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                 case 3:
                    createJoint(bulldozerPhysicsComponent.body,body,+ BulldozerDrawableComponent.width /2-(bulldozerPhysicsComponent.radius*2)- BulldozerDrawableComponent.proportionalToBulldozer(0.6f), +bulldozerDrawableComponent.height/2+BulldozerDrawableComponent.proportionalToBulldozer(0.3f),0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                 case 4:
                    createJoint(bulldozerPhysicsComponent.body,body,- BulldozerDrawableComponent.width /2+(bulldozerPhysicsComponent.radius*2)+ BulldozerDrawableComponent.proportionalToBulldozer(0.6f), +bulldozerDrawableComponent.height/2+BulldozerDrawableComponent.proportionalToBulldozer(0.3f),0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                 default:
                    try {
                        throw new Exception("Error Damper Joint");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

        private void createJoint(Body a, Body b, float coordinateLocalOneX, float coordinateLocalOneY, float coordinateLocalTwoX, float coordinateLocalTwoY){
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(coordinateLocalOneX,coordinateLocalOneY);
            jointDef.setLocalAnchorB(coordinateLocalTwoX,coordinateLocalTwoY);
            jointDef.setLocalAxisA(0,1);
            jointDef.setEnableMotor(true);
            jointDef.setEnableLimit(false);
            jointDef.setUpperTranslation(0);
            jointDef.setMaxMotorForce(this.body.getMass()*8.5f);

            gameWorld.world.createJoint(jointDef);
            DistanceJointDef springJoint = new DistanceJointDef();
            springJoint.setBodyA(a);
            springJoint.setBodyB(b);
            springJoint.setLocalAnchorA(coordinateLocalOneX,coordinateLocalOneY);
            springJoint.setLocalAnchorB(coordinateLocalTwoX,coordinateLocalTwoY);
            springJoint.setDampingRatio(0.8f);
            springJoint.setFrequencyHz(2f);
            springJoint.setLength(-BulldozerDrawableComponent.proportionalToBulldozer(0.2f));
            gameWorld.world.createJoint(springJoint);
        }

    }


    class ShovelPhysicsComponent extends PhysicsComponent{
        private GameWorld gameWorld;
        private float coordinate_x,coordinate_y;
        private Body bodyShovel;
        public ShovelPhysicsComponent(GameObject gameObject,BulldozerPhysicsComponent bulldozerPhysicsComponent){
            super();
            this.owner = gameObject;
            BodyDef shovelDef = new BodyDef();
            if(invert ==1 ){
                shovelDef.setAngle(-1.5708f);
                }
            shovelDef.setType(BodyType.dynamicBody);
            DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
            coordinate_x=dynamicPositionComponent.coordinateX;
            coordinate_y=dynamicPositionComponent.coordinateY+invert*BulldozerDrawableComponent.proportionalToBulldozer(3f);
            shovelDef.setPosition(coordinate_x, coordinate_y);
            gameWorld = gameObject.gameWorld;
            this.body = gameWorld.world.createBody(shovelDef);
            bodyShovel=this.body;
            this.body.setUserData(this);
            body.setSleepingAllowed(false);
            PolygonShape boxShapeOne = new PolygonShape();
            PolygonShape boxShapeTwo = new PolygonShape();

            boxShapeOne.setAsBox(BulldozerDrawableComponent.proportionalToBulldozer(0.25f),BulldozerDrawableComponent.proportionalToBulldozer(0.8f),0,0,0);
            FixtureDef fixtureBox1def = new FixtureDef();
            fixtureBox1def.setShape(boxShapeOne);
            fixtureBox1def.setDensity(4f);
            body.createFixture(fixtureBox1def);
            fixtureBox1def.delete();
            boxShapeOne.delete();

            boxShapeTwo.setAsBox(BulldozerDrawableComponent.proportionalToBulldozer(0.5f),BulldozerDrawableComponent.proportionalToBulldozer(0.25f),invert*-BulldozerDrawableComponent.proportionalToBulldozer(0.5f),-BulldozerDrawableComponent.proportionalToBulldozer(0.8f),0);
            FixtureDef fixtureBox2def = new FixtureDef();
            fixtureBox2def.setShape(boxShapeTwo);
            fixtureBox2def.setDensity(4f);
            body.createFixture(fixtureBox2def);
            fixtureBox2def.delete();
            boxShapeTwo.delete();

            shovelDef.delete();
            createJoint(bulldozerPhysicsComponent.body,body,invert*(BulldozerDrawableComponent.width -BulldozerDrawableComponent.proportionalToBulldozer(2))+invert*0.05f, +BulldozerDrawableComponent.proportionalToBulldozer(0.2f),invert*(-BulldozerDrawableComponent.proportionalToBulldozer(0.8f)),-BulldozerDrawableComponent.proportionalToBulldozer(0.6f));

        }
        private void createJoint(Body a, Body b, float coordinateLocalOneX, float coordinateLocalOneY, float coordinateLocalTwoX, float coordinateLocalTwoY){
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);

            jointDef.setLocalAnchorA(coordinateLocalOneX,coordinateLocalOneY);
            jointDef.setLocalAnchorB(coordinateLocalTwoX, coordinateLocalTwoY);
            jointDef.setEnableLimit(true);
            if(invert==1) { //dx
                jointDef.setUpperAngle(0);
                jointDef.setLowerAngle(-1.5708f);
            }
            else{
                jointDef.setUpperAngle(1.5708f);
                jointDef.setLowerAngle(0);
            }

            gameWorld.world.createJoint(jointDef);
            jointDef.delete();
        }


        class WheelShovelPhysicsComponent extends PhysicsComponent{
            float radius;
            public WheelShovelPhysicsComponent(GameObject gameObject){
                super();
                this.owner = gameObject;
                BodyDef wheelShovelDef = new BodyDef();
                radius= BulldozerDrawableComponent.proportionalToBulldozer(0.3f);
                wheelShovelDef.setType(BodyType.dynamicBody);
                wheelShovelDef.setPosition(coordinate_x, coordinate_y-BulldozerDrawableComponent.proportionalToBulldozer(0.8f));
                this.body = gameWorld.world.createBody(wheelShovelDef);
                this.body.setUserData(this);
                this.body.setSleepingAllowed(false);
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(radius);
                FixtureDef fixturedef = new FixtureDef();
                fixturedef.setShape(circleShape);
                fixturedef.setFriction(1f);
                fixturedef.setRestitution(0.1f);
                fixturedef.setDensity(4f);
                this.body.createFixture(fixturedef);
                fixturedef.delete();
                circleShape.delete();
                wheelShovelDef.delete();
                createJoint(bodyShovel,this.body, 0,BulldozerDrawableComponent.proportionalToBulldozer(0.8f),0,0);

            }
            private void createJoint(Body a, Body b, float coordinateLocalOneX, float coordinateLocalOneY, float coordinateLocalTwoX, float coordinateLocalTwoY){
                RevoluteJointDef jointDef = new RevoluteJointDef();
                jointDef.setBodyA(a);
                jointDef.setBodyB(b);
                jointDef.setLocalAnchorA(coordinateLocalOneX,coordinateLocalOneY);
                jointDef.setLocalAnchorB(coordinateLocalTwoX, coordinateLocalTwoY);
                jointDef.setEnableMotor(true);
                gameWorld.world.createJoint(jointDef);
                jointDef.delete();
            }
        }
    }
}

class TowerPhysicsComponent extends PhysicsComponent{


    TowerPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        PositionComponent positionComponent = (TrianglePositionComponent) owner.getComponent(ComponentType.Position);
        bodyDef.setPosition(positionComponent.coordinateX, positionComponent.coordinateY);
        bodyDef.setType(BodyType.staticBody);
        bodyDef.setAngle(1.5708f);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        body.setUserData(this);
        PolygonShape triangle = new PolygonShape();
        triangle.setAsTriangle(positionComponent.coordinateLocalOneX, positionComponent.coordinateLocalOneY, positionComponent.coordinateLocalTwoX, positionComponent.coordinateLocalTwoY, positionComponent.coordinateLocalThreeX, positionComponent.coordinateLocalThreeY);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(triangle);
        fixturedef.setFriction(10f);
        fixturedef.setRestitution(0.4f);
        fixturedef.setDensity(4f);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        triangle.delete();
    }
}

class IncineratorPhysicsComponent extends PhysicsComponent{


    IncineratorPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bodyDef.setPosition(staticPositionComponent.coordinateX, staticPositionComponent.coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        PolygonShape box = new PolygonShape();
        IncineratorDrawableComponent incineratorDrawableComponent = (IncineratorDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(incineratorDrawableComponent.width/2 , incineratorDrawableComponent.height/2);
        body.createFixture(box, 0);
        bodyDef.delete();
        box.delete();
    }
}

class BarrelPhysicsComponent extends PhysicsComponent{

    BarrelPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(BodyType.dynamicBody);
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
        bodyDef.setPosition(dynamicPositionComponent.coordinateX, dynamicPositionComponent.coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        BarrelDrawableComponent barrelDrawableComponent = (BarrelDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(barrelDrawableComponent.width/2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circleShape);
        fixturedef.setFriction(0.5f);
        fixturedef.setRestitution(0.1f);
        fixturedef.setDensity(1f);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        circleShape.delete();
    }
}

class SeaPhysicsComponent extends PhysicsComponent{

    SeaPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(BodyType.staticBody);
        StaticPositionComponent staticPositionComponent = (StaticPositionComponent) gameObject.getComponent(ComponentType.Position);
        bodyDef.setPosition(staticPositionComponent.coordinateX, staticPositionComponent.coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        PolygonShape box = new PolygonShape();
        SeaDrawableComponent seaDrawableComponent = (SeaDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
        box.setAsBox(seaDrawableComponent.width/2 , seaDrawableComponent.height/2);
        body.createFixture(box, 0);
        bodyDef.delete();
        box.delete();
    }

}

class BridgePhysicsComponent extends PhysicsComponent{

   public BridgePhysicsComponent (GameObject gameObject,BridgePosition bridgePosition, TowerPhysicsComponent towerPhysicsComponent){
       this.owner = gameObject;
       BodyDef bodyDef = new BodyDef();

       if(bridgePosition==BridgePosition.LEFT) bodyDef.setAngle(1.5708f);
       else bodyDef.setAngle(0f);

       bodyDef.setType(BodyType.dynamicBody);
       DynamicPositionComponent  dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
       bodyDef.setPosition(dynamicPositionComponent.coordinateX+2f, dynamicPositionComponent.coordinateY);
       GameWorld gameWorld = gameObject.gameWorld;
       this.body = gameWorld.world.createBody(bodyDef);
       this.body.setUserData(this);
       PolygonShape box = new PolygonShape();
       BridgeDrawableComponent bridgeDrawableComponet = (BridgeDrawableComponent) gameObject.getComponent(ComponentType.Drawable);
       box.setAsBox(bridgeDrawableComponet.width/2 , bridgeDrawableComponet.height/2);
       body.createFixture(box, 4f);
       bodyDef.delete();
       box.delete();

       if (bridgePosition == BridgePosition.LEFT) createJoint(this.body, towerPhysicsComponent.body,-1.3f,0,2f,-0.9f,bridgePosition,gameWorld);
       else createJoint(this.body, towerPhysicsComponent.body,1.3f,0,-2f,-0.9f,bridgePosition,gameWorld);
   }

   private void createJoint(Body a, Body b, float localOneX, float localOneY, float localTwoX, float localTwoY, BridgePosition bridgePosition, GameWorld gameWorld){

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(localOneX,localOneY);
        jointDef.setLocalAnchorB(localTwoX,localTwoY);
        jointDef.setEnableLimit(true);
        jointDef.setEnableMotor(true);
        jointDef.setMaxMotorTorque(500f);


        if(bridgePosition == BridgePosition.LEFT) {
            jointDef.setUpperAngle(6.28319f); //6.28319f
            jointDef.setLowerAngle(6.28319f);
            //jointDef.setMotorSpeed(speed);
        }
        else{
            jointDef.setUpperAngle(0);
            jointDef.setLowerAngle(0);
            //jointDef.setMotorSpeed(-speed);
        }

        gameWorld.world.createJoint(jointDef);
        jointDef.delete();
    }

}

class EnclosurePhysicsComponent extends PhysicsComponent{
    private static final float THICKNESS = 0.1f;

    public EnclosurePhysicsComponent (GameObject gameObject,float coordinateXMax,float coordinateXMin,float coordinateYMax,float coordinateYMin){
            super();
            BodyDef bodyDef = new BodyDef();
            bodyDef.setType(BodyType.staticBody);
            this.body = gameObject.gameWorld.world.createBody(bodyDef);
            body.setUserData(this);
            PolygonShape box = new PolygonShape();
            box.setAsBox(coordinateXMax - coordinateXMin, THICKNESS, coordinateXMin + (coordinateXMax - coordinateXMin)/2, coordinateYMin, 0); // last is rotation angle
            body.createFixture(box, 0);
            box.setAsBox(coordinateXMax - coordinateXMin, THICKNESS, coordinateXMin + (coordinateXMax - coordinateXMin)/2, coordinateYMax, 0);
            body.createFixture(box, 0);
            box.setAsBox(THICKNESS, coordinateYMax - coordinateYMin, coordinateXMin, coordinateYMin + (coordinateYMax - coordinateYMin)/2, 0);
            body.createFixture(box, 0);
            box.setAsBox(THICKNESS, coordinateYMax - coordinateYMin, coordinateXMin, coordinateYMin + (coordinateYMax - coordinateYMin) / 2, 0);
            body.createFixture(box, 0);
            bodyDef.delete();
            box.delete();
        }
    }
