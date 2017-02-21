package ${model.packageName};

import java.util.List;
import java.util.Map;
/**********************************************************************************************************************
 * 
 *   版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 *   
 * 
 * ********************************************************************************************************************/
public interface  ${model.basedaoname}<T>{
   /**
     * 对对象进行持久化操作，如果成功则返回持久化后的ID
     * 失败则返回null
     * @param obj
     * @return
     */
    void save(T obj) throws Exception;
    
    /**
     * 删除指定id的持久化对象
     * @param id
     */
    void delete(T obj) throws Exception;
    /**
     * 修改指定的持久化对象
     * @param obj
     */
    void update(T obj) throws Exception;
    /**
     * 根据ID返回持久化对象
     * @param id
     * @return 找到则返回，否则返回空
     */
    T getById(T t) throws Exception;
    /**
     * 根据ID返回持久化对象
     * @param id
     * @return 找到则返回，否则返回空
     */
    List<T> getAll() throws Exception;
    /**
     * 返回所有持久化对象
     * @return
     */
    List<T> list(T t) throws Exception;
    
    /**
     * 传入页码对象，进行分页查询
     * @param pn
     * @return
     */
    List<T> list(Map pageMap) throws Exception;
}