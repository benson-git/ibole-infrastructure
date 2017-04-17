package com.github.ibole.infrastructure.persistence.db.example.dao;

import com.github.ibole.infrastructure.persistence.db.example.domain.Resources;
import com.github.ibole.infrastructure.persistence.db.example.domain.ResourcesCriteria;
import com.github.ibole.infrastructure.persistence.db.mybatis.annotation.MyBatisDao;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.PagingCriteria;

import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface ResourcesDao {
    /**
     * 根据指定的条件删除系统资源的数据库记录,auth_resources
     *
     * @param example 动态SQL条件实例
     */
    int deleteByExample(ResourcesCriteria example);

    /**
     * 根据主键删除系统资源的数据库记录,auth_resources
     *
     * @param id 主键唯一标志
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新写入系统资源的数据库记录,auth_resources
     *
     * @param record 系统资源
     */
    int insert(Resources record);

    /**
     * 动态字段,写入系统资源的数据库记录,auth_resources
     *
     * @param record 系统资源
     */
    int insertSelective(Resources record);

    /**
     * 根据指定的条件查询符合条件的系统资源的数据库记录,auth_resources
     *
     * @param example 动态SQL条件实例
     */
    List<Resources> selectByExample(ResourcesCriteria example);

    /**
     * 根据指定主键获取系统资源的数据库记录,auth_resources
     *
     * @param id 主键唯一标志
     */
    Resources selectByPrimaryKey(String id);

    /**
     * 动态根据指定的条件来更新符合条件的系统资源的数据库记录,auth_resources
     *
     * @param record  系统资源
     * @param example 动态SQL条件实例
     */
    int updateByExampleSelective(@Param("record") Resources record, @Param("example") ResourcesCriteria example);

    /**
     * 根据指定的条件来更新符合条件的系统资源的数据库记录,auth_resources
     *
     * @param record  系统资源
     * @param example 动态SQL条件实例
     */
    int updateByExample(@Param("record") Resources record, @Param("example") ResourcesCriteria example);

    /**
     * 动态字段,根据主键来更新符合条件的系统资源的数据库记录,auth_resources
     *
     * @param record 系统资源
     */
    int updateByPrimaryKeySelective(Resources record);

    /**
     * 根据主键来更新符合条件的系统资源的数据库记录,auth_resources
     *
     * @param record 系统资源
     */
    int updateByPrimaryKey(Resources record);

    PageList<Resources> selectByPage(PagingCriteria pagingCriteria);
    PageList<Resources> selectByPageOrder(PagingCriteria pagingCriteria);
    PageList<Resources> selectByPageOrderAndWhere(PagingCriteria pagingCriteria,@Param("name") String name);
}