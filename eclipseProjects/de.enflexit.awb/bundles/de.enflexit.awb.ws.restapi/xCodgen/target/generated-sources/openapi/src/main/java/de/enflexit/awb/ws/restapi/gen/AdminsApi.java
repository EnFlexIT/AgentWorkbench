package de.enflexit.awb.ws.restapi.gen;

import de.enflexit.awb.ws.restapi.gen.handler.ApiException;
import de.enflexit.awb.ws.restapi.gen.handler.ApiClient;
import de.enflexit.awb.ws.restapi.gen.handler.ApiResponse;
import de.enflexit.awb.ws.restapi.gen.handler.Configuration;
import de.enflexit.awb.ws.restapi.gen.handler.Pair;

import jakarta.ws.rs.core.GenericType;

import de.enflexit.awb.ws.restapi.gen.model.ExecutionState;
import de.enflexit.awb.ws.restapi.gen.model.SystemInformation;
import de.enflexit.awb.ws.restapi.gen.model.SystemLoad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-01T18:30:17.301645800+02:00[Europe/Berlin]")
public class AdminsApi {
  private ApiClient apiClient;

  public AdminsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public AdminsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get the API client
   *
   * @return API client
   */
  public ApiClient getApiClient() {
    return apiClient;
  }

  /**
   * Set the API client
   *
   * @param apiClient an instance of API client
   */
  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Returns system information
   * Returns Hardware and system  inforamtion. 
   * @return SystemInformation
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> AWB-State </td><td>  -  </td></tr>
     </table>
   * See description on GitBook
   * @see <a href="https://www.gitbook.io">Returns system information Documentation</a>
   */
  public SystemInformation infoGet() throws ApiException {
    return infoGetWithHttpInfo().getData();
  }

  /**
   * Returns system information
   * Returns Hardware and system  inforamtion. 
   * @return ApiResponse&lt;SystemInformation&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> AWB-State </td><td>  -  </td></tr>
     </table>
   * See description on GitBook
   * @see <a href="https://www.gitbook.io">Returns system information Documentation</a>
   */
  public ApiResponse<SystemInformation> infoGetWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("applicaion/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    String[] localVarAuthNames = new String[] {"AwbApiKey"};
    GenericType<SystemInformation> localVarReturnType = new GenericType<SystemInformation>() {};
    return apiClient.invokeAPI("AdminsApi.infoGet", "/info", "GET", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               localVarAuthNames, localVarReturnType, false);
  }
  /**
   * Returns the current System load
   * Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes 
   * @return SystemLoad
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> System Load </td><td>  -  </td></tr>
     </table>
   */
  public SystemLoad loadGet() throws ApiException {
    return loadGetWithHttpInfo().getData();
  }

  /**
   * Returns the current System load
   * Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes 
   * @return ApiResponse&lt;SystemLoad&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> System Load </td><td>  -  </td></tr>
     </table>
   */
  public ApiResponse<SystemLoad> loadGetWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("applicaion/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    String[] localVarAuthNames = new String[] {"AwbApiKey"};
    GenericType<SystemLoad> localVarReturnType = new GenericType<SystemLoad>() {};
    return apiClient.invokeAPI("AdminsApi.loadGet", "/load", "GET", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               localVarAuthNames, localVarReturnType, false);
  }
  /**
   * Returns the current AWB state
   * Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other. 
   * @return ExecutionState
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> AWB-State </td><td>  -  </td></tr>
     </table>
   * See description on GitBook
   * @see <a href="https://www.gitbook.io">Returns the current AWB state Documentation</a>
   */
  public ExecutionState stateGet() throws ApiException {
    return stateGetWithHttpInfo().getData();
  }

  /**
   * Returns the current AWB state
   * Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other. 
   * @return ApiResponse&lt;ExecutionState&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table summary="Response Details" border="1">
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> AWB-State </td><td>  -  </td></tr>
     </table>
   * See description on GitBook
   * @see <a href="https://www.gitbook.io">Returns the current AWB state Documentation</a>
   */
  public ApiResponse<ExecutionState> stateGetWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("applicaion/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    String[] localVarAuthNames = new String[] {"AwbApiKey"};
    GenericType<ExecutionState> localVarReturnType = new GenericType<ExecutionState>() {};
    return apiClient.invokeAPI("AdminsApi.stateGet", "/state", "GET", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               localVarAuthNames, localVarReturnType, false);
  }
}
