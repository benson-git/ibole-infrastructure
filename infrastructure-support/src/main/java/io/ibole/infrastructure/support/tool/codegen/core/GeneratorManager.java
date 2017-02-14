package io.ibole.infrastructure.support.tool.codegen.core;


import io.ibole.infrastructure.support.tool.codegen.dao.MysqlMetaDataDao;
import io.ibole.infrastructure.support.tool.codegen.model.TableModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import freemarker.template.TemplateException;

public class GeneratorManager {

  /**
   * 数据库访问对象
   */
  MysqlMetaDataDao dao = new MysqlMetaDataDao();
  IGenerator bussG = new BusinessServiceGenerator();
  /**
   * 生成pojo的实现
   */
  IGenerator pg = new PojoGenerator();
  /**
   * 生成mapper的实现
   */
  IGenerator mg = new MapperGenerator();
  /**
   * 生成dao接口的实现
   */
  IGenerator dg = new DaoGenerator();
  /**
   * 生成daoimpl的实现
   */
  IGenerator dig = new DaoImplGenerator();
  /**
   * 生成basedao的实现
   */
  IGenerator bdg = new BaseDaoGenerator();
  /**
   * 生成basedaoimpl的实现
   */
  IGenerator bidg = new BaseDaoImplGenerator();
  /**
   * 生成存储过程的实现
   */
  IGenerator prog = new ProcedureGenerator();
  IGenerator svrg = new ServiceGenerator();
  IGenerator svrImplg = new ServiceImplGenerator();
  IGenerator baseEntityG = new BaseEntityGenerator();
  IGenerator entityExt = new PojoGeneratorExt();

  /**
   * 根据表名生成dao接口,daoimpl,mapper pojo
   * 
   * @param tableName 表名称 格式：t_tablename
   * @param cover 是否覆盖原文件
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  private void generator(String tableName, boolean cover)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorPojo(tm, cover);
    generatorMapper(tableName, tm, cover);
    // generatorDao(tableName, tm,cover);
    generatorDaoImpl(tableName, cover);
    generatorService(tableName, cover);
    generatorServiceImpl(tableName, cover);

  }

  /**
   * 根据表名生成dao接口 如果文件已经存在，会覆盖原文件
   * 
   * @param tableName 表名称 格式：t_tablename
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  private void generator(String tableName) throws SQLException, IOException, TemplateException {
    generator(tableName, true);
  }

  /**
   * 根据表名生成daoimpl 如果文件已经存在，会覆盖原文件
   * 
   * @param tableName 表名称 格式：t_tablename
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorDaoImpl(String tableName)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorDaoImpl(tableName, tm, true);
  }

  /**
   * 根据表名生成daoimpl
   * 
   * @param tableName 表名称 格式：t_tablename
   * @param cover 是否覆盖原文件
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorDaoImpl(String tableName, boolean cover)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorDaoImpl(tableName, tm, cover);
  }

  /**
   * 生成Dao相关的类，baseDao imipl mapper
   * 
   * @param tablename
   * @throws TemplateException
   * @throws IOException
   * @throws SQLException
   */
  public void generatorDaoImplAllByTableName(String tablename)
      throws TemplateException, IOException, SQLException {
    generatorBaseImplDao();
    generatorDaoImpl(tablename);
    generatorMapper(tablename);
  }

  private void generatorDaoImpl(String tableName, TableModel tm, boolean cover)
      throws SQLException, IOException, TemplateException {
    dig.build(tm, cover);
  }

  /**
   * 根据表名生成pojo类
   * 
   * @param tableName 表名称 格式：t_tablename
   * @param cover 是否覆盖已存在的文件
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorPojo(String tableName, boolean cover)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorPojo(tm, cover);
  }

  /**
   * 根据表名生成pojo类 如果文件已经存在，会覆盖原文件
   * 
   * @param tableName 表名称 格式：t_tablename
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorPojo(String tableName) throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorPojo(tm, true);
  }

  private void generatorPojo(TableModel tm, boolean cover)
      throws SQLException, IOException, TemplateException {
    pg.build(tm, cover);
    entityExt.build(tm, cover);
  }

  /**
   * 根据表名生成mysql 映射文件
   * 
   * @param tableName 表名称 格式：t_tablename
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorMapper(String tableName)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorMapper(tableName, tm, true);
  }

  /**
   * 根据表名生成mysql 映射文件
   * 
   * @param tableName 表名称 格式：t_tablename
   * @param cover 如果文件已存在，是否覆盖
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorMapper(String tableName, boolean cover)
      throws SQLException, IOException, TemplateException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    generatorMapper(tableName, tm, cover);
  }

  private void generatorMapper(String tableName, TableModel tm, boolean cover)
      throws SQLException, IOException, TemplateException {
    mg.build(tm, cover);
  }

  /**
   * 生成BaseDao
   * 
   * @param cover 是否覆盖
   * @throws TemplateException
   * @throws IOException
   */
  public void generatorBaseDao(boolean cover) throws TemplateException, IOException {
    bdg.build(null, cover);
  }

  /**
   * 生成BaseDao
   * 
   * @throws TemplateException
   * @throws IOException
   */
  private void generatorBaseDao() throws TemplateException, IOException {
    generatorBaseDao(true);
  }

