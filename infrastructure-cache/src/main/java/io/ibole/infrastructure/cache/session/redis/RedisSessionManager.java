package io.ibole.infrastructure.cache.session.redis;

import io.ibole.infrastructure.cache.redis.RedisSimpleTempalte;
import io.ibole.infrastructure.common.serialization.KryoSerializationUtil;

import com.google.common.base.Strings;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public class RedisSessionManager {
  public static final String SESSION_PREFIX = "SESSION:";
  public static final String SESSION_ID_PREFIX = SESSION_PREFIX + "RJSID_";
  public static final String SESSION_ID_COOKIE = "RSESSIONID";
  public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;

  // Session最大更新间隔时间
  private int expirationUpdateInterval;
  // Session过期时间
  private int sessionTimeOut;

  private RedisSimpleTempalte redisClient;

  public RedisSessionManager() {
    this.expirationUpdateInterval = 600;
    this.sessionTimeOut = 1800;
  }

  public RedisSessionManager(String host, String port, String password) {
    this(host, port, password, 1800);
  }

  /**
   * Constructor.
   */
  public RedisSessionManager(String host, String port, String password, int sessionTimeOut) {
    this.expirationUpdateInterval = 300;
    this.sessionTimeOut =
        sessionTimeOut == 0 ? DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS : sessionTimeOut;

    this.redisClient = new RedisSimpleTempalte(host, Integer.parseInt(port), password);

  }

  public RedisSimpleTempalte getRedisClient() {
    return this.redisClient;
  }

  public void setExpirationUpdateInterval(int expirationUpdateInterval) {
    this.expirationUpdateInterval = expirationUpdateInterval;
  }

  public void setMaxInactiveInterval(int sessionTimeOut) {
    this.sessionTimeOut = sessionTimeOut;
  }

  /**
   * 每次请求取得最新Session.
   * 
   */
  public RedisHttpSession createSession(RedisHttpServletRequestWrapper request,
      HttpServletResponse response, RequestEventSubject requestEventSubject, boolean create) {

    String sessionId = getRequestedSessionId(request);

    RedisHttpSession session = null;
    // 首次登录没有SeeionID，并且不创建新Session则不处理
    if ((Strings.isNullOrEmpty(sessionId)) && (!(create))) {
      return null;
    }
    // 如果SessionID不为空则从Redis加载Session
    if (!Strings.isNullOrEmpty(sessionId)) {
      session = loadSession(sessionId);
    }
    // 如果是首次登录则Session为空,生成空Session
    if ((session == null) && (create)) {
      session = createEmptySession(request, response);
    }
    // 如果Session不为空则，附加各种回调事件
    if (session != null) {
      attachEvent(session, request, response, requestEventSubject);
    }

    return session;
  }

  /**
   * 从Request的Cookies中取出SessionId.
   */
  private String getRequestedSessionId(HttpServletRequestWrapper request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (SESSION_ID_COOKIE.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  /**
   * 保存Session.
   * 
   * @param session RedisHttpSession
   */
  private void saveSession(RedisHttpSession session) {
    String sessionid = generateSessionKey(session.id);
    try {
      // 如果Session过期
      if (session.expired) {
        // 清楚本地缓存数据
        // CacheUtil.removeKey(session.id);
        // 清楚Redis中的数据
        this.getRedisClient().del(sessionid);
      } else {
        // 在本地缓存中保存Session
        // CacheUtil.put(session.id, session);
        // 在远程Redis中保存Session并且重新设置过期时间
        this.getRedisClient().set(sessionid,
            KryoSerializationUtil.getInstance().serialize(session), this.sessionTimeOut);
      }
    } catch (Exception e) {
      throw new SessionException(e);
    }
  }

  /**
   * 增加Session过期和Request请求结束后的回调事件.
   * 
   * @param session RedisHttpSession
   * @param request HttpServletRequestWrapper
   * @param response HttpServletResponse
   * @param requestEventSubject RequestEventSubject
   */
  private void attachEvent(final RedisHttpSession session, final HttpServletRequestWrapper request,
      final HttpServletResponse response, RequestEventSubject requestEventSubject) {
    session.setListener(new RedisSessionListener() {
      public void onInvalidated(RedisHttpSession session) {
        // 设置客户端Cookies过期
        saveCookie(session, request, response);
        // 保存Redis中的Session信息
        saveSession(session);
      }
    });
    requestEventSubject.attach(new RequestEventObserver() {
      public void completed(HttpServletRequest servletRequest, HttpServletResponse response) {
        int updateInterval = (int) ((System.currentTimeMillis() - session.lastAccessedTime) / 1000);
        // 如果Session是初始化的空Session则需要同步到Redis
        // 如果 Session一致 并且在最小间隔同步时间内 则不与Redis同步
        if (session.isNew == false
            && session.isDirty == false && updateInterval < expirationUpdateInterval) {
          return;
        }
        // 如果 Session过期不与Redis同步
        if (session.expired) {
          return;
        }
        session.lastAccessedTime = System.currentTimeMillis();
        saveSession(session);
      }
    });
  }

  /**
   * 初始化空Session.
   * 
   * @param request RedisHttpServletRequestWrapper
   * @param response HttpServletResponse
   * @return RedisHttpSession
   */
  private RedisHttpSession createEmptySession(RedisHttpServletRequestWrapper request,
      HttpServletResponse response) {
    RedisHttpSession session = new RedisHttpSession();
    session.id = createSessionId();
    session.creationTime = System.currentTimeMillis();
    session.maxInactiveInterval = this.sessionTimeOut;
    session.isNew = true;
    saveCookie(session, request, response);
    return session;
  }

  private String createSessionId() {
    return UUID.randomUUID().toString().replace("-", "").toUpperCase();
  }

  private void saveCookie(RedisHttpSession session, HttpServletRequestWrapper request,
      HttpServletResponse response) {
    if (session.isNew == false && session.expired == false) {
      return;
    }
    Cookie cookie = new Cookie(SESSION_ID_COOKIE, null);
    cookie.setPath(request.getContextPath());
    // 如果Session过期则Cookies也过期
    if (session.expired) {
      cookie.setMaxAge(0);
      // 如果Session是新生成的，则需要在客户端设置SessionID
    } else if (session.isNew) {
      cookie.setValue(session.getId());
    }
    response.addCookie(cookie);
  }

  /**
   * 从Redis中重新加载Session.
   * 
   * @param sessionId String
   * @return RedisHttpSession
   */
  private RedisHttpSession loadSession(String sessionId) {
    RedisHttpSession session = null;
    byte[] bytes = getRedisClient().getByte(generateSessionKey(sessionId));
    if (bytes != null) {
      session = KryoSerializationUtil.getInstance().deserialize(bytes);
    }
    // 重新加载到本地缓存的Session需要重新设置同步标志与新建标志
    if (session != null) {
      session.isNew = false;
      session.isDirty = false;
    }
    return session;

  }

  private static String generateSessionKey(String sessionId) {
    return SESSION_ID_PREFIX.concat(sessionId);
  }
}
