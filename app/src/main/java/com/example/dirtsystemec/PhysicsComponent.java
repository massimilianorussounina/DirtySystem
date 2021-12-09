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
        width=bulldozerDrawableComponent.width;
        height=bulldozerDrawableComponent.height;
        radius=(Math.round((bulldozerDrawableComponent.width*11.363f)*100f)/100f)/100f;
        box1_x=bulldozerDrawableComponent.box1_x;
        box1_y=bulldozerDrawableComponent.box1_y;
        box2_x=bulldozerDrawableComponent.box2_x;
        box2_y=bulldozerDrawableComponent.box2_y;
        box3_x=bulldozerDrawableComponent.box3_x;
        box3_y=bulldozerDrawableComponent.box3_y;
        chassisShape.setAsBox(box1_x/2,box1_y/2);
        cabinShape.setAsBox(box2_x/2,box2_y/2,-(Math.round((width*11.363f)*100f)/100f)/100f,-(Math.round((height*34.285f)*100f)/100f)/100f,0);
        lightShape.setAsBox(box3_x/2,box3_y/2,-(Math.round((width*11.363f)*100f)/100f)/100f,-(Math.round((height*71.428f)*100f)/100f)/100f,0);

       /* chassisShape.setAsBox(2.2f,0.60f);
        cabinShape.setAsBox(1.15f,0.75f,-0.5f,-1.2f,0);
        lightShape.setAsBox(0.2f,0.15f,-0.5f,-2.5f,0);*/
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
          radius=(Math.round((bulldozerDrawableComponent.width*11.363f)*100f)/100f)/100f;
          BodyDef wheelDef = new BodyDef();
          wheelDef.setAngle(1.5708f);
          wheelDef.setType(BodyType.dynamicBody);
          DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
          BulldozerPhysicsComponent.DrapperPhysicsComponent drapperPhysicsComponent = (BulldozerPhysicsComponent.DrapperPhysicsComponent) gameObjectDrapper.getComponent(ComponentType.Physics);
          /*if (wheelPosition == WheelPosition.RIGHT){
              wheelDef.setPosition((dynamicPositionComponent.coordinate_x-(bulldozerDrawableComponent.height/2)), dynamicPositionComponent.coordinate_y+(bulldozerDrawableComponent.width/2)-0.5f);
          }else{
              wheelDef.setPosition((dynamicPositionComponent.coordinate_x-(bulldozerDrawableComponent.height/2)), dynamicPositionComponent.coordinate_y-(bulldozerDrawableComponent.width/2)+0.5f);

          }*/

           switch (count) {
               case 1:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (bulldozerDrawableComponent.width / 2) - radius);

                   break;
               case 2:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (bulldozerDrawableComponent.width / 2) + radius);
                   break;
               case 3:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (bulldozerDrawableComponent.width / 2) + (radius * 2) + Math.round(((bulldozerDrawableComponent.width*29.545f)/100f)*100f)/100f);
                   break;
               case 4:
                   wheelDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (bulldozerDrawableComponent.width / 2) - (radius * 2) - Math.round(((bulldozerDrawableComponent.width*29.545f)/100f)*100f)/100f);
                   break;

               default:
                   try {
                       throw new Exception("errore numero ruota sbagliato pos");
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
                  createVincolo(drapperPhysicsComponent.body,body,0, Math.round(((bulldozerDrawableComponent.width*9.09f)/100f)*100f)/100f,0,0);
                  break;
              case 2:
                  createVincolo(drapperPhysicsComponent.body,body,0, Math.round(((bulldozerDrawableComponent.width*9.09f)/100f)*100f)/100f,0,0);
                  break;
              case 3:
                  createVincolo(drapperPhysicsComponent.body,body,0, Math.round(((bulldozerDrawableComponent.width*9.09f)/100f)*100f)/100f,0,0);
                  break;
              case 4:
                  createVincolo(drapperPhysicsComponent.body,body,0, Math.round(((bulldozerDrawableComponent.width*9.09f)/100f)*100f)/100f,0,0);
                  break;

              default:
                  try {
                      throw new Exception("errore numero ruota sbagliato");
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
      private RevoluteJointDef createVincolo(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.setBodyA(a);
            jointDef.setBodyB(b);
            jointDef.setLocalAnchorA(local_x1,local_y1);
            jointDef.setLocalAnchorB(local_x2, local_y2);
            jointDef.setEnableMotor(true);
            jointDef.setMotorSpeed(0f);
            jointDef.setMaxMotorTorque(20f);
            gameWorld.world.createJoint(jointDef);
            jointDef.delete();
            return  jointDef;
        }
    }


    class DrapperPhysicsComponent extends PhysicsComponent{
        GameWorld gameWorld;
        float width,height;
        BulldozerDrawableComponent bulldozerDrawableComponent;
        public DrapperPhysicsComponent(GameObject gameObject,BulldozerDrawableComponent bulldozerDrawableComponent,BulldozerPhysicsComponent bulldozerPhysicsComponent,int count){
            super();
            this.owner = gameObject;
            this.width = Math.round(((bulldozerDrawableComponent.width*11.363f)/100f)*100f)/100f;
            this.height = Math.round(((bulldozerDrawableComponent.width*34.090f)/100f)*100f)/100f;
            this.bulldozerDrawableComponent=bulldozerDrawableComponent;
            BodyDef drapperDef = new BodyDef();
            drapperDef.setType(BodyType.dynamicBody);
            drapperDef.setAngle(1.5708f);
            DynamicPositionComponent dynamicPositionComponent = (DynamicPositionComponent) gameObject.getComponent(ComponentType.Position);
            Log.i("larg","  "+bulldozerDrawableComponent.width);
            switch (count) {
                case 1:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (bulldozerDrawableComponent.width / 2) + radius);

                    break;
                case 2:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (bulldozerDrawableComponent.width / 2) - radius);
                    break;
                case 3:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y - (bulldozerDrawableComponent.width / 2) + (bulldozerPhysicsComponent.radius * 2) + Math.round(((bulldozerDrawableComponent.width*13.636f)/100f)*100f)/100f);
                    break;
                case 4:
                    drapperDef.setPosition((dynamicPositionComponent.coordinate_x - (bulldozerDrawableComponent.height / 2)), dynamicPositionComponent.coordinate_y + (bulldozerDrawableComponent.width / 2) - (bulldozerPhysicsComponent.radius * 2) - Math.round(((bulldozerDrawableComponent.width*13.636f)/100f)*100f)/100f);
                    break;

                default:
                    try {
                        throw new Exception("errore numero ruota sbagliato pos");
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
                    //createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2,0,Math.round(((bulldozerDrawableComponent.width*20.454f)/100f)*100f)/100f);
                    break;
                case 2:
                    //createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-bulldozerPhysicsComponent.radius, +bulldozerDrawableComponent.height/2,0,Math.round(((bulldozerDrawableComponent.width*20.454f)/100f)*100f)/100f);
                    break;
                case 3:
                    //createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-(bulldozerPhysicsComponent.radius*2)-0.6f, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createVincolo(bulldozerPhysicsComponent.body,body,+bulldozerDrawableComponent.width/2-(bulldozerPhysicsComponent.radius*2)-Math.round(((bulldozerDrawableComponent.width*13.636f)/100f)*100f)/100f, +bulldozerDrawableComponent.height/2,0,Math.round(((bulldozerDrawableComponent.width*20.454f)/100f)*100f)/100f);
                    break;
                case 4:
                    //createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+(bulldozerPhysicsComponent.radius*2)+0.6f, +bulldozerDrawableComponent.height/2-(Math.round((bulldozerDrawableComponent.height*24.285)*100f)/100f)/100f,0,0);
                    createVincolo(bulldozerPhysicsComponent.body,body,-bulldozerDrawableComponent.width/2+(bulldozerPhysicsComponent.radius*2)+Math.round(((bulldozerDrawableComponent.width*13.636f)/100f)*100f)/100f, +bulldozerDrawableComponent.height/2,0,Math.round(((bulldozerDrawableComponent.width*20.454f)/100f)*100f)/100f);
                    break;

                default:
                    try {
                        throw new Exception("errore numero ruota sbagliato");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }



        }
        private PrismaticJointDef createVincolo(Body a, Body b,float local_x1,float local_y1, float local_x2, float local_y2){
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
            mollaDef.setFrequencyHz(2f);
            mollaDef.setLength(-Math.round(((bulldozerDrawableComponent.width*4.545f)/100f)*100f)/100f);
            gameWorld.world.createJoint(mollaDef);
            return  jointDef;

        }

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

