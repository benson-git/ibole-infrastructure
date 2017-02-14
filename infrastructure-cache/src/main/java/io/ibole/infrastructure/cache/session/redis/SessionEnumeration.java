package io.ibole.infrastructure.cache.session.redis;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
