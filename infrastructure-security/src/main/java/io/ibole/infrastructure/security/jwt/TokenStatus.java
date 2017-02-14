package io.ibole.infrastructure.security.jwt;

import java.util.HashMap;
import java.util.Map;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public final class TokenStatus {

  // Create the canonical list of TokenStatus instances indexed by their code values.
  private static final Map<String, TokenStatus> STATUS_MAP = buildStatusMap();

  public static final TokenStatus ACCESS_TOKEN_EXPIRED = Code.ACCESS_TOKEN_EXPIRED.toStatus();
  
  public static final TokenStatus REFRESH_TOKEN_EXPIRED = Code.REFRESH_TOKEN_EXPIRED.toStatus();

  public static final TokenStatus ILLEGAL = Code.ILLEGAL.toStatus();

  public static final TokenStatus VALIDATED = Code.VALIDATED.toStatus();

  private final Code code;

  public TokenStatus(Code code) {
    this.code = code;
  }


  public Code getCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return "Status:" + code;
  }

  private static Map<String, TokenStatus> buildStatusMap() {
    Map<String, TokenStatus> canonicalizer = new HashMap<String, TokenStatus>();
    for (Code code : Code.values()) {
      TokenStatus replaced = canonicalizer.put(code.value(), new TokenStatus(code));
      if (replaced != null) {
        throw new IllegalStateException(
            "Code value duplication between " + replaced.getCode().name() + " & " + code.name());
      }
    }
    return canonicalizer;
  }
  
  public enum Code {

    /**
     * The access token is expired.
     */
    ACCESS_TOKEN_EXPIRED("ACCESS_TOKEN_EXPIRED"),
    /**
     * The refresh token is expired.
     */
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED"),
    /**
     * The token is illegal, invalid signature and client identifier.
     */
    ILLEGAL("ILLEGAL"),
    /**
     * The token is validated.
     */
    VALIDATED("VALIDATED");

    private final String value;

    private Code(String value) {
      this.value = value;
    }

    /**
     * The value of the status.
     */
    public String value() {
      return value;
    }

    public TokenStatus toStatus() {
      return STATUS_MAP.get(value);
    }

  }

}
