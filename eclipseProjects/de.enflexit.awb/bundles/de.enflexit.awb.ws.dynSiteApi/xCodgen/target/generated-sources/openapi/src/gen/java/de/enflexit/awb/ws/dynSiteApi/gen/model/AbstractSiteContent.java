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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * AbstractSiteContent
 */
@JsonPropertyOrder({
  AbstractSiteContent.JSON_PROPERTY_UNIQUE_CONTENT_I_D,
  AbstractSiteContent.JSON_PROPERTY_EDITABLE,
  AbstractSiteContent.JSON_PROPERTY_UPDATE_PERIOD_IN_SECONDS
})
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2025-04-02T14:48:58.419716700+02:00[Europe/Berlin]", comments = "Generator version: 7.6.0")@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "AbstractSiteContent", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = SiteContentImage.class, name = "SiteContentImage"),
  @JsonSubTypes.Type(value = SiteContentProperties.class, name = "SiteContentProperties"),
  @JsonSubTypes.Type(value = SiteContentTable.class, name = "SiteContentTable"),
  @JsonSubTypes.Type(value = SiteContentText.class, name = "SiteContentText"),
  @JsonSubTypes.Type(value = SiteContentTimeSeriesChart.class, name = "SiteContentTimeSeriesChart"),
  @JsonSubTypes.Type(value = SiteContentXYChart.class, name = "SiteContentXYChart"),
})

public class AbstractSiteContent   {
  public static final String JSON_PROPERTY_UNIQUE_CONTENT_I_D = "uniqueContentID";
  @JsonProperty(JSON_PROPERTY_UNIQUE_CONTENT_I_D)
  private Integer uniqueContentID;

  public static final String JSON_PROPERTY_EDITABLE = "editable";
  @JsonProperty(JSON_PROPERTY_EDITABLE)
  private Boolean editable;

  public static final String JSON_PROPERTY_UPDATE_PERIOD_IN_SECONDS = "updatePeriodInSeconds";
  @JsonProperty(JSON_PROPERTY_UPDATE_PERIOD_IN_SECONDS)
  private Integer updatePeriodInSeconds;

  public AbstractSiteContent uniqueContentID(Integer uniqueContentID) {
    this.uniqueContentID = uniqueContentID;
    return this;
  }

  /**
   * Get uniqueContentID
   * @return uniqueContentID
   **/
  @JsonProperty(value = "uniqueContentID")
  @Schema(required = true, description = "")
  @NotNull 
  public Integer getUniqueContentID() {
    return uniqueContentID;
  }

  public void setUniqueContentID(Integer uniqueContentID) {
    this.uniqueContentID = uniqueContentID;
  }

  public AbstractSiteContent editable(Boolean editable) {
    this.editable = editable;
    return this;
  }

  /**
   * Get editable
   * @return editable
   **/
  @JsonProperty(value = "editable")
  @Schema(required = true, description = "")
  @NotNull 
  public Boolean getEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public AbstractSiteContent updatePeriodInSeconds(Integer updatePeriodInSeconds) {
    this.updatePeriodInSeconds = updatePeriodInSeconds;
    return this;
  }

  /**
   * Get updatePeriodInSeconds
   * @return updatePeriodInSeconds
   **/
  @JsonProperty(value = "updatePeriodInSeconds")
  @Schema(required = true, description = "")
  @NotNull 
  public Integer getUpdatePeriodInSeconds() {
    return updatePeriodInSeconds;
  }

  public void setUpdatePeriodInSeconds(Integer updatePeriodInSeconds) {
    this.updatePeriodInSeconds = updatePeriodInSeconds;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractSiteContent abstractSiteContent = (AbstractSiteContent) o;
    return Objects.equals(this.uniqueContentID, abstractSiteContent.uniqueContentID) &&
        Objects.equals(this.editable, abstractSiteContent.editable) &&
        Objects.equals(this.updatePeriodInSeconds, abstractSiteContent.updatePeriodInSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uniqueContentID, editable, updatePeriodInSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractSiteContent {\n");
    
    sb.append("    uniqueContentID: ").append(toIndentedString(uniqueContentID)).append("\n");
    sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
    sb.append("    updatePeriodInSeconds: ").append(toIndentedString(updatePeriodInSeconds)).append("\n");
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

