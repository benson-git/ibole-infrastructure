package io.ibole.infrastructure.support.tool.codegen.core;

import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
import io.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import io.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;

/**
*@author:    whm
*@createtime:2016年5月19日
*/
public class FrameworkServiceGenerator extends AbsGenerator {

	@Override
	public File getOutFile(TableModel tm) {
		String path=FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("frameworkservicedir");
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator+tm.getClassName() + "FrameworkService.java";
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
		return "frameworkservicetfl";
	}
}

