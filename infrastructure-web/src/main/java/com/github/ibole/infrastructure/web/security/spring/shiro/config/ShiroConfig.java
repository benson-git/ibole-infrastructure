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

package com.github.ibole.infrastructure.web.security.spring.shiro.config;

import com.github.ibole.infrastructure.security.jwt.JwtProvider;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.spi.cache.redis.RedisSimpleTempalte;
import com.github.ibole.infrastructure.web.security.spring.shiro.WsUserService;
import com.github.ibole.infrastructure.web.security.spring.shiro.filter.RolesAuthorizationFilter;
import com.github.ibole.infrastructure.web.security.spring.shiro.filter.StatelessAuthFilter;
import com.github.ibole.infrastructure.web.security.spring.shiro.realm.FormRealm;
import com.github.ibole.infrastructure.web.security.spring.shiro.realm.StatelessRealm;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
@Configuration
public class ShiroConfig {
  //角色or逻辑校验器
  private final static String ANY_ROLES = "anyRoles";
  //Restful校验器
  private final static String REST = "rest";
  //客户端无状态校验器
  private final static String STATELESS_AUTHC = "statelessAuthc";

  /*
   * @Bean public EhCacheManager getEhCacheManager() { EhCacheManager em = new EhCacheManager();
   * em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml"); return em; }
   */
  
  @Bean
  public RedisSimpleTempalte getRedisSimpleTempalte() {
    return new RedisSimpleTempalte("localhost", 6379, "ibole2017");
  }
  
  @Bean
  public TokenAuthenticator geTokenAuthenticator(RedisSimpleTempalte redisTemplate){
    return JwtProvider.provider().createTokenGenerator(redisTemplate);
  }
  
  @Bean
  public WsUserService getWsService(){
    return new WsUserService();
  }
  
  @Bean
  public StatelessAuthFilter getStatelessAuthFilter(){
    return new StatelessAuthFilter();
  }

  @Bean
  public HttpMethodPermissionFilter getHttpMethodPermissionFilter() {
    return new HttpMethodPermissionFilter();
  }

  @Bean
  public RolesAuthorizationFilter getRolesAuthorizationFilter() {
    return new RolesAuthorizationFilter();
  }

  @Bean
  public Realm getStatelessRealm() {  
    StatelessRealm statelessRealm = new StatelessRealm();
    statelessRealm.setCachingEnabled(false);
    statelessRealm.setCachingEnabled(false);
    // realm.setCacheManager(getEhCacheManager());
    return statelessRealm;
  }
  
  @Bean
  public Realm getFormRealm() {
    FormRealm formRealm = new FormRealm();
    formRealm.setCachingEnabled(false);
    formRealm.setCachingEnabled(false);
    return formRealm;
  }
  
  @Bean 
  public DefaultWebSubjectFactory getSubjectFactory(){
    DefaultWebSubjectFactory subjectFactory = new StatelessSubjectFactory();
    return subjectFactory;
  }

  @Bean(name = "lifecycleBeanPostProcessor")
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
    daap.setProxyTargetClass(true);
    return daap;
  }

  @Bean
  public DefaultWebSecurityManager setWebSecurityManager(DefaultWebSubjectFactory subjectFactory,
      SessionManager sessionManager) {
    Collection<Realm> realms = Lists.newArrayList();
    realms.add(getFormRealm());
    realms.add(getStatelessRealm());
    DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
    DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
    sessionStorageEvaluator.setSessionStorageEnabled(false);
    subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
    DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
    dwsm.setRealms(realms);
    dwsm.setSubjectFactory(subjectFactory);
    dwsm.setSubjectDAO(subjectDAO);
    //dwsm.setCacheManager(getEhCacheManager());
    dwsm.setSessionManager(sessionManager);
    return dwsm;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
      DefaultWebSecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
    aasa.setSecurityManager(securityManager);
    return aasa;
  }

  /**
   * 加载shiroFilter权限控制规则(从数据库读取然后配置).
   * 这些过滤器分为两组，一组是认证过滤器，一组是授权过滤器。
   * 其中anon，authcBasic，auchc，user是第一组，perms，roles，ssl，rest，port是第二组
   * 
   */
  private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
    filterChainDefinitionMap.put("/documentation/**", "anon");
    filterChainDefinitionMap.put("/v2/api-docs", "anon");
    filterChainDefinitionMap.put("/configuration/ui", "anon");
    filterChainDefinitionMap.put("/configuration/security", "anon");
    filterChainDefinitionMap.put("/swagger-resources/**", "anon");
    filterChainDefinitionMap.put("/swagger-ui.html", "anon");
    filterChainDefinitionMap.put("/webjars/**", "anon");
    // filterChainDefinitionMap.put("/users/**", "user, anyRoles[system,general]");
    // filterChainDefinitionMap.put("/users/**", "user," + ANY_ROLES + "[system,general]");
    // 例子/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user:method] ,
    // 其中method为post，get，delete等。
    filterChainDefinitionMap.put("/api/v1/users/**", STATELESS_AUTHC + "," + REST + "[user]");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
  }

  private void loadShiroFilters(ShiroFilterFactoryBean shiroFilterFactoryBean) {
    Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
    filters.put(ANY_ROLES, getRolesAuthorizationFilter());
    filters.put(REST, getHttpMethodPermissionFilter());
    filters.put(STATELESS_AUTHC, getStatelessAuthFilter());
    shiroFilterFactoryBean.setFilters(filters);
  }

  /**
   * 配置在web.xml的DelegatingFilterProxy表示这是一个代理filter，它会将实际的工作，
   * 交给spring配置文件中 id="shiroFilter"的bean来处理.
   * 
   */
  @Bean(name = "shiroFilter")
  public ShiroFilterFactoryBean setShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    // 必须设置 SecurityManager
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    //Returns the URL to which users should be redirected if they are denied access to an underlying path or resource
    shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
    // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
    shiroFilterFactoryBean.setLoginUrl("/api/v1/auth/authenticate");
    // shiroFilterFactoryBean.setUnauthorizedUrl("/403");

    loadShiroFilters(shiroFilterFactoryBean);
    loadShiroFilterChain(shiroFilterFactoryBean);
    return shiroFilterFactoryBean;
  }

  @Bean
  public SessionDAO getMemorySessionDAO() {
    return new MemorySessionDAO();
  }

  @Bean
  public MethodInvokingFactoryBean setSecurityManager(DefaultWebSecurityManager securityManager) {
    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
    methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
    methodInvokingFactoryBean.setArguments(ArrayUtils.toArray(securityManager));
    return methodInvokingFactoryBean;
  }


  @Bean
  public SessionManager setSessionMangaer(SessionDAO sessionDAO) {
   // DefaultSessionManager sessionManager = new DefaultWebSessionManager();
   // sessionManager.setSessionDAO(sessionDAO);
    DefaultSessionManager sessionManager = new DefaultWebSessionManager();
    sessionManager.setSessionValidationSchedulerEnabled(false);
    return sessionManager;
  }


}
