package ${config.basePackage}.entity;

import ${model.basePackage}.entity.Entity;
/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 *   
 *   该类为自动生成的实体类基类，不能手动修改。
 *   如有需要扩展的字段，请在${model.tableName?cap_first}类中进行修改
 * 
 * ********************************************************************************************************************/
public class ${model.tableName?cap_first}EntityBase  extends Entity{

  <#list model.cols as prop>
  /**
   *	${prop.code}数据库列名
   */
  public final static java.lang.String ${prop.code?upper_case}_FIELD =  "${prop.code}";
  
  /**
   *	${prop.code}字段中文名，用于视图展示
   */
  public final static java.lang.String ${prop.code?upper_case}_LABLE = "${prop.lable}";
  
  /**
   *	${prop.code}对应的java字段名,业务操作使用这个字段。
   */
  private ${prop.jdbcType} ${prop.fieldName?uncap_first};
  
  </#list>
  <#list model.cols as prop>
  /**
   * ${prop.lable} getter
   */
  public ${prop.jdbcType} get${prop.fieldName?cap_first}(){
    return ${prop.fieldName?uncap_first};
  }
  /**
   * ${prop.lable} setter
   * @param ${prop.code}
   */  
  public void set${prop.fieldName?cap_first}(${prop.jdbcType} ${prop.fieldName?uncap_first}){
    this.${prop.fieldName?uncap_first} = ${prop.fieldName?uncap_first};
  }
 
  </#list>
}