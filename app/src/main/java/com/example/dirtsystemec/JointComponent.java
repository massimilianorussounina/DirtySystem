package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.RevoluteJointDef;

public class JointComponent  extends  Component{

    protected  Body bodyOne;
    protected  Body bodyTwo;

    @Override
    public ComponentType type() {
        return ComponentType.Joint;
    }

}


class RevoluteJointComponent extends JointComponent{

    public RevoluteJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo,
                                  float coordinateOneX, float coordinateOneY,
                                  float coordinateTwoX, float coordinateTwoY, float motorTorque,
                                  float upperAngle, float lowerAngle) {
        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(bodyOne);
        revoluteJointDef.setBodyB(bodyTwo);
        revoluteJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        revoluteJointDef.setLocalAnchorB(coordinateTwoX, coordinateTwoY);
        revoluteJointDef.setEnableLimit(true);
        revoluteJointDef.setEnableMotor(true);
        revoluteJointDef.setMaxMotorTorque(motorTorque);
        revoluteJointDef.setUpperAngle(upperAngle);
        revoluteJointDef.setLowerAngle(lowerAngle);
        gameObject.gameWorld.world.createJoint(revoluteJointDef);
        revoluteJointDef.delete();
    }
    public   RevoluteJointComponent(GameObject gameObject, Body bodyOne, Body bodyTwo,
                                    float coordinateOneX, float coordinateOneY,
                                    float coordinateTwoX, float coordinateTwoY, float motorTorque,
                                    boolean enableMotor,float motorSpeed) {
        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.setBodyA(bodyOne);
        revoluteJointDef.setBodyB(bodyTwo);
        revoluteJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        revoluteJointDef.setLocalAnchorB(coordinateTwoX, coordinateTwoY);
        revoluteJointDef.setEnableMotor(enableMotor);
        revoluteJointDef.setMotorSpeed(motorSpeed);
        revoluteJointDef.setMaxMotorTorque(motorTorque);
        gameObject.gameWorld.world.createJoint(revoluteJointDef);
        revoluteJointDef.delete();

    }
}

class PrismaticJointComponet extends JointComponent{

    public PrismaticJointComponet(GameObject gameObject,Body bodyOne,Body bodyTwo,float coordinateOneX, float coordinateOneY,
                                  float coordinateTwoX, float coordinateTwoY, float localAxisA_x, float localAxisA_y, boolean enableMotor, boolean enableLimit, float upperTranslation, float maxMotorForce){

        super();
        this.owner = gameObject;
        this.bodyOne = bodyOne;
        this.bodyTwo = bodyTwo;
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.setBodyA(bodyOne);
        prismaticJointDef.setBodyB(bodyTwo);
        prismaticJointDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        prismaticJointDef.setLocalAnchorB(coordinateTwoX ,coordinateTwoY);
        prismaticJointDef.setLocalAxisA(localAxisA_x,localAxisA_y);
        prismaticJointDef.setEnableMotor(enableMotor);
        prismaticJointDef.setEnableLimit(enableLimit);
        prismaticJointDef.setUpperTranslation(upperTranslation);
        prismaticJointDef.setMaxMotorForce(maxMotorForce);
        gameObject.gameWorld.world.createJoint(prismaticJointDef);
        prismaticJointDef.delete();
    }
}

class DistanceJointComponet extends  JointComponent{
    public DistanceJointComponet(GameObject gameObject,Body bodyOne,Body bodyTwo,float coordinateOneX, float coordinateOneY,
                                 float coordinateTwoX, float coordinateTwoY,float dampingRatio, float frequencyHz, float length){
        DistanceJointDef mollaDef = new DistanceJointDef();
        mollaDef.setBodyA(bodyOne);
        mollaDef.setBodyB(bodyTwo);
        mollaDef.setLocalAnchorA(coordinateOneX, coordinateOneY);
        mollaDef.setLocalAnchorB(coordinateTwoX ,coordinateTwoY);
        mollaDef.setDampingRatio(dampingRatio);
        mollaDef.setFrequencyHz(frequencyHz);
        mollaDef.setLength(length);
        gameObject.gameWorld.world.createJoint(mollaDef);
        mollaDef.delete();
    }
}