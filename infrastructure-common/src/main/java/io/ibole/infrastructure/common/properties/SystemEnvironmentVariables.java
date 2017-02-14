package io.ibole.infrastructure.common.properties;

/**
 * Config name from System environment.
 * @author bwang
 * 
 */
public enum SystemEnvironmentVariables {

  EXTERNAL_CONFIG_PATH("EXTERNAL_CONFIG_PATH");

  private String value;

  SystemEnvironmentVariables(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
