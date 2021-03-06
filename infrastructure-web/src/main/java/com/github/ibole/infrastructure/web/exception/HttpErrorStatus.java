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

package com.github.ibole.infrastructure.web.exception;

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
 * @author bwang
 *
 */


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


public final class HttpErrorStatus {

  public static final HttpErrorStatus ACCOUNT_EXPIRED = Code.ACCOUNT_EXPIRED.toStatus();

  public static final HttpErrorStatus ACCOUNT_INVALID = Code.ACCOUNT_INVALID.toStatus();

  public static final HttpErrorStatus ACCOUNT_NOT_FOUND = Code.ACCOUNT_NOT_FOUND.toStatus();

  public static final HttpErrorStatus ACCOUNT_LOCKED = Code.ACCOUNT_LOCKED.toStatus();

  private final Code code;

  public HttpErrorStatus(Code code) {
    this.code = code;
  }


  public Code getCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return "Status:" + code;
  }

  // {"errors":[{"code":34, "message":"Sorry, that page does not exist"}]}
  public String toJson() {

    StringBuilder errorData = new StringBuilder();
    errorData.append("{").append("\"errors\"").append(":").append("[{").append("\"code\"")
        .append(":").append(getCode().value).append(",").append("\"message\"").append(":")
        .append("\"").append(getCode().getMessage()).append("\"").append("}]}");
    return errorData.toString();
  }

  public enum Code {

    /**
     * The token is expired.
     */
    ACCOUNT_EXPIRED(1034, "account.expired"),

    /**
     * The token is invalid.
     */
    ACCOUNT_INVALID(1035, "account.invalid"),

    /**
     * The token is not found.
     */
    ACCOUNT_NOT_FOUND(1036, "account.notfound"),

    /**
     * The token is locked.
     */
    ACCOUNT_LOCKED(1037, "account.locked");


    private final int value;

    private final String message;

    private Code(int code, String message) {
      this.value = code;
      this.message = message;
    }

    /**
     * The value of the status.
     */
    public int value() {
      return value;
    }

    public String getMessage() {
      return message;
    }

    public HttpErrorStatus toStatus() {
      return new HttpErrorStatus(this);
    }

  }

}
