/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ibole.infrastructure.web.model;

import com.github.ibole.infrastructure.security.jwt.TokenStatus;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public final class TokenRenewResponse {

  //identify whether the token that user provided is authenticated or not,
  // user need to login if loginRequired is true.
  private boolean loginRequired;
  
  private String accessToken;
  
  private String tokenStatus;

  /**
   * @return the loginRequired
   */
  public boolean isLoginRequired() {
    return loginRequired;
  }

  /**
   * @param loginRequired the loginRequired to set
   */
  public void setLoginRequired(boolean loginRequired) {
    this.loginRequired = loginRequired;
  }

  /**
   * @return the accessToken
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * @param accessToken the accessToken to set
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * @return the tokenStatus
   */
  public String getTokenStatus() {
    return tokenStatus;
  }

  /**
   * @param tokenStatus the tokenStatus to set
   */
  public void setTokenStatus(String tokenStatus) {
    this.tokenStatus = tokenStatus;
  }
  
  public void setTokenStatus(TokenStatus status) {
    this.tokenStatus = status.getCode().value();
  }
}
