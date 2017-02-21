package io.ibole.infrastructure.support.tool.codegen.core;


import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
import io.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import io.ibole.infrastructure.support.tool.codegen.util.LogUtils;

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
public class BaseEntityGenerator extends AbsGenerator{

	@Override
	public File getOutFile(TableModel tm) {
		//pojodir
		String path = FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("pojodir");
		GFileUtiles.enterDirExists(path);
		String fileName = path + File.separator + "Entity.java";
		File output = new File(fileName);
		try {
			GFileUtiles.enterFileExists(output);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.getLogger(this.getClass()).error(e);
		}
		return output;
	}
	@Override
	public void build(TableModel tm, boolean cover) throws TemplateException, IOException {
		File file = getOutFile(tm);
		Template template = FreeMarkerEnvConfig.getInstance().getTemplateByKey(getTemplateName());
		String basePackage = FreeMarkerEnvConfig.getInstance().getFreemarkerConf().getProperty("basepackage");
		Map<String, String> model = new HashMap<String,String>();
		model.put("packageName", basePackage);
		java.io.Writer writer = new java.io.FileWriter(file);
		template.process(model, writer);
		writer.flush();
		writer.close();
	}

	@Override
	public String getTemplateName() {
		return "baseentityftl";
	}


}

