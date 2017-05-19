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

package com.github.ibole.infrastructure.web.security.spring.shiro;

import com.github.ibole.infrastructure.web.security.model.UserModel;

import com.google.common.base.Strings;

import java.util.HashSet;
import java.util.Set;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * TODO: Cache impl.
 * 
 * @author bwang
 *
 */
public class WsUserService {

  /**
   * @param employeeid
   * @return
   */
  public Set<String> findUserRoles(String loginId) {
    Set<String> roles = new HashSet<String>();
    roles.add("admin");
    roles.add("user");
    return roles;
  }

  /**
   * @param employeeid
   * @return
   */
  public Set<String> findUserPermissions(String loginId) {
    Set<String> permissions = new HashSet<String>();
    permissions.add("user:read");
    permissions.add("user:create");
    permissions.add("user:update");
    permissions.add("user:delete");
    return permissions;
  }

  /**
   * @param userId String
   * @return the instance of UserModel 
   */
  public UserModel findWsUserById(String userId) {
    if (Strings.isNullOrEmpty(userId)){
      return null;
    }
    UserModel user = new UserModel();
    user.setUserId(userId);
    user.setUsername("test");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    return user;
  }

  /**
   * Find user by username and password, used for login.
   * @param username String
   * @param password String
   * @return UserModel the instance of UserModel if found
   */
  public UserModel findWsUser(String username, String password) {
    if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)){
      return null;
    }
    UserModel user = new UserModel();
    user.setUserId("test");
    user.setUsername(username);
    user.setFirstName("firstName");
    user.setLastName("lastName");
    return user;
  }
}
