package ${model.basePackage}.businessInterface;

import ${model.basePackage}.entity.${model.pojoName}Entity;
import ${model.basePackage}.businessInterface.IBusinessInterface;
import java.util.List;

/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 *   ${model.className} 业务接口
 *   
 * ********************************************************************************************************************/
public interface I${model.className}<${model.pojoName}Entity> extends IBusinessInterface{
	
	/**
	 * 根据id查询实体类[${model.pojoName}Entity]，考虑到有可能会有联合主键所以请传入实体对象。
	 * @param ${model.pojoName}Entity 包含主键值的实体对象
	 * @return   查询到的实体类
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult get${model.pojoName}EntityById(${model.basePackage}.model.InterfaceParams param) throws Exception;
	
	/**
	 * 查询所有的实体
	 * @return List<${model.pojoName}Entity>
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult getAll(${model.basePackage}.model.InterfaceParams param) throws Exception;
	
	/**
	 * 更新实体对象
	 * @param ${model.pojoName}Entity 一定要设置主键
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult update(${model.basePackage}.model.InterfaceParams param) throws Exception;
	
	/**
	 * 根据主键值删除对象
	 * @param ${model.pojoName}Entity
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult delete(${model.basePackage}.model.InterfaceParams param) throws Exception;
	
	/**
	 * 保存实体对象
	 * @param ${model.pojoName}Entity
	 * @throws Exception
	 */
	public ${model.basePackage}.model.InterfaceResult save(${model.basePackage}.model.InterfaceParams param) throws Exception;
}