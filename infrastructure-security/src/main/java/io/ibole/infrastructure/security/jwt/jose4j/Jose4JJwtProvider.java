package io.ibole.infrastructure.security.jwt.jose4j;

import org.jose4j.jwa.AlgorithmFactoryFactory;
import org.jose4j.jwk.EllipticCurveJsonWebKey;

import io.ibole.infrastructure.cache.redis.RedisSimpleTempalte;
import io.ibole.infrastructure.security.jwt.JwtProvider;
import io.ibole.infrastructure.security.jwt.TokenAuthenticator;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class Jose4JJwtProvider extends JwtProvider {

  /* (non-Javadoc)
   * @see org.toprank.infrastructure.security.jwt.JwtProvider#isAvailable()
   */
  @Override
  protected boolean isAvailable() {
   
    return true;
  }

  /* (non-Javadoc)
   * @see org.toprank.infrastructure.security.jwt.JwtProvider#priority()
   */
  @Override
  protected int priority() {
   
    return 5;
  }

  /* (non-Javadoc)
   * @see org.toprank.infrastructure.security.jwt.JwtProvider#createTokenGenerator()
   */
  @Override
  public TokenAuthenticator<EllipticCurveJsonWebKey, EllipticCurveJsonWebKey> createTokenGenerator(RedisSimpleTempalte redisTemplate) {
    //Initialized jose4j
    AlgorithmFactoryFactory.getInstance();
    return new EcTokenAuthenticator(redisTemplate);
  }

}
