package com.github.ibole.infrastructure.support.tool.codegen.core;

import com.github.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import com.github.ibole.infrastructure.support.tool.codegen.dao.MysqlMetaDataDao;
import com.github.ibole.infrastructure.support.tool.codegen.model.TableModel;
import com.github.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
*@author:    whm
*@createtime:2016年6月16日
*/
public class DaoXmlG implements IGenerator {

	public String getTemp(){
		return "daoimplXmlContext.ftl";
	}
	public String filePath(){
		return GFileUtiles.getResourcePath()+"application-"+getType()+".xml";
	}
	public String getType(){
		return "dao";
	}
	@Override
	public void build(TableModel tm, boolean cover) throws TemplateException, IOException {
		MysqlMetaDataDao dao = new MysqlMetaDataDao();
		try {
			List<String> tables = dao.getTables();
			Map<String, Object> model = new HashMap<String,Object>();
			model.put("tables", tables);
			model.put("basepackage",FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basepackage"));
			Template template = FreeMarkerEnvConfig.getInstance().getTemplate(getTemp());
			Map<String, Object> data = new HashMap<String, Object>();
			java.io.Writer writer = new java.io.FileWriter(filePath());
			data.put("model", model);
			template.process(data, writer);
			writer.flush();
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	
}

