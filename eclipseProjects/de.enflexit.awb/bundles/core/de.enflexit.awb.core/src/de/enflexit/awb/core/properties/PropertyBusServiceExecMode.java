package de.enflexit.awb.core.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.DeviceAgentDescription;
import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.awb.core.project.PlatformJadeConfig;
import de.enflexit.awb.core.project.PlatformJadeConfig.MTP_Creation;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.PropertyMessage.MessageType;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceExecMode.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceExecMode implements PropertyBusService {

	// --- Key constants --------------------------------------------------------------------------
	private static final String EXEC_MODE = "exec.mode";
	private static final String SERVER_MASTER_URL = "server.master.url";
	private static final String SERVER_MASTER_PORT = "server.master.port";
	private static final String SERVER_MASTER_PORT_MTP = "server.master.port.mtp";
	private static final String LOCAL_MTP_CREATION = "local.mtp.creation";
	private static final String LOCAL_URL = "local.mtp.url";
	private static final String LOCAL_MTP_PORT = "local.mtp.port";
	private static final String LOCAL_MTP_PROTOCOL = "local.mtp.protocol";
	private static final String FACTORY_ID = "factory_id";
	private static final String SERVICE_SETUP = "service.service_setup";
	private static final String DEVICE_EXEC_MODE = "device_system_exec_mode";
	private static final String SELECTED_PROJECT = "embeddedsystem.project";
	private static final String AUTO_INIT_BGSYSTEM = "bgsystem.auto_init";
	private static final String SERVER_MASTER_PROTOCOL = "server.master.protocol";

	// --- The hibernate factory id which is always used for the background system ----------------
	private static final String BGSYSTEM_FACTORY = "de.enflexit.awb.bgSystem.db";
	

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {

		return "EXEC.MODE";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		
		// --- If properties aren't valid, don't apply and return false -------------------------------------
		if (this.hasValidProperties(properties) == false) return false;
		
		// --- Determine the execution mode -----------------------------------------------------------------
		String executionMode = properties.getStringValue(EXEC_MODE);
		ExecutionMode execMode;
		switch (executionMode) {
		case "APPLICATION":
			execMode = ExecutionMode.APPLICATION;
			break;
		case "SERVER":
			execMode = ExecutionMode.SERVER;
			break;
		case "SERVER_MASTER":
			execMode = ExecutionMode.SERVER_MASTER;
			break;
		case "SERVER_SLAVE":
			execMode = ExecutionMode.SERVER_SLAVE;
			break;
		case "DEVICE_SYSTEM":
			execMode = ExecutionMode.DEVICE_SYSTEM;
			break;
		default:
			properties.setPropertyMessage(MessageType.Error, "Invalid execution mode");
			return false;
		}
		
		// --- Extract the common values --------------------------------------------------------------------
		String serverMasterUrl = properties.getStringValue(SERVER_MASTER_URL) == null ? "" : properties.getStringValue(SERVER_MASTER_URL);
		Integer serverMasterPort = properties.getIntegerValue(SERVER_MASTER_PORT) == null ? 1099 : properties.getIntegerValue(SERVER_MASTER_PORT);
		Integer serverMasterPortMtp = properties.getIntegerValue(SERVER_MASTER_PORT_MTP) == null ? 7778 : properties.getIntegerValue(SERVER_MASTER_PORT_MTP);
		MtpProtocol serverMasterProtocol = properties.getStringValue(SERVER_MASTER_PROTOCOL).equals("HTTP") ? MtpProtocol.HTTP : MtpProtocol.HTTPS;
		
		MTP_Creation localMtpCreation = properties.getStringValue(LOCAL_MTP_CREATION).equals("ConfiguredByJADE") ? MTP_Creation.ConfiguredByJADE : MTP_Creation.ConfiguredByIPandPort;
		MtpProtocol localMtpProtocol = properties.getStringValue(LOCAL_MTP_PROTOCOL).equals("HTTP") ? MtpProtocol.HTTP : MtpProtocol.HTTPS;
		Integer localMtpPort = properties.getIntegerValue(LOCAL_MTP_PORT) == null ? 7778 : properties.getIntegerValue(LOCAL_MTP_PORT);
		String localUrl = properties.getStringValue(LOCAL_URL);		

		// --- Set new values -------------------------------------------------------------------------------
		Application.getGlobalInfo().setServerMasterURL(serverMasterUrl);
		Application.getGlobalInfo().setServerMasterPort(serverMasterPort);
		Application.getGlobalInfo().setServerMasterPort4MTP(serverMasterPortMtp);
		Application.getGlobalInfo().setServerMasterProtocol(serverMasterProtocol);
		
		Application.getGlobalInfo().setOwnMtpCreation(localMtpCreation);
		Application.getGlobalInfo().setMtpProtocol(localMtpProtocol);
		Application.getGlobalInfo().setOwnMtpPort(localMtpPort);
		Application.getGlobalInfo().setOwnMtpIP(localUrl);
		
		// --- Background mode ------------------------------------------------------------------------------
		if (execMode == ExecutionMode.SERVER_MASTER || execMode == ExecutionMode.SERVER_SLAVE) {
			// --- In background mode auto run has to be true -----------------------------------------------
			Application.getGlobalInfo().setServerAutoRun(true);
		}
		
		// --- Device system mode ---------------------------------------------------------------------------
		else {
			// --- Common values ----------------------------------------------------------------------------
			String project = properties.getStringValue(SELECTED_PROJECT);
			Application.getGlobalInfo().setDeviceServiceProjectFolder(project);
			
			String deviceSystemExecModeString = properties.getStringValue(DEVICE_EXEC_MODE).toString();
			DeviceSystemExecutionMode deviceExecMode = deviceSystemExecModeString.equals(DeviceSystemExecutionMode.AGENT.toString()) ? DeviceSystemExecutionMode.AGENT : DeviceSystemExecutionMode.SETUP;
			Application.getGlobalInfo().setDeviceServiceExecutionMode(deviceExecMode);
			
			if (deviceExecMode.equals(DeviceSystemExecutionMode.SETUP)) {
				
				// --- Set values specific to DeviceSystemExecutionMode.SETUP -------------------------------
				String projectSetup = properties.getStringValue(SERVICE_SETUP);
				Application.getGlobalInfo().setDeviceServiceSetupSelected(projectSetup);
				
			} else {
				
				// --- Set values specific to DeviceSystemExecutionMode.AGENT -------------------------------
				int agentCounter = 0;
				Vector<DeviceAgentDescription> agents2Set = new Vector<>();
				
				while (properties.getStringValue("embeddedsystem.agent[" + agentCounter + "].classname") != null && properties.getStringValue("embeddedsystem.agent[" + agentCounter + "].agentname")!= null) {
					// --- Extract the next agent name and class and add them to the vector -----------------
					String agentName = properties.getStringValue("embeddedsystem.agent[" + agentCounter + "].agentname");
					String agentClass = properties.getStringValue("embeddedsystem.agent[" + agentCounter + "].classname");
					
					agents2Set.add(new DeviceAgentDescription(agentName, agentClass));
					agentCounter++;
				}
				Application.getGlobalInfo().setDeviceServiceAgents(agents2Set);
			}
		}
		
		Application.getGlobalInfo().setExecutionMode(execMode);
		Application.restart();
		
		return true;
	}

	/**
	 * Checks for valid properties. Adds an error message to the properties2check if invalid values are found.
	 *
	 * @param properties2check the properties 2 check
	 * @return true, if successful
	 */
	private boolean hasValidProperties(Properties properties2check) {

		List<String> invalidValues = new ArrayList<>();

		String execMode = properties2check.getStringValue(EXEC_MODE);
		if (execMode == null || execMode.isBlank()) {
			invalidValues.add("Execution mode is missing");
		}
		
		// --------------------------------------------------------------------------------------------------
		// --- Do the necessary checks for the background modes ---------------------------------------------
		// --------------------------------------------------------------------------------------------------
		if (execMode.equals(ExecutionMode.SERVER.toString()) || execMode.equals(ExecutionMode.SERVER_MASTER.toString()) || execMode.equals(ExecutionMode.SERVER_SLAVE.toString())) {
			
			// --- Extract all the necessary values for background mode -------------------------------------
			String serverMasterUrl = properties2check.getStringValue(SERVER_MASTER_URL);
			Integer serverMasterPort = properties2check.getIntegerValue(SERVER_MASTER_PORT);
			Integer serverMasterPortMtp = properties2check.getIntegerValue(SERVER_MASTER_PORT_MTP);
			String serverMasterProtocol = properties2check.getStringValue(SERVER_MASTER_PROTOCOL);
			
			String localMtpCreation = properties2check.getStringValue(LOCAL_MTP_CREATION);
			String localMtpProtocol = properties2check.getStringValue(LOCAL_MTP_PROTOCOL);
			Integer localMtpPort = properties2check.getIntegerValue(LOCAL_MTP_PORT);
			String localUrl = properties2check.getStringValue(LOCAL_URL);
			
			// --- Check the server.master values -----------------------------------------------------------
			if (serverMasterUrl == null || serverMasterUrl.isBlank()) {
				invalidValues.add("server.master url is missing");
			}
			if (serverMasterPort == null || serverMasterPort < 0) {
				invalidValues.add("Server.master port is missing or invalid");
			}
			if (serverMasterPort == null || serverMasterPortMtp < 0 ) {
				invalidValues.add("Server.master.mtp port is missing or invalid");
			}
			if (serverMasterProtocol == null || serverMasterProtocol.isBlank()) {
				invalidValues.add("Server.master protocol is missing");
			}
			// --- Check the local values -------------------------------------------------------------------
			if (localMtpCreation == null || localMtpCreation.isBlank()) {
				invalidValues.add("Local mtp creation is missing");
			}
			if (localMtpProtocol == null || localMtpProtocol.isBlank()) {
				invalidValues.add("local mtp protocol is missing");
			}
			if (localMtpPort == null || localMtpPort < 0) {
				invalidValues.add("Local mtp port is missing or invalid");
			}
			if (localUrl == null || localUrl.isBlank()) {
				invalidValues.add("local URL is missing");
			}
			
			
		// --------------------------------------------------------------------------------------------------	
		// --- Do the necessary checks for device system mode -----------------------------------------------	
		// --------------------------------------------------------------------------------------------------			
		} else if (execMode.equals(ExecutionMode.DEVICE_SYSTEM.toString())) {
			
			String deviceExecMode = properties2check.getStringValue(DEVICE_EXEC_MODE);
			if (deviceExecMode == null || deviceExecMode.isBlank()) {
				invalidValues.add("Device execution mode is missing");
			}
			
			String project = properties2check.getStringValue(SELECTED_PROJECT);
			if (project == null || project.isBlank()) {
				invalidValues.add("Project is missing");
			}
			
			// --- DeviceSystemExecutionMode.SETUP ----------------------------------------------------------
			if (deviceExecMode.equals(DeviceSystemExecutionMode.SETUP.toString())) {
				
				// --- Check if the service setup is defined ------------------------------------------------
				String serviceSetup = properties2check.getStringValue(SERVICE_SETUP);
				if (serviceSetup == null || serviceSetup.isBlank()) {
					invalidValues.add("Service setup is missing");
				}
				
			// --- DeviceSystemExecutionMode.AGENT ----------------------------------------------------------
			} else if (deviceExecMode.equals(DeviceSystemExecutionMode.AGENT.toString())) {
				
				// --- Check if at least one agent is specified ---------------------------------------------
				String agentName = properties2check.getStringValue("embeddedsystem.agent[" + 0 + "].classname");
				String agentClass = properties2check.getStringValue("embeddedsystem.agent[" + 0 + "].agentname");
				
				// TODO More criteria for valid agents??

				if (agentName == null || agentClass == null) {
					invalidValues.add("At least one Agent has to be specified in agent mode");
				}
				
			} else {
				// --- If deviceSystemExecutionMode is neither "Setup" nor "Agent", it must be invalid ------
				invalidValues.add("Device system execution mode is invalid");
			}
		}
		
		// --- If invalid values were found, add an error message to the properties -------------------------
		if (invalidValues.size() > 0) {
			properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, String.join(", ", invalidValues));
			return false;
		}
		
		return true;
	}
	

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();

		// --- Get the properties required for all execution modes --------------------------------
		this.getPropertiesForAllModes(properties);
		
		ExecutionMode execMode = Application.getGlobalInfo().getExecutionMode();
		properties.setStringValue(EXEC_MODE, execMode.toString());
		// --- Decide which additional properties are needed, based on execution mode -------------
		switch (execMode) {
		case APPLICATION:
			break;
		case DEVICE_SYSTEM:
			this.getPropertiesForDeviceSystemMode(properties);
			break;
			// --- Every server mode requires the same properties ---------------------------------
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.getPropertiesForBackgroundMode(properties);
			break;
		default:
			break;
		}
		
		return properties;
	}

	/**
	 * Gets the properties which are required for all execution modes.
	 *
	 * @param properties the properties
	 * @return the properties for all modes
	 */
	private void getPropertiesForAllModes(Properties properties) {
		
		// --- Get the Server.Master values -------------------------------------------------------
		String serverMasterUrl = Application.getGlobalInfo().getServerMasterURL();
		Integer serverMasterPort = Application.getGlobalInfo().getServerMasterPort();
		Integer serverMasterPortMtp = Application.getGlobalInfo().getServerMasterPort4MTP();
		String serverMasterProtocol = Application.getGlobalInfo().getServerMasterProtocol().toString();
		
		// --- Get the local MTP config -----------------------------------------------------------
		MTP_Creation localMtpCreation = Application.getGlobalInfo().getOwnMtpCreation();
		String localUrl;
		if (localMtpCreation == MTP_Creation.ConfiguredByJADE) {
			localUrl = PlatformJadeConfig.MTP_IP_AUTO_Config;
		} else {
			localUrl = Application.getGlobalInfo().getOwnMtpIP();
		}
		Integer localMtpPort = Application.getGlobalInfo().getOwnMtpPort();
		String localMtpProtocol = Application.getGlobalInfo().getMtpProtocol().toString();
		
		// --- Set property values ----------------------------------------------------------------
		properties.setStringValue(SERVER_MASTER_URL, serverMasterUrl);
		properties.setIntegerValue(SERVER_MASTER_PORT, serverMasterPort);
		properties.setIntegerValue(SERVER_MASTER_PORT_MTP, serverMasterPortMtp);
		properties.setStringValue(SERVER_MASTER_PROTOCOL, serverMasterProtocol);
		properties.setStringValue(LOCAL_MTP_CREATION, localMtpCreation.toString());
		properties.setIntegerValue(LOCAL_MTP_PORT, localMtpPort);
		properties.setStringValue(LOCAL_MTP_PROTOCOL, localMtpProtocol);
		properties.setStringValue(LOCAL_URL, localUrl);
		
	}
	
	/**
	 * Gets the properties for background mode.
	 *
	 * @param properties the properties
	 * @return the properties for background mode
	 */
	private void getPropertiesForBackgroundMode(Properties properties) {
		
		// --- Both values are constants ----------------------------------------------------------
		properties.setStringValue(FACTORY_ID, BGSYSTEM_FACTORY);
		properties.setBooleanValue(AUTO_INIT_BGSYSTEM, true);
	}

	/**
	 * Gets the properties for device system mode.
	 *
	 * @param properties the properties
	 * @return the properties for device system mode
	 */
	private void getPropertiesForDeviceSystemMode(Properties properties) {
		
		// --- Get the selected project and device system execution mode --------------------------
		DeviceSystemExecutionMode deviceExecMode = Application.getGlobalInfo().getDeviceServiceExecutionMode();
		String project = Application.getGlobalInfo().getDeviceServiceProjectFolder();
		
		properties.setStringValue(SELECTED_PROJECT, project);
		properties.setStringValue(DEVICE_EXEC_MODE, deviceExecMode.toString());
		
		if (deviceExecMode == DeviceSystemExecutionMode.SETUP) {
			// --- In setup mode, get the selected service setup ----------------------------------
			String serviceSetup = Application.getGlobalInfo().getDeviceServiceSetupSelected();
			properties.setStringValue(SERVICE_SETUP, serviceSetup);
			
		} else {
			// --- In agent mode, get all selected agent classes and their names ------------------
			Vector<DeviceAgentDescription> embeddedSystemAgents = Application.getGlobalInfo().getDeviceServiceAgents();
			for (int i = 0; i< embeddedSystemAgents.size(); i++) {
				properties.setStringValue("embeddedsystem.agent["+ i +"].classname", embeddedSystemAgents.get(i).getAgentClass());
				properties.setStringValue("embeddedsystem.agent["+ i +"].agentname", embeddedSystemAgents.get(i).getAgentName());
			}
		}
	}

}