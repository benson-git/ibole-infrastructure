package io.ibole.infrastructure.security.jwt.jose4j;

import io.ibole.infrastructure.security.jwt.JwtConstant;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.jwt.consumer.Validator;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * A validator for client id which generated JWT tokens.
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class ClientIdentifierValidator implements Validator {
  
  private String clientId;
  
  public ClientIdentifierValidator(String clientId){
    this.clientId = clientId;
  }

  /**
   * Make sure the JWT has the client id we expect to exist.
   *
   * @param jwtContext the JWT context
   * @return a description of the problem or null, if valid
   * @throws MalformedClaimException if a malformed claim is encountered
   */
  @Override
  public String validate(JwtContext jwtContext) throws MalformedClaimException {
    
      final JwtClaims claims = jwtContext.getJwtClaims();
      final StringBuilder builder = new StringBuilder();
      final String cid = claims.getClaimValue(JwtConstant.CLIENT_ID, String.class);
      if (cid == null) {
          builder.append("No client id field present and is required. ");
      }else if(!cid.equals(clientId)){
          builder.append("Client id '"+clientId+"' is invalid. ");
      }
      if (builder.length() == 0) {
          return null;
      } else {
          return builder.toString();
      }

  }

}
