/*
 * EnFlex.IT - Dynamic Content Api
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentMedia;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * SiteContentImage
 */
@JsonPropertyOrder({
  SiteContentImage.JSON_PROPERTY_DATA_IN_B64
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T14:48:58.419716700+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")
public class SiteContentImage extends SiteContentMedia  {
  public static final String JSON_PROPERTY_DATA_IN_B64 = "dataInB64";
  @JsonProperty(JSON_PROPERTY_DATA_IN_B64)
  private String dataInB64;

  public SiteContentImage dataInB64(String dataInB64) {
    this.dataInB64 = dataInB64;
    return this;
  }

  /**
   * Get dataInB64
   * @return dataInB64
   **/
  @JsonProperty(value = "dataInB64")
  @Schema(required = true, description = "")
  @NotNull 
  public String getDataInB64() {
    return dataInB64;
  }

  public void setDataInB64(String dataInB64) {
    this.dataInB64 = dataInB64;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiteContentImage siteContentImage = (SiteContentImage) o;
    return super.equals(o) && Objects.equals(this.dataInB64, siteContentImage.dataInB64);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), dataInB64);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiteContentImage {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    dataInB64: ").append(toIndentedString(dataInB64)).append("\n");
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

