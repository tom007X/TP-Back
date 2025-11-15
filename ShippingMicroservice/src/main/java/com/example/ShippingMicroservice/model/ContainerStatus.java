package com.example.ShippingMicroservice.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContainerStatus {
  ASIGNADO(1, "asignado"),
  LIBRE(2, "libre");

  private static final Map<Integer, ContainerStatus> BY_CODE = new HashMap<>();
  private static final Map<String, ContainerStatus> BY_VALUE = new HashMap<>();
  static {
    for (ContainerStatus s : values()) {
      BY_CODE.put(s.code, s);
      BY_VALUE.put(s.value, s);
    }
  }

  private final int code;
  private final String value;

  ContainerStatus(int code, String value) {
    this.code = code;
    this.value = value;
  }

  public int getCode() {
    return code;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static ContainerStatus fromCode(int code) {
    ContainerStatus s = BY_CODE.get(code);
    if (s == null)
      throw new IllegalArgumentException("Invalid ContainerStatus code: " + code);
    return s;
  }

  @JsonCreator
  public static ContainerStatus fromJson(Object v) {
    if (v instanceof Number)
      return fromCode(((Number) v).intValue());
    ContainerStatus s = BY_VALUE.get(String.valueOf(v));
    if (s == null)
      throw new IllegalArgumentException("Invalid ContainerStatus: " + v);
    return s;
  }
}
