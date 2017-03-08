package com.github.ibole.infrastructure.support.tool.codegen.core;


import com.github.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import com.github.ibole.infrastructure.support.tool.codegen.model.TableModel;
import com.github.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import com.github.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
*@author:    whm
*@createtime:2016年5月20日
*/
public class BaseDaoGenerator implements IGenerator{

	@Override
	public void build(TableModel tm,boolean cover) throws TemplateException, IOException {
		String path = FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("basedaodir");
		GFileUtiles.enterDirExists(path);
		String fileName = path + "\\" + FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basedaoname")+".java";
		Map<String,String> data = new HashMap<String,String>();
		data.put("packageName", FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basepackage") + ".dao");
		data.put("basedaoname", FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basedaoname"));
		File out = new File(fileName);
		if(!cover && out.exists()){
			return ;
		}
		GFileUtiles.enterFileExists(out);
		Template template = null;
		try {
			template = FreeMarkerEnvConfig.getInstance().getTemplateByKey("basedaoftl");
			java.io.Writer writer = new java.io.FileWriter(out);
			
			Map<String,Map> para = new HashMap<String,Map>();
			para.put("model", data);
			template.process(para, writer);
			writer.flush();
			writer.close();
			LogUtils.getLogger(this.getClass()).info(out.getName()+" 生成完成");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.getLogger(this.getClass()).error(e);
		}
	}

}

