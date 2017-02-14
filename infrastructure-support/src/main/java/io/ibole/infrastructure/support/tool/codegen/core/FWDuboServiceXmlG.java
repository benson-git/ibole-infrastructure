package io.ibole.infrastructure.support.tool.codegen.core;

/**
*@author:    whm
*@createtime:2016年6月16日
*/
public class FWDuboServiceXmlG extends DaoXmlG {

	public String getTemp(){
		return "frameworkServiceXmlContextDubbo.ftl";
	}
	public String filePath(){
		return "resource//application-"+getType()+".xml";
	}
	public String getType(){
		return "dubbo-api";
	}
	

	
}

