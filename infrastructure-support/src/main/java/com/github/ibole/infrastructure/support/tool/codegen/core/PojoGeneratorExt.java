package com.github.ibole.infrastructure.support.tool.codegen.core;

import com.github.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import com.github.ibole.infrastructure.support.tool.codegen.model.TableModel;
import com.github.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import com.github.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;



public class PojoGeneratorExt extends AbsGenerator{
	@Override
	public File getOutFile(TableModel tm) {
		String path=FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("pojodir");
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator+tm.getTableName().substring(0, 1).toUpperCase()+tm.getTableName().substring(1) + "Entity.java";
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
	public String getTemplateName() {
		return "pojoextftl";
	}
}
