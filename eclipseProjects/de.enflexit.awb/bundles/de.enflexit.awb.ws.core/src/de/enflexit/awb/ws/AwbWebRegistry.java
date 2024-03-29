package de.enflexit.awb.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jetty.server.Handler;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.model.HandlerHelper;

/**
 * The Class AwbWebRegistry stores all OSGI registered {@link AwbWebServerService}s and {@link AwbWebHandlerService}s
 * for the start and the stop along the execution of Agent.Workbench.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebRegistry {

	private List<AwbWebServerServiceWrapper> registeredWebServer;
	private List<AwbWebHandlerService> registeredHandler;

	// ------------------------------------------------------------------------
	// --- From here, methods for web server ----------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the list of registered web server services.
	 * @return the registered web server
	 */
	public List<AwbWebServerServiceWrapper> getRegisteredWebServer() {
		if (registeredWebServer==null) {
			registeredWebServer = new ArrayList<>();
		}
		return registeredWebServer;
	}
	/**
	 * Returns the registered web server services, sorted by their name.
	 * @return the registered web server
	 */
	public List<AwbWebServerServiceWrapper> getRegisteredWebServerSorted() {
		List<AwbWebServerServiceWrapper> serverServices = this.getRegisteredWebServer();
		Collections.sort(serverServices, new Comparator<AwbWebServerServiceWrapper>() {
			@Override
			public int compare(AwbWebServerServiceWrapper serverService1, AwbWebServerServiceWrapper serverService2) {
				return serverService1.getJettyConfiguration().getServerName().compareTo(serverService2.getJettyConfiguration().getServerName());
			}
		});
		return serverServices;
	}
	/**
	 * Adds the specified {@link AwbWebServerService} to the registry.
	 *
	 * @param newServerService the new server service to register
	 * @return the AwbWebServerServiceWrapper in case that the service was added, or <code>null</code>
	 */
	public AwbWebServerServiceWrapper addAwbWebServerService(AwbWebServerService newServerService) {
		AwbWebServerServiceWrapper serverWrapper = new AwbWebServerServiceWrapper(newServerService);
		if (this.getRegisteredWebServer().contains(serverWrapper)==false) {
			this.getRegisteredWebServer().add(serverWrapper);
		} else {
			serverWrapper = null;
		}
		return serverWrapper;
	}
	/**
	 * Removes the specified {@link AwbWebServerService} from the registry.
	 *
	 * @param serviceToRemove the web server service to remove
	 * @return true, if successful
	 */
	public boolean removeAwbWebServerService(AwbWebServerService serviceToRemove) {
		AwbWebServerServiceWrapper serviceToRemoveWrapper = new AwbWebServerServiceWrapper(serviceToRemove);
		return this.getRegisteredWebServer().remove(serviceToRemoveWrapper);
	}
	
	/**
	 * Returns the wrapped server service that corresponds to the server name.
	 *
	 * @param serverName the server name
	 * @return the server service
	 */
	public AwbWebServerServiceWrapper getRegisteredWebServerService(String serverName) {
		if (serverName!=null) {
			for (AwbWebServerServiceWrapper serverService : getRegisteredWebServer()) {
				if (serverService.getJettyConfiguration().getServerName().equals(serverName)==true) return serverService;
			}
		}
		return null;
	}
	/**
	 * Return the list of AwbWebServerService that have the specified {@link StartOn} configuration.
	 *
	 * @param startOn the {@link StartOn} to search for
	 * @return the list of AwbWebServerService
	 */
	public List<AwbWebServerServiceWrapper> getAwbWebServerService(StartOn startOn) {
		List<AwbWebServerServiceWrapper> serverServiceList = new ArrayList<>();
		for (AwbWebServerServiceWrapper serverService : this.getRegisteredWebServer()) {
			// --- Check if server definition is valid --------------
			if (this.isValidAwbWebServerService(serverService, true)==null) {
				// --- Add to result list? ---------------------------
				if (serverService.getJettyConfiguration().getStartOn()==startOn) {
					serverServiceList.add(serverService);
				}
			}
		}
		return serverServiceList;
	}

	/**
	 * Checks if the specified AwbWebServerService is valid.
	 *
	 * @param serviceToCheck the service to check
	 * @return Null, if the service is valid, otherwise an error message
	 */
	public String isValidAwbWebServerService(AwbWebServerServiceWrapper serviceToCheck) {
		return this.isValidAwbWebServerService(serviceToCheck, false);
	}
	/**
	 * Checks if the specified AwbWebServerService is valid.
	 *
	 * @param serviceToCheckWrapped the wrapped service to check
	 * @param printToConsole the indicator to print to console, if an error was found 
	 * @return Null, if the service is valid, otherwise an error message
	 */
	public String isValidAwbWebServerService(AwbWebServerServiceWrapper serviceToCheckWrapped, boolean printToConsole) {
		
		String error = null;
		
		// --- Check for an error in the service ----------
		String serviceClassName = serviceToCheckWrapped.getWebServerService().getClass().getName();
		JettyConfiguration config = serviceToCheckWrapped.getJettyConfiguration();
		if (config==null) {
			error = "No JettyConfiguration was provided by the service implementation '" + serviceClassName + "'.";
		}

		String serverName = config.getServerName();
		if (error==null && (serverName==null || serverName.isBlank())) {
			error =  "No server name was specified by the service implementation '" + serviceClassName + "'.";
		}

		if (error==null && (config.getHandler()==null && config.isMutableHandlerCollection()==false)) {
			error = "No Handler was specified for server '" + serverName + "', but the option for a mutable handler collection was set to 'false' by the service implementation '" + serviceClassName + "'.";
		}
		
		// --- Print to console? --------------------------
		if (printToConsole==true && error!=null) {
			BundleHelper.systemPrintln(this, error, true);
		}
		return error;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, methods for handler on servers --------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the registered handler.
	 * @return the registered handler
	 */
	public List<AwbWebHandlerService> getRegisteredWebHandler() {
		if (registeredHandler==null) {
			registeredHandler = new ArrayList<AwbWebHandlerService>();
		}
		return registeredHandler;
	}
	/**
	 * Adds the specified {@link AwbWebHandlerService} to the registry.
	 *
	 * @param newHandler the new handler
	 * @return true, if successful
	 */
	public boolean addAwbWebHandlerService(AwbWebHandlerService newHandler) {
		if (this.getRegisteredWebHandler().contains(newHandler)==false) {
			this.getRegisteredWebHandler().add(newHandler);
		}
		return true;
	}
	/**
	 * Removes the specified {@link AwbWebHandlerService} from the registry.
	 *
	 * @param handlerToRemove the handler service to remove
	 * @return true, if successful
	 */
	public boolean removeAwbWebHandlerService(AwbWebHandlerService handlerToRemove) {
		return this.getRegisteredWebHandler().remove(handlerToRemove);
	}

	/**
	 * Return the list of AwbWebHandlerService that belong to the server specified by its name.
	 * The list returned is sorted in the order as registered.
	 *
	 * @param serverName the server name
	 * @return the list of AwbWebServerService
	 */
	public List<AwbWebHandlerService> getAwbWebHandlerService(String serverName) {
		List<AwbWebHandlerService> handlerServiceList = new ArrayList<AwbWebHandlerService>();
		for (AwbWebHandlerService handlerService : this.getRegisteredWebHandler()) {
			// --- Check if server definition is valid --------------
			if (this.isValidAwbWebHandlerService(handlerService, true)==null && handlerService.getServerNameNotNull().equals(serverName)==true) {
				handlerServiceList.add(handlerService);
			}
		}
		return handlerServiceList;
	}
	/**
	 * Return the list of AwbWebHandlerService that belong to the server, specified by its name.
 	 * The list returned is sorted by the contexPath of the corresponding handler.
	 * @param serverName the server name
	 * @return the list of AwbWebServerService
	 */
	public List<AwbWebHandlerService> getAwbWebHandlerServiceSorted(String serverName) {
		List<AwbWebHandlerService> handlerServiceList = this.getAwbWebHandlerService(serverName);
		Collections.sort(handlerServiceList, new Comparator<AwbWebHandlerService>() {
			@Override
			public int compare(AwbWebHandlerService handlerService1, AwbWebHandlerService handlerService2) {
				String contextPath1 = HandlerHelper.getContextPath(handlerService1.getHandler());
				String contextPath2 = HandlerHelper.getContextPath(handlerService2.getHandler());
				return contextPath1.compareTo(contextPath2);
			}
		});
		return handlerServiceList;
	}
	
	/**
	 * Checks if the specified AwbWebHandlerService is valid.
	 *
	 * @param serviceToCheck the service to check
	 * @return Null, if the service is valid, otherwise an error message
	 */
	public String isValidAwbWebHandlerService(AwbWebHandlerService serviceToCheck) {
		return this.isValidAwbWebHandlerService(serviceToCheck, false);
	}
	/**
	 * Checks if the specified AwbWebHandlerService is valid.
	 *
	 * @param serviceToCheck the service to check
	 * @param printToConsole the indicator to print to console, if an error was found 
	 * @return Null, if the service is valid, otherwise an error message
	 */
	public String isValidAwbWebHandlerService(AwbWebHandlerService serviceToCheck, boolean printToConsole) {
		
		String error = null;
		
		// --- Check for an error in the service ----------
		Handler handler = serviceToCheck.getHandler();
		if (handler==null) {
			error = "No Handler was provided by the service implementation '" + serviceToCheck.getClass().getName() + "'.";
		}

		// --- Print to console? --------------------------
		if (printToConsole==true && error!=null) {
			BundleHelper.systemPrintln(this, error, true);
		}
		return error;
	}
	
	
}
