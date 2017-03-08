package com.github.ibole.infrastructure.support.tool.codegen.pojo;

public class ColPojo implements Comparable<ColPojo> {

	private String colType;
	private String dbTypeName;
	private String code;
	private String lable;
	private String colName;
	private String fieldName;
	private String jdbcType;
	private int colLength;
	private boolean isPk;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	//дtoStringIддtostringдtrue;
	private boolean overwriteToString = false;
	
	public ColPojo(){
	}
	
	public int getColLength() {
		return colLength;
	}

	public void setColLength(int colLength) {
		this.colLength = colLength;
	}

	public String getDbTypeName() {
		return dbTypeName;
	}

	public void setDbTypeName(String dbTypeName) {
		this.dbTypeName = dbTypeName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ColPojo(String colType,String dbTypeName,int colLength, String colName,String jdbcType,String filedName) {
		super();
		this.colType = colType;
		this.colLength = colLength;
		this.dbTypeName = dbTypeName;
		this.colName = colName;
		this.jdbcType = jdbcType;
		this.fieldName = filedName;
	}
	public void setIsPk(boolean ispk){
		this.isPk = ispk;
	}
	public boolean getIsPk(){
		return this.isPk;
	}
	public ColPojo(String colType,String dbTypeName,int colLength, String colName,String jdbcType,String fName, boolean overwriteToString) {
		this(colType, dbTypeName, colLength, colName,jdbcType,fName);
		this.overwriteToString = overwriteToString;
	}

	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public boolean isOverwriteToString() {
		return overwriteToString;
	}

	public String getJdbcType() {
		return this.jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	@Override
	public String toString() {
		return "ColPojo [colType=" + colType + ", dbTypeName=" + dbTypeName + ", code=" + code + ", lable=" + lable
				+ ", colName=" + colName + ", fieldName=" + fieldName + ", jdbcType=" + jdbcType + ", colLength="
				+ colLength + ", isPk=" + isPk + ", overwriteToString=" + overwriteToString + "]";
	}

	@Override
	public int compareTo(ColPojo o) {
		return (this.getFieldName().compareTo(o.getFieldName()));
	}
}
