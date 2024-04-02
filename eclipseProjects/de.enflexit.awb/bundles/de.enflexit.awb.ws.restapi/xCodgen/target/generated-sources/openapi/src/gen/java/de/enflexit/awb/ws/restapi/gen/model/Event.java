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
import com.fasterxml.jackson.annotation.JsonValue;
import de.enflexit.awb.ws.restapi.gen.model.EventLogTypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * An Event that occured and can be logged
 */
@ApiModel(description = "An Event that occured and can be logged")
@JsonPropertyOrder({
  Event.JSON_PROPERTY_TIME,
  Event.JSON_PROPERTY_TYPE_OF_EVENT,
  Event.JSON_PROPERTY_EVENT
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2024-03-21T17:19:35.482673500+01:00[Europe/Berlin]")
public class Event   {
  public static final String JSON_PROPERTY_TIME = "time";
  @JsonProperty(JSON_PROPERTY_TIME)
  private String time;

  public static final String JSON_PROPERTY_TYPE_OF_EVENT = "typeOfEvent";
  @JsonProperty(JSON_PROPERTY_TYPE_OF_EVENT)
  private EventLogTypes typeOfEvent;

  /**
   * the event that has happened
   */
  public enum EventEnum {
    RUNNING("Running"),
    
    STARTING("Starting"),
    
    OPEN_PROJECT("Open Project"),
    
    STARTING_AGENT_SYSTEM("Starting Agent System"),
    
    UPDATE_SHEDULED("Update Sheduled"),
    
    UPDATE_EXECUTED("Update Executed"),
    
    UPDATE_FAILURE("Update Failure"),
    
    UPDATE_IN_PROGESS("Update in Progess"),
    
    RESTART_SHEDULED("Restart Sheduled"),
    
    RESTART_EXECUTED("Restart Executed"),
    
    RESTART_FAILURE("Restart Failure"),
    
    RESTART_IN_PROGESS("Restart in Progess");

    private String value;

    EventEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EventEnum fromValue(String value) {
      for (EventEnum b : EventEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_EVENT = "event";
  @JsonProperty(JSON_PROPERTY_EVENT)
  private EventEnum event;

  public Event time(String time) {
    this.time = time;
    return this;
  }

  /**
   * The time at which the event happened
   * @return time
   **/
  @JsonProperty(value = "time")
  @ApiModelProperty(required = true, value = "The time at which the event happened")
  @NotNull 
  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Event typeOfEvent(EventLogTypes typeOfEvent) {
    this.typeOfEvent = typeOfEvent;
    return this;
  }

  /**
   * Get typeOfEvent
   * @return typeOfEvent
   **/
  @JsonProperty(value = "typeOfEvent")
  @ApiModelProperty(value = "")
  @Valid 
  public EventLogTypes getTypeOfEvent() {
    return typeOfEvent;
  }

  public void setTypeOfEvent(EventLogTypes typeOfEvent) {
    this.typeOfEvent = typeOfEvent;
  }

  public Event event(EventEnum event) {
    this.event = event;
    return this;
  }

  /**
   * the event that has happened
   * @return event
   **/
  @JsonProperty(value = "event")
  @ApiModelProperty(required = true, value = "the event that has happened")
  @NotNull 
  public EventEnum getEvent() {
    return event;
  }

  public void setEvent(EventEnum event) {
    this.event = event;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(this.time, event.time) &&
        Objects.equals(this.typeOfEvent, event.typeOfEvent) &&
        Objects.equals(this.event, event.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, typeOfEvent, event);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Event {\n");
    
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    typeOfEvent: ").append(toIndentedString(typeOfEvent)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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
