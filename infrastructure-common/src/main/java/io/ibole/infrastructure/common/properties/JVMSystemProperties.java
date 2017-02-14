package io.ibole.infrastructure.common.properties;

/**
 * Config name from JVM properties setting.
 * 
 * @author bwang
 * 
 */
public enum JVMSystemProperties {

  EXTERNAL_CONFIG_PATH("external.config");

  private String value;

  JVMSystemProperties(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
