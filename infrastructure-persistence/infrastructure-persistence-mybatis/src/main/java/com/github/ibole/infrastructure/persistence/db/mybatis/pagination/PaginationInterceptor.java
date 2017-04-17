/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.infrastructure.persistence.db.mybatis.pagination;

import com.github.ibole.infrastructure.persistence.db.mybatis.dialect.Dialect;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.Pager;
import com.github.ibole.infrastructure.persistence.pagination.model.PagingCriteria;
import com.github.ibole.infrastructure.persistence.pagination.model.SearchField;
import com.github.ibole.infrastructure.persistence.pagination.model.SortField;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/



/**
 * 用来处理mybatis对mysql分页的转换.
 * 
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
    Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {

  private final static Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class
      .getName());
  private final static int MAPPED_STATEMENT_INDEX = 0;
  private final static int PARAMETER_INDEX = 1;
  private final static int ROWBOUNDS_INDEX = 2;
  private static String sqlRegex = "[*]";
  private Dialect dialect;

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Object intercept(Invocation invocation) throws Throwable {
    Object[] queryArgs = invocation.getArgs();
    MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
    Object parameter = queryArgs[PARAMETER_INDEX];
    final RowBounds oldRow = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
    // the need for paging intercept.
    boolean interceptor = ms.getId().matches(sqlRegex);

    if (oldRow.getOffset() == RowBounds.NO_ROW_OFFSET
        && oldRow.getLimit() == RowBounds.NO_ROW_LIMIT && !interceptor) {
      return invocation.proceed();
    }
    
    final Executor executor = (Executor) invocation.getTarget();
    //obtain paging information.
    final PagingCriteria pageRequest = interceptor
            ? PagingParametersFinder.instance.findCriteria(parameter)
            : PagingCriteria.getDefaultCriteria();
            
    final RowBounds rowBounds = (interceptor) ? offsetPaging(oldRow, pageRequest) : oldRow;
    int offset = rowBounds.getOffset();
    int limit = rowBounds.getLimit();
    if (dialect.supportsLimit()
        && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
      BoundSql boundSql = ms.getBoundSql(parameter);
      String sql = boundSql.getSql().trim(); 
      Integer count = getCount(fileterSql(sql, pageRequest), executor, ms, rowBounds, boundSql, parameter, dialect);
      String newSql = sortSql(fileterSql(sql, pageRequest), pageRequest);
      if (dialect.supportsLimitOffset()) {
        newSql = dialect.getLimitString(newSql, offset, limit);
        offset = RowBounds.NO_ROW_OFFSET;
      } else {
        newSql = dialect.getLimitString(newSql, 0, limit);
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Pagination sql is :[" + newSql + "]");
      }
      limit = RowBounds.NO_ROW_LIMIT;
      queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
      BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, newSql);
      MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
      queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
      
      return new PageList((List) invocation.proceed(), new Pager(pageRequest.getPageNumber(),
          pageRequest.getDisplaySize(), count));
    }
    
    return invocation.proceed();
    
  }
  
  private Integer getCount(String sql, Executor executor, MappedStatement ms, RowBounds rowBounds, BoundSql boundSql, Object parameter, Dialect dialect) throws SQLException{
    Integer count = 0;
    Cache cache = ms.getCache();
    if(cache != null && ms.isUseCache() && ms.getConfiguration().isCacheEnabled()){
        CacheKey cacheKey = executor.createCacheKey(ms,parameter, rowBounds, boundSql);
        count = (Integer)cache.getObject(cacheKey);
        if(count == null){
            count = SqlHelper.getCount(sql, ms, executor.getTransaction(),parameter,boundSql,dialect);
            cache.putObject(cacheKey, count);
        }
    }else{
        count = SqlHelper.getCount(sql, ms, executor.getTransaction(),parameter,boundSql,dialect);
    }
    return count;
  }
  
  /**
   * Set the paging information,to RowBuounds.
   *
   * @param rowBounds rowBounds.
   * @return rowBounds.
   */
  private static RowBounds offsetPaging(RowBounds rowBounds, PagingCriteria pageRequest) {
      // rowBuounds has offset.
      if (rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET) {
          if (pageRequest != null) {
              return new RowBounds(pageRequest.getDisplayStart(), pageRequest.getDisplaySize());
          }
      }
      return rowBounds;
  }
  
  private static String fileterSql(String sql, PagingCriteria pagingCriteria){
    boolean order = SqlHelper.containOrder(sql);
    final List<SearchField> searchFields = pagingCriteria.getSearchFields();
    if (searchFields != null && !searchFields.isEmpty()) {
      List<String> whereField = Lists.newArrayList();
      for (SearchField searchField : searchFields) {
        // fix inject sql
        whereField.add(searchField.getField() + SqlHelper.LIKE_CHAR
            + SqlHelper.likeValue(searchField.getValue()));
      }
      boolean where = SqlHelper.containWhere(sql);
      String orderBy = SqlHelper.EMPTY;
      if (order) {
        String[] sqls = sql.split(SqlHelper.ORDER_REGEX);
        sql = sqls[0];
        orderBy = SqlHelper.SQL_ORDER + sqls[1];
      }
      sql =
          String.format((where ? SqlHelper.OR_SQL_FORMAT : SqlHelper.WHERE_SQL_FORMAT), sql,
              Joiner.on(SqlHelper.OR_JOINER).skipNulls().join(whereField), orderBy);
    }
    return sql;
  }
  
  /**
   * Sort and filter sql.
   *
   *
   * @param sql the sql
   * @param pagingCriteria the paging criteria
   * @return the string
   */
  private static String sortSql(String sql, PagingCriteria pagingCriteria) {
    boolean order = SqlHelper.containOrder(sql);
    final List<SortField> sortFields = pagingCriteria.getSortFields();
    if (sortFields != null && !sortFields.isEmpty()) {
      List<String> field_order = Lists.newArrayList();
      for (SortField sortField : sortFields) {
        field_order.add(sortField.getField() + SqlHelper.BLANK_CHAR
            + sortField.getDirection().getDirection());
      }
      return String.format(order ? SqlHelper.SQL_FORMAT : SqlHelper.ORDER_SQL_FORMAT, sql,
          Joiner.on(SqlHelper.DOT_CHAR).skipNulls().join(field_order));
    }
    return sql;
  }

  private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
    BoundSql newBoundSql =
        new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
            boundSql.getParameterObject());
    for (ParameterMapping mapping : boundSql.getParameterMappings()) {
      String prop = mapping.getProperty();
      if (boundSql.hasAdditionalParameter(prop)) {
        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
      }
    }
    return newBoundSql;
  }

  private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
    Builder builder =
        new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
            ms.getSqlCommandType());
    builder.resource(ms.getResource());
    builder.fetchSize(ms.getFetchSize());
    builder.statementType(ms.getStatementType());
    builder.keyGenerator(ms.getKeyGenerator());
    if (ms.getKeyProperties() != null) {
      StringBuilder propertyBuilder = new StringBuilder(20);
      for (String keyProperty : ms.getKeyProperties()) {
        propertyBuilder.append(',').append(keyProperty);
      }
      propertyBuilder = propertyBuilder.replace(0, 1, "");
      builder.keyProperty(propertyBuilder.toString());
    }
    // setStatementTimeout()
    builder.timeout(ms.getTimeout());
    // setStatementResultMap()
    builder.parameterMap(ms.getParameterMap());
    // setStatementResultMap()
    builder.resultMaps(ms.getResultMaps());
    builder.resultSetType(ms.getResultSetType());
    // setStatementCache()
    builder.cache(ms.getCache());
    builder.flushCacheRequired(ms.isFlushCacheRequired());
    builder.useCache(ms.isUseCache());
    return builder.build();
  }

  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  public void setProperties(Properties properties) {
    String dialectClass = properties.getProperty("dialectClass");
    
    String sqlregex = properties.getProperty("sqlRegex");
    if (!Strings.isNullOrEmpty(sqlregex)) {
      sqlRegex = sqlregex;
    }
    
    try {
      dialect = (Dialect) Class.forName(dialectClass).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass,
          e);
    }
    
    logger.info("dialect=" + dialectClass +" sqlRegex="+sqlRegex);
  }

  public static class BoundSqlSqlSource implements SqlSource {
    BoundSql boundSql;

    public BoundSqlSqlSource(BoundSql boundSql) {
      this.boundSql = boundSql;
    }

    public BoundSql getBoundSql(Object parameterObject) {
      return boundSql;
    }
  }
}
