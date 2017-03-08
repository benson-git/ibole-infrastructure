package com.github.ibole.infrastructure.support.tool.codegen.core;

import com.github.ibole.infrastructure.support.tool.codegen.model.TableModel;
import com.github.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import com.github.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;


/**
*@author:    whm
*@createtime:2016年5月23日
*/
public class ProcedureGenerator extends AbsGenerator implements IOutFileName {

	public final static String TEMP_NAME = "procedureftl";
	@Override
	public File getOutFile(TableModel tm) {
		String path = GFileUtiles.getHome() + File.separator + "data";
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator + getFileName(tm);
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
		return TEMP_NAME;
	}

	@Override
	public String getFileName(TableModel tm) {
		return "pro_" +tm.getTableName().toLowerCase() + ".sql";
	}

}

