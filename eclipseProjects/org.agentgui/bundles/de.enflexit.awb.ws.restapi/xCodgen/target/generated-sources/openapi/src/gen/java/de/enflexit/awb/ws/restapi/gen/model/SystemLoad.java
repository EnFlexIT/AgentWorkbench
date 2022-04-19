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
 * The systems current load, includung CPU, memoryand HEAP  usage. Further, the number of Java threads are returned.
 */
@ApiModel(description = "The systems current load, includung CPU, memoryand HEAP  usage. Further, the number of Java threads are returned.")
@JsonPropertyOrder({
  SystemLoad.JSON_PROPERTY_CPU_USAGE,
  SystemLoad.JSON_PROPERTY_MEM_USAGE,
  SystemLoad.JSON_PROPERTY_HEAP_USAGE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2022-04-19T14:42:04.744880100+02:00[Europe/Berlin]")
public class SystemLoad   {
  public static final String JSON_PROPERTY_CPU_USAGE = "cpuUsage";
  @JsonProperty(JSON_PROPERTY_CPU_USAGE)
  private Float cpuUsage;

  public static final String JSON_PROPERTY_MEM_USAGE = "memUsage";
  @JsonProperty(JSON_PROPERTY_MEM_USAGE)
  private Float memUsage;

  public static final String JSON_PROPERTY_HEAP_USAGE = "heapUsage";
  @JsonProperty(JSON_PROPERTY_HEAP_USAGE)
  private Float heapUsage;

  public SystemLoad cpuUsage(Float cpuUsage) {
    this.cpuUsage = cpuUsage;
    return this;
  }

  /**
   * The CPU usage in percent
   * @return cpuUsage
   **/
  @JsonProperty(value = "cpuUsage")
  @ApiModelProperty(value = "The CPU usage in percent")
  
  public Float getCpuUsage() {
    return cpuUsage;
  }

  public void setCpuUsage(Float cpuUsage) {
    this.cpuUsage = cpuUsage;
  }

  public SystemLoad memUsage(Float memUsage) {
    this.memUsage = memUsage;
    return this;
  }

  /**
   * The memory usage in percent
   * @return memUsage
   **/
  @JsonProperty(value = "memUsage")
  @ApiModelProperty(value = "The memory usage in percent")
  
  public Float getMemUsage() {
    return memUsage;
  }

  public void setMemUsage(Float memUsage) {
    this.memUsage = memUsage;
  }

  public SystemLoad heapUsage(Float heapUsage) {
    this.heapUsage = heapUsage;
    return this;
  }

  /**
   * The Heap usage in percen
   * @return heapUsage
   **/
  @JsonProperty(value = "heapUsage")
  @ApiModelProperty(value = "The Heap usage in percen")
  
  public Float getHeapUsage() {
    return heapUsage;
  }

  public void setHeapUsage(Float heapUsage) {
    this.heapUsage = heapUsage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemLoad systemLoad = (SystemLoad) o;
    return Objects.equals(this.cpuUsage, systemLoad.cpuUsage) &&
        Objects.equals(this.memUsage, systemLoad.memUsage) &&
        Objects.equals(this.heapUsage, systemLoad.heapUsage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpuUsage, memUsage, heapUsage);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemLoad {\n");
    
    sb.append("    cpuUsage: ").append(toIndentedString(cpuUsage)).append("\n");
    sb.append("    memUsage: ").append(toIndentedString(memUsage)).append("\n");
    sb.append("    heapUsage: ").append(toIndentedString(heapUsage)).append("\n");
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

