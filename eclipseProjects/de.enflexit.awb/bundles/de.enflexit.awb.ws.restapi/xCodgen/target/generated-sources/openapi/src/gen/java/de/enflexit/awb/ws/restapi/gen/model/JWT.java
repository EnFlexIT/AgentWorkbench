/*
 * Agent.Workbench REST - API
 * This is the REST-API for Agent.Workbench in an embbedded system mode.
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: admin@enflex.it
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package de.enflexit.awb.ws.restapi.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * the jwt which needs to be validated,
 */
@ApiModel(description = "the jwt which needs to be validated,")
@JsonPropertyOrder({
  JWT.JSON_PROPERTY_JWT
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-01-18T14:16:57.078043800+01:00[Europe/Berlin]")
public class JWT   {
  public static final String JSON_PROPERTY_JWT = "jwt";
  @JsonProperty(JSON_PROPERTY_JWT)
  private String jwt;

  public JWT jwt(String jwt) {
    this.jwt = jwt;
    return this;
  }

  /**
   * jwt in XXX.YYY.ZZZ format
   * @return jwt
   **/
  @JsonProperty(value = "jwt")
  @ApiModelProperty(required = true, value = "jwt in XXX.YYY.ZZZ format")
  @NotNull 
  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JWT JWT = (JWT) o;
    return Objects.equals(this.jwt, JWT.jwt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JWT {\n");
    
    sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
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
