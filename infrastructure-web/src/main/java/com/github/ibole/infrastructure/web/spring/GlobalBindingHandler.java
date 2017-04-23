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

package com.github.ibole.infrastructure.web.spring;

import com.github.ibole.infrastructure.common.utils.DateUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

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
@ControllerAdvice
public class GlobalBindingHandler {
  
  private static Logger log = LoggerFactory 
      .getLogger(GlobalBindingHandler.class);

  /**
   * 初始化数据绑定.
   *  1. 将所有传递进来的String进行HTML编码，防止XSS攻击 
   *  2. 将字段中Date类型转换为String类型
   *  @param binder WebDataBinder
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
    binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
      }

      @Override
      public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
      }
    });
    // Date 类型转换
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(DateUtils.parseDate(text));
      }
      // @Override
      // public String getAsText() {
      // Object value = getValue();
      // return value != null ? DateUtils.formatDateTime((Date)value) : "";
      // }
    });
  }
}
