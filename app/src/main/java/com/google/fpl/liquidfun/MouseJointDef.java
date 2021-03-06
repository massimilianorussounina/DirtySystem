/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class MouseJointDef extends JointDef {
  private transient long swigCPtr;

  protected MouseJointDef(long cPtr, boolean cMemoryOwn) {
    super(liquidfunJNI.MouseJointDef_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MouseJointDef obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_MouseJointDef(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public void setMaxForce(float value) {
    liquidfunJNI.MouseJointDef_maxForce_set(swigCPtr, this, value);
  }

  public float getMaxForce() {
    return liquidfunJNI.MouseJointDef_maxForce_get(swigCPtr, this);
  }

  public void setFrequencyHz(float value) {
    liquidfunJNI.MouseJointDef_frequencyHz_set(swigCPtr, this, value);
  }

  public float getFrequencyHz() {
    return liquidfunJNI.MouseJointDef_frequencyHz_get(swigCPtr, this);
  }

  public void setDampingRatio(float value) {
    liquidfunJNI.MouseJointDef_dampingRatio_set(swigCPtr, this, value);
  }

  public float getDampingRatio() {
    return liquidfunJNI.MouseJointDef_dampingRatio_get(swigCPtr, this);
  }

  public void setTarget(float targetX, float targetY) {
    liquidfunJNI.MouseJointDef_setTarget(swigCPtr, this, targetX, targetY);
  }

  public MouseJointDef() {
    this(liquidfunJNI.new_MouseJointDef(), true);
  }

}
