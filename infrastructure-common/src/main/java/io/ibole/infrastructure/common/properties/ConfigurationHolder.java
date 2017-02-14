package io.ibole.infrastructure.common.properties;

import java.util.Map;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * Holder for bootstrap configuration properties.
 *
 */
public final class ConfigurationHolder {
  
  private static Map<String, String> props;

  private ConfigurationHolder() {
    // empty
  }

  public static void set(final Map<String, String> properties) {
    props = properties;
  }

  public static Map<String, String> get() {
    return props;
  }

  public static void unset() {
    props.clear();
    props = null;
  }
}

