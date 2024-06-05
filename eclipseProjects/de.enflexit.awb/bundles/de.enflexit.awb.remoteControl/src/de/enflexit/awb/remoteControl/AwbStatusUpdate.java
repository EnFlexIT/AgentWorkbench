package de.enflexit.awb.remoteControl;

import com.google.gson.Gson;

public class AwbStatusUpdate {
	public enum AwbState {
		AWB_READY, PROJECT_READY, SETUP_READY, SIMULATION_READY, SIMULATION_STEP_DONE, SIMULATION_FINISHED, AWB_ERROR;
	}
	
	private AwbState awbState;
	
	private String stateDetails;

	public AwbState getAwbState() {
		return awbState;
	}

	public void setAwbState(AwbState awbState) {
		this.awbState = awbState;
	}

	public String getStateDetails() {
		return stateDetails;
	}

	public void setStateDetails(String stateDetails) {
		this.stateDetails = stateDetails;
	}
	
	public String toJsonString() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		return jsonString;
	}
	
	public static AwbStatusUpdate fromJsonString(String jsonString) {
		Gson gson = new Gson();
		AwbStatusUpdate awbStatusUpdate = gson.fromJson(jsonString, AwbStatusUpdate.class);
		return awbStatusUpdate;
	}
	
}
