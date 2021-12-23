/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class World {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected World(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(World obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_World(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public World(float gravityX, float gravityY) {
    this(liquidfunJNI.new_World(gravityX, gravityY), true);
  }

  public void setDebugDraw(Draw debugDraw) {
    liquidfunJNI.World_setDebugDraw(swigCPtr, this, Draw.getCPtr(debugDraw), debugDraw);
  }

  public Body createBody(BodyDef def) {
    long cPtr = liquidfunJNI.World_createBody(swigCPtr, this, BodyDef.getCPtr(def), def);
    return (cPtr == 0) ? null : new Body(cPtr, false);
  }

  public void destroyBody(Body body) {
    liquidfunJNI.World_destroyBody(swigCPtr, this, Body.getCPtr(body), body);
  }

  public void step(float timeStep, int velocityIterations, int positionIterations, int particleIterations) {
    liquidfunJNI.World_step(swigCPtr, this, timeStep, velocityIterations, positionIterations, particleIterations);
  }

  public void drawDebugData() {
    liquidfunJNI.World_drawDebugData(swigCPtr, this);
  }

  public int getBodyCount() {
    return liquidfunJNI.World_getBodyCount(swigCPtr, this);
  }

  public ParticleSystem createParticleSystem(ParticleSystemDef def) {
    long cPtr = liquidfunJNI.World_createParticleSystem(swigCPtr, this, ParticleSystemDef.getCPtr(def), def);
    return (cPtr == 0) ? null : new ParticleSystem(cPtr, false);
  }

  public void setGravity(float gravityX, float gravityY) {
    liquidfunJNI.World_setGravity(swigCPtr, this, gravityX, gravityY);
  }

  public void setContactListener(ContactListener listener) {
    liquidfunJNI.World_setContactListener(swigCPtr, this, ContactListener.getCPtr(listener), listener);
  }

  public void queryAABB(QueryCallback callback, float xmin, float ymin, float xmax, float ymax) {
    liquidfunJNI.World_queryAABB(swigCPtr, this, QueryCallback.getCPtr(callback), callback, xmin, ymin, xmax, ymax);
  }

  public void rayCast(RayCastCallback callback, float x1, float y1, float x2, float y2) {
    liquidfunJNI.World_rayCast(swigCPtr, this, RayCastCallback.getCPtr(callback), callback, x1, y1, x2, y2);
  }

  public Joint createJoint(JointDef def) {
    long cPtr = liquidfunJNI.World_createJoint(swigCPtr, this, JointDef.getCPtr(def), def);
    return (cPtr == 0) ? null : new Joint(cPtr, false);
  }

  public MouseJoint createMouseJoint(MouseJointDef def) {
    long cPtr = liquidfunJNI.World_createMouseJoint(swigCPtr, this, MouseJointDef.getCPtr(def), def);
    return (cPtr == 0) ? null : new MouseJoint(cPtr, false);
  }

  public void destroyJoint(Joint joint) {
    liquidfunJNI.World_destroyJoint(swigCPtr, this, Joint.getCPtr(joint), joint);
  }


  //prova

  public RevoluteJoint createRevoluteJoint(JointDef def) {
    long cPtr = liquidfunJNI.World_createJoint(swigCPtr, this, JointDef.getCPtr(def), def);
    return (cPtr == 0) ? null : new RevoluteJoint(cPtr, false);
  }

}
