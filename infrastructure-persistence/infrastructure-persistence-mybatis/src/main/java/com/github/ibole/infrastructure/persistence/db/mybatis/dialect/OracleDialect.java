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
 * Oracle方言.
 *
 */
public class OracleDialect extends Dialect {

  public boolean supportsLimit() {
    return true;
  }

  public boolean supportsLimitOffset() {
    return true;
  }

  public String getLimitString(String sql, int offset, int limit) {
    return getLimitString(sql, offset, String.valueOf(offset), limit, String.valueOf(limit));
  }
  
  public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit,
      String limitPlaceholder) {
    sql = sql.trim();
    boolean isForUpdate = false;
    if (sql.toLowerCase().endsWith(" for update")) {
      sql = sql.substring(0, sql.length() - 11);
      isForUpdate = true;
    }

    StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
    if (offset > 0) {
      pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
    } else {
      pagingSelect.append("select * from ( ");
    }
    pagingSelect.append(sql);
    if (offset > 0) {
      // int end = offset+limit;
      String endString = offsetPlaceholder + "+" + limitPlaceholder;
      pagingSelect.append(" ) row_ ) where rownum_ <= " + endString + " and rownum_ > "
          + offsetPlaceholder);
    } else {
      pagingSelect.append(" ) where rownum <= " + limitPlaceholder);
    }

    if (isForUpdate) {
      pagingSelect.append(" for update");
    }

    return pagingSelect.toString();
  }

  @Override
  public String getCountString(String querySqlString) {
      String sql = getNonOrderByPart(querySqlString);

      return "select count(1) from (" + sql + ") tmp_count";
  }
  
  
  /**
   * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始
   */
  public int getFirst(int pageNumber, int pageSize){
    return ((pageNumber - 1) * pageSize) + 1;
  }
}
