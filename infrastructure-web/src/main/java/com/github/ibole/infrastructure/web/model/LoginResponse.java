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



/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * Carry the token information of login result.
 * 
 * @author bwang
 *
 */
public class LoginResponse {
  
  //login successfully: true, otherwise is false
  private boolean authenticated;
  private String refreshToken;
  private String accessToken;
  //token status or errorMessage
  private String errorMessage;
  
  /**
   * @return the authenticated
   */
  public boolean isAuthenticated() {
    return authenticated;
  }
  /**
   * @param authenticated the authenticated to set
   */
  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }
  /**
   * @return the refreshToken
   */
  public String getRefreshToken() {
    return refreshToken;
  }
  /**
   * @param refreshToken the refreshToken to set
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
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
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }
  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String description) {
    this.errorMessage = description;
  }
  
}
