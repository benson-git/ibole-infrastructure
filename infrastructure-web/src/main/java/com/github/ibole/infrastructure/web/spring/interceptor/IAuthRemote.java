package com.github.ibole.infrastructure.web.spring.interceptor;

import com.github.ibole.infrastructure.common.exception.GenericException;

/**
 * 认证服务接口，需在各自项目中引入该接口的实现
 * @author Pxie
 *
 */
public interface IAuthRemote {

	/**
	 * 获取Token
	 * @param custId
	 * @return
	 */
	public String getToken(String custId) throws GenericException;
	
	
	/**
	 * 校验用户token
	 * @param info
	 * @throws BaseException
	 */
	public boolean checkToken(String custId, String token) throws GenericException;
}
