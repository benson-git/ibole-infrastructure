/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.infrastructure.persistence.pagination.model;

import java.util.ArrayList;
import java.util.Collection;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * 
 * 包含"分页"信息的List.
 * 
 * 
 */
public class PageList<E> extends ArrayList<E> {
  private static final long serialVersionUID = 1412759446332294208L;

  private Pager pager;

  public PageList() {}

  public PageList(Collection<? extends E> c) {
    super(c);
  }


  public PageList(Collection<? extends E> c, Pager p) {
    super(c);
    this.pager = p;
  }

  public PageList(Pager p) {
    this.pager = p;
  }


  /**
   * 得到分页器，通过Pager可以得到总页数等值.
   * 
   * @return the instance of Pager
   */
  public Pager getPager() {
    return pager;
  }


}
