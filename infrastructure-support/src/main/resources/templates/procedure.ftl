<#assign proNamePrefix="pro_${model.tableName?lower_case}"/>
<#include "./precedure_util.ftl"/>
DELIMITER $$
<#--查询所有记录的存储过程-->
<#assign proNameQueryAll="${proNamePrefix}_query_all"/>
DROP PROCEDURE IF EXISTS ${proNameQueryAll}$$
<#--根据ID查询存储过程-->
<#assign proNameQueryById="${proNamePrefix}_query_by_id"/>
DROP PROCEDURE IF EXISTS ${proNameQueryById}$$
<#--根据传入条件查询-->
<#assign proNameQueryBycondition="${proNamePrefix}_query_by_condition"/>
DROP PROCEDURE IF EXISTS ${proNameQueryBycondition}$$
<#--保存-->
<#assign proNameSave="${proNamePrefix}_save"/>
DROP PROCEDURE IF EXISTS ${proNameSave}$$
<#--更新-->
<#assign proNameUpdate="${proNamePrefix}_update"/>
DROP PROCEDURE IF EXISTS ${proNameUpdate}$$
<#--删除-->
<#assign proNameDelete="${proNamePrefix}_delete"/>
DROP PROCEDURE IF EXISTS ${proNameDelete}$$
<#--查询所有记录的存储过程-->
#查询所有记录的存储过程
CREATE PROCEDURE ${proNameQueryAll}(${pkCondition})
BEGIN
  select ${selector} from ${model.tableName};
END$$
<#--根据ID查询存储过程-->
#根据ID查询存储过程
CREATE PROCEDURE ${proNameQueryById}(${pkCondition})
BEGIN
  select ${selector} from ${model.tableName} ${pkWhere};
END$$
<#--根据传入条件查询-->
#根据传入条件查询
CREATE PROCEDURE ${proNameQueryBycondition}(${allCondition})
BEGIN
  select ${selector} from ${model.tableName} ${allWhereWithoutPk};
END$$
<#--保存-->
#保存
CREATE PROCEDURE ${proNameSave}(${allConditionHavePk})
BEGIN
  insert into ${model.tableName}
  	(${fieldsHavePk})
  values
    (${paramsHavePk});
END$$

<#--更新-->
#更新
CREATE PROCEDURE ${proNameUpdate}(${pkCondition},${allCondition})
BEGIN
DECLARE v_sql VARCHAR(2000);
SET v_sql = 'update ${model.tableName} set ';
<#list model.cols as col>
	<#if !col.isPk>
	IF p_${col.colName} IS NOT NULL AND p_${col.colName} <> '' THEN
	    SET v_sql = CONCAT(v_sql,' ${col.colName} = ',<#if col.dbTypeName ="VARCHAR" || col.dbTypeName ="CHAR">'''',</#if>p_${col.colName}<#if col.dbTypeName ="VARCHAR" || col.dbTypeName ="CHAR">,''''</#if>,',');
	END IF;
	</#if>
</#list>
IF (RIGHT(v_sql,1) = ',') THEN
	SET v_sql = LEFT(v_sql,LENGTH(v_sql)-1);
END IF;
SET v_sql = CONCAT(v_sql,' where ',<#list model.pks as pk><#list model.cols as col><#if col.colName = pk>'${col.colName} = ', p_${col.colName}<#if pk_has_next>,' and ',</#if></#if></#list></#list>);
SET @v_sql = v_sql;
PREPARE stmt FROM @v_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$

<#--删除-->
#删除
CREATE PROCEDURE ${proNameDelete}(${pkCondition})
BEGIN
  DELETE FROM ${model.tableName} ${pkWhere};
END$$

DELIMITER ;