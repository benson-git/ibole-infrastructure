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

package com.github.ibole.infrastructure.web.spring.shiro;

import org.apache.shiro.authc.AuthenticationToken;

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
 * Perform bearer token authentication In Shiro.
 * @author bwang
 *
 */
public class StatelessToken implements AuthenticationToken {

  private static final long serialVersionUID = -4328416906451280823L;
  //user id, user's principal in Shiro.
  private Object principal;
  //access token, user's credential in Shiro.
  private String credentials;
  
  private String clientId;

  public StatelessToken(String token, String userId, String clientId) {
    this.principal = userId;
    this.credentials = token;
    this.clientId = clientId;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  public void setCredentials(String credentials) {
    this.credentials = credentials;
  }

  //Principals are attributes that identify a Subject, 
  //such as a first name, a username, Social Security Number, etc.
  @Override
  public Object getPrincipal() {
    return principal;
  }
  
  //Credentials are secret values which normally are only known by the Subject it-self 
  //and which are used to verify that the Subject is the actual real owner of that identity.
  //Credentials can have multiple forms such as passwords, fingerprints or certificates.
  @Override
  public Object getCredentials() {
    return credentials;
  }

  public String getToken () {
    return String.valueOf(this.credentials);
  }
  
  /**
   * @return the clientId
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * @param clientId the clientId to set
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public String toString() {
    return "StatelessToken{" + "principal=" + principal + ", clientId=" + clientId +", credentials='"
        + credentials + '\'' + '}';
  }
}