  /**
   * 生成baseDaoimpl
   * 
   * @throws TemplateException
   * @throws IOException
   */
  public void generatorBaseImplDao() throws TemplateException, IOException {
    generatorBaseImplDao(true);
  }

  /**
   * 生成baseDaoimpl
   * 
   * @param cover 是否覆盖
   * @throws TemplateException
   * @throws IOException
   */
  public void generatorBaseImplDao(boolean cover) throws TemplateException, IOException {
    bidg.build(null, cover);
  }

  /**
   * 生成baseDao baseDaoimpl
   * 
   * @param cover 是否覆盖
   * @throws TemplateException
   * @throws IOException
   */
  public void generatorBase(boolean cover) throws TemplateException, IOException {
    generatorBaseDao(cover);
    generatorBaseImplDao(cover);
  }

  /**
   * 生成baseDao baseDaoimpl 如果已经存在会覆盖原文件
   * 
   * @throws TemplateException
   * @throws IOException
   */
  public void generatorBase() throws TemplateException, IOException {
    generatorBase(true);
  }

  public void generatorProcedure(String tableName)
      throws SQLException, TemplateException, IOException {
    generatorProcedure(tableName, true);
  }

  public void generatorAllProcedure() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    for (int i = 0; i < list.size(); i++)
      generatorProcedure(list.get(i));
  }

  public void generatorProcedure(String tableName, boolean cover)
      throws SQLException, TemplateException, IOException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    prog.build(tm, cover);
  }

  public void generatorService(String tableName, boolean cover)
      throws SQLException, TemplateException, IOException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    svrg.build(tm, cover);
  }

  public void generatorService(String tableName)
      throws SQLException, TemplateException, IOException {
    generatorService(tableName, true);
  }

  public void generatorServiceImpl(String tableName, boolean cover)
      throws SQLException, TemplateException, IOException {
    TableModel tm = dao.queryTableInfoByTableName(tableName);
    svrImplg.build(tm, cover);
  }

  public void generatorServiceImpl(String tableName)
      throws SQLException, TemplateException, IOException {
    generatorServiceImpl(tableName, true);
  }

  public void generatorBaseEntity(String tableName, boolean cover)
      throws SQLException, TemplateException, IOException {
    baseEntityG.build(null, cover);
  }

  public void generatorBaseEntity() throws SQLException, TemplateException, IOException {
    generatorBaseEntity(null, true);
  }

  /**
   * 整库生成
   * 
   * @throws SQLException
   * @throws IOException
   * @throws TemplateException
   */
  public void generatorAll() throws SQLException, IOException, TemplateException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    generatorBaseDao();
    generatorBaseImplDao();
    generatorBaseEntity();
    for (int i = 0; i < list.size(); i++)
      generator(list.get(i), true);
    // generatorAllProcedure();
  }

  public void generatorAllEntity() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    generatorBaseEntity();
    for (int i = 0; i < list.size(); i++)
      generatorPojo(list.get(i), true);
  }

  public void generatorAllDao() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    // generatorBaseDao();
    generatorBaseImplDao();
    for (int i = 0; i < list.size(); i++) {
      // generatorDao(list.get(i),true);
      generatorDaoImpl(list.get(i), true);
      generatorMapper(list.get(i), true);
    }
    // generatorAllProcedure();
  }

  public void generatorAllService() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    // generatorBusinessService(list.get(0));
    for (int i = 0; i < list.size(); i++) {
      generatorSericeByTName(list.get(i));
    }
  }

  public void generatorAllInterface() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    generatorBusinessService(list.get(0));
    for (int i = 0; i < list.size(); i++) {
      generatorService(list.get(i), true);
    }
  }

  public void generatorAllBusinessWithoutInterface()
      throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    generatorBaseImplDao(true);
    for (int i = 0; i < list.size(); i++) {
      generatorDaoImplAllByTableName(list.get(i));
    }
  }

  public void generatorAllInterfaceImpl() throws SQLException, TemplateException, IOException {
    List<String> list = dao.getTables();
    if (list == null || list.size() == 0) {
      throw new NullPointerException("指定数据库为空！");
    }
    generatorBaseImplDao(true);
    for (int i = 0; i < list.size(); i++) {
      generatorMapper(list.get(i), true);
      generatorDaoImpl(list.get(i), true);
      generatorServiceImpl(list.get(i), true);
    }
    generatorAllProcedure();
  }

  public void generatorBusinessService(String tn)
      throws TemplateException, IOException, SQLException {
    TableModel tm = dao.queryTableInfoByTableName(tn);
    bussG.build(tm, true);
  }

  ServiceImplGeneratorExt extservice = new ServiceImplGeneratorExt();

  public void generatorServiceImplExt(String tn)
      throws TemplateException, IOException, SQLException {
    TableModel tm = dao.queryTableInfoByTableName(tn);
    extservice.build(tm, true);
  }

  /**
   * 生成 serviceimpl serviceipmlExt
   * 
   * @param tn
   * @throws TemplateException
   * @throws IOException
   * @throws SQLException
   */
  public void generatorSericeByTName(String tn)
      throws TemplateException, IOException, SQLException {
    // generatorService(tn);
    generatorServiceImpl(tn);
    generatorServiceImplExt(tn);
  }



}
