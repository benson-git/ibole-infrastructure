package io.ibole.infrastructure.support.tool.codegen.core;


import io.ibole.infrastructure.support.tool.codegen.dao.MysqlMetaDataDao;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import freemarker.template.TemplateException;

/**
*@author:    whm
*@createtime:2016年6月15日
*/
public class ToprankBuild {
	/**
	 * 数据库访问对象
	 */
	MysqlMetaDataDao dao 	= new MysqlMetaDataDao();
	
	/**生成Base entity
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws TemplateException 
	 *
	 */
	public void buildParentEntity(String tn) throws SQLException, TemplateException, IOException{
		IGenerator g = new BaseEntityGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		g.build(tm, true);

	}
	/**根据表名生成entity
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws TemplateException 
	 *
	 */
	public void buildBaseEntity(String tn) throws SQLException, TemplateException, IOException{
		IGenerator g = new PojoGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		g.build(tm, true);
	}
	/**根据表名生成ext entity
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws TemplateException 
	 *
	 */
	public void buildEntityExt(String tn) throws SQLException, TemplateException, IOException{
		IGenerator g = new PojoGeneratorExt();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		g.build(tm, true);
	}
	/**根据表名生成entitybase and  ext entity
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws TemplateException 
	 *
	 */
	public void buildEntityBaseAndEntityExt(String tn) throws SQLException, TemplateException, IOException{
		buildBaseEntity(tn);
		buildEntityExt(tn);
	}
	/**
	 * 生成所有的entity,,一个项目，只能调用一次。生成的代码会覆盖以前的内容，请谨慎使用
	 * @throws SQLException
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void buildBusinessEntity() throws SQLException, TemplateException, IOException{
		List<String> list = dao.getTables();
		if(list == null || list.size() == 0){
			throw new NullPointerException("指定数据库为空！");
		}
		buildParentEntity(list.get(0));
		for(int i=0;i<list.size();i++){
			buildEntityBaseAndEntityExt(list.get(i));
		}
	}
	/**
	 * 生成  service 基接口 空
	 * @param tn
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildServiceParentInterface(String tn) throws TemplateException, IOException, SQLException{
		IGenerator bussG        = new BusinessServiceGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		bussG.build(tm, true);
	}
	/**
	 * 生成 业务类 service 接口
	 * @param tn
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildServiceInterface(String tn) throws TemplateException, IOException, SQLException{
		IGenerator g = new ServiceGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		g.build(tm, true);
	}
	/**
	 * 生成buildinterface
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildBusinessInterface()throws TemplateException, IOException, SQLException{
		List<String> list = dao.getTables();
		if(list == null || list.size() == 0){
			throw new NullPointerException("指定数据库为空！");
		}
		buildServiceParentInterface(list.get(0));
		for(int i=0;i<list.size();i++){
			buildServiceInterface(list.get(i));
		}
	}
	////////生成dao
	/**
	 * 生成baseDaoimpl
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void buildBaseImplDao() throws TemplateException, IOException{
		IGenerator bidg 		= new BaseDaoImplGenerator();
		bidg.build(null, true);
	}
	public void buildDaoImpl(String tableName,boolean cover)throws SQLException, IOException, TemplateException{
		IGenerator dig 			= new DaoImplGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tableName);
		dig.build(tm, cover);
	}
	
	/**
	 * 根据表名生成mysql 映射文件
	 * @param tableName 表名称 格式：t_tablename
	 * @throws SQLException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void buildMapper(String tableName)throws SQLException, IOException, TemplateException{
		IGenerator mg 		 	= new MapperGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tableName);
		mg.build(tm,true);
	}
	/**
	 * 生成Dao相关的类，baseDao imipl mapper
	 * @param tablename
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void generatorDaoImpl(String tablename) throws TemplateException, IOException, SQLException{
		buildBaseImplDao();
		buildDaoImpl(tablename,true);
		buildMapper(tablename);
	}
	/////service impl
	/**
	 * 生成业务接口实现 类
	 * @param tn
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildServiceImpl(String tn) throws TemplateException, IOException, SQLException{
		IGenerator svrImplg     = new ServiceImplGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		svrImplg.build(tm, true);
	}
	/**
	 * 生成业务接口实现类扩展
	 * @param tn
	 * @throws TemplateException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildServiceImplExt(String tn) throws TemplateException, IOException, SQLException{
		IGenerator extservice = new ServiceImplGeneratorExt();
		TableModel tm = dao.queryTableInfoByTableName(tn);
		extservice.build(tm, true);
	}
	public void buildBusinessImplement() throws TemplateException, IOException, SQLException{
		List<String> list = dao.getTables();
		if(list == null || list.size() == 0){
			throw new NullPointerException("指定数据库为空！");
		}
		buildBaseImplDao();
		for(int i=0;i<list.size();i++){
			buildDaoImpl(list.get(i), true);
			buildServiceImpl(list.get(i));
			buildServiceImplExt(list.get(i));
			buildMapper(list.get(i));
		}
	}
	/**
	 * 生成外部接口
	 */
	public void buildFrameworkService(String tableName) throws TemplateException, IOException, SQLException{
		FrameworkServiceGenerator fg = new FrameworkServiceGenerator();
		TableModel tm = dao.queryTableInfoByTableName(tableName);
		fg.build(tm, true);
	}
	public void buildFrameworkService() throws TemplateException, IOException, SQLException{
		List<String> list = dao.getTables();
		if(list == null || list.size() == 0){
			throw new NullPointerException("指定数据库为空！");
		}
		for(int i=0;i<list.size();i++){
			buildFrameworkService(list.get(i));
		}
	}
	
}

