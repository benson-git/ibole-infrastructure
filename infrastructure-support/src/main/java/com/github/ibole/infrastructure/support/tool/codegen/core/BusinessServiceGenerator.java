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
 * @author: whm
 * @createtime:2016年5月19日
 */
public class BusinessServiceGenerator extends AbsGenerator{

	@Override
	public File getOutFile(TableModel tm) {
		String path=FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("servicedir");
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator + "IBusinessInterface.java";
		File output = new File(outPath);
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
		Map<String,String> confData = new HashMap<String,String>();
		FreeMarkerEnvConfig config = FreeMarkerEnvConfig.getInstance();
		String basePackage = config.getFreemarkerConf().getProperty("basepackage");
		confData.put("basePackage",basePackage);
		File out = getOutFile(tm);
		if(!cover && out.exists()){
			return;
		}
		tm.setPackageName(basePackage + ".businessInterface");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("config", confData);
		Template template = null;
		try {
			template = FreeMarkerEnvConfig.getInstance().getTemplateByKey(getTemplateName());
			
			java.io.Writer writer = new java.io.FileWriter(out);
			data.put("model", tm);
			template.process(data, writer);
			writer.flush();
			writer.close();
			LogUtils.getLogger(this.getClass()).info(out.getName()+" 生成完成");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.getLogger(this.getClass()).error(e);
		}
	}
	@Override
	public String getTemplateName() {
		return "BusinessServiceftl";
	}

	
}
