package com.github.ibole.infrastructure.persistence.db.mybatis.dialect;

import com.github.ibole.infrastructure.persistence.db.mybatis.pagination.SqlHelper;

/**
 * 类似hibernate的Dialect,但只精简出分页部分.
 *
 */
public abstract class Dialect {

  /**
   * 数据库本身是否支持分页当前的分页查询方式 如果数据库不支持的话，则不进行数据库分页.
   *
   * @return true：支持当前的分页查询方式
   */
  public abstract boolean supportsLimit();

  public abstract boolean supportsLimitOffset();

  /**
   * 将sql转换为分页SQL，分别调用分页sql.
   *
   * @param sql SQL语句
   * @param offset 开始条数
   * @param limit 每页显示多少纪录条数
   * @return 分页查询的sql
   */
  public abstract String getLimitString(String sql, int offset, int limit);

  /**
   * 将sql转换为总记录数SQL
   * 
   * @param sql SQL语句
   * @return 总记录数的sql
   */
  public abstract String getCountString(String sql);

  public String getOrderByPart(String sql) {
    String loweredString = sql.toLowerCase();
    int orderByIndex = loweredString.indexOf("order by");
    if (orderByIndex != -1) {
      // if we find a new "order by" then we need to ignore
      // the previous one since it was probably used for a subquery
      return sql.substring(orderByIndex);
    } else {
      return "";
    }
  }

  /**
   * exclude in 'order by ' by sql
   *
   * @param sql sql
   * @return count sql.
   */
  public String getNonOrderByPart(String sql) {
    return SqlHelper.removeOrders(sql);
  }

}
