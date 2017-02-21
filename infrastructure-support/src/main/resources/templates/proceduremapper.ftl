<?xml version="1.0" encoding="gbk"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <#include "./precedure_util.ftl"/>
<#assign proNamePrefix="pro_${model.tableName?lower_case}"/>
<#--查询所有记录的存储过程-->
<#assign proNameQueryAll="Get${model.tableName}Collection"/>
<#--根据ID查询存储过程-->
<#assign proNameQueryById="Get${model.tableName}Entity"/>
<#--根据传入条件查询-->
<#assign proNameQueryBycondition="Get${model.tableName}_query_by_condition"/>
<#--保存-->
<#assign proNameSave="Insert${model.tableName}Entity"/>
<#--更新-->
<#assign proNameUpdate="Update${model.tableName}Entity"/>
<#--删除-->
<#assign proNameDelete="Delete${model.tableName}Entity"/>
  <mapper namespace="${config.basePackage}.mapper.${model.className?lower_case}entity">
  	<#--查询所有的记录-->
  	<select id="getAll" parameterType="${model.className}Entity" resultType="${model.className}Entity" statementType="CALLABLE">
		<![CDATA[  
			{call ${proNameQueryAll}()} 
		]]>  
	</select>
    <#--根据ID查询-->
  	<select id="get" parameterType="${model.className}Entity" resultType="${model.className}Entity" statementType="CALLABLE">
		<![CDATA[  
			{call ${proNameQueryById}(${pkParamIn})} 
		]]>  
	</select>
  	<delete id="delete" parameterType="${model.className}Entity" statementType="CALLABLE">
  		<![CDATA[  
			{call ${proNameDelete}(${pkParamIn})} 
		]]> 
  	</delete>
  	<insert id="save" useGeneratedKeys="true" keyProperty="<#list model.pks as pk>${pk}<#if pk_has_next>,</#if></#list>" parameterType="${model.className}Entity" statementType="CALLABLE">
  		<![CDATA[  
			{call ${proNameSave}(${paramInALL})} 
		]]>  
  	</insert>	
  	<update id="update" parameterType="${model.className}Entity" statementType="CALLABLE">
  		<![CDATA[  
			{call ${proNameUpdate}(${paramInALL})} 
		]]>  
  	</update>
  	<!--<select id="list" parameterType="${model.className}" resultType="${model.className}" statementType="CALLABLE">
  		<![CDATA[  
			{call ${proNameQueryBycondition}(${paramInWithoutPk})} 
		]]>  
  	</select>
  	-->
  </mapper>