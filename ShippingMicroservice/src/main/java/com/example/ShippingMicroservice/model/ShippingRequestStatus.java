package com.example.ShippingMicroservice.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ShippingRequestStatus {
  ESTIMADO(1, "estimado"),
  ASIGNADO(2, "asignado"),
  INICIADO(3, "iniciado"),
  FINALIZADO(4, "finalizado"),
  CANCELADO(5, "cancelado");

  private static final Map<Integer, ShippingRequestStatus> BY_CODE = new HashMap<>();
  private static final Map<String, ShippingRequestStatus> BY_VALUE = new HashMap<>();
  static {
    for (ShippingRequestStatus s : values()) {
      BY_CODE.put(s.code, s);
      BY_VALUE.put(s.value, s);
    }
  }

  private final int code;
  private final String value;

  ShippingRequestStatus(int code, String value) {
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

  public static ShippingRequestStatus fromCode(int code) {
    ShippingRequestStatus s = BY_CODE.get(code);
    if (s == null)
      throw new IllegalArgumentException("Invalid SectionStatus code: " + code);
    return s;
  }

  @JsonCreator
  public static ShippingRequestStatus fromJson(Object v) {
    if (v instanceof Number)
      return fromCode(((Number) v).intValue());
    ShippingRequestStatus s = BY_VALUE.get(String.valueOf(v));
    if (s == null)
      throw new IllegalArgumentException("Invalid SectionStatus: " + v);
    return s;
  }
}
