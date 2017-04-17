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

package com.github.ibole.infrastructure.persistence.db.examplenon.dao;

import com.github.ibole.infrastructure.persistence.db.example.domain.ResourceReq;
import com.github.ibole.infrastructure.persistence.db.example.domain.Resources;
import com.github.ibole.infrastructure.persistence.db.mybatis.BaseDao;
import com.github.ibole.infrastructure.persistence.pagination.model.PageList;
import com.github.ibole.infrastructure.persistence.pagination.model.Pager;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
@Component
@Repository
public class ResourcesNonAnnotationDao extends BaseDao<Resources> {

  @Autowired  
  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){  
        
      super.setSqlSessionFactory(sqlSessionFactory);  
  }
  
  public PageList<Resources> selectByPageOrderAndWhere(ResourceReq req, Pager page){
    return this.getList("resourcesDao.selectListOrderAndWhere", req, page);
  }
}
