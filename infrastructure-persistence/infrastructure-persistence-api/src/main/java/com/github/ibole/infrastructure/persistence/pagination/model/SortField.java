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
 * the sort's filed define.
 * </p>
 * 
 */
public final class SortField implements TransferObject {

  private static final long serialVersionUID = 1335553778778273045L;
  /** field name */
  private final String field;
  /** sort direction */
  private final SortDirection direction;

  /**
   * Instantiates a new Sort field.
   *
   * @param field the field
   * @param direction the direction
   */
  public SortField(String field, SortDirection direction) {
    this.field = field;
    this.direction = direction;
  }

  /**
   * Instantiates a new Sort field.
   *
   * @param field the field
   * @param direction the direction
   */
  public SortField(String field, String direction) {
    this.field = field;
    this.direction = SortDirection.valueOfCaseInsensitive(direction);
  }

  /**
   * Gets field.
   *
   * @return the field
   */
  public String getField() {
    return field;
  }

  /**
   * Gets direction.
   *
   * @return the direction
   */
  public SortDirection getDirection() {
    return direction;
  }

  /**
   * Returns a string representation of this {@code SortDirection}. This implementation returns a
   * representation based on the value and label.
   * 
   * @return a string representation of this {@code SortDirection}
   */
  @SuppressWarnings("nls")
  @Override
  public String toString() {
    final StringBuilder sb = ToStringUtil.start("field", field);
    ToStringUtil.append(sb, "direction", direction.getDirection());
    return ToStringUtil.end(sb);
  }
}
