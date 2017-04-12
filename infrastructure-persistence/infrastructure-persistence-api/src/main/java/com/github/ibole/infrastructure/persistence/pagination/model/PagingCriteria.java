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
 *  Paging Criteria.
 * </p>
 *
 */
public final class PagingCriteria implements TransferObject{

  private static final long serialVersionUID = -3474657905505855376L;
  /** The constant DEFAULT_CRITERIA. */
  private static final PagingCriteria DEFAULT_CRITERIA = new PagingCriteria(0,
      PagingCriteria.DEFAULT_SIZE, PagingCriteria.DEFAULT_SIZE);
  /** default page size. */
  private static final int DEFAULT_SIZE = 10;
  /** start display */
  private final int displayStart;
  /** disaplaySize */
  private final int displaySize;
  /** sort fields */
  private final List<SortField> sortFields;
  /** search field information */
  private final List<SearchField> searchFields;
  /** pageNumber */
  private final int pageNumber;

  /**
   * Instantiates a new Paging criteria.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param pageNumber the page number
   * @param sortFields the sort fields
   * @param searchFields the search information
   */
  private PagingCriteria(int displayStart, int displaySize, int pageNumber,
      List<SortField> sortFields, List<SearchField> searchFields) {
    this.displayStart = displayStart;
    this.displaySize = displaySize;
    this.pageNumber = pageNumber;
    this.sortFields = sortFields;
    this.searchFields = searchFields;
  }

  /**
   * Instantiates a new Paging criteria and not sort\search.
   *
   * @param displaySize the display size
   * @param displayStart the display start
   * @param pageNumber the page number
   */
  private PagingCriteria(int displaySize, int displayStart, int pageNumber) {
    this.displaySize = displaySize;
    this.displayStart = displayStart;
    this.pageNumber = pageNumber;
    this.searchFields = Lists.newArrayListWithCapacity(0);
    this.sortFields = Lists.newArrayListWithCapacity(0);
  }

  /**
   * Instantiates a new Paging criteria and no search.
   *
   * @param displaySize the display size
   * @param displayStart the display start
   * @param pageNumber the page number
   * @param sortFields the sort fields
   */
  private PagingCriteria(int displaySize, int displayStart, int pageNumber,
      List<SortField> sortFields) {
    this.sortFields = sortFields;
    this.displaySize = displaySize;
    this.displayStart = displayStart;
    this.pageNumber = pageNumber;
    this.searchFields = Lists.newArrayListWithCapacity(0);
  }

  /**
   * Instantiates a new Paging criteria and no sort.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param searchFields the search fields
   * @param pageNumber the page number
   */
  private PagingCriteria(int displayStart, int displaySize, List<SearchField> searchFields,
      int pageNumber) {
    this.displayStart = displayStart;
    this.displaySize = displaySize;
    this.searchFields = searchFields;
    this.pageNumber = pageNumber;
    this.sortFields = Lists.newArrayListWithCapacity(0);
  }

  /**
   * Create criteria with all paramter.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param pageNumber the page number
   * @param sortFields the sort fields
   * @param searchFields the search fields
   * @return the paging criteria
   */
  public static PagingCriteria createCriteriaWithAllParamter(int displayStart, int displaySize,
      int pageNumber, List<SortField> sortFields, List<SearchField> searchFields) {
    return new PagingCriteria(displayStart, displaySize, pageNumber, sortFields, searchFields);
  }

  /**
   * Create criteria with sort.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param pageNumber the page number
   * @param sortFields the sort fields
   * @return the paging criteria
   */
  public static PagingCriteria createCriteriaWithSort(int displayStart, int displaySize,
      int pageNumber, List<SortField> sortFields) {
    return new PagingCriteria(displayStart, displaySize, pageNumber, sortFields);
  }

  /**
   * Create criteria with search.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param pageNumber the page number
   * @param searchFields the search fields
   * @return the paging criteria
   */
  public static PagingCriteria createCriteriaWithSearch(int displayStart, int displaySize,
      int pageNumber, List<SearchField> searchFields) {
    return new PagingCriteria(displayStart, displaySize, searchFields, pageNumber);
  }

  /**
   * Create criteria.
   *
   * @param displayStart the display start
   * @param displaySize the display size
   * @param pageNumber the page number
   * @return the paging criteria
   */
  public static PagingCriteria createCriteria(int displayStart, int displaySize, int pageNumber) {
    return new PagingCriteria(displayStart, displaySize, pageNumber);
  }

  /**
   * Get default criteria.
   *
   * @return the paging criteria
   */
  public static PagingCriteria getDefaultCriteria() {
    return DEFAULT_CRITERIA;
  }

  /**
   * Gets display start.
   *
   * @return the display start
   */
  public Integer getDisplayStart() {
    return displayStart;
  }

  /**
   * Gets display size.
   *
   * @return the display size
   */
  public Integer getDisplaySize() {
    return displaySize;
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
   * Gets page number.
   *
   * @return the page number
   */
  public Integer getPageNumber() {
    return pageNumber;
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
    final StringBuilder sb = ToStringUtil.start("displayStart", displayStart);
    ToStringUtil.append(sb, "displaySize", displaySize);
    ToStringUtil.append(sb, "pageNumber", pageNumber);
    ToStringUtil.append(sb, "searchFields", searchFields);
    ToStringUtil.append(sb, "sortFields", sortFields);
    return ToStringUtil.end(sb);
  }
}
