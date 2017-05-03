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

package com.github.ibole.infrastructure.security.jwt.auth0;

import static com.google.common.base.Preconditions.checkArgument;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ibole.infrastructure.security.jwt.JWTEncryptionPreferences;
import com.github.ibole.infrastructure.security.jwt.JwtConstant;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.infrastructure.security.jwt.TokenStatus;

import com.google.common.base.Stopwatch;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * @author bwang
 *
 */
public class Auth0Utils {

  private Auth0Utils() {
    // nothing to do
  }


  public static void main(String[] args) throws Exception {
    
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
    keyPairGenerator.initialize(256);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
    ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
    
    JwtObject jwt = new JwtObject();
    jwt.setAudience("audience");
    jwt.setClientId("clientId");
    jwt.setIssuer("issuer");
    jwt.setLoginId("loginId");
    jwt.setSubject("subject");
    jwt.setTtlSeconds(5);
    jwt.getRoles().add("admin");
    
    final Stopwatch stopwatch = Stopwatch.createStarted();
    
    String token = createJwtWithECKey(jwt, ecPublicKey, ecPrivateKey);

    //Thread.currentThread().sleep(5000);
    
    TokenStatus status = validateToken(token, jwt.getClientId(), ecPublicKey, ecPrivateKey);
    
    //JwtObject newjwt = claimsOfTokenWithoutValidation(token, ecPublicKey, ecPrivateKey);

    
    String elapsedString = Long.toString(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println("Spent: "+elapsedString);
    System.out.println(token);
    System.out.println(status);
    
  }

  public static String createJwtWithECKey(JwtObject claimObj, String secret)
      throws TokenHandlingException {
    checkArgument(claimObj != null, "JwtObject cannot be null!");
    String[] roleArray = new String[claimObj.getRoles().size()];
    claimObj.getRoles().toArray(roleArray);
    String token = null;
    try {
      token =
          JWT.create().withSubject(claimObj.getSubject()).withAudience(claimObj.getAudience())
              .withIssuer(claimObj.getIssuer())
              .withArrayClaim(JwtConstant.ROLE_ID, roleArray)
              .withClaim(JwtConstant.CLIENT_ID, claimObj.getClientId())
              .withClaim(JwtConstant.LOGIN_ID, claimObj.getLoginId())
              .withExpiresAt(DateTime.now().plusSeconds(claimObj.getTtlSeconds()).toDate())
              .sign(Algorithm.HMAC256(secret));
    } catch (IllegalArgumentException | JWTCreationException | UnsupportedEncodingException ex) {
      throw new TokenHandlingException(ex);
    }
    return token;
  }
  
  public static String createJwtWithECKey(JwtObject claimObj, ECPublicKey publicKey, ECPrivateKey privateKey)
      throws TokenHandlingException {
    checkArgument(claimObj != null, "JwtObject cannot be null!");
    String[] roleArray = new String[claimObj.getRoles().size()];
    claimObj.getRoles().toArray(roleArray);
    String token = null;
    try {
      token =
          JWT.create().withSubject(claimObj.getSubject()).withAudience(claimObj.getAudience())
              .withIssuer(claimObj.getIssuer())
              .withArrayClaim(JwtConstant.ROLE_ID, roleArray)
              .withClaim(JwtConstant.CLIENT_ID, claimObj.getClientId())
              .withClaim(JwtConstant.LOGIN_ID, claimObj.getLoginId())
              .withExpiresAt(DateTime.now().plusSeconds(claimObj.getTtlSeconds()).toDate())
              .sign(Algorithm.ECDSA256(publicKey, privateKey));
    } catch (IllegalArgumentException | JWTCreationException ex) {
      throw new TokenHandlingException(ex);
    }
    return token;
  }
  
  /**
   * Validate Token based on the provided token.
   * <pre>
   * 1. validate the token signature
   * 2. check if the token is for the same client identifier
   * 3. check if the token is expired
   * @param token String
   * @param clientId client id
   * @param publicKey the public key
   * @param privateKey the private key
   * @return the token status
   * @throws TokenHandlingException
   */
  public static TokenStatus validateToken(String token, String clientId, ECPublicKey publicKey, ECPrivateKey privateKey) {
    TokenStatus status = TokenStatus.VALIDATED;;
    try {
      JWTVerifier verifier =
          JWT.require(Algorithm.ECDSA256(publicKey, privateKey)).withClaim(JwtConstant.CLIENT_ID, clientId)
              //.acceptLeeway(1) // second
              .build();

      DecodedJWT jwt = verifier.verify(token);

    } catch (JWTDecodeException | IllegalArgumentException e) {
      status = TokenStatus.INVALID;
    } catch (TokenExpiredException ex) {
      status = TokenStatus.ACCESS_TOKEN_EXPIRED;
    }
    return status;
  }
  
  /**
   * Restore the JwtObject from the provided token.
   * @param token String
   * @param publicKey the public key
   * @param privateKey the private key
   * @return the JwtObject
   * @throws TokenHandlingException TokenHandlingException
   */
  public static JwtObject claimsOfTokenWithoutValidation(String token, ECPublicKey publicKey, ECPrivateKey privateKey) throws TokenHandlingException {
    
    JwtObject jwtObj = null;
    try {
      
      JWTVerifier verifier =
          JWT.require(Algorithm.ECDSA256(publicKey, privateKey)).acceptExpiresAt(2).build();

      DecodedJWT jwt = verifier.verify(token);
      jwtObj = new JwtObject();
      if (jwt.getAudience() != null && jwt.getAudience().size() > 0) {
        jwtObj.setAudience(jwt.getAudience().get(0));
      }
      jwtObj.setIssuer(jwt.getIssuer());
      jwtObj.setClientId(jwt.getClaim(JwtConstant.CLIENT_ID).asString());
      jwtObj.setLoginId(jwt.getClaim(JwtConstant.LOGIN_ID).asString());
      jwtObj.setSubject(jwt.getSubject());
      jwtObj.getRoles()
          .addAll((List<String>) jwt.getClaim(JwtConstant.ROLE_ID).asList(String.class));

    } catch (IllegalArgumentException ex) {
      throw new TokenHandlingException(ex);
    }
    return jwtObj;
  }

  private PrivateKey decryptPrivateKey(JWTEncryptionPreferences preferences)
      throws TokenHandlingException {
    PrivateKey decryptedPrivateKey;

    try {
      PEMParser keyReader = new PEMParser(new StringReader(preferences.getPrivateKey()));
      Object keyPair = keyReader.readObject();
      keyReader.close();

      if (keyPair instanceof PEMEncryptedKeyPair) {
        JcePEMDecryptorProviderBuilder builder = new JcePEMDecryptorProviderBuilder();
        PEMDecryptorProvider decryptionProvider =
            builder.build(preferences.getPrivateKeyPassword().toCharArray());
        keyPair = ((PEMEncryptedKeyPair) keyPair).decryptKeyPair(decryptionProvider);
      }

      PrivateKeyInfo keyInfo = ((PEMKeyPair) keyPair).getPrivateKeyInfo();
      decryptedPrivateKey = (new JcaPEMKeyConverter()).getPrivateKey(keyInfo);
    } catch (IOException e) {
      throw new TokenHandlingException("Error parsing private key for Box Developer Edition.", e);
    }

    return decryptedPrivateKey;
  }

}
