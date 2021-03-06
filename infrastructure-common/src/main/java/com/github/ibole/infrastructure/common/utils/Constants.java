package com.github.ibole.infrastructure.common.utils;

import java.util.regex.Pattern;

/**
 * Global common constants.
 * @author bwang
 *
 */
public final class Constants {

  public static final String COMMA_SEPARATOR = ",";

  public static final Pattern COMMA_SEPERATOR_PATTERN = Pattern.compile("\\s*[,]+\\s*");
  
  public static final String APPLICATION_NAME = "toprank.application.name";
  
  public static final String PROPERTY_HTTP_SERVER_DOCS = "http.server.docs";
  
  public static final String CACHE_REDIS_SERVER = "redis.host";

  public static final String CACHE_REDIS_PORT = "redis.port";
  
  public static final String CACHE_REDIS_PASSWORD = "redis.password";
  
  public static final String SMS_RANDCODE_REGISTER_PREFIX = "sms.randcode.register.prefix";
  
  public static final String SMS_RANDCODE_VALIDATE_PREFIX = "sms.randcode.validate.prefix"; 
  
  public static final String SMS_SENDER_URL = "sms.sender.url"; 
  //client and user identifier: loginId and clientId
  public static final String USER_PRINCINPAL = "user.principal";
  
  public static final String MINI_DEVICE_INFO = "mini.device.info";

  public static final String AUTH_ID = "loginId";
  
  public static final String CLIENT_ID = "ClientId";
  
  public static final String ANONYMOUS_ID = "Anonymous";
  
  public static final String REFRESH_TOKEN_KEY_PREFIX = "auth.refresh.token.";
  
  public static final String ACCESS_TOKEN_KEY_PREFIX = "auth.access.token.";
  
  public static final String REFRESH_TOKEN = "RefreshToken";
  
  public static final String ACCESS_TOKEN = "AccessToken";
  
  public static final String REFRESH_TOKEN_TTL = "auth.refreshToken.ttl";
  
  public static final String ACCESS_TOKEN_TTL = "auth.accessToken.ttl";
  
  public static final String SENDER_JWK_PATH = "/META-INF/cert/jwt/senderKey.json";
  
  public static final String RECEIVER_JWK_PATH = "/META-INF/cert/jwt/receiverKey.json";

  public static final String SYSTEM_ENCODING = "utf-8";
  //客户端生成的消息摘要
  public static final String STATELESS_PARAM_DIGEST = "stateless.param.digest";
  //
  public static final String STATELESS_PARAM_USERNAME = "stateless.param.userName";
  public static final String WS_PARAM_PASSWORD = "ws.param.password";
  //Http Authorization header: Authorization: <type> <credentials>
  //<type> - Allows to distinguish between different auth mechanisms that the backend may support, 
  //by just parsing the Authorization header.
  //E.g. Baisc for username/password pair; Bearer for OAuth 2.0 and JWt.
  //"Authorization: Bearer <token>" proposed format, refer to https://jwt.io/introduction/
  public static final String HEADER_AUTH_NAME = "Authorization";
  public static final String HEADER_AUTH_PREFIX = "Bearer ";

  
  private Constants() {
    throw new IllegalAccessError("Utility class");
  }
}
