package io.ibole.infrastructure.support.tool.codegen.conf;


import io.ibole.infrastructure.support.tool.codegen.dao.MysqlMetaDataDao;
import io.ibole.infrastructure.support.tool.codegen.util.GFileUtiles;
import io.ibole.infrastructure.support.tool.codegen.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;



public class FreeMarkerEnvConfig {

	private Properties jdbcpro;
	private Properties jdbcTypeMap;
	private Properties freemarkerConf;
	private Map<String, String> columnsInfo = new HashMap<String,String>();
	private Configuration conf = new Configuration();
	private static FreeMarkerEnvConfig instance = null;
	public boolean initFlag = false;
	public static synchronized FreeMarkerEnvConfig getInstance(){
		if(instance == null){
			instance = new FreeMarkerEnvConfig();
		}
		return instance;
	}
	
	public Properties getJdbcpro() {
		return jdbcpro;
	}
	public Configuration getConf() {
		return conf;
	}

	public Properties getJdbcTypeMap() {
		return jdbcTypeMap;
	}

	public Properties getFreemarkerConf() {
		return freemarkerConf;
	}
	public Map<String, String> getColumnsInfo(){
		synchronized (columnsInfo) {
			if(columnsInfo.isEmpty()){
				try {
					columnsInfo = dao.getAppcolumns();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return columnsInfo;
		}
		
		
	}
	public static void main(String[] args) throws IOException {
		//System.out.println(getInstance().getJdbcpro().get("url"));
		//System.out.println(Thread.currentThread().getContextClassLoader().getResource("jdbc.properties") );
		FreeMarkerEnvConfig.getInstance().init();
		System.out.println(FreeMarkerEnvConfig.getInstance().getConf().getTemplate("pojo.ftl"));
	}
	private FreeMarkerEnvConfig(){
		if(!this.initFlag)
			init();
	}
	public String getMapperType(){
		return getFreemarkerConf().getProperty("mappertype");
	}
	MysqlMetaDataDao dao = new MysqlMetaDataDao();
	
	private void init(){
		if(initFlag){
			return;
		}
		initFlag = true;
		LogUtils.getLogger(this.getClass()).info("开始初始化环境");
		jdbcpro = new Properties();
		jdbcTypeMap = new Properties();
		freemarkerConf = new Properties();
		try {
			jdbcpro.load(FreeMarkerEnvConfig.class.getClassLoader().getResourceAsStream("jdbc.properties"));
			jdbcTypeMap.load(FreeMarkerEnvConfig.class.getClassLoader().getResourceAsStream("jdbctypemap.properties"));
			freemarkerConf.load(FreeMarkerEnvConfig.class.getClassLoader().getResourceAsStream("freemarker-conf.properties"));
			conf.setDefaultEncoding("utf-8");
			conf.setSharedVariable("name", "whm");
			conf.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/templates").toURI().getPath()));
			//初始化表字典
			//columnsInfo=dao.getAppcolumns();
		} catch (IOException | TemplateModelException | URISyntaxException e) {
			e.printStackTrace();
			initFlag = false;
		} 
		LogUtils.getLogger(this.getClass()).info("环境初始化结束");
	}
	/**
	 * 通过模板文件名获取模板
	 * @param templateFileName
	 * @return
	 * @throws IOException
	 */
	public Template getTemplate(String templateFileName) throws IOException{
		return conf.getTemplate(templateFileName);
	}
	/**
	 * 通过properties文件中配置的key 获取模板
	 * @param propertyKey
	 * @return
	 * @throws IOException
	 */
	public Template getTemplateByKey(String propertyKey) throws IOException{
		return getTemplate(getFreemarkerConf().getProperty(propertyKey));
	}
	public String getGeneratorPathByKey(String key){
		String prePath = GFileUtiles.getSRCPath();
		String srcPath = getFreemarkerConf().getProperty("src-path");
		if(srcPath != null && !"".equals(srcPath)){
			prePath = prePath + srcPath + File.separator;
		}
		return prePath  + getFreemarkerConf().getProperty(key);
	}
	
	public void destroy(){
		jdbcpro = null;
		jdbcTypeMap = null;
		freemarkerConf = null;
		initFlag = false;
	}
}
