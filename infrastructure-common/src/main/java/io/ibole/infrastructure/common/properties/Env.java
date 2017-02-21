package io.ibole.infrastructure.common.properties;

public enum Env {

  DEV("dev"), QA("qa"), PRODUCTION("production");

  private String value;

  Env(String env) {
    this.value = env;
  }

  public String getValue() {
    return value;
  }
  
}
