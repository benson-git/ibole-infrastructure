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
 * The DataTables search filed model.
 * </p>
 *
 */
public class SearchField implements TransferObject {

  private static final long serialVersionUID = -8493268499000005405L;
  /** field name */
  private final String field;
  
  /**
   * True if the individual column filter should be treated as a regular expression for advanced
   * filtering, false if not
   */
  private final boolean regex;
  /** Indicator for if a column is flagged as sortable or not on the client-side */
  private final boolean searchable;
  /** search value. */
  private final String value;

  /**
   * Instantiates a new Search field.
   *
   * @param field the field
   * @param regex the regex
   * @param searchable the searchable
   * @param value the value
   */
  public SearchField(String field, boolean regex, boolean searchable, String value) {
    this.field = field;
    this.regex = regex;
    this.searchable = searchable;
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
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
   * Is regex.
   *
   * @return the boolean
   */
  public boolean isRegex() {
    return regex;
  }

  /**
   * Is searchable.
   *
   * @return the boolean
   */
  public boolean isSearchable() {
    return searchable;
  }
  
  /**
   * Returns a string representation of this {@code SearchField}. This implementation returns a
   * representation based on the value and label.
   * 
   * @return a string representation of this {@code SearchField}
   */
  @SuppressWarnings("nls")
  @Override
  public String toString() {
    final StringBuilder sb = ToStringUtil.start("field", field);
    ToStringUtil.append(sb, "value", value);
    ToStringUtil.append(sb, "searchable", searchable);
    ToStringUtil.append(sb, "regex", regex);
    return ToStringUtil.end(sb);
  }
}
