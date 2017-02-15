package io.ibole.infrastructure.common.protobuf;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
/**
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */

public final class ProtobufUtils {

  private static final Logger logger = LoggerFactory.getLogger(ProtobufUtils.class);

  private static Map<Class<?>, Converter<?>> CONVERTER = Maps.newConcurrentMap();

  static {
    register();
  }

  private static void register() {

    CONVERTER.put(Date.class, new Date2TimstampConvertr());
    CONVERTER.put(Timestamp.class, new Timstamp2DateConvertr());

  }

  /**
   * Register converter.
   * 
   * @param clazz the type to be converted
   * @param converter the converter to be registered
   */
  public static void registerConverter(Class<?> clazz, Converter<?> converter) {
    if (!CONVERTER.containsKey(clazz)) {
      CONVERTER.putIfAbsent(clazz, converter);
    }
  }

  /**
   * Convert from Object to T.
   * 
   * @param <T> to
   */
  public interface Converter<T> {

    T convert(Object obj);

  }

  static class Date2TimstampConvertr implements Converter<Timestamp> {
    /**
     * Convert to timestamp.
     */
    public Timestamp convert(Object date) {

      return toTimestamp((Date) date);
    }
  }

  static class Timstamp2DateConvertr implements Converter<Date> {
    /**
     * Convert to date.
     */
    public Date convert(Object timestamp) {

      return toDate((Timestamp) timestamp);
    }
  }


  /**
   * Convert a Date type to a Timestamp.
   * 
   * @param date the input param to convert
   * @return the instance of Timestamp
   */
  public static Timestamp toTimestamp(Date date) {
    checkArgument(date != null, "Date cannot be null!");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    Timestamp time = Timestamp.newBuilder().setSeconds(calendar.getTimeInMillis() / 1000L).build();
    return time;

  }

  /**
   * Convert a Timestamp type to a Date.
   * 
   * @param time the input param to convert
   * @return the instance of Date
   */
  public static Date toDate(Timestamp time) {
    checkArgument(time != null, "Timestamp cannot be null!");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time.getSeconds() * 1000L);

