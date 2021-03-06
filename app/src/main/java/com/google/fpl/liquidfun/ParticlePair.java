/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class ParticlePair {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected ParticlePair(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ParticlePair obj) {
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
        liquidfunJNI.delete_ParticlePair(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setIndexA(int value) {
    liquidfunJNI.ParticlePair_indexA_set(swigCPtr, this, value);
  }

  public int getIndexA() {
    return liquidfunJNI.ParticlePair_indexA_get(swigCPtr, this);
  }

  public void setIndexB(int value) {
    liquidfunJNI.ParticlePair_indexB_set(swigCPtr, this, value);
  }

  public int getIndexB() {
    return liquidfunJNI.ParticlePair_indexB_get(swigCPtr, this);
  }

  public void setFlags(long value) {
    liquidfunJNI.ParticlePair_flags_set(swigCPtr, this, value);
  }

  public long getFlags() {
    return liquidfunJNI.ParticlePair_flags_get(swigCPtr, this);
  }

  public void setStrength(float value) {
    liquidfunJNI.ParticlePair_strength_set(swigCPtr, this, value);
  }

  public float getStrength() {
    return liquidfunJNI.ParticlePair_strength_get(swigCPtr, this);
  }

  public void setDistance(float value) {
    liquidfunJNI.ParticlePair_distance_set(swigCPtr, this, value);
  }

  public float getDistance() {
    return liquidfunJNI.ParticlePair_distance_get(swigCPtr, this);
  }

  public ParticlePair() {
    this(liquidfunJNI.new_ParticlePair(), true);
  }

}
