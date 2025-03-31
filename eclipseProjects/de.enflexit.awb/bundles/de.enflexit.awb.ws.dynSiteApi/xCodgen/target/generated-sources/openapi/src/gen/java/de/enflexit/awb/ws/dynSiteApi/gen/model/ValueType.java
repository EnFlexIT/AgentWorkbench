/*
 * Enflex.IT - Dynamic Content Api
 * This is the Definition of the Api to dynamically fetch content for any enflex.it application
 *
 * The version of the OpenAPI document: 0.0.1
 * Contact: admin@enflex.it
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package de.enflexit.awb.ws.dynSiteApi.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ValueType
 */
public enum ValueType {
  
  INTEGER("INTEGER"),
  
  BOOLEAN("BOOLEAN"),
  
  STRING("STRING"),
  
  LONG("LONG"),
  
  DOUBLE("DOUBLE");

  private String value;

  ValueType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ValueType fromValue(String value) {
    for (ValueType b : ValueType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

