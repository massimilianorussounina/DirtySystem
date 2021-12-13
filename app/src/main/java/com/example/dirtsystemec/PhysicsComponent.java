package com.example.dirtsystemec;

import android.util.Log;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.PrismaticJointDef;
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
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.1f);
        fixturedef.setRestitution(0.4f);
        fixturedef.setDensity(0.5f);
        body.createFixture(fixturedef);
        bdef.delete();
        box.delete();
    }

}


class BulldozerPhysicsComponent extends PhysicsComponent{
    float box1_x,box1_y,box2_x,box2_y,box3_x,box3_y;
    float width,height,radius;
    int invert;
    BulldozerPhysicsComponent(GameObject gameObject){
        super();
        this.owner = gameObject;
        BodyDef chassisDef = new BodyDef();
        chassisDef.setAngle(1.5708f);
        chassisDef.setType(BodyType.dynamicBody);
        DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
        chassisDef.setPosition(dynamicPositionComponent.coordinate_x, dynamicPositionComponent.coordinate_y);
        GameWorld gameWorld = gameObject.gameWorld;
        this.body = gameWorld.world.createBody(chassisDef);
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
        this.box1_x = bulldozerDrawableComponent.box1_x;
        this.box1_y = bulldozerDrawableComponent.box1_y;
        this.box2_x = bulldozerDrawableComponent.box2_x;
        this.box2_y = bulldozerDrawableComponent.box2_y;
        this.box3_x = bulldozerDrawableComponent.box3_x;
        this.box3_y = bulldozerDrawableComponent.box3_y;
        chassisShape.setAsBox(box1_x/2,box1_y/2);
        cabinShape.setAsBox(invert * box2_x/2,box2_y/2,invert * (-BulldozerDrawableComponent.proportionalToBulldozer(0.5f)),(-BulldozerDrawableComponent.proportionalToBulldozer(1.2f)),0);
        lightShape.setAsBox(invert*box3_x/2,box3_y/2,invert* (-BulldozerDrawableComponent.proportionalToBulldozer(0.5f)),(-BulldozerDrawableComponent.proportionalToBulldozer(2.5f)),0);

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
        chassisDef.delete();
        cabinShape.delete();
        chassisShape.delete();

    }


