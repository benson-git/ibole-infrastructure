package io.ibole.infrastructure.security.jwt.jjwt;
//package org.toprank.infrastructure.security.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.impl.crypto.MacProvider;
//
//import java.security.Key;
//import java.util.Date;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;
//
///*********************************************************************************************
// * .
// * 
// * 
// * <p>
// * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
// * 
// * <p>
// * </p>
// *********************************************************************************************/
//
//
//public class JwtHelper {
//
//  // Sample method to construct a JWT
//  private static String createJWT(String id, String issuer, String subject, long ttlMillis) {
//
//    // The JWT signature algorithm we will be using to sign the token
//    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//    long nowMillis = System.currentTimeMillis();
//    Date now = new Date(nowMillis);
//
//    // We need a signing key, so we'll create one just for this example. Usually
//    // the key would be read from your application configuration instead.
//    // Key key = MacProvider.generateKey();
//
//    // We will sign our JWT with our ApiKey secret
//    // We need a signing key, so we'll create one just for this example. Usually
//   // the key would be read from your application configuration instead.
//
//
//    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key.);
//    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//    // Let's set the JWT Claims
//    JwtBuilder builder =
//        Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
//            .signWith(signatureAlgorithm, signingKey);
//
//    // if it has been specified, let's add the expiration
//    if (ttlMillis >= 0) {
//      long expMillis = nowMillis + ttlMillis;
//      Date exp = new Date(expMillis);
//      builder.setExpiration(exp);
//    }
//
//    // Builds the JWT and serializes it to a compact, URL-safe string
//    return builder.compact();
//  }
//
//  // Sample method to validate and read the JWT
//  private static void parseJWT(String jwt) {
//    // This line will throw an exception if it is not a signed JWS (as expected)
//    Claims claims =
//        Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
//            .parseClaimsJws(jwt).getBody();
//    System.out.println("ID: " + claims.getId());
//    System.out.println("Subject: " + claims.getSubject());
//    System.out.println("Issuer: " + claims.getIssuer());
//    System.out.println("Expiration: " + claims.getExpiration());
//  }
//
//  public static Claims parseJWT(String jsonWebToken, String base64Security) {
//    try {
//      Claims claims =
//          Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
//              .parseClaimsJws(jsonWebToken).getBody();
//      return claims;
//    } catch (Exception ex) {
//      return null;
//    }
//  }
//
//  public static String createJWT(String name, String userId, String role, String audience,
//      String issuer, long TTLMillis, String base64Security) {
//    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//    long nowMillis = System.currentTimeMillis();
//    Date now = new Date(nowMillis);
//
//    // 生成签名密钥
//    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
//    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//    // 添加构成JWT的参数
//    JwtBuilder builder =
//        Jwts.builder().setHeaderParam("typ", "JWT").claim("role", role).claim("unique_name", name)
//            .claim("userid", userId).setIssuer(issuer).setAudience(audience)
//            .signWith(signatureAlgorithm, signingKey);
//    // 添加Token过期时间
//    if (TTLMillis >= 0) {
//      long expMillis = nowMillis + TTLMillis;
//      Date exp = new Date(expMillis);
//      builder.setExpiration(exp).setNotBefore(now);
//    }
//
//    // 生成JWT
//    return builder.compact();
//  }
//}
