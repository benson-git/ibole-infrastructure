package com.github.ibole.infrastructure.web.spring.interceptor;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



/**
 * 认证拦截,支持跨域.
 * 
 *  <mvc:interceptors>
 *        
 *       <mvc:interceptor>
 *          <mvc:mapping path="/**" />
 *          <bean class="com.github.ibole.infrastructure.web.spring.interceptor.AuthTokenInterceptor" />
 *      </mvc:interceptor>
 *      
 *  </mvc:interceptors>
 *
 */
public class AuthTokenInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {

  @Autowired
  private IAuthRemote authRemote;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Method", "GET, POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers",
        "Origin, accept, X-Requested-With, Content-Type, Authorization");

    response.setContentType("text/json;charset=utf-8");
    response.setCharacterEncoding("UTF-8");

    String httpMethod = request.getMethod();
    if (httpMethod.equals("OPTIONS")) {
      return false;
    }


    String act = request.getHeader("act");
    String adminId = request.getHeader("adminId");
    String token = request.getHeader("token");
    String custId = request.getParameter("custId");

    PrintWriter out = null;
    // 过滤大写custId
    if (!StringUtils.isBlank(custId)) {
      for (int i = 0; i < custId.length(); i++) {
        if (Character.isUpperCase(custId.charAt(i)) == true) {
          out = response.getWriter();
          out.append("{\"ret\":\"2\",\"msg\":\"请卸载后重新安装\"}");
          return false;
        }
      }
    }

    // 未登录，通用参数不合法检查
    if (StringUtils.isBlank(token)) {
      out = response.getWriter();
      out.append("{\"ret\":\"-1\",\"msg\":\"未登陆\"}");
      return false;
    }
    if ((StringUtils.isBlank(custId) && StringUtils.isBlank(adminId))) {
      out = response.getWriter();
      out.append("{\"ret\":\"3\",\"msg\":\"请检查认证参数(custId | adminId)\"}");
      return false;
    }

    boolean check = true;
    try {
      if (!StringUtils.isBlank(adminId) && !StringUtils.isBlank(token)) {
        check = authRemote.checkToken(adminId, token);
        check = true;
      } else {
        check = authRemote.checkToken(custId, token);
      }
    } catch (Exception e) {
      // ignore
      // RPC调用过程出错时，忽略token检查。 当rpc无法访问时，可能导致开放权限问题。
    }

    if (!check) {
      out = response.getWriter();
      out.append("{\"ret\":\"4\",\"msg\":\"请重新登录\"}");
      return false;
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {}

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {}
}
