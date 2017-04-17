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

import com.google.common.base.Preconditions;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * <p>
 * SQL拼装工具帮助类.
 * </p>
 *
 */
public class SqlHelper {

  private final static Logger logger = LoggerFactory.getLogger(SqlHelper.class.getName());

  /** Order by 正则表达式 */
  public static final String ORDER_BY_REGEX = "order\\s*by[\\w|\\W|\\s|\\S]*";
  /** Xsql Order by 正则表达式 */
  public static final String XSQL_ORDER_BY_REGEX = "/~.*order\\s*by[\\w|\\W|\\s|\\S]*~/";
  /** From正则表达式 */
  public static final String FROM_REGEX = "\\sfrom\\s";

  /** sql contains whre regex. */
  public static final String WHERE_REGEX = "\\s+where\\s+";
  /** sql contains <code>order by </code> regex. */
  public static final String ORDER_REGEX = "order\\s+by";

  public static boolean containOrder(String sql) {
    return containRegex(sql, ORDER_REGEX);
  }

  /*-------------------------------------------------*/

  /** The empty String {@code ""}. */
  public static final String EMPTY = "";
  /** The dot String {@code ","}. */
  public static final String DOT_CHAR = ",";
  /** The blank String {@code " "}. */
  public static final String BLANK_CHAR = " ";
  /** The equal sign String {@code "="} */
  public static final String EQUAL_SIGN_CHAR = "=";
  /**
   * The like String {@code "like"}
   */
  public static final String LIKE_CHAR = " like ";
  private static final String INJECTION_SQL = ".*([';]+|(--)+).*";
  // 使用%%对%进行转义
  private static final String LIKE_FORMATE = "'%%%s%%'";

  /*-------------------------------------------------*/

  public static final String SQL_ORDER = " order by ";
  public static final String OR_JOINER = " or ";
  public static final String OR_SQL_FORMAT = "%s or (%s) %s";
  public static final String WHERE_SQL_FORMAT = "%s where (%s) %s";
  public static final String SQL_FORMAT = "%s, %s";
  public static final String ORDER_SQL_FORMAT = "%s order by %s";

  /*-------------------------------------------------*/


  public static boolean containWhere(String sql) {
    return containRegex(sql, WHERE_REGEX);
  }



  public static boolean containRegex(String sql, String regex) {
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(sql);
    return matcher.find();
  }

  private static int indexOfByRegex(String input, String regex) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    if (m.find()) {
      return m.start();
    }
    return -1;
  }

  /**
   * 去除select 子句，未考虑union的情况
   *
   * @param sql sql
   * @return 删除掉的selcet的子句
   */
  public static String removeSelect(String sql) {
    Preconditions.checkNotNull(sql);
    int beginPos = indexOfByRegex(sql.toLowerCase(), FROM_REGEX);
    Preconditions.checkArgument(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
    return sql.substring(beginPos);
  }

  /**
   * 去除orderby 子句
   *
   * @param sql sql
   * @return 去掉order by sql
   */
  public static String removeOrders(String sql) {
    Preconditions.checkNotNull(sql);
    Pattern p = Pattern.compile(ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(sql);
    StringBuffer sb = new StringBuffer(sql.length());
    while (m.find()) {
      m.appendReplacement(sb, "");
    }
    m.appendTail(sb);
    return sb.toString();
  }

  public static String removeFetchKeyword(String sql) {
    return sql.replaceAll("(?i)fetch", "");
  }

  public static String removeXsqlBuilderOrders(String string) {
    Preconditions.checkNotNull(string);
    Pattern p = Pattern.compile(XSQL_ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(string);
    StringBuffer sb = new StringBuffer(string.length());
    while (m.find()) {
      m.appendReplacement(sb, "");
    }
    m.appendTail(sb);
    return removeOrders(sb.toString());
  }

  /**
   * <p>
   * Capitalizes a String changing the first letter to title case as per
   * {@link Character#toTitleCase(char)}. No other letters are changed.
   * </p>
   * <p/>
   * <p>
   * For a word based algorithm, see
   * {@link org.apache.commons.lang3.text.WordUtils#capitalize(String)}. A {@code null} input String
   * returns {@code null}.
   * </p>
   * <p/>
   * 
   * <pre>
   * StringUtils.capitalize(null)  = null
   * StringUtils.capitalize("")    = ""
   * StringUtils.capitalize("cat") = "Cat"
   * StringUtils.capitalize("cAt") = "CAt"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @return the capitalized String, {@code null} if null String input
   */
  public static String capitalize(String str) {
    if (str == null || (str.length()) == 0) {
      return str;
    }
    return String.valueOf(Character.toTitleCase(str.charAt(0))) + str.substring(1);
  }

  /**
   * Transact sQL injection.
   *
   * @param sql the sql
   * @return the string
   */
  public static String transactSQLInjection(String sql) {
    return sql.replaceAll(INJECTION_SQL, " ");
  }

  /**
   * Like value.
   *
   * @param value the value
   * @return the string
   */
  public static String likeValue(String value) {
    return String.format(LIKE_FORMATE, transactSQLInjection(value));
  }

  public static void main(String[] args) {
    String value = "11";
    System.out.print(likeValue(value));
  }

  /**
   * 查询总纪录数
   *
   * @param mappedStatement mapped
   * @param parameterObject 参数
   * @param boundSql boundSql
   * @param dialect database dialect
   * @return 总记录数
   * @throws java.sql.SQLException sql查询错误
   */
  public static int getCount(String sql, final MappedStatement mappedStatement,
      final Transaction transaction, final Object parameterObject, final BoundSql boundSql,
      Dialect dialect) throws SQLException {
    final String countSql = dialect.getCountString(sql);
    if (logger.isDebugEnabled()) {
      logger.debug("Total count SQL [{}] ", countSql);
      logger.debug("Total count Parameters: {} ", parameterObject);
    }
    Connection connection = transaction.getConnection();
    PreparedStatement countStmt = connection.prepareStatement(countSql);
    DefaultParameterHandler handler =
        new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    handler.setParameters(countStmt);

    ResultSet rs = countStmt.executeQuery();
    int count = 0;
    if (rs.next()) {
      count = rs.getInt(1);
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Total count: {}", count);
    }
    return count;

  }
}
