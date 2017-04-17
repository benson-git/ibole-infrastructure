/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ibole.infrastructure.persistence.db.mybatis;

import com.github.ibole.infrastructure.persistence.db.exception.DataBaseAccessException;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.Pager;
import com.sun.rowset.CachedRowSetImpl;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * MyBatis的Dao代理类.
 * @author bwang
 *
 */
public class BaseDao<T> extends SqlSessionDaoSupport {

  private static final String COUNT = "_Count";
  
  //从spring注入原有的sqlSessionTemplate
  //@Autowired
  //private SqlSessionTemplate sqlSessionTemplate;

  public int save(String key, T entity) throws DataBaseAccessException {
      try {
          return getSqlSession().insert(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " save exception:", e);
      }
  }

  public int saveObject(String key, Object entity) throws DataBaseAccessException {
      try {
          return getSqlSession().insert(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " save exception:", e);
      }
  }

  public int save(String key, List<T> entity) throws DataBaseAccessException {
      try {

          return getSqlSession().insert(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " save exception:", e);
      }
  }

  public int update(String key, Object entity) throws DataBaseAccessException {
      try {
          return getSqlSession().update(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " update exception:", e);
      }
  }

  public int update(String key, Map<Object, Object> entity) throws DataBaseAccessException {
      try {

          return getSqlSession().update(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " update exception:", e);
      }
  }

