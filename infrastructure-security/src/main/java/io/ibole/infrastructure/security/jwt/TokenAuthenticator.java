package io.ibole.infrastructure.security.jwt;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/

/**
 * 创建Token处理接口.
 * 管理短有效期的access token和长生命期的refresh token.
 *  Refresh token并不能用于请求api.它是用来在access token过期后刷新access token的一个标记.
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public interface TokenAuthenticator<S,R> {

  /**
   * Create new access token base on the claim object.
   * @param claim JwtObject
   * @param refreshToken boolean
   * @param sender 
   * @param receiver
   * @return generated token
   * @throws TokenParseException 
   */
  String createAccessToken(JwtObject claim, S sender, R receiver) throws TokenParseException;
  
  /**
   * Create new refresh token base on the claim object.
   * @param claim JwtObject
   * @param refreshToken boolean
   * @param sender 
   * @param receiver
   * @return generated token
   * @throws TokenParseException 
   */
  String createRefreshToken(JwtObject claim, S sender, R receiver) throws TokenParseException;
  /**
   * Renew token base on the old token.
   * @param token the old token to be renewed
   * @param ttlSeconds the time to live 
   * @param refreshToken if it is a refresh token
   * @param sender
   * @param receiver
   * @return the new token
   * @throws TokenParseException
   */
  String renewToken(String token, int ttlSeconds, boolean refreshToken, S sender, R receiver) throws TokenParseException;

  /**
   * 验证Access Token.
   */
  TokenStatus validAccessToken(String token, String clientId, String loginId, S sender, R receiver);
  /**
   * 验证Refresh Token.
   */
  boolean validRefreshToken(String token, String clientId, String loginId, S sender, R receiver);
}
