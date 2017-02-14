package io.ibole.infrastructure.security.jwt.jose4j;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
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
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;

import io.ibole.infrastructure.common.exception.GenericRuntimeException;
import io.ibole.infrastructure.security.jwt.JwtConstant;
import io.ibole.infrastructure.security.jwt.JwtObject;
import io.ibole.infrastructure.security.jwt.TokenParseException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
    
    generateECKeyPairFiles("d:/senderJWK.json", "d:/receiverJWK.json");
  }
  
  // 生成秘钥工具类
  //Generate an EC key pair, which will be used for signing and verification of the JWT, wrapped in a JWK.
  public static String[] generateECKeyPair(){
    String[] pair = new String[2];
    try {
      EllipticCurveJsonWebKey senderJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);
      EllipticCurveJsonWebKey receiverJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);
      pair[0] = senderJwk.toJson(OutputControlLevel.INCLUDE_PRIVATE);
      pair[1] = receiverJwk.toJson(OutputControlLevel.INCLUDE_PRIVATE);
    } catch (JoseException ex) {
      throw new GenericRuntimeException("Generating jwk error happened", ex);
    }
    return pair;
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
   * Generate ECKey Pair Files.
   * @param senderFilePath
   * @param receiveFilePath
   */
  public static void generateECKeyPairFiles(String senderFilePath, String receiveFilePath){
    String[] pairs = generateECKeyPair();
    generateJsonFile(senderFilePath, pairs[0]);
    generateJsonFile(receiveFilePath, pairs[1]);
    
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
  

  /**
   * Create jwt with rsa jwk.
   * @param claimObj JwtObject
   * @param jsonWebKey RsaJsonWebKey
   * @return generated jwt the generated token
   * @throws JoseException Exception
   */
  public static String createJwtWithRSAKey(JwtObject claimObj, RsaJsonWebKey jsonWebKey) throws JoseException {

    jsonWebKey.setKeyId(JwtConstant.KID_RSA);
    JwtClaims claims = new JwtClaims();
    // token发行者
    claims.setIssuer(claimObj.getIssuer());
    claims.setAudience(claimObj.getAudience()); // to whom the token is intended to be sent
    // 过期时间为现在起后xxx seconds
    claims.setExpirationTime(NumericDate
        .fromMilliseconds(System.currentTimeMillis() + claimObj.getTtlSeconds() * 1000l));

    claims.setGeneratedJwtId(); // a unique identifier for the token
    claims.setIssuedAtToNow();// when the token was issued/created (now)
    claims.setSubject(claimObj.getSubject());
    claims.setClaim(JwtConstant.CLIENT_ID, claimObj.getClientId());
    //考虑到时间偏差,设置在过去的一分钟内 token仍然有效，一分钟前无效
    claims.setNotBeforeMinutesInThePast(1);
    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS so we create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(claims.toJson());
    // 设置私钥
    jws.setKey(jsonWebKey.getPrivateKey());
    jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

    return jws.getCompactSerialization();
  }
 
  /**
   * 验证方法.
   * @param token the token string
   * @param clientId the Client Id
   * @param jsonWebKey the RsaJsonWebKey
   * @return boolean true if the token is validated, otherwise return false
   * @throws InvalidJwtException Exception
   * @throws MalformedClaimException Exception
   */
  public static boolean validJwtWithRSAKey(String token, String clientId, RsaJsonWebKey jsonWebKey) throws InvalidJwtException, MalformedClaimException {
    
    // Build a JwtConsumer that doesn't check signatures or do any validation.
    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .build();

    //The first JwtConsumer is basically just used to parse the JWT into a JwtContext object.
    JwtContext jwtContext = firstPassJwtConsumer.process(token);
    String issuer = jwtContext.getJwtClaims().getIssuer();
    List<String> audiences = jwtContext.getJwtClaims().getAudience();
    String subject = jwtContext.getJwtClaims().getSubject();
    
    JwtConsumer secondPassJwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime() // 必须有过期时间
        .setMaxFutureValidityInMinutes(60) 
        .setAllowedClockSkewInSeconds(10) // allow some leeway in validating time based claims to account for clock skew
        .setExpectedIssuer(issuer) // whom the JWT needs to have been issued by
        .setExpectedAudience(audiences.toArray(new String[0])) // to whom the JWT is intended for
        .setRequireSubject().setRequireNotBefore().setExpectedSubject(subject)
        .setVerificationKey(jsonWebKey.getKey()) // verify the signature with the public key
        .registerValidator(new ClientIdentifierValidator(clientId))
        .build(); // create the JwtConsumer instance
    
    secondPassJwtConsumer.processToClaims(token);

    return true;
  }
  
  public static JsonObject toJson(String jwtJson) {

      return new Gson().fromJson(jwtJson, JsonObject.class);

  }

  /**
   * Create Jwt.
   * @param claimObj the JwtObject
   * @return token generated
   * @throws JoseException Exception
   */
  public static String createJwtWithECKey(JwtObject claimObj, EllipticCurveJsonWebKey pSenderJwk, EllipticCurveJsonWebKey pReceiverJwk) throws JoseException{
    checkArgument(claimObj != null, "Param cannot be null!");
   
    EllipticCurveJsonWebKey senderJwk = pSenderJwk;
    EllipticCurveJsonWebKey receiverJwk = pReceiverJwk;
    // Give the JWK a Key ID (kid): 密钥 id
    senderJwk.setKeyId(String.valueOf(claimObj.getIssuer().hashCode()));
    receiverJwk.setKeyId(String.valueOf(claimObj.getIssuer().hashCode()));
    
    // Create the Claims, which will be the content of the JWT
    JwtClaims claims = new JwtClaims();
    claims.setIssuer(claimObj.getIssuer()); 
    claims.setAudience(claimObj.getAudience()); 
    claims.setExpirationTime(NumericDate
        .fromMilliseconds(System.currentTimeMillis() + claimObj.getTtlSeconds() * 1000l));
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

    // The outer JWT is a JWE
    JsonWebEncryption jwe = new JsonWebEncryption();

    // The output of the ECDH-ES key agreement will encrypt a randomly generated content encryption key
    jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.ECDH_ES_A128KW);

    // The content encryption key is used to encrypt the payload
    // with a composite AES-CBC / HMAC SHA2 encryption algorithm
    String encAlg = ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256;
    jwe.setEncryptionMethodHeaderParameter(encAlg);

    // We encrypt to the receiver using their public key
    jwe.setKey(receiverJwk.getPublicKey());
    jwe.setKeyIdHeaderValue(receiverJwk.getKeyId());

    // A nested JWT requires that the cty (Content Type) header be set to "JWT" in the outer JWT
    //在使用嵌套签名或加密时，这个头部参数必须存在；在这种情况下，它的值必须是 "JWT"，来表明这是一个在 JWT 中嵌套的 JWT。
    jwe.setContentTypeHeaderValue("JWT");

    // The inner JWT is the payload of the outer JWT
    jwe.setPayload(innerJwt);

    // Produce the JWE compact serialization, which is the complete JWT/JWE representation,
    // which is a string consisting of five dot ('.') separated
    // base64url-encoded parts in the form Header.EncryptedKey.IV.Ciphertext.AuthenticationTag

    return jwe.getCompactSerialization();
  }
  
  public static boolean isExpired(String token, String audience, PublicJsonWebKey pSenderJwk, PublicJsonWebKey pReceiverJwk){

    JwtConsumer secondPassJwtConsumer  = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setMaxFutureValidityInMinutes(60)
            .setAllowedClockSkewInSeconds(10) // allow some leeway in validating time based claims to account for clock skew
            .setDecryptionKey(pReceiverJwk.getPrivateKey()) // decrypt with the receiver's private key
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
   * @param receiverJwk
   * @return true if pass the validation, otherwise return false
   * @throws TokenParseException
   */
  public static boolean validateToken(String token, String clientId, String loginId,
      PublicJsonWebKey senderJwk, PublicJsonWebKey receiverJwk) throws TokenParseException {
    if (validateClientUniqueIdentifier(token, clientId, loginId, receiverJwk)
        && validateSignature(token, senderJwk, receiverJwk)) {
      return true;
    }
    return false;
  }
  /**
   * Check if the both client id and login id are match with the provided token.
   * 
   * @param token
   * @param clientId
   * @param loginId
   * @param receiverJwk
   * @return true if pass the verification, otherwise return false
   * @throws TokenParseException
   */
  private static boolean validateClientUniqueIdentifier(String token, String clientId, String loginId, PublicJsonWebKey receiverJwk) throws TokenParseException{
    try {
      JwtContext jwtctx = parseJwt(token, receiverJwk);
      if(clientId.equals(jwtctx.getJwtClaims().getStringClaimValue(JwtConstant.CLIENT_ID)) 
          && loginId.equals(jwtctx.getJwtClaims().getStringClaimValue(JwtConstant.LOGIN_ID))){
        return true;
      } 
    } catch (InvalidJwtException | MalformedClaimException ex) {
        throw new TokenParseException(ex);
    }
    
    return false;
  }
  
  private static boolean validateSignature(String token, PublicJsonWebKey senderJwk, PublicJsonWebKey receiverJwk) throws TokenParseException{
    // Validate token signature
    JsonWebSignature jws = new JsonWebSignature();
    try {
      JsonWebEncryption jwe = new JsonWebEncryption();
      jwe.setKey(receiverJwk.getPrivateKey());
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
   * @param receiverJwk
   * @return JwtObject
   * @throws TokenParseException
   */
  @SuppressWarnings("unchecked")
  public static JwtObject claimsOfTokenWithoutValidation(String token, PublicJsonWebKey receiverJwk) throws TokenParseException {
    
    JwtObject jwtObj = null;
    try {

      JwtContext jwtctx = parseJwt(token, receiverJwk);
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
   * @param receiverJwk PublicJsonWebKey
   * @return JwtContext
   * @throws InvalidJwtException
   */
  private static JwtContext parseJwt(String token, PublicJsonWebKey receiverJwk) throws InvalidJwtException
       {
    // Build a JwtConsumer that doesn't check signatures or do any validation.
    JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
            .setSkipAllValidators()
            .setDisableRequireSignature()
            .setSkipSignatureVerification()
            .setDecryptionKey(receiverJwk.getPrivateKey())
            .build();

    //The first JwtConsumer is basically just used to parse the JWT into a JwtContext object.
    JwtContext jwtContext = firstPassJwtConsumer.process(token);
    return jwtContext;
  }
  
  /**
   * The public key used to verify the signatures of JWT tokens.
   *
   * @param keyValue The string of the public key to use in either RSA or X.509 format
   * @return A public key object to use when validating JWT tokens
   * @throws TokenParseException 
   */
  public PublicKey jwtPublicKey(final String keyValue) throws TokenParseException
  {
    final String certBegin = "-----BEGIN CERTIFICATE-----";
    final String rsaBegin = "-----BEGIN PUBLIC KEY-----";
    checkArgument(keyValue != null, "Param cannot be null!");
    try {
      if (keyValue.startsWith(certBegin)) {
        // X.509 cert
        try (
            final ByteArrayInputStream bis = new ByteArrayInputStream(keyValue.getBytes("UTF-8"))) {
          final CertificateFactory fact = CertificateFactory.getInstance("X.509");
          final X509Certificate cer = (X509Certificate) fact.generateCertificate(bis);
          return cer.getPublicKey();
        }
      } else if (keyValue.startsWith(rsaBegin)) {
        // RSA Public Key
        return new RsaKeyUtil().fromPemEncoded(keyValue);
      } else {
        throw new IllegalArgumentException(
            "Only support X.509 pem certs or Public RSA Keys for jwt keyValue");
      }
    } catch (Exception ex) {
        throw new TokenParseException(ex);
    }
  }
}