  public int delete(String key, Serializable id) throws DataBaseAccessException {
      try {
          return getSqlSession().delete(key, id);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " delete exception:", e);
      }
  }

  public int delete(String key, Object entity) throws DataBaseAccessException {
      try {
          return getSqlSession().delete(key, entity);
      } catch (Exception e) {
          throw new DataBaseAccessException(getClass().getName() + " delete exception:", e);
      }
  }

  public List<T> getAll(String key) {
      try {
          SqlSession session = getSqlSession();
          return session.selectList(key);
      } catch (Exception e) {
          logger.error(getClass().getName() + " getAll exception and key is" + key, e);
          return null;
      }
  }

  @SuppressWarnings("unchecked")
  public T get(String key, Object params) {
      try {
          return (T) getSqlSession().selectOne(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " get exception and key is" + key, e);
          return null;
      }
  }

  public <K, V> Map<K, V> getMap(String statement, Object parameter, String mapKey) {
    return getSqlSession().selectMap(statement, parameter, mapKey);
  }

  public Object getObject(String key, Object params) {
      try {
          return getSqlSession().selectOne(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " get exception and key is" + key, e);
          return null;
      }
  }

  public List<T> getList(String key, Object params) {
      try {
          return getSqlSession().selectList(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " getList exception and key is" + key, e);
          return null;
      }
  }

  @SuppressWarnings("rawtypes")
  public List getObjectList(String key, Object params) {
      try {
          return getSqlSession().selectList(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " getList exception and key is" + key, e);
          return null;
      }
  }

  public List<String> getStringList(String key, Object params) {
      try {
          return getSqlSession().selectList(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " getList exception and key is" + key, e);
          return null;
      }
  }
  
  //Notes: page.getOffset() only used for mysql
  @SuppressWarnings({"rawtypes", "unchecked"})
  public PageList<T> getList(String key, Object params, Pager page) {
    PageList pages = new PageList(page);
      try {
          Integer totalCounts = count(key + COUNT, params);
          // add 最大页数判断
          int pageM = maxPage(totalCounts, page.getPageSize(), page.getPageNumber());
          if (pageM > 0) {
            pages.getPager().setPageNumber(pageM);
          } // end
          if (totalCounts != null && totalCounts.longValue() > 0) {
              List<T> list = getSqlSession().selectList(key, params,
                      new RowBounds(page.getOffset(), page.getPageSize()));
              pages.addAll(list);
              pages.getPager().setTotalCount(totalCounts.longValue());
          }
          return pages;
      } catch (Exception e) {
          logger.error(getClass().getName() + " getList exception and key is" + key, e);
          return null;
      }
  }

  /**
   * 判断是否超出最大页数
   * 
   * @param tcount
   *            总条数
   * @param pageS
   *            每页条数
   * @param pNo
   *            当前页
   * @return int 大于0表示超出最大页数,返回最大页数
   */
  private int maxPage(Integer tcount, Integer pageS, Integer pNo) {
      if(tcount==null)
          return -1;
      int maxPage = tcount % pageS == 0 ? tcount / pageS : tcount / pageS + 1;// 最大页数
      if (maxPage < pNo)
          return maxPage;
      else
          return -1;
  }

  public Integer count(String key, Object params) {
      try {
          return (Integer) getSqlSession().selectOne(key, params);
      } catch (Exception e) {
          logger.error(getClass().getName() + " count exception and key is" + key, e);
          return Integer.valueOf(0);
      }
  }

  public Connection getConnection() {
      try {
          Connection conn = getSqlSession().getConfiguration().getEnvironment().getDataSource().getConnection();
          conn.setAutoCommit(false);
          return conn;
      } catch (SQLException e) {
          return null;
      }

  }

  /**
   * 执行sql语句
   * 
   * @param sql
   * @param params
   *            执行sql时的参数
   * @throws DaoException
   */
  public PreparedStatement execute(String sql, Object[] params, Connection conn) throws Exception {

      PreparedStatement stmt = null;
      stmt = conn.prepareStatement(sql);
      fillParams(stmt, params);
      stmt.execute();

      return stmt;
  }

  /**
   * 执行sql语句
   * 
   * @param sql
   * @param params
   *            执行sql时的参数
   * @throws DaoException
   */
  public PreparedStatement execute(String sql, Connection conn) throws Exception {

      PreparedStatement stmt = null;
      stmt = conn.prepareStatement(sql);
      stmt.execute();

      return stmt;
  }

  /**
   * 执行sql语句
   * 
   * @param sql
   * @param params
   *            执行sql时的参数
   * @throws DaoException
   */
  public RowSet queryset(String sql, Object[] params) throws DataBaseAccessException {

      Connection conn = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      CachedRowSetImpl crs = null;
      try {
          conn = getConnection();
          stmt = conn.prepareStatement(sql);
          fillParams(stmt, params);
          rs = stmt.executeQuery();
          //CachedRowSetImpl:
          //1. CachedRowSet 对象是可序列化的
          //2. 保存在其中的数据不会随着数据库和ResultSet的连接的关闭而丢失，可以传递.
          crs = new CachedRowSetImpl();
          //crs.populate(new ResultSetWrapper(rs));
          crs.populate(rs);
      } catch (Exception e) {
          logger.error(sql, e);
          throw new DataBaseAccessException("query error", e);
      } finally {
          releaseConnection(rs, stmt, conn);
      }
      return crs;
  }

  /**
   * 数据库批量处理.
   * 
   * @param sql The sql to be executed
   * @param params The parameters for PreparedStatement 
   * @return  an array of update counts containing one element for each
   * command in the batch. 
   * @throws DataBaseAccessException
   */
  public int[] executeBatch(String sql, Object[][] params) throws DataBaseAccessException {
      Connection connection = null;
      PreparedStatement ps = null;
      try {
          connection = getConnection();
          ps = connection.prepareStatement(sql);
          for (int i = 0; i < params.length; i++) {
              fillParams(ps, params[i]);
              ps.addBatch();
          }
          return ps.executeBatch();
      } catch (Exception e) {
          logger.error(getClass().getName(), e);
          throw new DataBaseAccessException("batch error", e);
      } finally {
          try {
              if (connection != null) {
                  connection.commit();
                  connection.setAutoCommit(true);
              }
          } catch (Exception e) {
              logger.error(getClass().getName(), e);
              throw new DataBaseAccessException("batch error", e);
          }
          releaseConnection(null, ps, connection);
      }
  }

  /**
   * 数据库批量处理
   * 
   * @param sql
   * @param params
   * @return
   * @throws DataBaseAccessException
   */
  public PreparedStatement executeBatch(String sql, Object[][] params, Connection conn) throws Exception {
      PreparedStatement ps = null;

      conn = getConnection();
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
          fillParams(ps, params[i]);
          ps.addBatch();
      }
      ps.executeBatch();

      return ps;

  }

  public void fillParams(PreparedStatement ps, Object[] params) throws DataBaseAccessException {
      if (params != null && params.length != 0) {
          for (int i = 0; i < params.length; i++) {
              try {
                  if (params[i] == null) {
                      ps.setNull(i + 1, Types.VARCHAR);
                  } else {
                      ps.setObject(i + 1, params[i]);
                  }
              } catch (Exception e) {
                  logger.error(getClass().getName(), e);
                  throw new DataBaseAccessException("params error", e);
              }
          }
      }
  }

  public void releaseConnection(ResultSet rs, Statement stmt, Connection connection) throws DataBaseAccessException {
      try {
          if (rs != null) {
              rs.close();
          }
          if (stmt != null) {
              stmt.close();
          }
          if (connection != null) {
              connection.close();
          }
      } catch (Exception e) {
          logger.error(getClass().getName(), e);
          throw new DataBaseAccessException("conncention close error", e);
      }
  }

  public void releaseConnection(ResultSet rs, Statement[] stmt, Connection connection)
          throws DataBaseAccessException {
      try {

          if (rs != null) {
              rs.close();
          }
          if (stmt != null) {
              for (int i = 0; i < stmt.length; i++) {
                  if (stmt[i] != null)
                      stmt[i].close();
              }
          }
          if (connection != null) {
              connection.close();
          }
      } catch (Exception e) {
          logger.error(getClass().getName(), e);
          throw new DataBaseAccessException("conncention close error", e);
      }
  }
  
  public void rollback(Connection conn) throws DataBaseAccessException
  {
      try {
          if(conn!=null)
          conn.rollback();
      } catch (SQLException e) {
          logger.error(getClass().getName(), e);
          throw new DataBaseAccessException("rollback error", e);
      }
  }
  
  public void commit(Connection conn, Statement[] stmt) throws DataBaseAccessException {
      try {
          if (conn != null) {
              conn.commit();
              conn.setAutoCommit(true);
          }
      } catch (Exception e) {
          logger.error(getClass().getName(), e);
          throw new DataBaseAccessException("commit error", e);
      }
      releaseConnection(null, stmt,conn);
  }
}

