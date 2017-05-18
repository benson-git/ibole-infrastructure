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

package com.github.ibole.infrastructure.web.spring.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * 
 * 由于Shiro filterChainDefinitions中roles默认是and.
 *  * = user,roles[system,general]
 * 比如：roles[system,general] ，表示同时需要“system”和“general” 2个角色才通过认证
 * 所以需要自定义 继承 AuthorizationFilter, 实现 or逻辑.
 * @author bwang
 *
 */
public class RolesAuthorizationFilter extends AuthorizationFilter {

  @Override  
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)  
          throws Exception {  
      Subject subject = getSubject(request, response);   
      String[] rolesArray = (String[]) mappedValue;   

      if (rolesArray == null || rolesArray.length == 0) {   
          //no roles specified, so nothing to check - allow access.   
          return true;   
      }   

      for(int i=0;i<rolesArray.length;i++){    
          if(subject.hasRole(rolesArray[i])){    
              return true;    
          }    
      }    
      return false;    
  }  
}
