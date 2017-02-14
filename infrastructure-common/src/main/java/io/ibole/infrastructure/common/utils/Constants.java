package io.ibole.infrastructure.common.utils;

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
  
  public static final String SENDER_JWK_PATH = "/certs/jwt/senderJWK.json";
  
  public static final String RECEIVER_JWK_PATH = "/certs/jwt/receiverJWK.json";

  
  private Constants() {
    throw new IllegalAccessError("Utility class");
  }
}
