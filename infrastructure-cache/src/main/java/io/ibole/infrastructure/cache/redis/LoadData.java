package io.ibole.infrastructure.cache.redis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * @author LiJun
 *         <p>
 *         </p>
 *********************************************************************************************/


public interface LoadData {

  public <T> T loadDbData();
}
