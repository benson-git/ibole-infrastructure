package com.github.ibole.infrastructure.common.utils;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.
 * </p>
 *********************************************************************************************/


public class Tuple<X, Y> {
  public final X xobj;
  public final Y yobj;

  public Tuple(X x, Y y) {
    this.xobj = x;
    this.yobj = y;
  }

  @Override
  public int hashCode() {
    int hashCode = 11;
    if (xobj != null) {
      hashCode += xobj.hashCode();
    }
    if (yobj != null) {
      hashCode += yobj.hashCode();
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Tuple)) {
      return false;
    }
    @SuppressWarnings("rawtypes")
    Tuple other = (Tuple) obj;
    if (xobj != null) {
      if (!this.xobj.equals(other.xobj)) {
        return false;
      }
    } else if (other.xobj != null) {
      return false;
    }
    if (yobj != null) {
      if (!this.yobj.equals(other.yobj)) {
        return false;
      }
    } else if (other.yobj != null) {
      return false;
    }
    return true;
  }
}
