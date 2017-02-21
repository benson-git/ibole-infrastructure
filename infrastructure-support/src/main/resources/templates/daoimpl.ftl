package ${config.basePackage}.dao;


import ${config.basePackage}.entity.${model.pojoName}Entity;
<#--import ${config.basePackage}.dao.${model.pojoName}Dao;-->

/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 *
 *   ${model.pojoName}数据访问类
 * 
 * ********************************************************************************************************************/
public class ${model.className} extends ${config.baseDaoImplName}<${model.pojoName}Entity> <#--implements ${model.pojoName}Dao<${model.pojoName}>-->{

}