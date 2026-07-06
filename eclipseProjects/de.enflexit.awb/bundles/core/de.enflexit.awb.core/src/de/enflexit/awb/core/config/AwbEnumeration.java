package de.enflexit.awb.core.config;

import de.enflexit.awb.core.config.GlobalInfo.DeviceSystemExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.core.config.GlobalInfo.MtpProtocol;
import de.enflexit.awb.core.project.PlatformJadeConfig.MTP_Creation;

/**
 * The Class AwbEnumeration provides static methods to get
 * enum constants from strings (Not case sensitive).
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbEnumeration {

	/**
	 * Returns the de.enflexit.awb.core.config.GlobalInfo.ExecutionMode equivalent for the provided string, ignoring case, or null,
	 * if no match is found. 
	 *
	 * @param executionModeString the execution mode as a string
	 * @return the execution mode
	 */
	public static ExecutionMode getExecutionMode(String executionModeString) {
		
		if (executionModeString==null || executionModeString.isBlank()==true) return null;
		
		try {
			return ExecutionMode.valueOf(executionModeString);
		} catch (Exception ex) {
		}
		
		for (ExecutionMode execMode : ExecutionMode.values()) {
			if (executionModeString.equalsIgnoreCase(execMode.name()) == true) {
				return execMode;
			}
		}
		return null;
	}
	
	/**
	 * Returns the DeviceSystemExecution mode equivalent for the provided string, ignoring case, or null,
	 * if no match is found
	 *
	 * @param deviceSystemExecutionModeString the device system execution mode string
	 * @return the device system execution mode
	 */
	public static DeviceSystemExecutionMode getDeviceSystemExecutionMode(String deviceSystemExecutionModeString) {
		
		if (deviceSystemExecutionModeString==null || deviceSystemExecutionModeString.isBlank()==true) return null;
		
		try {
			return DeviceSystemExecutionMode.valueOf(deviceSystemExecutionModeString);
		} catch (Exception ex) {
		}
		
		for (DeviceSystemExecutionMode deviceExecMode : DeviceSystemExecutionMode.values()) {
			if (deviceSystemExecutionModeString.equalsIgnoreCase(deviceExecMode.name())) {
				return deviceExecMode;
			}
		}
		return null;
	}
	
	/**
	 * Returns the MtpCreation equivalent for the provided string, ignoring case, or null,
	 * if no match is found.
	 * 
	 * @param mtpCreationString the mtp creation string
	 * @return the mtp creation
	 */
	public static MTP_Creation getMtpCreation(String mtpCreationString) {
		
		if (mtpCreationString==null || mtpCreationString.isBlank()==true) return null;
		
		try {
			return MTP_Creation.valueOf(mtpCreationString);
		} catch (Exception ex) {
		}
		
		for (MTP_Creation mtpCreation : MTP_Creation.values()) {
			if (mtpCreationString.equalsIgnoreCase(mtpCreation.name())) {
				return mtpCreation;
			}
		}
		return null;
	}
	
	/**
	 * Returns the MtpProtocol equivalent for the provided string, ignoring case, or null,
	 * if no match is found.
	 *
	 * @param mtpProtocolString the mtp protocol string
	 * @return the mtp protocol
	 */
	public static MtpProtocol getMtpProtocol(String mtpProtocolString) {
		
		if (mtpProtocolString==null || mtpProtocolString.isBlank()==true) return null;
		
		try {
			return MtpProtocol.valueOf(mtpProtocolString);
		} catch (Exception ex) {
		}
		
		for (MtpProtocol mtpProtocol : MtpProtocol.values()) {
			if (mtpProtocolString.equalsIgnoreCase(mtpProtocol.name())) {
				return mtpProtocol;
			}
		}
		return null;
	}
	/**
	 * Returns the MtpProtocol equivalent for the provided string, ignoring case, or null,
	 * if no match is found.
	 *
	 * @param embeddedSystemAgentVisualizationString the mtp protocol string
	 * @return the mtp protocol
	 */
	public static EmbeddedSystemAgentVisualisation getEmbeddedSystemAgentVisualization(String embeddedSystemAgentVisualizationString) {
		
		if (embeddedSystemAgentVisualizationString==null || embeddedSystemAgentVisualizationString.isBlank()==true) return null;
		
		try {
			return EmbeddedSystemAgentVisualisation.valueOf(embeddedSystemAgentVisualizationString);
		} catch (Exception ex) {
		}
		
		for (EmbeddedSystemAgentVisualisation esaVis : EmbeddedSystemAgentVisualisation.values()) {
			if (embeddedSystemAgentVisualizationString.equalsIgnoreCase(esaVis.name())) {
				return esaVis;
			}
		}
		return null;
	}
	
}