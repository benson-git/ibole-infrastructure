/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ibole.infrastructure.security.jwt.auth0;

import com.github.ibole.infrastructure.security.jwt.JwtProvider;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.spi.cache.redis.RedisSimpleTempalte;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class Auth0JwtProvider extends JwtProvider {

  /* (non-Javadoc)
   * @see com.github.ibole.infrastructure.security.jwt.JwtProvider#isAvailable()
   */
  @Override
  protected boolean isAvailable() {
    
    return true;
  }

  /* (non-Javadoc)
   * @see com.github.ibole.infrastructure.security.jwt.JwtProvider#priority()
   */
  @Override
  protected int priority() {
    return 4;
  }

  /* (non-Javadoc)
   * @see com.github.ibole.infrastructure.security.jwt.JwtProvider#createTokenGenerator(com.github.ibole.infrastructure.spi.cache.redis.RedisSimpleTempalte)
   */
  @Override
  public TokenAuthenticator createTokenGenerator(RedisSimpleTempalte redisTemplate) {
   
    return new EcAuth0TokenAuthenticator(redisTemplate);
  }

}
