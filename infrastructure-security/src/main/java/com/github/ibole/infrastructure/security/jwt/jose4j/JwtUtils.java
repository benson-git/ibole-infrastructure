package com.github.ibole.infrastructure.security.jwt.jose4j;

import static com.google.common.base.Preconditions.checkArgument;

import com.github.ibole.infrastructure.common.exception.GenericRuntimeException;
import com.github.ibole.infrastructure.security.jwt.JwtConstant;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.JoseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/

/**
 * Producing and consuming a signed JWT.
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public final class JwtUtils {
 

  private JwtUtils(){
    //nothing to do
  }
  
  
  public static void main(String[] args) throws Exception{
    
    generateECKeyFile("d:/senderJWK.json");
  }
  
  // 生成秘钥工具类
  //Generate an EC key, which will be used for signing and verification of the JWT, wrapped in a JWK.
  public static String generateECKey(){
    String key;
    try {
      EllipticCurveJsonWebKey senderJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);
      key = senderJwk.toJson(OutputControlLevel.INCLUDE_PRIVATE);
    } catch (JoseException ex) {
      throw new GenericRuntimeException("Generating jwk error happened", ex);
    }
    return key;
  }
  
  public static PublicJsonWebKey toJsonWebKey(String jsonFile){
    PublicJsonWebKey jsonWebKey;
    File file = new File(jsonFile);
    BufferedReader reader = null;
    StringBuilder laststr = new StringBuilder();
    try {
     reader = new BufferedReader(new FileReader(file));
     String tempString = null;
     while ((tempString = reader.readLine()) != null) {
        laststr.append(tempString);
     }
     
     jsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(laststr.toString());
     
    } catch (IOException | JoseException ex) {
      throw new GenericRuntimeException("Reading Json file error happened", ex);
    } finally {      
      IOUtils.closeQuietly(reader);
    }
    
    return jsonWebKey;
  }
  /**
   * Generate ECKey File.
   * @param senderFilePath the generated key file path.
   */
  public static void generateECKeyFile(String senderFilePath){
    String key = generateECKey();
    generateJsonFile(senderFilePath, key);
    
  }
  
  private static void generateJsonFile(String filePath, String jsonData) {
    FileWriter fw = null;
    Writer write = null;
    try {
      fw = new FileWriter(filePath);
      write = new PrintWriter(fw);
      write.write(jsonData);
      write.flush();
    } catch (IOException ex) {
      throw new GenericRuntimeException("Generating Json file error happened", ex);
    } finally {
      IOUtils.closeQuietly(write);
      IOUtils.closeQuietly(fw);
    }

  }
  
  public static JsonObject toJson(String jwtJson) {

      return new Gson().fromJson(jwtJson, JsonObject.class);

  }

  /**
   * Create Jwt.
   * @param claimObj the JwtObject
   * @param pSenderJwk the EllipticCurveJsonWebKey
   * @return token generated
   * @throws JoseException Exception
   */
  public static String createJwtWithECKey(JwtObject claimObj, EllipticCurveJsonWebKey pSenderJwk) throws JoseException{
    checkArgument(claimObj != null, "Param cannot be null!");
   
    EllipticCurveJsonWebKey senderJwk = pSenderJwk;
    // Give the JWK a Key ID (kid): 密钥 id
    senderJwk.setKeyId(String.valueOf(claimObj.getIssuer().hashCode()));
    
    // Create the Claims, which will be the content of the JWT
    NumericDate numericDate = NumericDate.now();
    numericDate.addSeconds(claimObj.getTtlSeconds());
    JwtClaims claims = new JwtClaims();
    claims.setIssuer(claimObj.getIssuer()); 
    claims.setAudience(claimObj.getAudience()); 
    claims.setExpirationTime(numericDate);
    // a unique identifier for the token
    claims.setGeneratedJwtId(); 
    claims.setIssuedAtToNow(); 
    claims.setNotBeforeMinutesInThePast(1);
    claims.setSubject(claimObj.getSubject()); 
    claims.setClaim(JwtConstant.CLIENT_ID, claimObj.getClientId());
    claims.setClaim(JwtConstant.LOGIN_ID, claimObj.getLoginId());
    //multi-valued claims work too and will end up as a JSON array
    claims.setStringListClaim(JwtConstant.ROLE_ID, claimObj.getRoles());
    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS nested inside a JWE
    // So we first create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();
    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());
    // The JWT is signed using the sender's private key
    jws.setKey(senderJwk.getPrivateKey());
    // Set the Key ID (kid) header.
    jws.setKeyIdHeaderValue(senderJwk.getKeyId());
    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);
    // Sign the JWS and produce the compact serialization, which will be the inner JWT/JWS
    // representation, which is a string consisting of three dot ('.') separated
    // base64url-encoded parts in the form Header.Payload.Signature
    String innerJwt = jws.getCompactSerialization();
    return innerJwt;
  }
  
  public static boolean isExpired(String token, String audience, PublicJsonWebKey pSenderJwk){

    JwtConsumer secondPassJwtConsumer  = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setMaxFutureValidityInMinutes(60)
            .setAllowedClockSkewInSeconds(10) // allow some leeway in validating time based claims to account for clock skew
            .setVerificationKey(pSenderJwk.getPublicKey()) // verify the signature with the sender's public key
            //.registerValidator(new ClientIdentifierValidator(clientId))
            .setExpectedAudience(audience)
            .build(); // create the JwtConsumer instance
        
        try {
            secondPassJwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            return true;
        }
        return false;
  }
  
  /**
   * Validate Token based on the provided token.
   * <pre>
   * 1. validate the token signature
   * 2. validate the client id and login id
   * @param token
   * @param clientId
   * @param loginId
   * @param senderJwk
   * @return true if pass the validation, otherwise return false
   * @throws TokenParseException
   */
  public static boolean validateToken(String token, String clientId, String loginId, PublicJsonWebKey senderJwk) throws TokenParseException {
    if (validateSignature(token, senderJwk)) {
      return true;
    }
    return false;
  }
  
  private static boolean validateSignature(String token, PublicJsonWebKey senderJwk) throws TokenParseException{
    // Validate token signature
    JsonWebSignature jws = new JsonWebSignature();
    try {
      JsonWebEncryption jwe = new JsonWebEncryption();
      jwe.setCompactSerialization(token);
      jws.setCompactSerialization(jwe.getPayload());
      // Give the JWK a Key ID (kid): 密钥 id
      jws.setKey(senderJwk.getPublicKey());
      if(jws.verifySignature()) {
        return true;
      }
    } catch (JoseException ex) {
      throw new TokenParseException(ex);
    }
    return false;
  }
  /**
   * Restore the JwtObject from the provided token.
   * @param token
   * @param clientId
   * @param senderJwk
   * @return JwtObject
   * @throws TokenParseException
   */
  @SuppressWarnings("unchecked")
  public static JwtObject claimsOfTokenWithoutValidation(String token) throws TokenParseException {
    
    JwtObject jwtObj = null;
    try {

      JwtContext jwtctx = parseJwt(token);
      jwtObj = new JwtObject();
      if (jwtctx.getJwtClaims().getAudience() != null
          && jwtctx.getJwtClaims().getAudience().size() > 0) {
        jwtObj.setAudience(jwtctx.getJwtClaims().getAudience().get(0));
      }
      jwtObj.setIssuer(jwtctx.getJwtClaims().getIssuer());
      jwtObj.setClientId(jwtctx.getJwtClaims().getStringClaimValue(JwtConstant.CLIENT_ID));
      jwtObj.setLoginId(jwtctx.getJwtClaims().getStringClaimValue(JwtConstant.LOGIN_ID));
      jwtObj.setSubject(jwtctx.getJwtClaims().getSubject());
      jwtObj.getRoles()
          .addAll((List<String>) jwtctx.getJwtClaims().getClaimsMap().get(JwtConstant.ROLE_ID));

    } catch (MalformedClaimException | InvalidJwtException ex) {
      throw new TokenParseException(ex);
    }
    return jwtObj;
  }


  /**
   * Parse Jwt with public JWK.
   * @param token the token to parse
   * @return JwtContext
   * @throws InvalidJwtException
   */
  private static JwtContext parseJwt(String token) throws InvalidJwtException
       {
    // Build a JwtConsumer that doesn't check signatures or do any validation.
    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    //The first JwtConsumer is basically just used to parse the JWT into a JwtContext object.
    JwtContext jwtContext = firstPassJwtConsumer.process(token);
    return jwtContext;
  }
  
}
