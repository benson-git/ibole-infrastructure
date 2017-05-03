/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.infrastructure.spi.cache.ehcache;

import com.github.ibole.infrastructure.spi.cache.CacheAdapter;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class EhCache extends CacheAdapter {

  private final String cacheName;
  private final CacheManager cacheManager;

  public EhCache(final String cacheName, final CacheManager cacheManager) {
    this.cacheName = cacheName;
    this.cacheManager = cacheManager;
  }

  public Ehcache getCache() {
    return cacheManager.getEhcache(cacheName);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T doGet(String key) {
    Element element = getCache().get(key);
    if (element == null) {
      return null;
    }
    return (T) element.getObjectValue();
  }

  @Override
  protected <T> void doSet(int ttlSeconds, String key, T value) {
    getCache().put(element(ttlSeconds, key, value));
  }

  @Override
  protected void doRemove(String key) {
    getCache().remove(key);
  }

  private Element element(int ttlSeconds, String key, Object value) {
    Element element = new Element(key, value);
    if (ttlSeconds == 0) {
      element.setEternal(true);
    } else {
      element.setTimeToLive(ttlSeconds);
    }
    return element;
  }
}
