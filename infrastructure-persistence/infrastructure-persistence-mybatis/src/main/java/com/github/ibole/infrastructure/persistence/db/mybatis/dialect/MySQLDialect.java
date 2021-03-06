package com.github.ibole.infrastructure.persistence.db.mybatis.dialect;


/**
 * Mysql方言.
 * 
 */
public class MySQLDialect extends Dialect {

	public boolean supportsLimitOffset() {
		return true;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, String.valueOf(offset), limit, String.valueOf(limit));
	}
	
    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limit             分页纪录条数
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset > 0) {
			return sql + " limit " + offsetPlaceholder + "," + limitPlaceholder;
		} else {
			return sql + " limit " + limitPlaceholder;
		}
	}
   /**
	* 将sql转换为总记录数SQL
	* 
	* @param querySqlString SQL语句
    * @return 总记录数的sql
	*/
    @Override
    public String getCountString(String querySqlString) {
        String sql = getNonOrderByPart(querySqlString);
        return "select count(1) from (" + sql + ") as tmp_count";
    }
    
    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始
     */
    public int getFirst(int pageNumber, int pageSize){
      return ((pageNumber - 1) * pageSize);
    }
}
