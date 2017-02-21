package io.ibole.infrastructure.support.tool.codegen.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
*@author:    whm
*@createtime:2016520
*/
public class LogUtils {

	private static Map<String, Logger> loggerCache = new HashMap<String,Logger>();
	public static synchronized Logger getLogger(Class clazz){
		Logger logger = loggerCache.get(clazz.getName());
		if(logger == null){
			logger = Logger.getLogger(clazz);
			loggerCache.put(clazz.getName(), logger);
		}
		return logger;
	}
}

