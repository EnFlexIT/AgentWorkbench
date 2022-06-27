package de.enflexit.awb.ws.credential;

public enum LocationInRequest {

	HEADER("HEADER"), BODY("BODY");

	private final String locationInRequest;

	private LocationInRequest(String apiKeyLocation) {
		this.locationInRequest = apiKeyLocation;
	}

	public String getLocationInRequestAsString() {
		return locationInRequest;
	}
}
