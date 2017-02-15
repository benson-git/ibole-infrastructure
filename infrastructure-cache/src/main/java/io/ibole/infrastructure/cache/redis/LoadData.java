package io.ibole.infrastructure.cache.redis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * @author LiJun
 *         <p>
 *         </p>
 *********************************************************************************************/


public interface LoadData {

  public <T> T loadDbData();
}
