package de.enflexit.awb.remoteControl;

import com.google.gson.Gson;

/**
 * This class describes a status update from the AWB to an {@link AwbRemoteControl} implementation.
 * It consists of a mandatory {@link AwbState} and an optional description string.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class AwbStatusUpdate {
	
	/**
	 * Possible AWB states for the status update.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum AwbState {
		AWB_READY, PROJECT_READY, SETUP_READY, SIMULATION_READY, SIMULATION_STEP_DONE, SIMULATION_FINISHED, AWB_ERROR;
	}
	
	private AwbState awbState;
	private String stateDetails;

	/**
	 * Gets the awb state.
	 * @return the awb state
	 */
	public AwbState getAwbState() {
		return awbState;
	}

	/**
	 * Sets the awb state.
	 * @param awbState the new awb state
	 */
	public void setAwbState(AwbState awbState) {
		this.awbState = awbState;
	}

	/**
	 * Gets the state details.
	 * @return the state details
	 */
	public String getStateDetails() {
		return stateDetails;
	}

	/**
	 * Sets the state details.
	 * @param stateDetails the new state details
	 */
	public void setStateDetails(String stateDetails) {
		this.stateDetails = stateDetails;
	}
	
	/**
	 * Creates the corresponding JSON String for this {@link AwbStatusUpdate}. 
	 * @return the string
	 */
	public String toJsonString() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}
	
	/**
	 * Creates a new {@link AwbStatusUpdate} from the provided JSON string.
	 * @param jsonString the json string
	 * @return the awb status update
	 */
	public static AwbStatusUpdate fromJsonString(String jsonString) {
		Gson gson = new Gson();
		AwbStatusUpdate awbStatusUpdate = gson.fromJson(jsonString, AwbStatusUpdate.class);
		return awbStatusUpdate;
	}
	
}
