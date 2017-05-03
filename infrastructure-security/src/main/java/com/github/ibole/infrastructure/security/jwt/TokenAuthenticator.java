package com.github.ibole.infrastructure.security.jwt;

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
 * 创建Token处理接口. 管理短有效期的access token和长生命期的refresh token. Refresh token并不能用于请求api.它是用来在access
 * token过期后刷新access token的一个标记.
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public interface TokenAuthenticator {

  /**
   * Create new access token base on the claim object.
   * 
   * @param claim JwtObject
   * @return generated token
   * @throws TokenHandlingException
   */
  String createAccessToken(JwtObject claim) throws TokenHandlingException;

  /**
   * Create new refresh token base on the claim object.
   * 
   * @param claim JwtObject
   * @return generated token
   * @throws TokenHandlingException
   */
  String createRefreshToken(JwtObject claim) throws TokenHandlingException;

  /**
   * Revoke a refresh token base on the claim object.
   */
  void revokeRefreshToken(String clientId, String loginId);

  /**
   * Renew token base on the old token.
   * 
   * @param token the old token to be renewed
   * @param ttlSeconds the time to live
   * @param refreshToken if it is a refresh token
   * @return the new token
   * @throws TokenHandlingException
   */
  String renewAccessToken(String token, int ttlSeconds, boolean refreshToken)
      throws TokenHandlingException;

  /**
   * 验证Access Token.
   */
  TokenStatus validAccessToken(String token, String clientId, String loginId);

  /**
   * 验证Refresh Token.
   */
  boolean validRefreshToken(String token, String clientId, String loginId);
}
