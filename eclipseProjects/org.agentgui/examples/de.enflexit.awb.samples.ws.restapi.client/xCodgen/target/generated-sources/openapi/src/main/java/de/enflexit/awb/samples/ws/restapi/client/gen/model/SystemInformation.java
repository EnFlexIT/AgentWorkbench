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


package de.enflexit.awb.samples.ws.restapi.client.gen.model;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.NetworkConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.JSON;


/**
 * The system information consisting of Hardware and OS information
 */
@ApiModel(description = "The system information consisting of Hardware and OS information")
@JsonPropertyOrder({
  SystemInformation.JSON_PROPERTY_OS_DESCRIPTION,
  SystemInformation.JSON_PROPERTY_OS_MANUFACTURER,
  SystemInformation.JSON_PROPERTY_OS_FAMILLY,
  SystemInformation.JSON_PROPERTY_OS_VERSION,
  SystemInformation.JSON_PROPERTY_PROCESSOR_NAME,
  SystemInformation.JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ,
  SystemInformation.JSON_PROPERTY_PROCESSOR_NO_PHYSICAL,
  SystemInformation.JSON_PROPERTY_PROCESSOR_NO_LOGICAL,
  SystemInformation.JSON_PROPERTY_MEMORY_TOTAL_IN_G_B,
  SystemInformation.JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B,
  SystemInformation.JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B,
  SystemInformation.JSON_PROPERTY_NETWORK_CONNECTIONS
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-04-20T17:08:39.430297+02:00[Europe/Berlin]")
public class SystemInformation {
  public static final String JSON_PROPERTY_OS_DESCRIPTION = "osDescription";
  private String osDescription;

  public static final String JSON_PROPERTY_OS_MANUFACTURER = "osManufacturer";
  private String osManufacturer;

  public static final String JSON_PROPERTY_OS_FAMILLY = "osFamilly";
  private String osFamilly;

  public static final String JSON_PROPERTY_OS_VERSION = "osVersion";
  private String osVersion;

  public static final String JSON_PROPERTY_PROCESSOR_NAME = "processorName";
  private String processorName;

  public static final String JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ = "processorFrequenceInMhz";
  private Double processorFrequenceInMhz;

  public static final String JSON_PROPERTY_PROCESSOR_NO_PHYSICAL = "processorNoPhysical";
  private Integer processorNoPhysical;

  public static final String JSON_PROPERTY_PROCESSOR_NO_LOGICAL = "processorNoLogical";
  private Integer processorNoLogical;

  public static final String JSON_PROPERTY_MEMORY_TOTAL_IN_G_B = "memoryTotalInGB";
  private Double memoryTotalInGB;

  public static final String JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B = "swapMemoryTotalInGB";
  private Double swapMemoryTotalInGB;

  public static final String JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B = "heapMemoryMaxInGB";
  private Double heapMemoryMaxInGB;

  public static final String JSON_PROPERTY_NETWORK_CONNECTIONS = "networkConnections";
  private List<NetworkConnection> networkConnections = null;

  public SystemInformation() { 
  }

  public SystemInformation osDescription(String osDescription) {
    this.osDescription = osDescription;
    return this;
  }

   /**
   * Get osDescription
   * @return osDescription
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OS_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOsDescription() {
    return osDescription;
  }


  @JsonProperty(JSON_PROPERTY_OS_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOsDescription(String osDescription) {
    this.osDescription = osDescription;
  }


  public SystemInformation osManufacturer(String osManufacturer) {
    this.osManufacturer = osManufacturer;
    return this;
  }

   /**
   * Get osManufacturer
   * @return osManufacturer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OS_MANUFACTURER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOsManufacturer() {
    return osManufacturer;
  }


  @JsonProperty(JSON_PROPERTY_OS_MANUFACTURER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOsManufacturer(String osManufacturer) {
    this.osManufacturer = osManufacturer;
  }


  public SystemInformation osFamilly(String osFamilly) {
    this.osFamilly = osFamilly;
    return this;
  }

   /**
   * Get osFamilly
   * @return osFamilly
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OS_FAMILLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOsFamilly() {
    return osFamilly;
  }


  @JsonProperty(JSON_PROPERTY_OS_FAMILLY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOsFamilly(String osFamilly) {
    this.osFamilly = osFamilly;
  }


  public SystemInformation osVersion(String osVersion) {
    this.osVersion = osVersion;
    return this;
  }

   /**
   * Get osVersion
   * @return osVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_OS_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOsVersion() {
    return osVersion;
  }


  @JsonProperty(JSON_PROPERTY_OS_VERSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }


  public SystemInformation processorName(String processorName) {
    this.processorName = processorName;
    return this;
  }

   /**
   * Get processorName
   * @return processorName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getProcessorName() {
    return processorName;
  }


  @JsonProperty(JSON_PROPERTY_PROCESSOR_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProcessorName(String processorName) {
    this.processorName = processorName;
  }


  public SystemInformation processorFrequenceInMhz(Double processorFrequenceInMhz) {
    this.processorFrequenceInMhz = processorFrequenceInMhz;
    return this;
  }

   /**
   * Get processorFrequenceInMhz
   * @return processorFrequenceInMhz
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getProcessorFrequenceInMhz() {
    return processorFrequenceInMhz;
  }


  @JsonProperty(JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProcessorFrequenceInMhz(Double processorFrequenceInMhz) {
    this.processorFrequenceInMhz = processorFrequenceInMhz;
  }


  public SystemInformation processorNoPhysical(Integer processorNoPhysical) {
    this.processorNoPhysical = processorNoPhysical;
    return this;
  }

   /**
   * Get processorNoPhysical
   * @return processorNoPhysical
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_PHYSICAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getProcessorNoPhysical() {
    return processorNoPhysical;
  }


  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_PHYSICAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProcessorNoPhysical(Integer processorNoPhysical) {
    this.processorNoPhysical = processorNoPhysical;
  }


  public SystemInformation processorNoLogical(Integer processorNoLogical) {
    this.processorNoLogical = processorNoLogical;
    return this;
  }

   /**
   * Get processorNoLogical
   * @return processorNoLogical
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_LOGICAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getProcessorNoLogical() {
    return processorNoLogical;
  }


  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_LOGICAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setProcessorNoLogical(Integer processorNoLogical) {
    this.processorNoLogical = processorNoLogical;
  }


  public SystemInformation memoryTotalInGB(Double memoryTotalInGB) {
    this.memoryTotalInGB = memoryTotalInGB;
    return this;
  }

   /**
   * Get memoryTotalInGB
   * @return memoryTotalInGB
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_MEMORY_TOTAL_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getMemoryTotalInGB() {
    return memoryTotalInGB;
  }


  @JsonProperty(JSON_PROPERTY_MEMORY_TOTAL_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMemoryTotalInGB(Double memoryTotalInGB) {
    this.memoryTotalInGB = memoryTotalInGB;
  }


  public SystemInformation swapMemoryTotalInGB(Double swapMemoryTotalInGB) {
    this.swapMemoryTotalInGB = swapMemoryTotalInGB;
    return this;
  }

   /**
   * Get swapMemoryTotalInGB
   * @return swapMemoryTotalInGB
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getSwapMemoryTotalInGB() {
    return swapMemoryTotalInGB;
  }


  @JsonProperty(JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSwapMemoryTotalInGB(Double swapMemoryTotalInGB) {
    this.swapMemoryTotalInGB = swapMemoryTotalInGB;
  }


  public SystemInformation heapMemoryMaxInGB(Double heapMemoryMaxInGB) {
    this.heapMemoryMaxInGB = heapMemoryMaxInGB;
    return this;
  }

   /**
   * Get heapMemoryMaxInGB
   * @return heapMemoryMaxInGB
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Double getHeapMemoryMaxInGB() {
    return heapMemoryMaxInGB;
  }


  @JsonProperty(JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setHeapMemoryMaxInGB(Double heapMemoryMaxInGB) {
    this.heapMemoryMaxInGB = heapMemoryMaxInGB;
  }


  public SystemInformation networkConnections(List<NetworkConnection> networkConnections) {
    this.networkConnections = networkConnections;
    return this;
  }

  public SystemInformation addNetworkConnectionsItem(NetworkConnection networkConnectionsItem) {
    if (this.networkConnections == null) {
      this.networkConnections = new ArrayList<>();
    }
    this.networkConnections.add(networkConnectionsItem);
    return this;
  }

   /**
   * Get networkConnections
   * @return networkConnections
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_NETWORK_CONNECTIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<NetworkConnection> getNetworkConnections() {
    return networkConnections;
  }


  @JsonProperty(JSON_PROPERTY_NETWORK_CONNECTIONS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNetworkConnections(List<NetworkConnection> networkConnections) {
    this.networkConnections = networkConnections;
  }


  /**
   * Return true if this SystemInformation object is equal to o.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemInformation systemInformation = (SystemInformation) o;
    return Objects.equals(this.osDescription, systemInformation.osDescription) &&
        Objects.equals(this.osManufacturer, systemInformation.osManufacturer) &&
        Objects.equals(this.osFamilly, systemInformation.osFamilly) &&
        Objects.equals(this.osVersion, systemInformation.osVersion) &&
        Objects.equals(this.processorName, systemInformation.processorName) &&
        Objects.equals(this.processorFrequenceInMhz, systemInformation.processorFrequenceInMhz) &&
        Objects.equals(this.processorNoPhysical, systemInformation.processorNoPhysical) &&
        Objects.equals(this.processorNoLogical, systemInformation.processorNoLogical) &&
        Objects.equals(this.memoryTotalInGB, systemInformation.memoryTotalInGB) &&
        Objects.equals(this.swapMemoryTotalInGB, systemInformation.swapMemoryTotalInGB) &&
        Objects.equals(this.heapMemoryMaxInGB, systemInformation.heapMemoryMaxInGB) &&
        Objects.equals(this.networkConnections, systemInformation.networkConnections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(osDescription, osManufacturer, osFamilly, osVersion, processorName, processorFrequenceInMhz, processorNoPhysical, processorNoLogical, memoryTotalInGB, swapMemoryTotalInGB, heapMemoryMaxInGB, networkConnections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemInformation {\n");
    sb.append("    osDescription: ").append(toIndentedString(osDescription)).append("\n");
    sb.append("    osManufacturer: ").append(toIndentedString(osManufacturer)).append("\n");
    sb.append("    osFamilly: ").append(toIndentedString(osFamilly)).append("\n");
    sb.append("    osVersion: ").append(toIndentedString(osVersion)).append("\n");
    sb.append("    processorName: ").append(toIndentedString(processorName)).append("\n");
    sb.append("    processorFrequenceInMhz: ").append(toIndentedString(processorFrequenceInMhz)).append("\n");
    sb.append("    processorNoPhysical: ").append(toIndentedString(processorNoPhysical)).append("\n");
    sb.append("    processorNoLogical: ").append(toIndentedString(processorNoLogical)).append("\n");
    sb.append("    memoryTotalInGB: ").append(toIndentedString(memoryTotalInGB)).append("\n");
    sb.append("    swapMemoryTotalInGB: ").append(toIndentedString(swapMemoryTotalInGB)).append("\n");
    sb.append("    heapMemoryMaxInGB: ").append(toIndentedString(heapMemoryMaxInGB)).append("\n");
    sb.append("    networkConnections: ").append(toIndentedString(networkConnections)).append("\n");
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

