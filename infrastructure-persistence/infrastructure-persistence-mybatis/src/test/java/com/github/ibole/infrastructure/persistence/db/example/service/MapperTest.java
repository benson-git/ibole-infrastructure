package com.github.ibole.infrastructure.persistence.db.example.service;

import com.github.ibole.infrastructure.persistence.db.example.dao.ResourcesDao;
import com.github.ibole.infrastructure.persistence.db.example.dao.ResourcesNonAnnotationDao;
import com.github.ibole.infrastructure.persistence.db.example.domain.ResourceReq;
import com.github.ibole.infrastructure.persistence.db.example.domain.Resources;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.Pager;
import com.github.ibole.infrastructure.persistence.pagination.model.PagingCriteria;
import com.github.ibole.infrastructure.persistence.pagination.model.SearchField;
import com.github.ibole.infrastructure.persistence.pagination.model.SortDirection;
import com.github.ibole.infrastructure.persistence.pagination.model.SortField;

import com.google.common.collect.Lists;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.annotation.Resource;

/**
 * 
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-context.xml")
public class MapperTest {

    /**
     * Mybatis MapperScannerConfigurer自动扫描将Mapper接口生成代理注入到Spring.
     */
    @Resource
    private ResourcesDao resourcesDao;
    
    /**
     * 不使用Mybatis MapperScannerConfigurer自动扫描，所以需要自己实现Dao接口.
     * 
     * <context:component-scan base-package="com.github.ibole.infrastructure.persistence.db.example.dao">
     *     <context:exclude-filter type="annotation" expression="com.github.ibole.infrastructure.persistence.db.mybatis.annotation.MyBatisDao"/>  
    *  </context:component-scan>
     */
    @Resource
    private ResourcesNonAnnotationDao resourcesNonAnnotationDao;

//    @Before
//    //使用该注释会使用事务，而且在测试完成之后会回滚事务，也就是说在该方法中做出的一切操作都不会对数据库中的数据产生任何影响
//    @Rollback(true) //这里设置为false，就让事务不回滚
//    public void setUp() throws Exception {
//        Resources resources;
//        for (int i = 0; i < 1000; i++) {
//            resources = new Resources();
//            resources.setId(UUID.randomUUID().toString());
//            resources.setName("测试数据" + i);
//            resources.setPath("test/pageh/" + i);
//            resourcesDao.insertSelective(resources);
//        }
//
//    }
//
    @Test
    public void testPagaination() throws Exception {

        PagingCriteria baseCriteria = PagingCriteria.createCriteria(1, 15);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPage(baseCriteria);
        System.out.println(pageMyBatis.getPager());

    }

    @Test
    public void testPagainationSqlContainOrder() throws Exception {
      PagingCriteria baseCriteria = PagingCriteria.createCriteria(1, 15);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPageOrder(baseCriteria);
        System.out.println(pageMyBatis.getPager());

    }

    @Test
    public void testPagainationAndOrder() throws Exception {

        List<SortField> sortFields = Lists.newArrayList();
        sortFields.add(new SortField("name", SortDirection.DESC));
        sortFields.add(new SortField("path", SortDirection.ASC));

        PagingCriteria baseCriteria = PagingCriteria.createCriteriaWithSort(1, 15, sortFields);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPage(baseCriteria);
        System.out.println(pageMyBatis.getPager());
    }

    @Test
    public void testPagainationAndSearch() throws Exception {
        List<SearchField> searchFields = Lists.newArrayList();
        searchFields.add(new SearchField("name", "11"));

        PagingCriteria baseCriteria = PagingCriteria.createCriteriaWithSearch(1, 15, searchFields);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPage(baseCriteria);
        System.out.println(pageMyBatis.getPager());
    }

    @Test
    public void testPagainationAndOrderSearch() throws Exception {
        List<SearchField> searchFields = Lists.newArrayList();
        searchFields.add(new SearchField("name", "11"));

        PagingCriteria baseCriteria = PagingCriteria.createCriteriaWithSearch(1, 15, searchFields);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPageOrder(baseCriteria);
        System.out.println(pageMyBatis.getPager());
    }

    @Test
    public void testPaginationMoreWhere() throws Exception {
        List<SearchField> searchFields = Lists.newArrayList();
        searchFields.add(new SearchField("name", "11"));

        PagingCriteria baseCriteria = PagingCriteria.createCriteriaWithSearch(1, 15, searchFields);
        PageList<Resources> pageMyBatis = resourcesDao.selectByPageOrderAndWhere(baseCriteria,"aa");
        System.out.println(pageMyBatis.getPager());

    }
    
    @Test
    public void testPaginationMoreWhereWithNonAnnotation() throws Exception {
        Pager pager = new Pager(1,10);
        ResourceReq req = new ResourceReq();
        req.setName("测试数据11");
        PageList<Resources> pageMyBatis = resourcesNonAnnotationDao.selectByPageOrderAndWhere(req, pager);
        System.out.println(pageMyBatis.getPager());
        if(pageMyBatis.get(0) != null){
          System.out.println(pageMyBatis.get(0).getId());
        }
    }
}
