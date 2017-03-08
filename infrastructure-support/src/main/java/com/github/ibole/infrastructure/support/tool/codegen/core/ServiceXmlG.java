package com.github.ibole.infrastructure.support.tool.codegen.core;

/**
*@author:    whm
*@createtime:2016年6月16日
*/
public class ServiceXmlG extends DaoXmlG {

	public String getTemp(){
		return "serviceXmlContext.ftl";
	}
	public String filePath(){
		return "resource//application-"+getType()+".xml";
	}
	public String getType(){
		return "service";
	}
	

	
}

