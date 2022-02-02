package com.example.dirtsystemec;


import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;


public class PhysicsComponent  extends Component{

    protected Body body;
    protected String name;

    PhysicsComponent(String name){
        this.name = name;
    }

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



class CirclePhysicsComponent extends PhysicsComponent {

    CirclePhysicsComponent(String name,GameObject gameObject, BodyType bodyType, float coordinateX, float coordinateY, float width, float height,
                           float friction, float restitution, float density) {
        super(name);
        this.name = name;
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);
        bodyDef.setAllowSleep(false);
        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(width);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circleShape);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        circleShape.delete();
    }

   /* CirclePhysicsComponent(String name,GameObject gameObject, BodyType bodyType,float coordinateX, float coordinateY, float width, float height) {
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);
        bodyDef.setPosition(coordinateX,coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setSleepingAllowed(false);
        this.body.setUserData(this);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(width/2);
        bodyDef.delete();
        circleShape.delete();
    }*/

}


class PolygonPhysicsComponent extends PhysicsComponent{


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);
        bodyDef.setAllowSleep(false);


        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width , height);
        body.createFixture(box, density);
        bodyDef.delete();
        box.delete();
    }

    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density,float restitution,
                            float friction){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);

        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width , height);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        bodyDef.delete();
        box.delete();
    }


    PolygonPhysicsComponent(String name, GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,
                            float coordinateLocalOneX, float coordinateLocalOneY,
                            float coordinateLocalTwoX, float coordinateLocalTwoY,
                            float coordinateLocalThreeX, float coordinateLocalThreeY,
                            float density,float restitution, float friction){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(coordinateX,coordinateY);
        bodyDef.setType(bodyType);
        bodyDef.setAngle(1.5708f);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        body.setUserData(this);
        PolygonShape triangle = new PolygonShape();
        triangle.setAsTriangle(coordinateLocalOneX,coordinateLocalOneY,
                coordinateLocalTwoX, coordinateLocalTwoY,
                coordinateLocalThreeX, coordinateLocalThreeY);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(triangle);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        fixturedef.delete();
        bodyDef.delete();
        triangle.delete();
    }


    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float centerX, float centerY,float width, float height, float angle,float density){
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(bodyType);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height, centerX, centerY, angle);
        body.createFixture(box, density);
        bodyDef.delete();
        box.delete();
    }

    PolygonPhysicsComponent(String name,GameObject gameObject,BodyType bodyType,float coordinateX, float coordinateY,float width, float height, float density,float restitution,
                            float friction,boolean isSensor) {
        super(name);
        this.owner = gameObject;
        BodyDef bodyDef = new BodyDef();
        bodyDef.setAngle(1.5708f);
        bodyDef.setType(bodyType);

        bodyDef.setPosition(coordinateX, coordinateY);
        GameWorld gameWorld = gameObject.gameWorld;

        this.body = gameWorld.world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        body.getFixtureList().setSensor(isSensor);

        bodyDef.delete();
        box.delete();
        }



}






/*class BulldozerPhysicsComponent extends PhysicsComponent{
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
}*/




