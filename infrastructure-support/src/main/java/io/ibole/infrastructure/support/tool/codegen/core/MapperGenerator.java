package io.ibole.infrastructure.support.tool.codegen.core;

import io.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;
import io.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import io.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;



public class MapperGenerator extends AbsGenerator implements IOutFileName {

	@Override
	public File getOutFile(TableModel tm) {
		String path=FreeMarkerEnvConfig.getInstance().getGeneratorPathByKey("mapperdir");
		GFileUtiles.enterDirExists(path);
		String outPath = path + File.separator+getFileName(tm);
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
		String type = FreeMarkerEnvConfig.getInstance().getMapperType();
		return "sql".equals(type)?"mapperftl":"proceduremapper";
	}

	@Override
	public String getFileName(TableModel tm) {
		return tm.getTableName() + "-mapper.xml";
	}

}
