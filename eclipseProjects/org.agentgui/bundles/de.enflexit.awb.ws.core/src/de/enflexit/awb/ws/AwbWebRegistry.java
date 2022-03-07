package de.enflexit.awb.ws;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;

/**
 * The Class AwbWebRegistry stores all OSGI registered {@link AwbWebServerService}s and {@link AwbWebHandlerService}s
 * for the start and the stop along the execution of Agent.Workbench.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebRegistry {

	private List<AwbWebServerService> registeredWebServer;
	private List<AwbWebHandlerService> registeredHandler;

	// ------------------------------------------------------------------------
	// --- From here, methods for web server ----------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the registered web server by server name.
	 * @return the registered web server
	 */
	public List<AwbWebServerService> getRegisteredWebServer() {
		if (registeredWebServer==null) {
			registeredWebServer = new ArrayList<AwbWebServerService>();
		}
		return registeredWebServer;
	}
	/**
	 * Adds the specified {@link AwbWebServerService} to the registry.
	 *
	 * @param newServer the new server to register
	 * @return true, if successful
	 */
	public boolean addAwbWebServerService(AwbWebServerService newServer) {
		if (this.getRegisteredWebServer().contains(newServer)==false) {
			this.getRegisteredWebServer().add(newServer);
		}
		return true;
	}
	/**
	 * Removes the specified {@link AwbWebServerService} from the registry.
	 *
	 * @param serviceToRemove the web server service to remove
	 * @return true, if successful
	 */
	public boolean removeAwbWebServerService(AwbWebServerService serviceToRemove) {
		return this.getRegisteredWebServer().remove(serviceToRemove);
	}
	
	/**
	 * Return the list of AwbWebServerService that have the specified {@link StartOn} configuration.
	 *
	 * @param startOn the {@link StartOn} to search for
	 * @return the list of AwbWebServerService
	 */
	public List<AwbWebServerService> getAwbWebServerService(StartOn startOn) {
		List<AwbWebServerService> serverServiceList = new ArrayList<AwbWebServerService>();
		for (AwbWebServerService serverService : this.getRegisteredWebServer()) {
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
	public String isValidAwbWebServerService(AwbWebServerService serviceToCheck) {
		return this.isValidAwbWebServerService(serviceToCheck, false);
	}
	/**
	 * Checks if the specified AwbWebServerService is valid.
	 *
	 * @param serviceToCheck the service to check
	 * @param printToConsole the indicator to print to console, if an error was found 
	 * @return Null, if the service is valid, otherwise an error message
	 */
	public String isValidAwbWebServerService(AwbWebServerService serviceToCheck, boolean printToConsole) {
		
		String error = null;
		
		// --- Check for an error in the service ----------
		JettyConfiguration config = serviceToCheck.getJettyConfiguration();
		if (config==null) {
			error = "No JettyConfiguration was provided by the service implementation '" + serviceToCheck.getClass().getName() + "'.";
		}
		
		String serverName = config.getServerName();
		if (serverName==null || serverName.isBlank()) {
			error =  "No server name was specified by the service implementation '" + serviceToCheck.getClass().getName() + "'.";
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






	
	
	
}
