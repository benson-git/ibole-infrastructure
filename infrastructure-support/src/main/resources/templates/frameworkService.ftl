package ${model.packageName};
<#assign daoName>${model.pojoName}DBO</#assign>
import ${config.basePackage}.entity.${model.pojoName}Entity;
import ${config.basePackage}.dao.${model.pojoName}DBO;
import ${config.basePackage}.businessInterface.I${model.pojoName}Interface;
import ${model.basePackage}.model.InterfaceResult;
import ${model.basePackage}.model.InterfaceParams;
import java.util.List;

/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016，深圳市拓润计算机软件开发有限公司
 *   B_AttachmentInfoService 业务实现类，不允许修改
 *   
 * ********************************************************************************************************************/
public class ${model.className}FrameworkService implements I${model.pojoName}Interface{
	
	private ${model.pojoName}Service ${model.pojoName?uncap_first}Service; 
	
	public void set${model.pojoName}Service(${model.pojoName}Service ${model.pojoName?uncap_first}Service){
		this.${model.pojoName?uncap_first}Service = ${model.pojoName?uncap_first}Service;
	}
	/**
	 * 根据id查询实体类[${model.pojoName}Entity]，考虑到有可能会有联合主键所以请传入实体对象。
	 * @param ${model.pojoName}Entity 包含主键值的实体对象
	 * @return   查询到的实体类
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult get${model.pojoName}EntityById(${model.basePackage}.model.InterfaceParams param) throws Exception{
		InterfaceResult result = new InterfaceResult();
		${model.pojoName} ${model.pojoName?lower_case} = (${model.pojoName})param.getEntity();
		result.setEntity(${model.pojoName?lower_case});
		return result;
	}
	/**
	 * 查询所有的实体
	 * @return List<${model.pojoName}Entity>
	 * @throws Exception
	 */
	public InterfaceResult getAll(InterfaceParams param) throws Exception;
		InterfaceResult result = new InterfaceResult();
		List<${model.pojoName}> list = ${model.pojoName?uncap_first}Service.getAll();
		result.setList(list);
		return result;
	}
	/**
	 * 更新实体对象
	 * @param ${model.pojoName}Entity 一定要设置主键
	 * @throws Exception
	 */
	public InterfaceResult update(InterfaceParams param) throws Exception{
		InterfaceResult result = new InterfaceResult();
		${model.pojoName} ${model.pojoName?lower_case} = (${model.pojoName})param.getEntity();
		${model.pojoName?uncap_first}Service.update(${model.pojoName?lower_case});
		return result;
	}
	/**
	 * 根据主键值删除对象
	 * @param ${model.pojoName}Entity
	 * @throws Exception
	 */
	public InterfaceResult delete(InterfaceParams param) throws Exception{
		InterfaceResult result = new InterfaceResult();
		${model.pojoName?uncap_first}Service.delete(${model.pojoName?lower_case});
		return result;
	}
	
	<#--public List<${model.pojoName}Entity> getBy${model.pojoName}(${model.pojoName} ${model.pojoName?lower_case}) throws Exception{
		return ${daoName?uncap_first}.list(${model.pojoName?lower_case});
	}
	public ${model.pojoName}Entity getById(${model.pojoName} ${model.pojoName?lower_case}) throws Exception{
		return ${daoName?uncap_first}.getById(${model.pojoName?lower_case});
	}
	-->
	/**
	 * 保存实体对象
	 * @param ${model.pojoName}Entity
	 * @throws Exception
	 */
	public InterfaceResult save(InterfaceParams param) throws Exception{
		InterfaceResult result = new InterfaceResult();
		${model.pojoName?uncap_first}Service.save(${model.pojoName?lower_case});
		return result;
	}
}