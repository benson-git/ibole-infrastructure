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

package com.github.ibole.infrastructure.persistence.db.mybatis.pagination;

import com.github.ibole.infrastructure.persistence.pagination.model.PagingCriteria;

import com.google.common.collect.Maps;

import org.apache.commons.beanutils.BeanMap;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * <p>
 * Paging <code>PaginationCriteria</code> finds.
 * </p>
 *
 */
public enum PagingParametersFinder {

  instance;

  /**
   * The search parameters by use of interim storage of results.
   */
  private final Map<Object, String> searchMap = Maps.newHashMap();

  /**
   * private constructor
   */
  private PagingParametersFinder() {}


  /**
   * from the formulation of the objects found in the paging parameters object.
   *
   * @param object object.
   * @return paging parameters.
   */
  public PagingCriteria findCriteria(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return findCriteriaFromObject(object);
    } finally {
      // cleanup query the value of the temporary Map.
      searchMap.clear();
    }
  }

  /**
   * In the object to find whether contains <code>PaginationCriteria</code> objects.
   *
   * @param object parameter object.
   * @return PaginationCriteria
   */
  @SuppressWarnings("rawtypes")
  private PagingCriteria findCriteriaFromObject(Object object) {

    // 如果已经寻找过这个对象，现在再来这里肯定是没找到。就直接返回NULL
    if (searchMap.containsKey(object)) {
      return null;
    }
    // object class
    Class<?> objClass = object.getClass();
    PagingCriteria pc;
    // primitive
    if (isPrimitiveType(objClass)) {
      pc = null;
    } else if (object instanceof PagingCriteria) {
      pc = (PagingCriteria) object;
    } else if (object instanceof Map) {
      pc = findCriteriaFromMap((Map) object);
    } else if (object instanceof Collection) {
      pc = findCriteriaFromCollection((Collection) object);
    } else if (objClass.isArray()) {
      pc = findCriteriaFromArray(object);
    } else {
      BeanMap map = new BeanMap(object);
      return findCriteriaFromMap(map);
    }

    searchMap.put(object, SqlHelper.EMPTY);
    return pc;
  }

  /**
   * In the array to find whether it contains the <code>PaginationCriteria</code> object.
   *
   * @param array the array.
   * @return PageQuery
   */
  private PagingCriteria findCriteriaFromArray(Object array) {
    if (searchMap.containsKey(array)) {
      return null;
    }

    Object object;
    PagingCriteria pc;
    for (int i = 0; i < Array.getLength(array); i++) {
      object = Array.get(array, i);
      pc = findCriteriaFromObject(object);
      if (pc != null) {
        searchMap.put(array, SqlHelper.EMPTY);
        return pc;
      }
    }
    searchMap.put(array, SqlHelper.EMPTY);
    return null;
  }

  /**
   * In the Collection to find whether contains <code>PaginationCriteria</code> objects.
   *
   * @param collection parameter collection.
   * @return PageQuery
   */
  @SuppressWarnings("rawtypes")
  private PagingCriteria findCriteriaFromCollection(Collection collection) {
    if (searchMap.containsKey(collection)) {
      return null;
    }
    PagingCriteria pc;

    for (Object e : collection) {
      pc = findCriteriaFromObject(e);
      if (pc != null) {
        searchMap.put(collection, SqlHelper.EMPTY);
        return pc;
      }
    }

    searchMap.put(collection, SqlHelper.EMPTY);
    return null;
  }

  /**
   * In the Map to find whether contains <code>PaginationCriteria</code> objects.
   *
   * @param map parameter map.
   * @return PaginationCriteria
   */
  @SuppressWarnings("rawtypes")
  private PagingCriteria findCriteriaFromMap(Map map) {
    if (searchMap.containsKey(map)) {
      return null;
    }

    PagingCriteria pc;
    for (Object value : map.values()) {
      pc = findCriteriaFromObject(value);
      if (pc != null) {
        searchMap.put(map, SqlHelper.EMPTY);
        return pc;
      }
    }

    searchMap.put(map, SqlHelper.EMPTY);
    return null;
  }



  /**
   * 返回指定类型所对应的primitive类型。包含String类
   * <p/>
   * fixed:paramter string type.
   *
   * @param clazz 要检查的类型
   * @return 如果指定类型为<code>null</code>或不是primitive类型的包装类，则返回<code>false</code>，否则返回<code>true</code>。
   */
  @SuppressWarnings("rawtypes")
  public static boolean isPrimitiveType(Class clazz) {
    return clazz != null
        && (clazz.isPrimitive() || clazz.equals(Long.class) || clazz.equals(Integer.class)
            || clazz.equals(Short.class) || clazz.equals(Byte.class) || clazz.equals(Double.class)
            || clazz.equals(Float.class) || clazz.equals(Boolean.class)
            || clazz.equals(Character.class) || clazz.equals(String.class));

  }
}