    class WheelPhysicsComponent extends PhysicsComponent{
        GameWorld gameWorld;
        float radius;
        public WheelPhysicsComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent,BulldozerPhysicsComponent bulldozerPhysicsComponent,int count,GameObject gameObjectDrapper) {
          super();
          this.owner = gameObject;
          this.radius= BulldozerDrawableComponent.proportionalToBulldozer(0.5f);
          BodyDef wheelDef = new BodyDef();
          wheelDef.setAngle(1.5708f);
          wheelDef.setType(BodyType.dynamicBody);
          DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
          BulldozerPhysicsComponent.DamperPhysicsComponent drapperPhysicsComponent = (BulldozerPhysicsComponent.DamperPhysicsComponent) gameObjectDrapper.getComponent(ComponentType.Physics);
          /*if (wheelPosition == WheelPosition.RIGHT){
              wheelDef.setPosition((dynamicPositionComponent.coordinate_x-(bulldozerDrawableComponent.height/2)), dynamicPositionComponent.coordinate_y+(bulldozerDrawableComponent.width/2)-0.5f);
          }else{
              wheelDef.setPosition((dynamicPositionComponent.coordinate_x-(bulldozerDrawableComponent.height/2)), dynamicPositionComponent.coordinate_y-(bulldozerDrawableComponent.width/2)+0.5f);

          }*/

           switch (count) {
               case 1:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (BulldozerDrawableComponent.width / 2) - radius);

                   break;
               case 2:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (BulldozerDrawableComponent.width / 2) + radius);
                   break;
               case 3:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (BulldozerDrawableComponent.width / 2) + (radius * 2) + BulldozerDrawableComponent.proportionalToBulldozer(1.3f));
                   break;
               case 4:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (BulldozerDrawableComponent.width / 2) - (radius * 2) - BulldozerDrawableComponent.proportionalToBulldozer(1.3f));
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
                      throw new Exception("Error Wheel Vincolo");
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
          }

          /*if(wheelPosition == WheelPosition.LEFT) {

              createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+radius, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
          }else{
              createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-radius, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);

          }*/

        }
      private RevoluteJointDef createJoint(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(local_x1,local_y1);
            jointDef.setLocalAnchorB(local_x2, local_y2);
            jointDef.setEnableMotor(true);
            jointDef.setMotorSpeed(invert * 20f);
            jointDef.setMaxMotorTorque(60f);
            gameWorld.world.createJoint(jointDef);
            jointDef.delete();
            return  jointDef;
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
            BodyDef drapperDef = new BodyDef();
            drapperDef.setType(BodyType.dynamicBody);
            drapperDef.setAngle(1.5708f);
            DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);

            switch (count) {
                case 1:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (BulldozerDrawableComponent.width / 2) + radius);
                    break;
                case 2:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (BulldozerDrawableComponent.width / 2) - radius);
                    break;
                case 3:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (BulldozerDrawableComponent.width / 2) + (bulldozerPhysicsComponent.radius * 2) + BulldozerDrawableComponent.proportionalToBulldozer(0.6f));
                    break;
                case 4:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (BulldozerDrawableComponent.width / 2) - (bulldozerPhysicsComponent.radius * 2) -  BulldozerDrawableComponent.proportionalToBulldozer(0.6f));
                    break;

                default:
                    try {
                        throw new Exception("Error Drapper Position");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            gameWorld = gameObject.gameWorld;
            this.body = gameWorld.world.createBody(drapperDef);
            this.body.setUserData(this);
            body.setSleepingAllowed(false);


            PolygonShape drapperShape = new PolygonShape();
            drapperShape.setAsBox(width/2,height/2);
            FixtureDef fixtureDrupperdef = new FixtureDef();
            fixtureDrupperdef.setShape(drapperShape);
            fixtureDrupperdef.setDensity(4f);


            body.createFixture(fixtureDrupperdef);

            drapperShape.delete();
            fixtureDrupperdef.delete();
            drapperDef.delete();

          switch (count){
                case 1:
                    createJoint(bulldozerPhysicsComponent.body,body,- BulldozerDrawableComponent.width /2+bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2,0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                case 2:
                    createJoint(bulldozerPhysicsComponent.body,body,+ BulldozerDrawableComponent.width /2-bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2,0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                case 3:
                    //createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-(bulldozerPhysicsComponent.radius*2)-0.6f, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createJoint(bulldozerPhysicsComponent.body,body,+ BulldozerDrawableComponent.width /2-(bulldozerPhysicsComponent.radius*2)- BulldozerDrawableComponent.proportionalToBulldozer(0.6f), +bulldozerDrawableComponent.height/2,0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;
                case 4:
                    //createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+(bulldozerPhysicsComponent.radius*2)+0.6f, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createJoint(bulldozerPhysicsComponent.body,body,- BulldozerDrawableComponent.width /2+(bulldozerPhysicsComponent.radius*2)+ BulldozerDrawableComponent.proportionalToBulldozer(0.6f), +bulldozerDrawableComponent.height/2,0,BulldozerDrawableComponent.proportionalToBulldozer(0.9f));
                    break;

                default:
                    try {
                        throw new Exception("Error Drapper Vincolo");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

        private PrismaticJointDef createJoint(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
            PrismaticJointDef jointDef = new PrismaticJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(local_x1, local_y1);
            jointDef.setLocalAnchorB(local_x2 ,local_y2);
            jointDef.setLocalAxisA(0,1);
            jointDef.setEnableMotor(true);
            jointDef.setEnableLimit(false);
            jointDef.setUpperTranslation(0);
            jointDef.setMaxMotorForce(this.body.getMass()*8.5f);

            gameWorld.world.createJoint(jointDef);
            DistanceJointDef mollaDef = new DistanceJointDef();
            mollaDef.setBodyA(a);
            mollaDef.setBodyB(b);
            mollaDef.setLocalAnchorA(local_x1, local_y1);
            mollaDef.setLocalAnchorB(local_x2 ,local_y2);
            mollaDef.setDampingRatio(0.7f);
            mollaDef.setFrequencyHz(4f);
            mollaDef.setLength(-Math.round(((BulldozerDrawableComponent.width *4.545f)/100f)*100f)/100f);
            gameWorld.world.createJoint(mollaDef);
            return  jointDef;
        }

    }
    class ShovelPhysicsComponent extends PhysicsComponent{
        GameWorld gameWorld;
        float coordinate_x,coordinate_y;
        Body bodyShovel ;
        public ShovelPhysicsComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent,BulldozerPhysicsComponent bulldozerPhysicsComponent){
            super();
            this.owner = gameObject;
            BodyDef shovelDef = new BodyDef();
            shovelDef.setType(BodyType.dynamicBody);
            DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
            coordinate_x=dynamicPositionComponent.coordinate_x;
            coordinate_y=dynamicPositionComponent.coordinate_y+invert*BulldozerDrawableComponent.proportionalToBulldozer(3f);
            shovelDef.setPosition(coordinate_x, coordinate_y);
            gameWorld = gameObject.gameWorld;
            this.body = gameWorld.world.createBody(shovelDef);
            bodyShovel=this.body;
            this.body.setUserData(this);
            body.setSleepingAllowed(false);
            PolygonShape box1Shape = new PolygonShape();
            PolygonShape box2Shape = new PolygonShape();

            box1Shape.setAsBox(BulldozerDrawableComponent.proportionalToBulldozer(0.25f),BulldozerDrawableComponent.proportionalToBulldozer(0.8f),0,0,0);
            FixtureDef fixtureBox1def = new FixtureDef();
            fixtureBox1def.setShape(box1Shape);
            fixtureBox1def.setDensity(4f);
            body.createFixture(fixtureBox1def);
            fixtureBox1def.delete();
            box1Shape.delete();

           box2Shape.setAsBox(BulldozerDrawableComponent.proportionalToBulldozer(0.5f),BulldozerDrawableComponent.proportionalToBulldozer(0.25f),invert*-BulldozerDrawableComponent.proportionalToBulldozer(0.5f),-BulldozerDrawableComponent.proportionalToBulldozer(0.8f),0);
            FixtureDef fixtureBox2def = new FixtureDef();
            fixtureBox2def.setShape(box2Shape);
            fixtureBox2def.setDensity(4f);
            body.createFixture(fixtureBox2def);
            fixtureBox2def.delete();
            box2Shape.delete();

            shovelDef.delete();
            createJoint(bulldozerPhysicsComponent.body,body,invert*(BulldozerDrawableComponent.width -BulldozerDrawableComponent.proportionalToBulldozer(2)), 0,invert*(-BulldozerDrawableComponent.proportionalToBulldozer(0.8f)),-BulldozerDrawableComponent.proportionalToBulldozer(0.6f));

        }
        private RevoluteJointDef createJoint(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(local_x1,local_y1);
            jointDef.setLocalAnchorB(local_x2, local_y2);
            jointDef.setEnableLimit(true);
            if(invert==1) {
                jointDef.setUpperAngle(0);
                jointDef.setLowerAngle(-1.5708f);
            }
            else{
                jointDef.setUpperAngle(1.5708f);
                jointDef.setLowerAngle(0);
            }

            gameWorld.world.createJoint(jointDef);
            jointDef.delete();
            return  jointDef;
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
            private RevoluteJointDef createJoint(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
                RevoluteJointDef jointDef = new RevoluteJointDef();
                jointDef.setBodyA(a);
                jointDef.setBodyB(b);
                jointDef.setLocalAnchorA(local_x1,local_y1);
                jointDef.setLocalAnchorB(local_x2, local_y2);
                jointDef.setEnableMotor(true);
                gameWorld.world.createJoint(jointDef);
                jointDef.delete();
                return  jointDef;
            }
        }
    }
}

class TowerPhysicsComponent extends PhysicsComponent{


    TowerPhysicsComponent(GameObject gameObject){
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
        fixturedef.setFriction(3f);
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
        fixturedef.setDensity(1f);
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

class BridgePhysicsComponent extends PhysicsComponent{

   public BridgePhysicsComponent (GameObject gameObject,BridgePosition bridgePosition, TowerPhysicsComponent towerPhysicsComponent){
       this.owner = gameObject;
       BodyDef bdef = new BodyDef();
       bdef.setAngle(1.5708f);
       bdef.setType(BodyType.dynamicBody);
       DynamicPositionComponent  dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
       bdef.setPosition(dynamicPositionComponent.coordinate_x, dynamicPositionComponent.coordinate_y);
       GameWorld gameWorld = gameObject.gameWorld;
       this.body = gameWorld.world.createBody(bdef);
       this.body.setUserData(this);
       PolygonShape box = new PolygonShape();
       BridgeDrawableComponet bridgeDrawableComponet = (BridgeDrawableComponet) gameObject.getComponent(ComponentType.Drawable);
       box.setAsBox(bridgeDrawableComponet.width/2 , bridgeDrawableComponet.height/2);
       body.createFixture(box, 4f);
       bdef.delete();
       box.delete();
       if (bridgePosition == BridgePosition.LEFT) createJoint(this.body, towerPhysicsComponent.body,-1.1f,0,2f,-0.8f,bridgePosition,gameWorld);
       else createJoint(this.body, towerPhysicsComponent.body,1.1f,0,-2f,-0.8f,bridgePosition,gameWorld);
   }

    private RevoluteJointDef createJoint(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2, BridgePosition bridgePosition, GameWorld gameWorld){
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.setBodyA(a);
        jointDef.setBodyB(b);
        jointDef.setLocalAnchorA(local_x1,local_y1);
        jointDef.setLocalAnchorB(local_x2, local_y2);
        jointDef.setEnableLimit(true);
        jointDef.setEnableMotor(true);
        jointDef.setMaxMotorTorque(50f);
        jointDef.setMotorSpeed(10f);

        if(bridgePosition == BridgePosition.LEFT) {
            jointDef.setUpperAngle(6.28319f);
            jointDef.setLowerAngle(6.28319f);
        }
        else{
            jointDef.setUpperAngle(0);
            jointDef.setLowerAngle(-1.309f);
        }

        gameWorld.world.createJoint(jointDef);
        jointDef.delete();
        return  jointDef;
    }


}

