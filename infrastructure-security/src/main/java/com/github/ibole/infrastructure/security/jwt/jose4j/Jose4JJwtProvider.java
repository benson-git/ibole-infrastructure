package com.github.ibole.infrastructure.security.jwt.jose4j;

import org.jose4j.jwa.AlgorithmFactoryFactory;
import org.jose4j.jwk.EllipticCurveJsonWebKey;

import com.github.ibole.infrastructure.cache.redis.RedisSimpleTempalte;
import com.github.ibole.infrastructure.security.jwt.JwtProvider;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
  public TokenAuthenticator<EllipticCurveJsonWebKey> createTokenGenerator(RedisSimpleTempalte redisTemplate) {
    //Initialized jose4j
    AlgorithmFactoryFactory.getInstance();
    return new EcTokenAuthenticator(redisTemplate);
  }

}