    return calendar.getTime();

  }

  /**
   * Convert a Message to json string. Example: <code>
   * String json2 = toJsonString(HelloReply.newBuilder().setMessage("dd").build());
   *  </code>
   * 
   * @param message to convert to json string
   * @return string the Json format
   * @throws IOException parse error happen
   */
  public static String toJsonString(Message message) throws IOException {
    return JsonFormat.printer().print(message);
  
  }

  /**
   * Convert to json string to specified message builder. Example: <code> String json =      "{\n"
   *   + "  \"message\": 12345\n"
   *   + "}";
   *  Builder builder = HelloReply.newBuilder();
   *  mergeFromJson(json2, builder);</code>
   * 
   * @param json the json string to merge to a builder.
   * @param builder the messagge builder
   * @throws IOException merge error happen
   */
  public static void mergeFromJson(String json, Message.Builder builder) throws IOException {
    JsonFormat.parser().merge(json, builder);
  }

  /**
   * 判断如果bean字段是原生类型包装类型，并且它的值是空值， 就为其设置一个默认值，默认值的设置按照java原生类型默认值规范。 Check if bean properties are
   * null, if the property is null, it will be set to a default value following the specification of
   * the default of primitive.
   * 
   * @param bean the populated bean
   * @return Object the bean to check and set default value for its properties
   */
  public static <T> T populateDefaultValue(T bean) {
    checkArgument(bean != null, "Object cannot be null!");
    try {

      BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
      PropertyDescriptor[] classProperties = beanInfo.getPropertyDescriptors();

      for (PropertyDescriptor classPropertie : classProperties) {
        Method getter = classPropertie.getReadMethod();
        Class<?> returnType = getter.getReturnType();
        Method setter = classPropertie.getWriteMethod();
        if (isPrimitiveWrapper(returnType) && getter.invoke(bean) == null) {
          setDefaultValue4Boolean(bean, returnType, setter);
          setDefaultValue4Byte(bean, returnType, setter);
          setDefaultValue4Character(bean, returnType, setter);
          setDefaultValue4Short(bean, returnType, setter);
          setDefaultValue4Integer(bean, returnType, setter);
          setDefaultValue4Long(bean, returnType, setter);
          setDefaultValue4Float(bean, returnType, setter);
          setDefaultValue4Double(bean, returnType, setter);
          setDefaultValue4String(bean, returnType, setter);
          setDefaultValue4Date(bean, returnType, setter);
          setDefaultValue4BigDecimal(bean, returnType, setter);
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException("Error happen when handling null value.", ex);
    }

    return bean;
  }

  private static void setDefaultValue4Boolean(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Boolean.class) {
      setter.invoke(obj, false);
    }
  }

  private static void setDefaultValue4Byte(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Byte.class) {
      setter.invoke(obj, 0);
    }
  }

  private static void setDefaultValue4Character(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Character.class) {
      setter.invoke(obj, '\u0000');
    }
  }

  private static void setDefaultValue4Short(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Short.class) {
      setter.invoke(obj, 0);
    }
  }

  private static void setDefaultValue4Integer(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Integer.class) {
      setter.invoke(obj, 0);
    }
  }

  private static void setDefaultValue4Long(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Long.class) {
      setter.invoke(obj, 0L);
    }
  }

  private static void setDefaultValue4Float(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Float.class) {
      setter.invoke(obj, 0.0f);
    }
  }

  private static void setDefaultValue4Double(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Double.class) {
      setter.invoke(obj, 0.0d);
    }
  }

  private static void setDefaultValue4String(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == String.class) {
      setter.invoke(obj, "");
    }
  }

  private static void setDefaultValue4Date(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == Date.class) {
      setter.invoke(obj, toDate(Timestamp.newBuilder().build()));
    }
  }

  private static void setDefaultValue4BigDecimal(Object obj, Class<?> cls, Method setter)
      throws Exception {
    if (cls == BigDecimal.class) {
      setter.invoke(obj, BigDecimal.ZERO);
    }
  }

  private static boolean isPrimitiveWrapper(Class<?> cls) {
    return /** cls.isPrimitive() || **/
    cls == Boolean.class || cls == Byte.class || cls == Character.class || cls == Short.class
        || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class
        || cls == String.class || cls == Date.class || cls == BigDecimal.class
        || cls == Timestamp.class;
  }

  /**
   * Create a new protobuf message based on the provide message type, and copy the property value of
   * POJO to the new protobuf message.
   * 
   * @param msgClazz the target type of protobuf message
   * @param srcObject the source object
   * @return a new protobuf message object
   * @throws Exception copy exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T> T copy2ProtoBufMessage(
      Class<? extends com.google.protobuf.GeneratedMessageV3> msgClazz, Object srcObject)
      throws Exception {
    checkArgument(msgClazz != null, "Target protoBuf message cannot be null!");
    checkArgument(srcObject != null, "Source object cannot be null!");
    // MethodUtils.invokeMethod();
    Method newBuilder = msgClazz.getMethod("newBuilder");
    com.google.protobuf.GeneratedMessageV3.Builder builder =
        (com.google.protobuf.GeneratedMessageV3.Builder) newBuilder.invoke(null);

    return (T) copy2ProtoBufMessageBuilder(builder, srcObject).build();
  }

  /**
   * Copy the property value from the provided list of POJO to the list of new protobuf message.
   * 
   * @param msgClazz the target type of protobuf message
   * @param srcObjList the list of source object
   * @return a new protobuf message object
   * @throws Exception copy exception
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> copy2ProtoBufMessageList(
      Class<? extends com.google.protobuf.GeneratedMessageV3> msgClazz, List<?> srcObjList)
      throws Exception {
    checkArgument(msgClazz != null, "Target protoBuf message cannot be null!");
    checkArgument(srcObjList != null, "Source object cannot be null!");
    List<T> messages = Lists.newArrayList();
    for (Object obj : srcObjList) {
      messages.add((T) copy2ProtoBufMessage(msgClazz, obj));
    }
    return messages;
  }

  /**
   * Copy the property value from the provided list of Protobuf message object to the list of new
   * POJO.
   * 
   * @param srcMessageList source message object list to be copied
   * @param targetPojo target pojo be copy
   * @return the list POJO
   * @throws Exception copy exception
   */
  public static <T> List<T> copy2PojoList(
      List<? extends com.google.protobuf.GeneratedMessageV3> srcMessageList, Class<T> targetPojo)
      throws Exception {
    checkArgument(targetPojo != null, "Target POJO cannot be null!");
    checkArgument(srcMessageList != null, "Source protobuf message cannot be null!");
    List<T> objects = Lists.newArrayList();
    for (com.google.protobuf.GeneratedMessageV3 obj : srcMessageList) {
      objects.add((T) copy2Pojo(obj, targetPojo.newInstance()));
    }
    return objects;
  }

  /**
   * Copy the value of properties from protobuf message object to a POJO bean.
   * 
   * @param messageObj the protobuf message object
   * @param targetObject the POJO
   * @return the pojo bean
   * @throws Exception exception happen during the copy
   */
  @SuppressWarnings("rawtypes")
  public static <T> T copy2Pojo(com.google.protobuf.GeneratedMessageV3 messageObj, T targetObject)
      throws Exception {

    checkArgument(targetObject != null, "Target POJO cannot be null!");
    checkArgument(messageObj != null, "Source protobuf message cannot be null!");

    Map propertiesDesc = PropertyUtils.describe(targetObject);

    Descriptors.Descriptor descriptor = messageObj.getDescriptorForType();

    List<FieldDescriptor> fields = descriptor.getFields();
    Object value;
    Class<?> returnType;
    for (FieldDescriptor fd : fields) {
      value = messageObj.getField(fd);
      if (value == null || Strings.isNullOrEmpty(value + "")) {
        continue;
      }
      returnType = value.getClass();
      if (!isPrimitiveWrapper(returnType)) {
        logger.warn(" Property " + fd.getName() + "  set ko, the type {} is not be supported.",
            returnType);
        continue;
      }
      if (propertiesDesc.containsKey(fd.getName())) {
        logger.debug(" Matched property: " + fd.getName());
        if (CONVERTER.containsKey(returnType)) {
          value = CONVERTER.get(returnType).convert(value);
        }
        PropertyUtils.setSimpleProperty(targetObject, fd.getName(), value);
        logger.debug(" Property " + fd.getName() + "  set ok");
      }
    }

    return targetObject;
  }

  /**
   * Copy the properties of source object to a new protobuf message builder.
   * 
   * @param builder the target message builder
   * @param srcObject the source object
   * @return message builder object
   * @throws Exception copy exception
   */
  @SuppressWarnings("rawtypes")
  public static com.google.protobuf.GeneratedMessageV3.Builder copy2ProtoBufMessageBuilder(
      com.google.protobuf.GeneratedMessageV3.Builder builder, Object srcObject) throws Exception {

    checkArgument(builder != null, "Target protoBuf builder cannot be null!");
    checkArgument(srcObject != null, "Source object cannot be null!");

    Descriptors.Descriptor descriptor = builder.getDescriptorForType();
    BeanInfo srcBeanInfo = Introspector.getBeanInfo(srcObject.getClass());
    List<PropertyDescriptor> srcPd = Arrays.asList(srcBeanInfo.getPropertyDescriptors());

    logger.debug("Target Message Builder:" + builder.getClass().getName());
    logger.debug("Source Object:" + srcObject.getClass().getName());
    Object value;
    Class<?> returnType;
    for (PropertyDescriptor propertyDesc : srcPd) {
      value = PropertyUtils.getProperty(srcObject, propertyDesc.getName());
      Descriptors.FieldDescriptor fd = descriptor.findFieldByName(propertyDesc.getName());
      if (value == null || fd == null) {
        if (fd != null) {
          builder.clearField(fd);
        }
        logger.debug(" Property '{}' with value '{}', found fd : '{}'", propertyDesc.getName(),
            value, fd);
        continue;
      }
      returnType = value.getClass();
      if (!isPrimitiveWrapper(returnType)) {
        logger.warn(
            " property " + propertyDesc.getName() + "  set ko, the type {} is not be supported.",
            returnType);
        continue;
      }

      if (CONVERTER.containsKey(returnType)) {
        value = CONVERTER.get(returnType).convert(value);
      }

      builder.setField(fd, value);
      logger.debug(" property '{}'  set ok, found fd: '{}', set ok.", propertyDesc.getName(), fd);

    }
    return builder;
  }

  /**
   * Convert proto message to compact json string.
   * @throws IOException
   */
  public String toCompactJsonString(Message message) throws IOException {
    return JsonFormat.printer().omittingInsignificantWhitespace().print(message);
  }
  
  /**
   * Copy the value of properties from protobuf message object to HashMap.
   * 
   * @param messageObj the protobuf message object
   * @return the map
   * @throws Exception exception happen during the copy
   */
  public static Map<String, Object> copy2Map(com.google.protobuf.GeneratedMessageV3 messageObj)
      throws Exception {

    checkArgument(messageObj != null, "Source protobuf message cannot be null!");

    Descriptors.Descriptor descriptor = messageObj.getDescriptorForType();

    List<FieldDescriptor> fields = descriptor.getFields();
    Object value;
    Class<?> returnType;
    Map<String, Object> mapValues = new HashMap<String, Object>();
    for (FieldDescriptor fd : fields) {
      value = messageObj.getField(fd);
      returnType = value.getClass();
      if (!isPrimitiveWrapper(returnType)) {
        logger.warn(" Property " + fd.getName() + "  set ko, the type {} is not be supported.",
            returnType);
        continue;
      }

        if (CONVERTER.containsKey(returnType)) {
          value = CONVERTER.get(returnType).convert(value);
        }
        mapValues.put(fd.getName(), value);
        logger.debug(" Property " + fd.getName() + "  set ok");
   
    }

    return mapValues;
  }

  /**
   * main.
   */
  public static void main(String[] args) throws Exception {
    
    Date date = new Date();
    Timestamp ts = toTimestamp(date);
    Date convered = toDate(ts);
    System.out.println(date);
    System.out.println(convered);
  }

  class TestBean {
    private String name;
    private Integer age;
    private Boolean flag;
    private Date date;
    private Double income;
    private BigDecimal outcome;
    private Byte[] file;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }

    public Boolean getFlag() {
      return flag;
    }

    public void setFlag(Boolean flag) {
      this.flag = flag;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public Double getIncome() {
      return income;
    }

    public void setIncome(Double income) {
      this.income = income;
    }

    public BigDecimal getOutcome() {
      return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
      this.outcome = outcome;
    }

    public Byte[] getFile() {
      return file;
    }

    public void setFile(Byte[] file) {
      this.file = file;
    }



  }
}
