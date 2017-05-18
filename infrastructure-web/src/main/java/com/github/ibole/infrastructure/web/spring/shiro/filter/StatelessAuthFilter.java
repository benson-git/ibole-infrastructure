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

package com.github.ibole.infrastructure.web.spring.shiro.filter;

import com.github.ibole.infrastructure.common.utils.Constants;
import com.github.ibole.infrastructure.common.utils.JsonUtils;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.infrastructure.security.jwt.jose4j.JoseUtils;
import com.github.ibole.infrastructure.web.exception.AuthenticationServiceException;
import com.github.ibole.infrastructure.web.exception.HttpErrorStatus;
import com.github.ibole.infrastructure.web.spring.WsWebUtil;
import com.github.ibole.infrastructure.web.spring.shiro.StatelessToken;

import com.google.common.base.Strings;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
 * <pre>
 * 
 *  When you call Subject.login(AuthenticationToken), the token makes its 
 *  way to the SecurityManager. The SecurityManager calls a wrapped  delegate Authenticator.
 *  This Authenticator uses an AuthenticationStrategy to interact with one or more Realms to
 *  support PAM (Pluggable Authentication Module)-like behavior. 
 *  
 *  Typically each realm is consulted by the Strategy to see if it 
 *  supports the submitted token, and if so, asks the realm to 
 *  'getAuthenticationInfo'.
 *  
 *  High level picture: 
 *  AuthenticationToken -- (submitted to) --> Subject.login --> 
 *   SecurityManager --> Authenticator --> AuthenticationStrategy --> (1 or more Realms).
 * </pre>
 * 
 * @author bwang
 *
 */
public class StatelessAuthFilter extends AuthenticatingFilter {

  private Logger logger = LoggerFactory.getLogger(StatelessAuthFilter.class.getName());
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";

  @Autowired
  private TokenAuthenticator tokenMgr;

  public StatelessAuthFilter() {
    setLoginUrl(DEFAULT_LOGIN_URL);
  }

  @Override
  public void setLoginUrl(String loginUrl) {
    String previous = getLoginUrl();
    if (previous != null) {
      this.appliedPaths.remove(previous);
    }
    super.setLoginUrl(loginUrl);
    this.appliedPaths.put(getLoginUrl(), null);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {

    JwtObject jwtObject = JwtObject.getEmpty();
    boolean loggedIn = false;
    try {

      if (isLoginRequest(request, response)) {
        loggedIn = executeLogin(request, response);
      }

      if (reequestWithToken(request, response)) {
        String jwtToken = WsWebUtil.getTokenFromHeader(WebUtils.toHttp(request));
        if (Strings.isNullOrEmpty(jwtToken)) {
          loggedIn = false;
        } else {
          jwtObject = JoseUtils.claimsOfTokenWithoutValidation(jwtToken);
          StatelessToken token =
              new StatelessToken(jwtToken, jwtObject.getLoginId(), jwtObject.getClientId());
          // Delegate to Realm {@link StatelessRealm} to authenticate the request
          Subject subject = getSubject(request, response);
          subject.login(token);
          loggedIn = true;
          logger.debug("Authenticated successfully for '{}' from '{}'!", jwtObject.getLoginId(),
              jwtObject.getClientId());
        }
      }

    } catch (AuthenticationServiceException ex) {
      loggedIn = false;
      logger.error("Authenticated failed for '{}' from '{}' - ", jwtObject.getLoginId(),
          jwtObject.getClientId(), ex.getMessage());
      onLoginFail(response, HttpStatus.UNAUTHORIZED, ex.getErrorStatus());
    } catch (Exception ex) {
      loggedIn = false;
      logger.error("Authenticated failed for '{}' from '{}' - ", jwtObject.getLoginId(),
          jwtObject.getClientId(), ex.getMessage());
    }
    
    if(!loggedIn) {
      onLoginFail(response, HttpStatus.UNAUTHORIZED, HttpErrorStatus.ACCOUNT_INVALID);
    }
    
    return loggedIn;
  }

  @Override
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
      throws IOException {

    if (isLoginRequest(request, response)) {
      String json = IOUtils.toString(request.getInputStream(), Constants.SYSTEM_ENCODING);
      if (json != null && !json.isEmpty()) {

        Map<String, String> loginInfo = JsonUtils.fromJson(json);
        String username = loginInfo.get(USERNAME);
        String password = loginInfo.get(PASSWORD);
        return new UsernamePasswordToken(username, password);
      }
    }

    if (reequestWithToken(request, response)) {
      String jwtToken = WsWebUtil.getTokenFromHeader(WebUtils.toHttp(request));
      if (jwtToken != null) {
        return createStatelessToken(jwtToken);
      }
    }

    return new UsernamePasswordToken();
  }

  @Override
  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
      ServletRequest request, ServletResponse response) {

    try {
      WsWebUtil.customServletReponse(response, HttpStatus.UNAUTHORIZED,
          HttpErrorStatus.ACCOUNT_INVALID);
    } catch (IOException e1) {
      return false;
    }
    logger.warn("Authenticated failed for '{}'. ", token.getPrincipal());
    return false;
  }

  protected boolean reequestWithToken(ServletRequest request, ServletResponse response) {
    String authzHeader = getAuthzHeader(request);
    return authzHeader != null;
  }

  public StatelessToken createStatelessToken(String token) {
    try {
      JwtObject jwtObj = tokenMgr.parseTokenWithoutValidation(token);
      StatelessToken statelessToken =
          new StatelessToken(token, jwtObj.getLoginId(), jwtObj.getClientId());
      return statelessToken;
    } catch (TokenHandlingException ex) {
      throw new AuthenticationException(ex);
    }
  }

  protected String getAuthzHeader(ServletRequest request) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    return httpRequest.getHeader(Constants.HEADER_AUTH_NAME);
  }

  private void onLoginFail(ServletResponse response, HttpStatus httpStatus,
      HttpErrorStatus customErrorStatus) throws IOException {
    WsWebUtil.customServletReponse(response, httpStatus, customErrorStatus);
  }

}
