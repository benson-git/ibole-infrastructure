package ${config.basePackage}.service;
<#assign daoName>${model.pojoName}DBO</#assign>
import ${config.basePackage}.entity.${model.pojoName}Entity;
import ${config.basePackage}.dao.${model.pojoName}DBO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016，深圳市拓润计算机软件开发有限公司
 *   B_AttachmentInfoService 业务实现类，不允许修改
 *   
 * ********************************************************************************************************************/
public abstract class ${model.className}Base<${model.pojoName}Entity> <#--implements I${model.pojoName}Interface<${model.pojoName}Entity-->{
	
	@Autowired      
	@Qualifier("${daoName?uncap_first}")     
	protected ${daoName}<${model.pojoName}Entity> ${daoName?uncap_first};
	
	public void set${daoName}(${daoName}<${model.pojoName}Entity> ${daoName?uncap_first}){
		this.${daoName?uncap_first} = ${daoName?uncap_first};
	}
	public ${model.pojoName}Entity get${model.pojoName}EntityById(${model.pojoName}Entity ${model.pojoName?lower_case}) throws Exception{
		return ${daoName?uncap_first}.getById(${model.pojoName?lower_case});
	}
	public List<${model.pojoName}Entity> getAll() throws Exception{
		return ${daoName?uncap_first}.getAll();
	}
	
	public void update(${model.pojoName}Entity ${model.pojoName?lower_case}) throws Exception{
		${daoName?uncap_first}.update(${model.pojoName?lower_case});
	}
	
	public void delete(${model.pojoName}Entity ${model.pojoName?lower_case}) throws Exception{
		${daoName?uncap_first}.delete(${model.pojoName?lower_case});
	}
	
	<#--public List<${model.pojoName}Entity> getBy${model.pojoName}(${model.pojoName} ${model.pojoName?lower_case}) throws Exception{
		return ${daoName?uncap_first}.list(${model.pojoName?lower_case});
	}
	public ${model.pojoName}Entity getById(${model.pojoName} ${model.pojoName?lower_case}) throws Exception{
		return ${daoName?uncap_first}.getById(${model.pojoName?lower_case});
	}
	-->
	public void save(${model.pojoName}Entity ${model.pojoName?lower_case}) throws Exception{
		${daoName?uncap_first}.save(${model.pojoName?lower_case});
	}
}