package de.enflexit.awb.samples.ws.restapi.client.gen;

import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiException;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiResponse;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.Configuration;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.Pair;

import javax.ws.rs.core.GenericType;

import de.enflexit.awb.samples.ws.restapi.client.gen.model.ExecutionState;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemInformation;
import de.enflexit.awb.samples.ws.restapi.client.gen.model.SystemLoad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-04-20T17:08:39.430297+02:00[Europe/Berlin]")
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
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/info";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    
    final String[] localVarAccepts = {
      "applicaion/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "AwbApiKey" };

    GenericType<SystemInformation> localVarReturnType = new GenericType<SystemInformation>() {};

    return apiClient.invokeAPI("AdminsApi.infoGet", localVarPath, "GET", localVarQueryParams, localVarPostBody,
                               localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
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
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/load";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    
    final String[] localVarAccepts = {
      "applicaion/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "AwbApiKey" };

    GenericType<SystemLoad> localVarReturnType = new GenericType<SystemLoad>() {};

    return apiClient.invokeAPI("AdminsApi.loadGet", localVarPath, "GET", localVarQueryParams, localVarPostBody,
                               localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
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
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/state";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    
    final String[] localVarAccepts = {
      "applicaion/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "AwbApiKey" };

    GenericType<ExecutionState> localVarReturnType = new GenericType<ExecutionState>() {};

    return apiClient.invokeAPI("AdminsApi.stateGet", localVarPath, "GET", localVarQueryParams, localVarPostBody,
                               localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                               localVarAuthNames, localVarReturnType, false);
  }
}
