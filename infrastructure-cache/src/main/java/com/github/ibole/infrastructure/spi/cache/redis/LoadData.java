package com.github.ibole.infrastructure.spi.cache.redis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 *  <p>.
 *  </p>
 *********************************************************************************************/


public interface LoadData {

  public <T> T loadDbData();
}
