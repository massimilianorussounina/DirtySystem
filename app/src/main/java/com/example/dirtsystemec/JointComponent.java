package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Body;
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
}
