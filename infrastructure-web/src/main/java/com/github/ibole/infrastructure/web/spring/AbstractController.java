package com.github.ibole.infrastructure.web.spring;

import com.github.ibole.infrastructure.common.dto.PaginationData;
import com.github.ibole.infrastructure.common.utils.BeanValidators;
import com.github.ibole.infrastructure.common.utils.DateUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

/**
 * The basic controller of Spring MVC/Web.
 */
public abstract class AbstractController {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 验证Bean实例对象.
   * <pre>
   * 配置 JSR303 Bean Validator定义:  <code>&lt;bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" /&gt;</code>
   * </pre>
   */
  @Autowired
  protected Validator validator;

  /**
   * Method which will help to retrieving values from Session based on the key being passed to the
   * method.
   * @param <T> T
   * @param key String
   * @param request HttpServletRequest
   * @return value stored in session corresponding to the key
   */
  protected <T> T getSessionAttribute(final String key, HttpServletRequest request) {
    return null;

  }

  protected void setSessionAttribute(final String key, final Object value,
      HttpServletRequest request) {

  }


  protected void removeAttribute(final String key, HttpServletRequest request) {

  }



  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Model model, Exception ex, HttpServletRequest request) {


    return null;

  }

  protected PaginationData createPaginaionData(final int pageNumber, final int pageSize) {
    final PaginationData paginaionData = new PaginationData(pageSize, pageNumber);

    return paginaionData;
  }

  protected PaginationData calculatePaginaionData(final PaginationData paginationData,
      final int pageSize, final int resultCount) {

    int currentPage = paginationData.getCurrentPage();


    int count = Math.min((currentPage * pageSize), resultCount);
    paginationData.setCountByPage(count);

    paginationData.setTotalCount(resultCount);
    return paginationData;
  }

  /**
   * 服务端参数有效性验证
   * 
   * @param object 验证的实体对象
   * @param groups 验证组
   * @param model Model
   * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
   */
  protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
    try {
      BeanValidators.validateWithException(validator, object, groups);
    } catch (ConstraintViolationException ex) {
      List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
      list.add(0, "数据验证失败：");
      addMessage(model, list.toArray(new String[] {}));
      return false;
    }
    return true;
  }

  /**
   * 服务端参数有效性验证
   * 
   * @param object 验证的实体对象
   * @param groups 验证组
   * @param redirectAttributes RedirectAttributes
   * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
   */
  protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object,
      Class<?>... groups) {
    try {
      BeanValidators.validateWithException(validator, object, groups);
    } catch (ConstraintViolationException ex) {
      List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
      list.add(0, "数据验证失败：");
      addMessage(redirectAttributes, list.toArray(new String[] {}));
      return false;
    }
    return true;
  }

  /**
   * 服务端参数有效性验证
   * 
   * @param object 验证的实体对象
   * @param groups 验证组，不传入此参数时，同@Valid注解验证
   */
  protected void beanValidator(Object object, Class<?>... groups) {
    BeanValidators.validateWithException(validator, object, groups);
  }

  /**
   * 添加Model消息
   * @param model Model
   * @param messages String Array
   */
  protected void addMessage(Model model, String... messages) {
    StringBuilder sb = new StringBuilder();
    for (String message : messages) {
      sb.append(message).append(messages.length > 1 ? "<br/>" : "");
    }
    model.addAttribute("message", sb.toString());
  }

  /**
   * 添加Flash消息
   * @param redirectAttributes RedirectAttributes
   * @param messages String
   */
  protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
    StringBuilder sb = new StringBuilder();
    for (String message : messages) {
      sb.append(message).append(messages.length > 1 ? "<br/>" : "");
    }
    redirectAttributes.addFlashAttribute("message", sb.toString());
  }

  /**
   * 客户端返回JSON字符串.
   * 
   * @param response HttpServletResponse
   * @param object Object
   * @return rendered string
   */
  protected String renderString(HttpServletResponse response, Object object) {
    return renderString(response, JsonMapper.toJsonString(object), "application/json");
  }

  /**
   * 客户端返回字符串.
   * 
   * @param response HttpServletResponse
   * @param string String
   * @param type String
   * @return rendered string
   */
  protected String renderString(HttpServletResponse response, String string, String type) {
    try {
      response.reset();
      response.setContentType(type);
      response.setCharacterEncoding("utf-8");
      response.getWriter().print(string);
      return null;
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * 参数绑定异常
   * @return error/404
   */
  @ExceptionHandler({BindException.class, ConstraintViolationException.class,
      ValidationException.class})
  public String bindException() {
    return "error/400";
  }

  /**
   * 授权登录异常
   * @return error/403
   */
  @ExceptionHandler({AuthenticationException.class})
  public String authenticationException() {
    return "error/403";
  }

  /**
   * 初始化数据绑定.
   *  1. 将所有传递进来的String进行HTML编码，防止XSS攻击 
   *  2. 将字段中Date类型转换为String类型
   *  @param binder WebDataBinder
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
    binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
      }

      @Override
      public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
      }
    });
    // Date 类型转换
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(DateUtils.parseDate(text));
      }
      // @Override
      // public String getAsText() {
      // Object value = getValue();
      // return value != null ? DateUtils.formatDateTime((Date)value) : "";
      // }
    });
  }

}
