package io.ibole.infrastructure.support.tool.codegen.core;


import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
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
public abstract class AbsGenerator implements IGenerator {

	public abstract File getOutFile(TableModel tm);
	public abstract String getTemplateName();
	@Override
	public void build(TableModel tm,boolean cover) throws TemplateException, IOException {
		Map<String,String> confData = new HashMap<String,String>();
		Map<String, Object> data = new HashMap<String, Object>();
		FreeMarkerEnvConfig config = FreeMarkerEnvConfig.getInstance();
		String basePackage = config.getFreemarkerConf().getProperty("basepackage");
		if(this instanceof IClassNamePostfix){
			IClassNamePostfix ibcn = (IClassNamePostfix)this;
			tm.setPojoName(tm.getClassName());
			tm.setClassName(tm.getClassName()+ibcn.postfix());
		}
		
		File out = getOutFile(tm);
		if(!cover && out.exists()){
			return;
		}
		confData.put("basePackage",basePackage);
		confData.put("baseDaoName", config.getFreemarkerConf().getProperty("basedaoname"));
		confData.put("baseDaoFullName", basePackage+".dao."+config.getFreemarkerConf().getProperty("basedaoname"));
		confData.put("baseDaoImplName", config.getFreemarkerConf().getProperty("basedaoimplname"));
		confData.put("baseDaoImplFullName", basePackage + ".dao.impl." + confData.get("baseDaoImplName"));
		data.put("config", confData);
		LogUtils.getLogger(this.getClass()).info("开始生成 "+out.getName());
		String path=out.getAbsolutePath();
		String className=tm.getClassName();
//		if(this instanceof MapperGenerator){
//			className = tm.getTableName().toLowerCase();
//		}
//		if(this instanceof ProcedureGenerator){
//			className = "pro_" +tm.getTableName().toLowerCase();
//		}
		if(this instanceof IOutFileName){
			IOutFileName iofn = (IOutFileName)this;
			className = iofn.getFileName(tm);
		}
		String tempPath = path.toLowerCase();
		int classNameIndex = 0;
		classNameIndex = tempPath.indexOf(className.toLowerCase());
		String p = path.substring(path.indexOf("src")+4,classNameIndex-1);
		tm.setPackageName(p.replace("\\", "."));
		if(this instanceof IClassNamePostfix){
			tm.setPojoFullName(tm.getPackageName().substring(0, tm.getPackageName().lastIndexOf(".")) + ".entity." + tm.getPojoName());
		}
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

}

