package com.github.ibole.infrastructure.support.tool.codegen.model;


import com.github.ibole.infrastructure.support.tool.codegen.pojo.ColPojo;
import com.github.ibole.infrastructure.support.tool.codegen.pojo.TabPojo;

import java.util.List;

public class TableModel {

	private String packageName = "com.whm.test.freemarker.pojo";
	private TabPojo tabPojo = new TabPojo();
	private String pojoName;
	private String className;
	public List<String> getPks(){
		return this.tabPojo.getPks();
	}
	private String basePackage;
	private String pojoFullName;
	
	public String getPojoFullName() {
		return pojoFullName;
	}

	public void setPojoFullName(String pojoFullName) {
		this.pojoFullName = pojoFullName;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getPojoName() {
		return pojoName;
	}

	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}

	public void setCols(List<ColPojo> cols){
		this.tabPojo.setCols(cols);
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	public TabPojo getTabPojo(){
		return this.tabPojo;
	}
	public void setTableName(String name){
		this.tabPojo.setTabName(name);
	}
	public String getPackageName(){
		return packageName;
	}
	public void setPackageName(String pkgName){
		this.packageName = pkgName;
	}
	public String getTableName(){
		return this.tabPojo.getTabName();
	}
	public String getClassName(){
		return className;
	}
	
	public void addColPojo(ColPojo col){
		this.tabPojo.getCols().add(col);
	}
	public boolean colsIsEmpty(){
		return this.tabPojo.getCols().isEmpty();
	}
	public List<ColPojo> getCols(){
		return this.tabPojo.getCols();
	}
	public void buildPks(List<String> pks){
		for(int i=0;i<this.getCols().size();i++){
			ColPojo cp = this.getCols().get(i);
			if(pks.contains(cp.getColName())){
				cp.setIsPk(true);
			}
		}
		tabPojo.setPks(pks);
	}
}
