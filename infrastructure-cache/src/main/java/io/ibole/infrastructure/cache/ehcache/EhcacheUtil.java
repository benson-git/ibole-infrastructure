package io.ibole.infrastructure.cache.ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.Serializable;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public class EhcacheUtil {
  private static Logger logger = LoggerFactory.getLogger(EhcacheUtil.class.getName());
  static CacheManager manager = null;
  static String configfile = "ehcache.xml";
  static {
    try {
      manager =
          CacheManager.create(EhcacheUtil.class.getClassLoader().getResourceAsStream(configfile));
    } catch (CacheException e) {
      logger.error("Ehcache creation exception", e);
    }
  }

  public static void put(String cachename, Serializable key, Serializable value) {
    manager.getCache(cachename).put(new Element(key, value));
  }

  public static Serializable get(String cachename, Serializable key) {
    try {
      Element element = manager.getCache(cachename).get(key);
      if (element == null)
        return null;
      return element.getValue();
    } catch (IllegalStateException | CacheException e) {
      logger.error("Getting cache from {} with key {} exception", cachename, key, e);

    }
    return null;
  }

  public static void clearCache(String cachename) {
    try {
      manager.getCache(cachename).removeAll();
    } catch (IllegalStateException e) {
      logger.error("Clear cache from {} exception", cachename, e);
    }
  }

  public static void remove(String cachename, Serializable key) {
    manager.getCache(cachename).remove(key);
  }

  public static void removeAll() {
    manager.removalAll();
  }

  public static void clearAll() {
    manager.clearAll();
  }

  public static void shutdown() {
    manager.shutdown();
  }
}
