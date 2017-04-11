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

package com.github.ibole.infrastructure.persistence.db.exception;

import java.sql.SQLException;

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
 * @author bwang
 *
 */
public class DataBaseAccessException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -1696456770601931878L;

  public DataBaseAccessException(String msg) {
    super(msg);
    this.sql = "";
  }

  public DataBaseAccessException(Throwable cause) {
    super(cause);
    this.sql = "";
  }

  public DataBaseAccessException(String msg, Throwable cause) {
    super(msg, cause);
    this.sql = "";
  }

  /** SQL that led to the problem */
  private final String sql;

  /**
   * Constructor for PangoSqlException.
   * 
   * @param task name of current task
   * @param sql the offending SQL statement
   * @param ex the root cause
   */
  public DataBaseAccessException(String task, String sql, SQLException ex) {
    super(task + "SQLException for SQL [" + sql + "]; SQL state [" + ex.getSQLState()
        + "]; error code [" + ex.getErrorCode() + "]; " + ex.getMessage(), ex);
    this.sql = sql;
  }

  /**
   * Return the SQLException.
   */
  public SQLException getSQLException() {
    return (SQLException) getCause();
  }

  /**
   * Return the SQL that led to the problem.
   */
  public String getSql() {
    return this.sql;
  }

  /**
   * Return the detail message.
   */
  public String getMessage() {
    return super.getMessage();
  }
}
