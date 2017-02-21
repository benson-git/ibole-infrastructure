package io.ibole.infrastructure.support.tool.codegen.util;

import java.io.File;
import java.io.IOException;

/**
*@author:    whm
*@createtime:2016519
*/
public class GFileUtiles {

	public static String getSRCPath(){
		return getHome()+File.separator+"src\\";
	}
	public static String getResourcePath(){
		return getHome()+File.separator + "resource\\";
	}
	public static String getHome(){
		return System.getProperty("user.dir");
	}
	public static void enterDirExists(String path){
		File p = new File(path);
		if(!p.exists()){
			p.mkdirs();
		}
	}
	public static void enterFileExists(File f) throws IOException{
		if(!f.exists()){
			f.createNewFile();
		}
	}
}

