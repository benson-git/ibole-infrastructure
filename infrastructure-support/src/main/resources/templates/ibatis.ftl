<?xml version="1.0" encoding="gbk"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="${model.packageName}.${model.className?lower_case}">
  	<!--公用所有字段的sql-->
  	<sql id="selector">
  		<#list  model.cols as prop>
  			${prop.colName}<#if prop_has_next>,</#if>
  		</#list>
  	</sql>
  	<select id="queryAll" resultType="${model.className}">
  		select <include refid="selector"/> from ${model.tableName}
  	</select>
  	
  	<select id="get" parameterType="${model.className}" resultType="${model.className}">
  		select <include refid="selector"/> 
  		 from
  		${model.tableName}
  		<#list model.cols as col>
  			<#if col.isPk>
  				<trim prefix="WHERE" prefixOverrides="AND |OR ">
  			     ${col.colName} = ${r'#{'}${col.fieldName}${r'}'}
  			    </trim>
  			</#if>
  		</#list>
  	</select>
  	<delete id="delete" parameterType="${model.className}">
  		delete from ${model.tableName} 
  		<#list model.cols as col>
  			<#if col.isPk>
  				<trim prefix="WHERE" prefixOverrides="AND |OR ">
  			     ${col.colName} = ${r'#{'}${col.fieldName}${r'}'}
  			    </trim>
  			</#if>
  		</#list>
  	</delete>
  	<insert id="save" useGeneratedKeys="true" keyProperty="<#list model.pks as pk>${pk}<#if pk_has_next>,</#if></#list>" parameterType="${model.className}">
  		insert into ${model.tableName} 
  		(
  			<#list model.cols as col>
  				${col.colName}<#if col_has_next>,</#if>
  			</#list>
  		)
  		values
  		(
  			<#list model.cols as col>
  				${r'#{'}${col.fieldName}${r'}'}<#if col_has_next>,</#if>
  			</#list>
  		)
  	</insert>
  	<update id="update" parameterType="${model.className}">
  		update ${model.tableName} 
  		<set>
  			<#list model.cols as col>
  				<#if !col.isPk>
  					<if test="${col.fieldName} != null and ${col.fieldName} != ''">
  					${col.colName}=${r'#{'}${col.fieldName}${r'}'}<#if col_has_next>,</#if>
  					</if>
  				</#if>
  			</#list>
  		</set>
  		where
  		    <#list model.cols as col>
  		    	<#list model.pks as pk>
  		    		<#if col.colName = pk>${col.colName}=${r'#{'}${col.fieldName}${r'}'}<#if pk_has_next> and </#if></#if>
  		    	</#list>
  		    </#list>
  	</update>
  	<select id="list" parameterType="${model.className}" resultType="${model.className}">
  		select <include refid="selector"/> 
  		 from
  		${model.tableName}
  		<trim prefix="WHERE" prefixOverrides="AND |OR ">
  		<#list model.cols as col>
  			<if test="${col.fieldName} != null and ${col.fieldName} != ''">
	  		
	  			<#assign operator="="/>
	  			<#assign percentpre=""/>
	  			<#assign percentpos=""/>
	  			<#if col.jdbcType = "java.lang.String" && !col.isPk>
	  				<#assign operator="like"/>
	  				<#assign percentpre="concat('%',"/>
	  				<#assign percentpos=",'%')"/>
	  			</#if>
	  			<#if col_index != 0> and </#if>${col.colName} ${operator} ${percentpre}${r'#{'}${col.fieldName}${r'}'}${percentpos}
	  		</if>
  		</#list>
  		</trim>
  	</select>
  </mapper>