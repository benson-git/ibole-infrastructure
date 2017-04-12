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


public enum SortDirection {
  /**
   * The ASC.
   */
  ASC("asc"),
  /**
   * The DESC.
   */
  DESC("desc");
  
  private String direction;

  private SortDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Value of case insensitive.
   *
   * @param value the value
   * @return the sort direction
   */
  public static SortDirection valueOfCaseInsensitive(String value) {
    String valueUpper = value.toUpperCase();
    return SortDirection.valueOf(valueUpper);
  }

  /**
   * Gets direction.
   *
   * @return the direction
   */
  public String getDirection() {
    return this.direction;
  }
}
