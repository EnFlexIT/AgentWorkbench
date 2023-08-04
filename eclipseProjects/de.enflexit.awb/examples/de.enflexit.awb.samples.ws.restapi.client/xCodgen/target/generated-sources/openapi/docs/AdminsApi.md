# AdminsApi

All URIs are relative to *http://localhost:8080/api*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**infoGet**](AdminsApi.md#infoGet) | **GET** /info | Returns system information |
| [**loadGet**](AdminsApi.md#loadGet) | **GET** /load | Returns the current System load |
| [**stateGet**](AdminsApi.md#stateGet) | **GET** /state | Returns the current AWB state |



## infoGet

> SystemInformation infoGet()

Returns system information

Returns Hardware and system  inforamtion.


### Example

```java
// Import classes:
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiException;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.Configuration;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.auth.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.model.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");
        
        // Configure API key authorization: AwbApiKey
        ApiKeyAuth AwbApiKey = (ApiKeyAuth) defaultClient.getAuthentication("AwbApiKey");
        AwbApiKey.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //AwbApiKey.setApiKeyPrefix("Token");

        AdminsApi apiInstance = new AdminsApi(defaultClient);
        try {
            SystemInformation result = apiInstance.infoGet();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AdminsApi#infoGet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SystemInformation**](SystemInformation.md)

### Authorization

[AwbApiKey](../README.md#AwbApiKey)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: applicaion/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | AWB-State |  -  |


## loadGet

> SystemLoad loadGet()

Returns the current System load

Returns the current system load measured by Agent.Workbench that includes CPU-, memory- and Java Heap - load. Further, the number of threads and agents will be returnes


### Example

```java
// Import classes:
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiException;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.Configuration;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.auth.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.model.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");
        
        // Configure API key authorization: AwbApiKey
        ApiKeyAuth AwbApiKey = (ApiKeyAuth) defaultClient.getAuthentication("AwbApiKey");
        AwbApiKey.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //AwbApiKey.setApiKeyPrefix("Token");

        AdminsApi apiInstance = new AdminsApi(defaultClient);
        try {
            SystemLoad result = apiInstance.loadGet();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AdminsApi#loadGet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SystemLoad**](SystemLoad.md)

### Authorization

[AwbApiKey](../README.md#AwbApiKey)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: applicaion/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | System Load |  -  |


## stateGet

> ExecutionState stateGet()

Returns the current AWB state

Returns the current state of Agent.Workbench consisiting information  about the execution mode, the currently open project and other.


### Example

```java
// Import classes:
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiClient;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.ApiException;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.Configuration;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.auth.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.handler.model.*;
import de.enflexit.awb.samples.ws.restapi.client.gen.AdminsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api");
        
        // Configure API key authorization: AwbApiKey
        ApiKeyAuth AwbApiKey = (ApiKeyAuth) defaultClient.getAuthentication("AwbApiKey");
        AwbApiKey.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //AwbApiKey.setApiKeyPrefix("Token");

        AdminsApi apiInstance = new AdminsApi(defaultClient);
        try {
            ExecutionState result = apiInstance.stateGet();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AdminsApi#stateGet");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**ExecutionState**](ExecutionState.md)

### Authorization

[AwbApiKey](../README.md#AwbApiKey)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: applicaion/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | AWB-State |  -  |

