package io.ibole.infrastructure.support.tool.codegen.core;

import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
import io.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import io.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;



public class PojoGenerator extends AbsGenerator{
	@Override
	public File getOutFile(TableModel tm) {
		String path=FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("pojodir");
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator+tm.getTableName().substring(0, 1).toUpperCase()+tm.getTableName().substring(1) + "EntityBase.java";
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
		return "pojoftl";
	}
}
