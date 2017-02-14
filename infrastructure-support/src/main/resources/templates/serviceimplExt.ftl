package ${config.basePackage}.service;

import ${config.basePackage}.entity.${model.pojoName}Entity;
import java.util.List;

import org.springframework.stereotype.Service;

/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 *   B_AttachmentInfoService 真实的业务实现类，允许修改
 *   
 * ********************************************************************************************************************/
@Service("${model.pojoName?uncap_first}Service") public class ${model.pojoName}Service extends ${model.pojoName}ServiceBase<${model.pojoName}Entity> <#--implements I${model.pojoName}Interface<${model.pojoName}Entity-->{
	
}