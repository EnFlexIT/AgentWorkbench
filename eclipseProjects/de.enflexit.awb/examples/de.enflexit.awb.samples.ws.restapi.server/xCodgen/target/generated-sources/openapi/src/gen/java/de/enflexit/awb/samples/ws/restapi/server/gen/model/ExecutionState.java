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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * The Agent.Workbench execution state
 */
@ApiModel(description = "The Agent.Workbench execution state")
@JsonPropertyOrder({
  ExecutionState.JSON_PROPERTY_EXECUTION_MODE,
  ExecutionState.JSON_PROPERTY_DEVICE_SYSTEM_EXECUTION_MODE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2023-04-17T17:09:44.461949400+02:00[Europe/Berlin]")
public class ExecutionState   {
  /**
   * * &#39;APPLICATION&#39; - Runs as end user application in an desktop environment * &#39;SERVER&#39; - Runs as Background server-system * &#39;SERVER_MASTER&#39; - Runs as central &#39;server. master&#39; system and manages all &#39;server.slave&#39; systems * &#39;SERVER_SLAVE&#39; - Runs as central &#39;server. slave&#39; system and wait for start order from the &#39;server.master&#39; * &#39;DEVICE_SYSTEM&#39; - Runs as system that directly executes single agents or projects 
   */
  public enum ExecutionModeEnum {
    APPLICATION("APPLICATION"),
    
    SERVER("SERVER"),
    
    SERVER_MASTER("SERVER_MASTER"),
    
    SERVER_SLAVE("SERVER_SLAVE"),
    
    DEVICE_SYSTEM("DEVICE_SYSTEM");

    private String value;

    ExecutionModeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExecutionModeEnum fromValue(String value) {
      for (ExecutionModeEnum b : ExecutionModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_EXECUTION_MODE = "executionMode";
  @JsonProperty(JSON_PROPERTY_EXECUTION_MODE)
  private ExecutionModeEnum executionMode;

  /**
   * * &#39;SETUP&#39; - Runs the selected setup of an AWB projekt * &#39;AGENT&#39; - Runs one or more agents from an AWB project 
   */
  public enum DeviceSystemExecutionModeEnum {
    SETUP("SETUP"),
    
    AGENT("AGENT");

    private String value;

    DeviceSystemExecutionModeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DeviceSystemExecutionModeEnum fromValue(String value) {
      for (DeviceSystemExecutionModeEnum b : DeviceSystemExecutionModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_DEVICE_SYSTEM_EXECUTION_MODE = "deviceSystemExecutionMode";
  @JsonProperty(JSON_PROPERTY_DEVICE_SYSTEM_EXECUTION_MODE)
  private DeviceSystemExecutionModeEnum deviceSystemExecutionMode;

  public ExecutionState executionMode(ExecutionModeEnum executionMode) {
    this.executionMode = executionMode;
    return this;
  }

  /**
   * * &#39;APPLICATION&#39; - Runs as end user application in an desktop environment * &#39;SERVER&#39; - Runs as Background server-system * &#39;SERVER_MASTER&#39; - Runs as central &#39;server. master&#39; system and manages all &#39;server.slave&#39; systems * &#39;SERVER_SLAVE&#39; - Runs as central &#39;server. slave&#39; system and wait for start order from the &#39;server.master&#39; * &#39;DEVICE_SYSTEM&#39; - Runs as system that directly executes single agents or projects 
   * @return executionMode
   **/
  @JsonProperty(value = "executionMode")
  @ApiModelProperty(value = "* 'APPLICATION' - Runs as end user application in an desktop environment * 'SERVER' - Runs as Background server-system * 'SERVER_MASTER' - Runs as central 'server. master' system and manages all 'server.slave' systems * 'SERVER_SLAVE' - Runs as central 'server. slave' system and wait for start order from the 'server.master' * 'DEVICE_SYSTEM' - Runs as system that directly executes single agents or projects ")
  
  public ExecutionModeEnum getExecutionMode() {
    return executionMode;
  }

  public void setExecutionMode(ExecutionModeEnum executionMode) {
    this.executionMode = executionMode;
  }

  public ExecutionState deviceSystemExecutionMode(DeviceSystemExecutionModeEnum deviceSystemExecutionMode) {
    this.deviceSystemExecutionMode = deviceSystemExecutionMode;
    return this;
  }

  /**
   * * &#39;SETUP&#39; - Runs the selected setup of an AWB projekt * &#39;AGENT&#39; - Runs one or more agents from an AWB project 
   * @return deviceSystemExecutionMode
   **/
  @JsonProperty(value = "deviceSystemExecutionMode")
  @ApiModelProperty(value = "* 'SETUP' - Runs the selected setup of an AWB projekt * 'AGENT' - Runs one or more agents from an AWB project ")
  
  public DeviceSystemExecutionModeEnum getDeviceSystemExecutionMode() {
    return deviceSystemExecutionMode;
  }

  public void setDeviceSystemExecutionMode(DeviceSystemExecutionModeEnum deviceSystemExecutionMode) {
    this.deviceSystemExecutionMode = deviceSystemExecutionMode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutionState executionState = (ExecutionState) o;
    return Objects.equals(this.executionMode, executionState.executionMode) &&
        Objects.equals(this.deviceSystemExecutionMode, executionState.deviceSystemExecutionMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionMode, deviceSystemExecutionMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutionState {\n");
    
    sb.append("    executionMode: ").append(toIndentedString(executionMode)).append("\n");
    sb.append("    deviceSystemExecutionMode: ").append(toIndentedString(deviceSystemExecutionMode)).append("\n");
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
