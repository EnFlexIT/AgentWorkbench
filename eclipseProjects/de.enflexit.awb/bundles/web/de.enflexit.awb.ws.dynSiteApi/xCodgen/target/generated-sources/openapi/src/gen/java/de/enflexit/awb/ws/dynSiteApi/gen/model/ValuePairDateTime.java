/*
 * EnFlex.IT - Dynamic Content Api
 * This is the Definition of the Api to dynamically fetch content for any enflex.it application
 *
 * The version of the OpenAPI document: 0.1.0
 * Contact: admin@enflex.it
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package de.enflexit.awb.ws.dynSiteApi.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractValuePair;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * ValuePairDateTime
 */
@JsonPropertyOrder({
  ValuePairDateTime.JSON_PROPERTY_ISO_DATE_TIME
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-07-23T11:55:38.634832400+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class ValuePairDateTime extends AbstractValuePair  {
  public static final String JSON_PROPERTY_ISO_DATE_TIME = "isoDateTime";
  @JsonProperty(JSON_PROPERTY_ISO_DATE_TIME)
  private String isoDateTime;

  public ValuePairDateTime isoDateTime(String isoDateTime) {
    this.isoDateTime = isoDateTime;
    return this;
  }

  /**
   * Get isoDateTime
   * @return isoDateTime
   **/
  @JsonProperty(value = "isoDateTime")
  @Schema(required = true, description = "")
  @NotNull 
  public String getIsoDateTime() {
    return isoDateTime;
  }

  public void setIsoDateTime(String isoDateTime) {
    this.isoDateTime = isoDateTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValuePairDateTime valuePairDateTime = (ValuePairDateTime) o;
    return super.equals(o) && Objects.equals(this.isoDateTime, valuePairDateTime.isoDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), isoDateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValuePairDateTime {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    isoDateTime: ").append(toIndentedString(isoDateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

