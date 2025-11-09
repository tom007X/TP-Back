package com.example.ShippingMicroservice.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SectionStatus {
  ESTIMADO(1, "Estimado"),
  ASIGNADO(2, "Asignado"),
  INICIADO(3, "Iniciado"),
  FINALIZADO(4, "Finalizado"),
  CANCELADO(5, "Cancelado");

  private static final Map<Integer, SectionStatus> BY_CODE = new HashMap<>();
  private static final Map<String, SectionStatus> BY_VALUE = new HashMap<>();
  static {
    for (SectionStatus s : values()) {
      BY_CODE.put(s.code, s);
      BY_VALUE.put(s.value, s);
    }
  }

  private final int code;
  private final String value;

  SectionStatus(int code, String value) {
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

  public static SectionStatus fromCode(int code) {
    SectionStatus s = BY_CODE.get(code);
    if (s == null)
      throw new IllegalArgumentException("Invalid SectionStatus code: " + code);
    return s;
  }

  @JsonCreator
  public static SectionStatus fromJson(Object v) {
    if (v instanceof Number)
      return fromCode(((Number) v).intValue());
    SectionStatus s = BY_VALUE.get(String.valueOf(v));
    if (s == null)
      throw new IllegalArgumentException("Invalid SectionStatus: " + v);
    return s;
  }
}