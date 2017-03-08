package com.github.ibole.infrastructure.cache.session.redis;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/

public class SessionEnumeration implements Enumeration<String> {

  private Iterator<String> iterator;

  public SessionEnumeration(Map<String, Object> data) {
    this.iterator = data.keySet().iterator();
  }


  public boolean hasMoreElements() {
    return this.iterator.hasNext();
  }


  public String nextElement() {
    return this.iterator.next();
  }

}
