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

import com.github.ibole.infrastructure.common.dto.TransferObject;
import com.github.ibole.infrastructure.common.utils.ToStringUtil;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

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
 * <p>
 * Paging Criteria.
 * </p>
 *
 */
public final class PagingCriteria implements TransferObject {

  private static final long serialVersionUID = -3474657905505855376L;
  private static final int DEFAULT_SIZE = 10;
  /** default page size. */
  /** sort fields */
  private final List<SortField> sortFields;
  /** search field information */
  private final List<SearchField> searchFields;

  private int pageNumber = 1;

  private int pageSize = 10; // 默认为每页20条记录

  /**
   * Instantiates a new Paging criteria.
   * 
   * @param pageNumber the page number
   * @param pageSize the page size
   * @param sortFields the sort fields
   * @param searchFields the search information
   */
  private PagingCriteria(int pageNumber, int pageSize, List<SortField> sortFields,
      List<SearchField> searchFields) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.sortFields = sortFields;
    this.searchFields = searchFields;
  }

  /**
   * Instantiates a new Paging criteria and not sort\search.
   * 
   * @param pageNumber the page number
   * @param pageSize the page size
   */
  private PagingCriteria(int pageNumber, int pageSize) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.searchFields = Lists.newArrayListWithCapacity(0);
    this.sortFields = Lists.newArrayListWithCapacity(0);
  }



  /**
   * @return the pageNumber
   */
  public int getPageNumber() {
    return pageNumber;
  }

  /**
   * @param pageNumber the pageNumber to set
   */
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  /**
   * @return the pageSize
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * @param pageSize the pageSize to set
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Get default criteria.
   *
   * @return the paging criteria
   */
  public static PagingCriteria getDefaultCriteria() {
    return new PagingCriteria(1, DEFAULT_SIZE);
  }

  /**
   * Gets search fields.
   *
   * @return the search fields
   */
  public List<SearchField> getSearchFields() {
    if (this.searchFields == null) {
      return Lists.newArrayListWithCapacity(0);
    }
    return Collections.unmodifiableList(searchFields);
  }

  /**
   * Gets sort fields.
   *
   * @return the sort fields
   */
  public List<SortField> getSortFields() {
    if (this.sortFields == null) {
      return Lists.newArrayListWithCapacity(0);
    }
    return Collections.unmodifiableList(sortFields);
  }

  /**
   * @param pageNumber the page number
   * @param pageSize the page size
   * @return instance of PagingCriteria
   */
  public static PagingCriteria createCriteria(int pageNumber, int pageSize) {
    return new PagingCriteria(pageNumber, pageSize);
  }

  /**
   * @param pageNumber the page number
   * @param pageSize the page size
   * @param searchFields the search fields information
   * @return instance of PagingCriteria
   */
  public static PagingCriteria createCriteriaWithSearch(int pageNumber, int pageSize,
      List<SearchField> searchFields) {
    return new PagingCriteria(pageNumber, pageSize, Lists.newArrayListWithCapacity(0), searchFields);
  }

  /**
   * @param pageNumber the page number
   * @param pageSize the page size
   * @param sortFields the order fields information
   * @return instance of PagingCriteria
   */
  public static PagingCriteria createCriteriaWithSort(int pageNumber, int pageSize,
      List<SortField> sortFields) {
    return new PagingCriteria(pageNumber, pageSize, sortFields, Lists.newArrayListWithCapacity(0));
  }

  /**
   * Returns a string representation of this {@code PagingCriteria}. This implementation returns a
   * representation based on the value and label.
   * 
   * @return a string representation of this {@code PagingCriteria}
   */
  @SuppressWarnings("nls")
  @Override
  public String toString() {
    final StringBuilder sb = ToStringUtil.start("pageNumber", pageNumber);
    ToStringUtil.append(sb, "pageSize", pageSize);
    ToStringUtil.append(sb, "searchFields", searchFields);
    ToStringUtil.append(sb, "sortFields", sortFields);
    return ToStringUtil.end(sb);
  }
}
