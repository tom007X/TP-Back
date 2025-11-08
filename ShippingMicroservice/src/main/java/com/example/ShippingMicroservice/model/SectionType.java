package com.example.ShippingMicroservice.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SectionType {
  ORIGEN_DEPOSITO(1, "origen-deposito"),
  DEPOSITO_DEPOSITO(2, "deposito-deposito"),
  DEPOSITO_DESTINO(3, "deposito-destino"),
  ORIGEN_DESTINO(4, "origen-destino");

  private static final Map<Integer, SectionType> BY_CODE = new HashMap<>();
  private static final Map<String, SectionType> BY_VALUE = new HashMap<>();
  static {
    for (SectionType t : values()) {
      BY_CODE.put(t.code, t);
      BY_VALUE.put(t.value, t);
    }
  }

  private final int code; // <- store this in DB
  private final String value; // <- human/JSON value

  SectionType(int code, String value) {
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

  public static SectionType fromCode(int code) {
    SectionType t = BY_CODE.get(code);
    if (t == null)
      throw new IllegalArgumentException("Invalid SectionType code: " + code);
    return t;
  }

  @JsonCreator
  public static SectionType fromJson(Object v) {
    if (v instanceof Number)
      return fromCode(((Number) v).intValue());
    SectionType t = BY_VALUE.get(String.valueOf(v));
    if (t == null)
      throw new IllegalArgumentException("Invalid SectionType: " + v);
    return t;
  }
}