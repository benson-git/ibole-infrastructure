package com.github.ibole.infrastructure.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class can be used for reflection purpose. Both class should match the getter and setter.
 * supports collections list and set copy.
 */
public class ReflectionUtil {
  
  private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class.getName());
  private static final String SETTER_PREFIX = "set";
  private static final String GETTER_PREFIX = "get";
  private static final String CGLIB_CLASS_SEPARATOR = "$$";

  /**
   * This method is used to copy one bean values to another bean.
   * 
   * @param toClazz Class object of Destination bean, which will be used to create class instance to
   *        copy values from passed object
   * @param from Origin bean from where to copy values
   * @param <T> T Type parameter
   * @return object copied from
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   * @throws InstantiationException If not able to create an instance of an passed toClass
   * @throws IllegalAccessException If access denied to access given class
   */
  public static <T> T copy(Class<T> toClazz, Object from) throws Exception {
    return copy(toClazz, from, false);
  }

  /**
   * This method is used to copy one bean values to another bean.
   * 
   * @param to : Destination bean where values need to be copy from origin
   * @param from : Origin bean from where to copy values
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   */
  public static void copy(final Object to, final Object from) throws Exception {
    copy(to, from, false);
  }

  /**
   * This method is used to copy one bean values to another bean.
   * 
   * @param to : Origin bean from where to copy values
   * @param from : Destination bean where values need to be copy from origin
   * @param strictCopy boolean value denoting that copy should be in strict manner or not. If this
   *        is true then null values will also copied to destination bean from origin bean
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   */
  public static synchronized void copy(final Object to, final Object from, boolean strictCopy)
      throws Exception {
    copy(to, from, strictCopy, null);
  }

  /**
   * This method is used to copy one bean values to another bean.
   * 
   * @param toClazz Class object of Destination bean, which will be used to create class instance to
   *        copy values from passed object
   * @param from Origin bean from where to copy values
   * @param strictCopy boolean value denoting that copy should be in strict manner or not. If this
   *        is true then null values will also copied to destination bean from origin bean
   * @param <T> T Type parameter
   * @return object copied from
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   * @throws InstantiationException If not able to create an instance of an passed toClass
   * @throws IllegalAccessException If access denied to access given class
   */
  @SuppressWarnings("unchecked")
  public static <T> T copy(Class<T> toClazz, Object from, boolean strictCopy) throws Exception {
    if (toClazz == null)
      throw new IllegalArgumentException("No destination bean specified");
    Object to = toClazz.newInstance();
    copy(to, from, strictCopy);
    return (T) to;
  }

  /**
   * This method is used to copy one bean values to another bean. This method accepts the Map of
   * excluded classes which means if any of the class in Map key found then instead of throwing
   * exception it copies its value to its value class object.
   * 
   * @param toClazz Class object of Destination bean, which will be used to create class instance to
   *        copy values from passed object
   * @param from Origin bean from where to copy values
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   * @param <T> T Type parameter
   * @return Object copied from
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   * @throws InstantiationException If not able to create an instance of an passed toClass
   * @throws IllegalAccessException If access denied to access given class
   * 
   */
  @SuppressWarnings("unchecked")
  public static <T> T copy(Class<T> toClazz, Object from, Map<Class<?>, Class<?>> excludeClassesMap)
      throws Exception {
    if (toClazz == null)
      throw new IllegalArgumentException("No destination bean specified");
    Object to = toClazz.newInstance();
    copy(to, from, false, excludeClassesMap);
    return (T) to;
  }

  /**
   * This method is used to copy one bean values to another bean. This method accepts the Map of
   * excluded classes which means if any of the class in Map key found then instead of throwing
   * exception it copies its value to its value class object.
   * 
   * @param to : Destination bean where values need to be copy from origin
   * @param from : Origin bean from where to copy values
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   */
  public static void copy(final Object to, final Object from,
      final Map<Class<?>, Class<?>> excludeClassesMap) throws IllegalArgumentException,
      InvocationTargetException {
    copy(to, from, false, excludeClassesMap);
  }

  /**
   * This method is used to copy one bean values to another bean.
   * 
   * @param to : Origin bean from where to copy values
   * @param from : Destination bean where values need to be copy from origin
   * @param strictCopy boolean value denoting that copy should be in strict manner or not. If this
   *        is true then null values will also copied to destination bean from origin bean
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   */
  private static synchronized void copy(final Object to, final Object from, boolean strictCopy,
      final Map<Class<?>, Class<?>> excludeClassesMap) throws IllegalArgumentException,
      InvocationTargetException {
    if (to == null) {
      throw new IllegalArgumentException("No destination bean specified");
    }
    if (from == null) {
      throw new IllegalArgumentException("No origin bean specified");
    }
    try {
      if (checkForCollection(to, from)) {
        copyCollection((Collection<?>) to, (Collection<?>) from, excludeClassesMap);
      } else {
        Class<? extends Object> toClass = to.getClass();
        Method[] toMethods = toClass.getMethods();
        Set<Method> toMethodsList = new LinkedHashSet<Method>(Arrays.asList(toMethods));

        Class<? extends Object> fromClass = from.getClass();
        Method[] fromMethods = fromClass.getMethods();
        Set<Method> fromMethodsList = new LinkedHashSet<Method>(Arrays.asList(fromMethods));

        Iterator<Method> fromMethodsIterator = fromMethodsList.iterator();
        while (fromMethodsIterator.hasNext()) {
          Method fromMethod = fromMethodsIterator.next();
          boolean isBoolean = false;
          Class<?> returnType = fromMethod.getReturnType();
          if (fromMethod.getName().startsWith("is") && boolean.class.equals(returnType)) {
            isBoolean = true;
          }
          if (isGetter(fromMethod, isBoolean)) {
            Object value = null;
            try {
              value = fromMethod.invoke(from);
            } catch (Exception e) {
              throw new InvocationTargetException(e,
                  "Getter method cannot have any parameter. Error on method : "
                      + fromMethod.getName());
            }
            if (strictCopy) {
              setValues(returnType, value, fromMethod, to, toMethodsList, isBoolean,
                  excludeClassesMap);
            } else {
              if (value != null) {
                setValues(returnType, value, fromMethod, to, toMethodsList, isBoolean,
                    excludeClassesMap);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {

    }
  }

  /**
   * This method is used to find setter method.
   * 
   * @param clazz The class
   * @param fieldName the field name used to find the matching matching method.
   * @return setter Method if found, otherwise return null.
   */
  public static Method findSetterMethod(Class<?> clazz, String fieldName) {
    String expectedName =
        "set" + new String(new char[] {fieldName.charAt(0)}).toUpperCase() + fieldName.substring(1);

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getName().equals(expectedName) && method.getParameterTypes().length == 1) {
        if (method.getParameterTypes()[0].equals(fieldName.getClass())) {
          return method;
        }
      }
    }
    return null;
  }

  /**
   * This method is used to check method is getter or not.
   * 
   * @param method Method which needs to be check
   * @param isBoolean If the given method is primitive boolean type because getter for it starts
   *        with "is".
   * @return true is method is getter
   */
  private static boolean isGetter(Method method, boolean isBoolean) {
    if (isBoolean && !method.getName().startsWith("is"))
      return false;
    if (!isBoolean && !method.getName().startsWith("get"))
      return false;
    if (method.getParameterTypes().length != 0)
      return false;
    if (void.class.equals(method.getReturnType()))
      return false;
    return true;
  }

  /**
   * This method is used to check method is setter or not.
   * 
   * @param method Method which needs to be check
   * @return true is method is setter
   */
  private static boolean isSetter(Method method) {
    if (!method.getName().startsWith("set"))
      return false;
    if (method.getParameterTypes().length != 1)
      return false;
    return true;
  }

  /**
   * This method set the values to destination bean
   * 
   * @param returnType Return type of from class method
   * @param value value of from class getter method to be copied to destination
   * @param fromMethod method of from class
   * @param to destination class object
   * @param toMethodsList set of to class all methods
   * @param isBoolean method return type is boolean or not
   * @throws IllegalArgumentException If passed bean is null.
   * @throws InvocationTargetException If method access is denied
   * @throws IllegalAccessException If access denied to access given class
   */
  private static void setValues(Class<?> returnType, Object value, final Method fromMethod,
      final Object to, Set<Method> toMethodsList, boolean isBoolean,
      Map<Class<?>, Class<?>> excludeClassesMap) throws IllegalArgumentException,
      IllegalAccessException, InvocationTargetException {
    if (!returnType.isPrimitive()) {
      fromMethod.getReturnType().cast(value);
    }
    Iterator<Method> toMethodsIterator = toMethodsList.iterator();
    while (toMethodsIterator.hasNext()) {
      Method toMethod = (Method) toMethodsIterator.next();
      if (isSetter(toMethod)) {
        int fromSubstringIndex = 3;
        if (isBoolean)
          fromSubstringIndex = 2;
        if (toMethod.getName().substring(3)
            .equals(fromMethod.getName().substring(fromSubstringIndex))) {
          try {
            if (excludeClassesMap != null && value instanceof Collection) {
              processInnerCollections(toMethod, fromMethod, value, to, excludeClassesMap);
            } else {
              toMethod.invoke(to, value);
            }
          } catch (Exception e) {
            if (value != null) {
              Class<?>[] parameterTypes = toMethod.getParameterTypes();
              Class<?> pTypeClazz = parameterTypes[0];
              if (pTypeClazz.isInstance(to)) {
                // Instance found for same class.
                try {
                  toMethod.invoke(
                      to,
                      toMethod.getDeclaringClass().cast(
                          copy(toMethod.getDeclaringClass(), value, excludeClassesMap)));
                } catch (Exception e1) {
                  System.err.println(e1.getMessage()
                      + ": Not able to copy the same instance of given object");
                  e1.printStackTrace();
                }
              } else {
                if (excludeClassesMap != null && excludeClassesMap.get(pTypeClazz) != null
                    && excludeClassesMap.get(pTypeClazz).equals(fromMethod.getReturnType())) {
                  try {
                    toMethod
                        .invoke(to, pTypeClazz.cast(copy(pTypeClazz, value, excludeClassesMap)));
                  } catch (Exception e2) {
                    e2.printStackTrace();
                  }
                }
              }
            } else {
              throw new InvocationTargetException(e,
                  "Data type mismatched or access specifier is wrong. Error on method: TO method = "
                      + toMethod.getName() + " FROM method = " + fromMethod.getName());
            }
          }
          toMethodsList.remove(toMethod);
          break;
        }
      }
    }
  }

  /**
   * This method is used to copy collections of User Defined classes.
   * 
   * @param toMethod method of to class
   * @param fromMethod method of from class
   * @param value - value to copy
   * @param to destination class object
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private static void processInnerCollections(Method toMethod, Method fromMethod, Object value,
      Object to, Map<Class<?>, Class<?>> excludeClassesMap) throws Exception {

    Type[] types = toMethod.getGenericParameterTypes();
    ParameterizedType pType = (ParameterizedType) types[0];
    Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];

    Type type = fromMethod.getGenericReturnType();
    ParameterizedType fpType = (ParameterizedType) type;
    Class<?> fromSubclazz = (Class<?>) fpType.getActualTypeArguments()[0];
    if (excludeClassesMap.get(clazz) != null && excludeClassesMap.get(clazz).equals(fromSubclazz)) {
      if (value instanceof List) {
        @SuppressWarnings("rawtypes")
        List list = new ArrayList();
        for (@SuppressWarnings("rawtypes")
        Iterator it = ((List) value).iterator(); it.hasNext();) {
          list.add(copy(clazz, it.next(), excludeClassesMap));
        }
        toMethod.invoke(to, list);
      } else if (value instanceof Set) {
        @SuppressWarnings("rawtypes")
        Set set = new HashSet();
        for (@SuppressWarnings("rawtypes")
        Iterator it = ((Set) value).iterator(); it.hasNext();) {
          set.add(copy(clazz, it.next(), excludeClassesMap));
        }
        toMethod.invoke(to, set);
      }
    } else {
      toMethod.invoke(to, value);
    }
  }

  /**
   * This method put the given field value into given object.
   * 
   * @param obj - object where value has to be put
   * @param clazz - class of passed object
   * @param fieldName - {@link String} field(variable) name
   * @param value - value which has to be set
   * @param <T> Type parameter
   */
  public static <T> void findPut(Object obj, Class<T> clazz, String fieldName, Object value) {
    if (clazz.isInstance(obj)) {
      clazz.cast(obj);
    }
    try {
      Field field = clazz.getDeclaredField(fieldName);
      String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method setter = clazz.getMethod(methodName, field.getType());
      setter.invoke(obj, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method put the given map string key field key with their respective value into given
   * object
   * 
   * @param obj - object where value has to be put
   * @param map - {@link Map} String key and Object value map, where key will be field name and
   *        object will be value for that field.
   */
  public static void findNPut(Object obj, Map<String, Object> map) {
    if (obj == null || map == null || map.size() == 0) {
      System.err.println("Invalid Input.");
      return;
    }
    Class<?> clazz = obj.getClass();
    if (clazz.isInstance(obj)) {
      try {
        clazz.cast(obj);
        Set<String> fields = map.keySet();
        for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
          String variable = (String) iterator.next();
          Field field = null;
          try {
            field = clazz.getDeclaredField(variable);
          } catch (Exception e) {
            System.err.println(variable
                + " : field not found in this class hence finding in its super class.");
            try {
              field = clazz.getSuperclass().getDeclaredField(variable);
            } catch (Exception e1) {
              System.err.println(variable
                  + " : field also not found in super class, hence skipping it.");
              continue;
            }
          }
          if (field != null) {
            Method setter = null;
            String methodName =
                "set" + variable.substring(0, 1).toUpperCase() + variable.substring(1);
            try {
              setter = clazz.getDeclaredMethod(methodName, field.getType());
            } catch (Exception e) {
              System.err.println(methodName
                  + " : method not found in this class hence finding in its super class.");
              try {
                setter = clazz.getSuperclass().getDeclaredMethod(methodName, field.getType());
              } catch (Exception e1) {
                System.err.println(methodName
                    + " : method also not found in super class, hence skipping it.");
                continue;
              }
            }
            if (setter != null) {
              try {
                setter.invoke(obj, map.get(variable));
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Cannot cast passed object " + obj + " to given " + clazz + " Class.");
      }
    } else {
      System.err.println("Sent Object " + obj + " is not an instance of passed " + clazz
          + " Class.");
    }
  }

  /**
   * This method check that to and from object are collections object or not.
   * 
   * @param to - to object
   * @param from - from object
   * @exception {@link IllegalArgumentException} - if one is collection and other is not.
   * @return true if both are collections else if both are not collections then false otherwise
   *         throw {@link IllegalArgumentException}
   */
  private static boolean checkForCollection(Object to, Object from) throws IllegalArgumentException {
    if (to instanceof Collection) {
      if (from instanceof Collection) {
        return true;
      } else {
        throw new IllegalArgumentException(
            "From object found as collection but To object is not a collection.");
      }
    } else
      return false;
  }

  /**
   * This method copy the one collection object values into another collection.
   * 
   * @param to - to object
   * @param from - from object
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   */
  @SuppressWarnings("rawtypes")
  private static void copyCollection(Collection<?> to, Collection<?> from,
      Map<Class<?>, Class<?>> excludeClassesMap) {
    if (!from.isEmpty()) {
      if (to instanceof List && from instanceof List) {
        copyList((List) to, (List) from, excludeClassesMap);
      } else if (to instanceof Set && from instanceof Set) {
        copySet((Set) to, (Set) from, excludeClassesMap);
      }
    }
  }

  /**
   * This method copy the one list object values into another list.
   * 
   * @param to - to object
   * @param from - from object
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void copyList(List to, List from, Map<Class<?>, Class<?>> excludeClassesMap) {
    for (Object obj : from) {
      if (obj != null) {
        Class<?> fromListParametrizedClass = obj.getClass();
        try {
          if (excludeClassesMap != null && excludeClassesMap.get(fromListParametrizedClass) != null) {
            Object convertedObj =
                ReflectionUtil.copy(excludeClassesMap.get(fromListParametrizedClass), obj);
            to.add(convertedObj);
          } else {
            to.add(obj);
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println("Exception while copying list data: + " + obj);
        }
      }
    }
  }

  /**
   * This method copy the one set object values into another set.
   * 
   * @param to - to object
   * @param from - from object
   * @param excludeClassesMap {@link Map} map of excluded classes key-value pair. eg. key will be
   *        class in "to" and value will be class in "from".
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void copySet(Set to, Set from, Map<Class<?>, Class<?>> excludeClassesMap) {
    for (Object obj : from) {
      if (obj != null) {
        Class<?> fromListParametrizedClass = obj.getClass();
        if (fromListParametrizedClass != null)
          fromListParametrizedClass = obj.getClass();
        try {
          if (excludeClassesMap != null && excludeClassesMap.get(fromListParametrizedClass) != null) {
            Object convertedObj =
                ReflectionUtil.copy(excludeClassesMap.get(fromListParametrizedClass), obj);
            to.add(convertedObj);
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println("Exception while copying set data: + " + obj);
        }
      }
    }
  }

  /**
   * 调用Getter方法. 支持多级，如：对象名.对象名.方法
   */
  public static Object invokeGetter(Object obj, String propertyName) {
    Object object = obj;
    for (String name : StringUtils.split(propertyName, ".")) {
      String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
      object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
    }
    return object;
  }

  /**
   * 调用Setter方法, 仅匹配方法名。 支持多级，如：对象名.对象名.方法
   */
  public static void invokeSetter(Object obj, String propertyName, Object value) {
    Object object = obj;
    String[] names = StringUtils.split(propertyName, ".");
    for (int i = 0; i < names.length; i++) {
      if (i < names.length - 1) {
        String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
        object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
      } else {
        String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
        invokeMethodByName(object, setterMethodName, new Object[] {value});
      }
    }
  }

  /**
   * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
   */
  public static Object getFieldValue(final Object obj, final String fieldName) {
    Field field = getAccessibleField(obj, fieldName);

    if (field == null) {
      throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target ["
          + obj + "]");
    }

    Object result = null;
    try {
      result = field.get(obj);
    } catch (IllegalAccessException e) {
      logger.error("不可能抛出的异常{}", e.getMessage());
    }
    return result;
  }

  /**
   * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
   */
  public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
    Field field = getAccessibleField(obj, fieldName);

    if (field == null) {
      throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target ["
          + obj + "]");
    }

    try {
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      logger.error("不可能抛出的异常:{}", e.getMessage());
    }
  }

  /**
   * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
   * 同时匹配方法名+参数类型，
   */
  public static Object invokeMethod(final Object obj, final String methodName,
      final Class<?>[] parameterTypes, final Object[] args) {
    Method method = getAccessibleMethod(obj, methodName, parameterTypes);
    if (method == null) {
      throw new IllegalArgumentException("Could not find method [" + methodName + "] on target ["
          + obj + "]");
    }

    try {
      return method.invoke(obj, args);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }

  /**
   * 直接调用对象方法, 无视private/protected修饰符， 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
   * 只匹配函数名，如果有多个同名函数调用第一个。
   */
  public static Object invokeMethodByName(final Object obj, final String methodName,
      final Object[] args) {
    Method method = getAccessibleMethodByName(obj, methodName);
    if (method == null) {
      throw new IllegalArgumentException("Could not find method [" + methodName + "] on target ["
          + obj + "]");
    }

    try {
      return method.invoke(obj, args);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }

  /**
   * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
   * 如向上转型到Object仍无法找到, 返回null.
   */
  public static Field getAccessibleField(final Object obj, final String fieldName) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(fieldName, "fieldName can't be blank");
    for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass =
        superClass.getSuperclass()) {
      try {
        Field field = superClass.getDeclaredField(fieldName);
        makeAccessible(field);
        return field;
      } catch (NoSuchFieldException e) {
        // Field不在当前类定义,继续向上转型
        continue;// new add
      }
    }
    return null;
  }

  /**
   * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 
   * 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型.
   * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   */
  public static Method getAccessibleMethod(final Object obj, final String methodName,
      final Class<?>... parameterTypes) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(methodName, "methodName can't be blank");

    for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType =
        searchType.getSuperclass()) {
      try {
        Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
        makeAccessible(method);
        return method;
      } catch (NoSuchMethodException e) {
        // Method不在当前类定义,继续向上转型
        continue;// new add
      }
    }
    return null;
  }

  /**
   * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 
   * 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
   * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   */
  public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(methodName, "methodName can't be blank");

    for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType =
        searchType.getSuperclass()) {
      Method[] methods = searchType.getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(methodName)) {
          makeAccessible(method);
          return method;
        }
      }
    }
    return null;
  }

  /**
   * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨.
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass()
        .getModifiers())) && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨.
   */
  public static void makeAccessible(Field field) {
    if ((!Modifier.isPublic(field.getModifiers())
        || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field
        .getModifiers())) && !field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  /**
   * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. 
   * <pre>
   * eg. 
   * {@code
   *   public UserDao extendsHibernateDao<User>
   * }
   * </pre>
   * @param clazz The class to introspect
   * @return the first generic declaration, or Object.class if cannot be determined
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> Class<T> getClassGenricType(final Class clazz) {
    return getClassGenricType(clazz, 0);
  }

  /**
   * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
   * @param clazz clazz The class to introspect
   * @param index the Index of the generic declaration,start from 0.
   * @return the index generic declaration, or Object.class if cannot be determined
   */
  public static Class getClassGenricType(final Class clazz, final int index) {

    Type genType = clazz.getGenericSuperclass();
    if (!(genType instanceof ParameterizedType)) {
      return Object.class;
    }

    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

    if (index >= params.length || index < 0) {
      logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
          + "'s Parameterized Type: " + params.length);
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      logger.warn(clazz.getSimpleName()
          + " not set the actual class on superclass generic parameter");
      return Object.class;
    }

    return (Class) params[index];
  }

  /**
   * Get User Class.
   * @param instance Object
   * @return the user class
   */
  public static Class<?> getUserClass(Object instance) {
    //Assert.notNull(instance, "Instance must not be null");
    Class clazz = instance.getClass();
    if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
      Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass)) {
        return superClass;
      }
    }
    return clazz;

  }

  /**
   * 将反射时的checked exception转换为unchecked exception.
   */
  public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
    if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
        || e instanceof NoSuchMethodException) {
      return new IllegalArgumentException(e);
    } else if (e instanceof InvocationTargetException) {
      return new RuntimeException(((InvocationTargetException) e).getTargetException());
    } else if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    }
    return new RuntimeException("Unexpected Checked Exception.", e);
  }
}
