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

package com.github.ibole.infrastructure.web.security.spring.shiro.realm;

import com.github.ibole.infrastructure.web.security.model.UserModel;
import com.github.ibole.infrastructure.web.security.spring.shiro.WsUserService;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
 * @author bwang
 *
 */
public class FormRealm extends AuthorizingRealm {

  private Logger logger = LoggerFactory.getLogger(FormRealm.class.getName());

  @Autowired
  private WsUserService wsService;

  @Override
  public boolean supports(AuthenticationToken token) {
    return token != null && token instanceof UsernamePasswordToken;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    logger.debug("doGetAuthenticationInfo from DB.");
    UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    UserModel user = wsService.findWsUser(upToken.getUsername(), upToken.getPassword().toString());
    if (user != null) {
      SimpleAccount account = new SimpleAccount(user.getUserId(), null, getName());
      return account;
    }

    return null;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    String userId = String.valueOf(principals.getPrimaryPrincipal());
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.setRoles(wsService.findUserRoles(userId));
    authorizationInfo.setStringPermissions(wsService.findUserPermissions(userId));
    logger.debug("doGetAuthorizationInfo from DB.");
    return authorizationInfo;
  }
}
