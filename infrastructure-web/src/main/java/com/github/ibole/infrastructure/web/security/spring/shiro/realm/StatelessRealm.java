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

import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.TokenStatus;
import com.github.ibole.infrastructure.web.exception.AuthenticationServiceException;
import com.github.ibole.infrastructure.web.exception.HttpErrorStatus;
import com.github.ibole.infrastructure.web.security.model.UserModel;
import com.github.ibole.infrastructure.web.security.spring.shiro.WsUserService;
import com.github.ibole.infrastructure.web.security.spring.shiro.token.StatelessToken;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
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
 * A Realm implementation that supports this {@link StatelessToken} implementation. This ensures
 * that only this Realm will process these types of authentication attempts.
 * 
 * @author bwang
 *
 */
public class StatelessRealm extends AuthorizingRealm {

  private Logger logger = LoggerFactory.getLogger(StatelessRealm.class);

  @Autowired
  private WsUserService wsService;

  @Autowired
  private TokenAuthenticator tokenMgr;

  public StatelessRealm() {
    // This makes the supports(...) method return true only if the token is an instanceof
    // StatelessToken.
    setAuthenticationTokenClass(StatelessToken.class);
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

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    // Assert the StatelessToken, and if valid, look up the account data and return
    // an AuthenticationInfo instance representing that account.
    StatelessToken statelessToken = (StatelessToken) token;
    HttpErrorStatus errorStatus = null;
    UserModel user = wsService.findWsUserById(statelessToken.getPrincipal().toString());
        
    TokenStatus status =
        tokenMgr.validAccessToken(statelessToken.getToken(), statelessToken.getClientId());
    
    if(user == null) {
      errorStatus = HttpErrorStatus.ACCOUNT_NOT_FOUND;
    }
    
    if(user.isLocked()) {
      errorStatus = HttpErrorStatus.ACCOUNT_LOCKED;
    }
    
    if(status.isExpired()) {
      errorStatus = HttpErrorStatus.ACCOUNT_EXPIRED;
    }
    
    if(status.isInvalid()) {
      errorStatus = HttpErrorStatus.ACCOUNT_INVALID;
    }

    logger.debug("doGetAuthenticationInfo...");
    
    if(errorStatus != null) {
      throw new AuthenticationServiceException(errorStatus);
    }

    return new SimpleAuthenticationInfo(statelessToken.getPrincipal(), statelessToken.getToken(),
        getName() // realm name
    );
  }
}
