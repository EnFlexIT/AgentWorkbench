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


package de.enflexit.awb.samples.ws.restapi.server.gen.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.enflexit.awb.samples.ws.restapi.server.gen.model.NetworkConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

/**
 * The system information consisting of Hardware and OS information
 */
@Schema(description = "The system information consisting of Hardware and OS information")
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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-08-15T11:14:24.521899400+02:00[Europe/Berlin]")
public class SystemInformation   {
  public static final String JSON_PROPERTY_OS_DESCRIPTION = "osDescription";
  @JsonProperty(JSON_PROPERTY_OS_DESCRIPTION)
  private String osDescription;

  public static final String JSON_PROPERTY_OS_MANUFACTURER = "osManufacturer";
  @JsonProperty(JSON_PROPERTY_OS_MANUFACTURER)
  private String osManufacturer;

  public static final String JSON_PROPERTY_OS_FAMILLY = "osFamilly";
  @JsonProperty(JSON_PROPERTY_OS_FAMILLY)
  private String osFamilly;

  public static final String JSON_PROPERTY_OS_VERSION = "osVersion";
  @JsonProperty(JSON_PROPERTY_OS_VERSION)
  private String osVersion;

  public static final String JSON_PROPERTY_PROCESSOR_NAME = "processorName";
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NAME)
  private String processorName;

  public static final String JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ = "processorFrequenceInMhz";
  @JsonProperty(JSON_PROPERTY_PROCESSOR_FREQUENCE_IN_MHZ)
  private Double processorFrequenceInMhz;

  public static final String JSON_PROPERTY_PROCESSOR_NO_PHYSICAL = "processorNoPhysical";
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_PHYSICAL)
  private Integer processorNoPhysical;

  public static final String JSON_PROPERTY_PROCESSOR_NO_LOGICAL = "processorNoLogical";
  @JsonProperty(JSON_PROPERTY_PROCESSOR_NO_LOGICAL)
  private Integer processorNoLogical;

  public static final String JSON_PROPERTY_MEMORY_TOTAL_IN_G_B = "memoryTotalInGB";
  @JsonProperty(JSON_PROPERTY_MEMORY_TOTAL_IN_G_B)
  private Double memoryTotalInGB;

  public static final String JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B = "swapMemoryTotalInGB";
  @JsonProperty(JSON_PROPERTY_SWAP_MEMORY_TOTAL_IN_G_B)
  private Double swapMemoryTotalInGB;

  public static final String JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B = "heapMemoryMaxInGB";
  @JsonProperty(JSON_PROPERTY_HEAP_MEMORY_MAX_IN_G_B)
  private Double heapMemoryMaxInGB;

  public static final String JSON_PROPERTY_NETWORK_CONNECTIONS = "networkConnections";
  @JsonProperty(JSON_PROPERTY_NETWORK_CONNECTIONS)
  private List<NetworkConnection> networkConnections;

  public SystemInformation osDescription(String osDescription) {
    this.osDescription = osDescription;
    return this;
  }

  /**
   * Get osDescription
   * @return osDescription
   **/
  @JsonProperty(value = "osDescription")
  @Schema(description = "")
  
  public String getOsDescription() {
    return osDescription;
  }

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
  @JsonProperty(value = "osManufacturer")
  @Schema(description = "")
  
  public String getOsManufacturer() {
    return osManufacturer;
  }

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
  @JsonProperty(value = "osFamilly")
  @Schema(description = "")
  
  public String getOsFamilly() {
    return osFamilly;
  }

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
  @JsonProperty(value = "osVersion")
  @Schema(description = "")
  
  public String getOsVersion() {
    return osVersion;
  }

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
  @JsonProperty(value = "processorName")
  @Schema(description = "")
  
  public String getProcessorName() {
    return processorName;
  }

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
  @JsonProperty(value = "processorFrequenceInMhz")
  @Schema(description = "")
  
  public Double getProcessorFrequenceInMhz() {
    return processorFrequenceInMhz;
  }

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
  @JsonProperty(value = "processorNoPhysical")
  @Schema(description = "")
  
  public Integer getProcessorNoPhysical() {
    return processorNoPhysical;
  }

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
  @JsonProperty(value = "processorNoLogical")
  @Schema(description = "")
  
  public Integer getProcessorNoLogical() {
    return processorNoLogical;
  }

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
  @JsonProperty(value = "memoryTotalInGB")
  @Schema(description = "")
  
  public Double getMemoryTotalInGB() {
    return memoryTotalInGB;
  }

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
  @JsonProperty(value = "swapMemoryTotalInGB")
  @Schema(description = "")
  
  public Double getSwapMemoryTotalInGB() {
    return swapMemoryTotalInGB;
  }

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
  @JsonProperty(value = "heapMemoryMaxInGB")
  @Schema(description = "")
  
  public Double getHeapMemoryMaxInGB() {
    return heapMemoryMaxInGB;
  }

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
  @JsonProperty(value = "networkConnections")
  @Schema(description = "")
  @Valid 
  public List<NetworkConnection> getNetworkConnections() {
    return networkConnections;
  }

  public void setNetworkConnections(List<NetworkConnection> networkConnections) {
    this.networkConnections = networkConnections;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemInformation systemInformation = (SystemInformation) o;
    return Objects.equals(osDescription, systemInformation.osDescription) &&
        Objects.equals(osManufacturer, systemInformation.osManufacturer) &&
        Objects.equals(osFamilly, systemInformation.osFamilly) &&
        Objects.equals(osVersion, systemInformation.osVersion) &&
        Objects.equals(processorName, systemInformation.processorName) &&
        Objects.equals(processorFrequenceInMhz, systemInformation.processorFrequenceInMhz) &&
        Objects.equals(processorNoPhysical, systemInformation.processorNoPhysical) &&
        Objects.equals(processorNoLogical, systemInformation.processorNoLogical) &&
        Objects.equals(memoryTotalInGB, systemInformation.memoryTotalInGB) &&
        Objects.equals(swapMemoryTotalInGB, systemInformation.swapMemoryTotalInGB) &&
        Objects.equals(heapMemoryMaxInGB, systemInformation.heapMemoryMaxInGB) &&
        Objects.equals(networkConnections, systemInformation.networkConnections);
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

