/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ibole.infrastructure.persistence.db.example.service;

import com.github.ibole.infrastructure.persistence.db.example.domain.ResourceReq;
import com.github.ibole.infrastructure.persistence.db.example.domain.Resources;
import com.github.ibole.infrastructure.persistence.db.examplenon.dao.ResourcesNonAnnotationDao;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.Pager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * <p>
 * 不使用Mybatis MapperScannerConfigurer 自动扫描，所以需要自己实现Dao接口.
 * </p>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-test-context.xml")
public class MapperNonAnnotationTest {

  @Resource
  ResourcesNonAnnotationDao dao;
  
  @Test
  public void testPaginationMoreWhere() throws Exception {
      Pager pager = new Pager(1,10);
      ResourceReq req = new ResourceReq();
      req.setName("测试数据11");
      PageList<Resources> pageMyBatis = dao.selectByPageOrderAndWhere(req, pager);
      System.out.println(pageMyBatis.getPager());
      if(pageMyBatis.get(0) != null){
        System.out.println(pageMyBatis.get(0).getId());
      }
  }

}
