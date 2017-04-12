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

package com.github.ibole.infrastructure.persistence.db.mybatis.dialect;

import com.google.common.base.Strings;

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
 * Sql 2005的方言实现.
 * (SQL Server2005有了更方便的分页方法，这就是ROW_NUMBER()函数)
 *
 */
public class SQLServer2005Dialect extends Dialect {

  @Override
  public boolean supportsLimit() {
    return true;
  }
  
  @Override
  public boolean supportsLimitOffset() {
    return true;
  }

  @Override
  public String getLimitString(String sql, int offset, int limit) {
    return getLimitString(sql, offset, limit, Integer.toString(limit));
  }

  /**
   * Add a LIMIT clause to the given SQL SELECT
   * <p/>
   * The LIMIT SQL will look like:
   * <p/>
   * WITH query AS (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as
   * __row_number__, * from table_name) SELECT * FROM query WHERE __row_number__ BETWEEN :offset and
   * :lastRows ORDER BY __row_number__
   *
   * @param querySqlString The SQL statement to base the limit query off of.
   * @param offset Offset of the first row to be returned by the query (zero-based)
   * @param limit Maximum number of rows to be returned by the query
   * @param limitPlaceholder limitPlaceholder
   * @return A new SQL statement with the LIMIT clause applied.
   */
  private String getLimitString(String querySqlString, int offset, int limit,
      String limitPlaceholder) {
    StringBuilder pagingBuilder = new StringBuilder();
    String orderby = getOrderByPart(querySqlString);
    String distinctStr = "";

    String loweredString = querySqlString.toLowerCase();
    String sqlPartString = querySqlString;
    if (loweredString.trim().startsWith("select")) {
      int index = 6;
      if (loweredString.startsWith("select distinct")) {
        distinctStr = "DISTINCT ";
        index = 15;
      }
      sqlPartString = sqlPartString.substring(index);
    }
    pagingBuilder.append(sqlPartString);

    // if no ORDER BY is specified use fake ORDER BY field to avoid errors
    if (Strings.isNullOrEmpty(orderby)) {
      orderby = "ORDER BY CURRENT_TIMESTAMP";
    }

    StringBuilder result = new StringBuilder();
    result.append("WITH query AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ")
        .append(" ROW_NUMBER() OVER (").append(orderby).append(") as __row_number__, ")
        .append(pagingBuilder).append(") SELECT * FROM query WHERE __row_number__ BETWEEN ")
        .append(offset + 1).append(" AND ").append(offset + limit)
        .append(" ORDER BY __row_number__");

    return result.toString();
  }

  @Override
  public String getCountString(String querySqlString) {
    String sql = getNonOrderByPart(querySqlString);

    return "select count(1) from (" + sql + ") as tmp_count";
  }

}
