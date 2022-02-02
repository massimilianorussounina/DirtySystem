/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public enum BodyType {
  staticBody(0),
  kinematicBody,
  dynamicBody;

  public final int swigValue() {
    return swigValue;
  }

  public static BodyType swigToEnum(int swigValue) {
    BodyType[] swigValues = BodyType.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (BodyType swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + BodyType.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private BodyType() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private BodyType(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private BodyType(BodyType swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

