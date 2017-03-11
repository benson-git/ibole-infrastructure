package com.github.ibole.infrastructure.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * JSR303 Validator(Hibernate Validator)工具类.
 * ConstraintViolation中包含propertyPath, message 和invalidValue等信息. 提供了各种convert方法，适合不同的i18n需求: 1.
 * List&lt;String&gt;, String内容为message 2. List&lt;String&gt;, String内容为propertyPath + separator + message
 * 3. Map&lt;propertyPath,message&gt;
 * 
 */
public class BeanValidators {

  /**
   * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
   * 
   * @param validator the validator
   * @param object the object to validate
   * @param groups the group or list of groups targeted for validation
   * @exception ConstraintViolationException exception if constraint violations are not empty
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static void validateWithException(Validator validator, Object object, Class<?>... groups)
      throws ConstraintViolationException {
    Set constraintViolations = validator.validate(object, groups);
    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(constraintViolations);
    }
  }

  /**
   * 辅助方法, 转换ConstraintViolationException中的Set &lt;ConstraintViolations&gt;中为List&lt;message&gt;.
   * 
   * @param e the constraint violation exception
   * @return the error message list
   */
  public static List<String> extractMessage(ConstraintViolationException e) {
    return extractMessage(e.getConstraintViolations());
  }

  /**
   * 辅助方法, 转换Set&lt;ConstraintViolation&gt;为List&lt;message&gt;
   * 
   * @param constraintViolations the set of constraint violation
   * @return the error message list
   */
  @SuppressWarnings("rawtypes")
  public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
    List<String> errorMessages = Lists.newArrayList();
    for (ConstraintViolation violation : constraintViolations) {
      errorMessages.add(violation.getMessage());
    }
    return errorMessages;
  }

  /**
   * 辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations&gt;为Map&lt;property,
   * \message&gt;.
   * 
   * @param e the constraint violation exception
   * @return the error message Map
   */
  public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
    return extractPropertyAndMessage(e.getConstraintViolations());
  }

  /**
   * 辅助方法, 转换Set&lt;ConstraintViolation&gt;为Map&lt;property, message&gt;.
   * 
   * @param constraintViolations the set of constraint violation
   * @return the error message Map
   */
  @SuppressWarnings("rawtypes")
  public static Map<String, String> extractPropertyAndMessage(
      Set<? extends ConstraintViolation> constraintViolations) {
    Map<String, String> errorMessages = Maps.newHashMap();
    for (ConstraintViolation violation : constraintViolations) {
      errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return errorMessages;
  }

  /**
   * 辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations&gt;为List&lt;propertyPath
   * message&gt;.
   * 
   * @param e the constraint violation exception
   * @return the error message list
   */
  public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
    return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
  }

  /**
   * 辅助方法, 转换Set&lt;ConstraintViolations&gt;为List&lt;propertyPath message&gt;.
   * 
   * @param constraintViolations the set of constraint violation
   * @return the error message list
   */
  @SuppressWarnings("rawtypes")
  public static List<String> extractPropertyAndMessageAsList(
      Set<? extends ConstraintViolation> constraintViolations) {
    return extractPropertyAndMessageAsList(constraintViolations, " ");
  }

  /**
   * 辅助方法, 转换ConstraintViolationException中的Set&lt;ConstraintViolations&gt;为List&lt;propertyPath
   * +separator+ message&gt;.
   * 
   * @param e the constraint violation exception
   * @param separator the separator to concatenate char
   * @return the error message list
   */
  public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e,
      String separator) {
    return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
  }

  /**
   * 辅助方法, 转换Set&lt;ConstraintViolation&gt;为List&lt;propertyPath +separator+ message&gt;.
   * 
   * @param constraintViolations the set of constraint violation
   * @param separator the separator to concatenate char
   * @return the error message list
   */
  @SuppressWarnings("rawtypes")
  public static List<String> extractPropertyAndMessageAsList(
      Set<? extends ConstraintViolation> constraintViolations, String separator) {
    List<String> errorMessages = Lists.newArrayList();
    for (ConstraintViolation violation : constraintViolations) {
      errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
    }
    return errorMessages;
  }
}
