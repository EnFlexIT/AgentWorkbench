package de.enflexit.awb.core.propertyBusServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.ApplicationSwitch;
import de.enflexit.awb.core.config.DeviceAgentDescription;
import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.awb.core.jade.NetworkAddresses;
import de.enflexit.awb.core.jade.NetworkAddresses.NetworkAddress;
import de.enflexit.awb.core.project.PlatformJadeConfig;
import de.enflexit.awb.core.project.PlatformJadeConfig.MTP_Creation;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceExecMode can be used to configure the different
 * execution modes of the agent workbench through the ApplicationPropertyBus.
 * Since certain values, like server.master URL, are essential for the background modes,
 * but unnecessary in other modes, the {@link #setProperties(Properties, String)} method 
 * replaces provided null values with default values, if the selected execution mode doesn't
 * require them specifically.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceExecMode implements PropertyBusService {

	// --- Key constants ------------------------------------------------------------------------------------
	public static final String EXEC_MODE = "exec.mode";
	public static final String SERVER_MASTER_URL = "server.master.url";
	public static final String SERVER_MASTER_PORT = "server.master.port";
	public static final String SERVER_MASTER_PORT_MTP = "server.master.port.mtp";
	public static final String LOCAL_MTP_CREATION = "local.mtp.creation";
	public static final String LOCAL_URL = "local.mtp.url";
	public static final String LOCAL_MTP_PORT = "local.mtp.port";
	public static final String LOCAL_MTP_PROTOCOL = "local.mtp.protocol";
	public static final String FACTORY_ID = "factory_id";
	public static final String SERVICE_SETUP = "service.service_setup";
	public static final String DEVICE_EXEC_MODE = "device_system_exec_mode";
	public static final String SELECTED_PROJECT = "embeddedsystem.project";
	public static final String AUTO_INIT_BGSYSTEM = "bgsystem.auto_init";
	public static final String SERVER_MASTER_PROTOCOL = "server.master.protocol";
	public final static String EMBEDDEDSYSTEM_AGENT_CLASSNAME = "embeddedsystem.agent[X].classname";
	public final static String EMBEDDEDSYSTEM_AGENT_AGENTNAME = "embeddedsystem.agent[X].agentname";


	// --- The hibernate factory id which is always used for the background system --------------------------
	public static final String BGSYSTEM_FACTORY = "de.enflexit.awb.bgSystem.db";
	
	
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
		
		// --- Determine old and new execution mode ---------------------------------------------------------
		ExecutionMode oldExecutionMode = Application.getGlobalInfo().getExecutionMode(); 
		ExecutionMode newExecutionMode = this.findExecutionModeFromString(properties.getStringValue(EXEC_MODE));
		
		// --- Extract the common values --------------------------------------------------------------------
		String serverMasterUrl = properties.getStringValue(SERVER_MASTER_URL); 
		Integer serverMasterPort = properties.getIntegerValue(SERVER_MASTER_PORT);
		Integer serverMasterPortMtp = properties.getIntegerValue(SERVER_MASTER_PORT_MTP);
		MtpProtocol serverMasterProtocol = this.findMtpProtocolFromString(properties.getStringValue(SERVER_MASTER_PROTOCOL));
		
		MTP_Creation localMtpCreation = this.findMtpCreationFromString(properties.getStringValue(LOCAL_MTP_CREATION));
		MtpProtocol localMtpProtocol = this.findMtpProtocolFromString(properties.getStringValue(LOCAL_MTP_PROTOCOL));
		Integer localMtpPort = properties.getIntegerValue(LOCAL_MTP_PORT);
		String localUrl = properties.getStringValue(LOCAL_URL);		
		
		// --- Replace non-essential null values with default values ----------------------------------------
		if (serverMasterUrl == null) {
			serverMasterUrl = "";
		}
		if (serverMasterPort == null) {
			serverMasterPort = 1099;
		}
		if (serverMasterPortMtp == null) {
			serverMasterPortMtp = 7778;
		}
		
		if (localMtpPort == null) {
			localMtpPort = 7778;
		}
		if (localUrl == null) {
			localUrl = PlatformJadeConfig.MTP_IP_AUTO_Config;
		}
		
		// --- Set common values ----------------------------------------------------------------------------
		Application.getGlobalInfo().setServerAutoRun(false);
		Application.getGlobalInfo().setServerMasterURL(serverMasterUrl);
		Application.getGlobalInfo().setServerMasterPort(serverMasterPort);
		Application.getGlobalInfo().setServerMasterPort4MTP(serverMasterPortMtp);
		Application.getGlobalInfo().setServerMasterProtocol(serverMasterProtocol);
		
		Application.getGlobalInfo().setOwnMtpCreation(localMtpCreation);
		Application.getGlobalInfo().setMtpProtocol(localMtpProtocol);
		Application.getGlobalInfo().setOwnMtpPort(localMtpPort);
		Application.getGlobalInfo().setOwnMtpIP(localUrl);
		
		// --- Set additional values based on execution mode ------------------------------------------------
		// --------------------------------------------------------------------------------------------------
		
		// --- Background mode ------------------------------------------------------------------------------
		if (newExecutionMode == ExecutionMode.SERVER) {
			// --- In background mode auto run has to be true -----------------------------------------------
			Application.getGlobalInfo().setServerAutoRun(true);
		
			
			// --- Device system mode ---------------------------------------------------------------------------
		} else if (newExecutionMode == ExecutionMode.DEVICE_SYSTEM) {
		
		
			// --- Common values ----------------------------------------------------------------------------
			String project = properties.getStringValue(SELECTED_PROJECT);
			Application.getGlobalInfo().setDeviceServiceProjectFolder(project);
			
			DeviceSystemExecutionMode deviceExecMode = this.findDeviceSystemExecutionModeFromString(properties.getStringValue(DEVICE_EXEC_MODE));
			Application.getGlobalInfo().setDeviceServiceExecutionMode(deviceExecMode);
			
			// --- Set values specific to DeviceSystemExecutionMode.SETUP -----------------------------------
			if (deviceExecMode == DeviceSystemExecutionMode.SETUP) {
				
				String projectSetup = properties.getStringValue(SERVICE_SETUP);
				Application.getGlobalInfo().setDeviceServiceSetupSelected(projectSetup);
				
				// --- Set values specific to DeviceSystemExecutionMode.AGENT -------------------------------
			} else {
				
				// --- Prepare the first agent name and class -----------------------------------------------
				int agentCounter = 0;
				Vector<DeviceAgentDescription> agents2Set = new Vector<>();
				String agentClass = properties.getStringValue(EMBEDDEDSYSTEM_AGENT_CLASSNAME.replace("X", String.valueOf(agentCounter)));
				String agentName = properties.getStringValue(EMBEDDEDSYSTEM_AGENT_AGENTNAME.replace("X", String.valueOf(agentCounter)));
				agentName = agentName.replace(" ", "_");
				
				while (agentClass != null && agentName != null) {
					agents2Set.add(new DeviceAgentDescription(agentName, agentClass));

					// --- Prepare the next agent -----------------------------------------------------------
					agentCounter++;
					agentName = properties.getStringValue(EMBEDDEDSYSTEM_AGENT_AGENTNAME.replace("X", String.valueOf(agentCounter)));
					agentClass = properties.getStringValue(EMBEDDEDSYSTEM_AGENT_CLASSNAME.replace("X", String.valueOf(agentCounter)));
					agentName = agentName.replace(" ", "_");	
				}
				Application.getGlobalInfo().setDeviceServiceAgents(agents2Set);
			}
			
		}
		// --- Save and restart -----------------------------------------------------------------------------
		Application.getGlobalInfo().setExecutionMode(newExecutionMode);
		Application.getGlobalInfo().doSaveConfiguration();
		ApplicationSwitch.switchExecutionMode(oldExecutionMode, newExecutionMode);
		
		return true;
	}

	/**
	 * Checks for valid properties in relation to the provided execution mode. 
	 * Adds an error message to the properties2check if invalid values are found.
	 *
	 * @param properties2check the properties 2 check
	 * @return true, if successful
	 */
	private boolean hasValidProperties(Properties properties2check) {
		
		List<String> invalidValues = new ArrayList<String>();
		
		// --------------------------------------------------------------------------------------------------
		// --- Checks necessary for all execution modes -----------------------------------------------------
		// --------------------------------------------------------------------------------------------------
		ExecutionMode execMode = this.findExecutionModeFromString(properties2check.getStringValue(EXEC_MODE));
		if (execMode == null) {
			invalidValues.add("Execution mode is missing or invalid");
		}
		
		// --- Make sure the mtp protocols to set are either HTTP or HTTPS ----------------------------------
		MtpProtocol serverMasterProtocol = this.findMtpProtocolFromString(properties2check.getStringValue(SERVER_MASTER_PROTOCOL));
		if (serverMasterProtocol == null) {
			invalidValues.add("server.master protocol is missing or invalid");
		}
		
		MtpProtocol localMtpProtocol = this.findMtpProtocolFromString(properties2check.getStringValue(LOCAL_MTP_PROTOCOL));
		if (localMtpProtocol == null) {
			invalidValues.add("Local mtp protocol is missing or invalid");
		}
		
		MTP_Creation mtpCreation = this.findMtpCreationFromString(properties2check.getStringValue(LOCAL_MTP_CREATION));
		if (mtpCreation == null) {
			invalidValues.add("mtp creation is missing or invalid");
		}
		
		// --- If ports are specified, make sure they are valid ---------------------------------------------
		Integer serverMasterPortMtp = properties2check.getIntegerValue(SERVER_MASTER_PORT_MTP);
		if (serverMasterPortMtp != null) {
			if (serverMasterPortMtp < 1 || serverMasterPortMtp > 65535) {
				properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, "Server.master.mtp port is invalid");
			}
		}
		
		Integer serverMasterPort = properties2check.getIntegerValue(SERVER_MASTER_PORT);
		if (serverMasterPort != null) {
			if (serverMasterPort < 1 || serverMasterPort > 65535) {
				properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, "Server.master port is invalid");
			}		
		}
			
		Integer localMtpPort = properties2check.getIntegerValue(LOCAL_MTP_PORT);
		if (localMtpPort != null) {
			if (localMtpPort < 1 || localMtpPort > 65535) {
				properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, "local mtp port is invalid");
			}
		}
		
		// --------------------------------------------------------------------------------------------------
		// --- Additional checks based on execution mode ----------------------------------------------------
		// --------------------------------------------------------------------------------------------------
		if (execMode != null) {
			
			switch (execMode) {
			case APPLICATION:
				break;
			case DEVICE_SYSTEM:
				this.hasValidPropertiesForDeviceSystemMode(properties2check, invalidValues);
				break;
				// --- Every server mode requires the same properties -----------------------------------------------
			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				this.hasValidPropertiesForBackgroundMode(properties2check, invalidValues);
				break;
			default:
				break;
			}
		}
		
		if (invalidValues.size() > 0) {
			properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, String.join(", ", invalidValues));
			return false;
		}
		return true;
	}
	
	/**
	 * Checks for valid properties for background mode.
	 *
	 * @param properties2check the properties 2 check
	 * @param invalidValues the list to add found errors to
	 * @return true, if successful
	 */
	private void hasValidPropertiesForBackgroundMode(Properties properties2check, List<String> invalidValues) {
		
		// --- Extract all the necessary values for background mode -----------------------------------------
		String serverMasterUrl = properties2check.getStringValue(SERVER_MASTER_URL);
		Integer serverMasterPort = properties2check.getIntegerValue(SERVER_MASTER_PORT);
		Integer serverMasterPortMtp = properties2check.getIntegerValue(SERVER_MASTER_PORT_MTP);
		
		Integer localMtpPort = properties2check.getIntegerValue(LOCAL_MTP_PORT);
		String localUrl = properties2check.getStringValue(LOCAL_URL);
		
		// --- Check the server.master values ---------------------------------------------------------------
		if (serverMasterUrl == null || serverMasterUrl.isBlank()) {
			invalidValues.add("server.master url is missing");
		}
		if (serverMasterPort == null || serverMasterPort < 1 || serverMasterPort > 65535) {
			invalidValues.add("Server.master port is missing or invalid");
		}
		if (serverMasterPortMtp == null || serverMasterPortMtp < 1 || serverMasterPortMtp > 65535) {
			invalidValues.add("Server.master.mtp port is missing or invalid");
		}
		// --- Check the local values -----------------------------------------------------------------------
		if (localMtpPort == null || localMtpPort < 1 || localMtpPort > 65535) {
			invalidValues.add("Local mtp port is missing or invalid");
		}
		if (localUrl == null || localUrl.isBlank()) {
			invalidValues.add("local URL is missing");
		}
		
	}
	
	/**
	 * Checks for valid properties for device system mode.
	 *
	 * @param properties2check the properties 2 check
	 * @param invalidValues the list to add found errors to 
	 * @return true, if successful
	 */
	private void hasValidPropertiesForDeviceSystemMode(Properties properties2check, List<String> invalidValues) {
		
		DeviceSystemExecutionMode deviceExecMode = this.findDeviceSystemExecutionModeFromString(properties2check.getStringValue(DEVICE_EXEC_MODE));
		if (deviceExecMode == null) {
			invalidValues.add("Device execution mode is missing or invalid");
		}
		
		// --- Check if a project is specified --------------------------------------------------------------
		String projectString = properties2check.getStringValue(SELECTED_PROJECT);
		if (projectString == null || projectString.isBlank()) {
			invalidValues.add("Project is missing");
		}
		
		if (deviceExecMode == DeviceSystemExecutionMode.SETUP) {
			
			// --- Check whether the Setup is missing  ------------------------------------------------------
			String serviceSetup = properties2check.getStringValue(SERVICE_SETUP);
			if (serviceSetup == null || serviceSetup.isBlank()) {
				invalidValues.add("Service setup is missing");
			}

		} else if (deviceExecMode == DeviceSystemExecutionMode.AGENT){
			
			int agentKeyCounter = 0;
			String agentClass;
			String agentName;
			String agentClassKey = EMBEDDEDSYSTEM_AGENT_CLASSNAME.replace("X", String.valueOf(agentKeyCounter));
			String agentNameKey = EMBEDDEDSYSTEM_AGENT_AGENTNAME.replace("X", String.valueOf(agentKeyCounter));
			List<String> identifierList = properties2check.getIdentifierList();
			
			// --- Look for the next agent key --------------------------------------------------------------
			while (identifierList.contains(agentClassKey) || identifierList.contains(agentNameKey)) {
				
				// --- Get agent name and agent class -------------------------------------------------------
				agentClass = properties2check.getStringValue(agentClassKey);
				agentName = properties2check.getStringValue(agentNameKey);
				
				// --- Both should be specified since a key was found ---------------------------------------
				if (agentName == null || agentName.isBlank()) {
					invalidValues.add("embeddedsystem.agent[" + agentKeyCounter + "].agentname is missing");
				}
				if (agentClass == null || agentClass.isBlank()) {
					invalidValues.add("embeddedsystem.agent[" + agentKeyCounter + "].agentclass is missing");
				}
				// --- Prepare the next keys ----------------------------------------------------------------
				agentKeyCounter++;
				agentClassKey = EMBEDDEDSYSTEM_AGENT_CLASSNAME.replace("X", String.valueOf(agentKeyCounter));
				agentNameKey = EMBEDDEDSYSTEM_AGENT_AGENTNAME.replace("X", String.valueOf(agentKeyCounter));
			}
			
			if (agentKeyCounter == 0) {
				// --- If agentCounter is zero, no valid agent was provided ---------------------------------
				invalidValues.add("At least one agent has to be specified in agent mode");
			}
		} 
	}
	

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();

		// --- Get the properties required for all execution modes --------------------------------
		this.getPropertiesForAllModes(properties);
		
		// --- Decide which additional properties are needed, based on execution mode -------------
		ExecutionMode execMode = Application.getGlobalInfo().getExecutionMode();
		properties.setStringValue(EXEC_MODE, execMode.toString());
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
		
		// --- Get the available network addresses ------------------------------------------------
		NetworkAddresses networkaddresses = new NetworkAddresses();
		Vector<NetworkAddress> addressSelection = networkaddresses.getNetworkAddressVector();
		int networkCounter = 0;
		for (NetworkAddress networkaddress : addressSelection) {
			properties.setStringValue("local.ip.selection[" + networkCounter + "]", networkaddress.toString());
			networkCounter++;
		}
		
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
				properties.setStringValue(EMBEDDEDSYSTEM_AGENT_CLASSNAME.replace("X", String.valueOf(i)), embeddedSystemAgents.get(i).getAgentClass());
				properties.setStringValue(EMBEDDEDSYSTEM_AGENT_AGENTNAME.replace("X", String.valueOf(i)), embeddedSystemAgents.get(i).getAgentName());
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------------------
	// --- From here, methods to find enum constants from strings -------------------------------------------
	// ------------------------------------------------------------------------------------------------------
	
	/**
	 * Returns the de.enflexit.awb.core.config.GlobalInfo.ExecutionMode equivalent for the provided string, ignoring case, or null,
	 * if no match is found. 
	 *
	 * @param executionModeString the execution mode as a string
	 * @return the execution mode
	 */
	private ExecutionMode findExecutionModeFromString(String executionModeString) {
		
		if (executionModeString==null || executionModeString.isBlank()==true) return null;
		
		ExecutionMode executionMode = null;
		try {
			executionMode = ExecutionMode.valueOf(executionModeString.toUpperCase());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return executionMode;
	}
	
	/**
	 * Find device system execution mode from string.
	 *
	 * @param deviceSystemExecutionModeString the device system execution mode string
	 * @return the device system execution mode
	 */
	private DeviceSystemExecutionMode findDeviceSystemExecutionModeFromString(String deviceSystemExecutionModeString) {
		
		if (deviceSystemExecutionModeString==null || deviceSystemExecutionModeString.isBlank()==true) return null;
		
		DeviceSystemExecutionMode deviceSystemExecutionMode = null;
		try {
			deviceSystemExecutionMode = DeviceSystemExecutionMode.valueOf(deviceSystemExecutionModeString.toUpperCase());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return deviceSystemExecutionMode;
	}
	
	/**
	 * Find MtpProtocol enum constant from string.
	 *
	 * @param mtpProtocolString the mtp protocol string
	 * @returns the MtpProtocol
	 */
	private MtpProtocol findMtpProtocolFromString(String mtpProtocolString) {
		
		if (mtpProtocolString==null || mtpProtocolString.isBlank()==true) return null;
		
		MtpProtocol mtpProtocol = null;
		try {
			mtpProtocol = MtpProtocol.valueOf(mtpProtocolString.toUpperCase());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Backup-solution --------------------------------------
		for (MtpProtocol mpc : MtpProtocol.values()) {
			if (mpc.name().toLowerCase().equals(mtpProtocolString.toLowerCase())==true) {
				mtpProtocol = mpc;
				break;
			}
		}
		
		return mtpProtocol;
	}
	
	/**
	 * Find MTP_creation enum constant from string.
	 *
	 * @param mtpCreationString the mtp creation string
	 * @returns the MTP_creation enum constant equivalent to the input string
	 */
	private MTP_Creation findMtpCreationFromString(String mtpCreationString) {
		
		if (mtpCreationString==null || mtpCreationString.isBlank()==true) return null;
		
		MTP_Creation mtpCreation = null;
		try {
			mtpCreation = MTP_Creation.valueOf(mtpCreationString);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Backup-solution --------------------------------------
		for (MTP_Creation mtpC : MTP_Creation.values()) {
			if (mtpC.name().toLowerCase().equals(mtpCreationString.toLowerCase())==true) {
				mtpCreation = mtpC;
				break;
			}
		}
		
		return mtpCreation;
	}
}