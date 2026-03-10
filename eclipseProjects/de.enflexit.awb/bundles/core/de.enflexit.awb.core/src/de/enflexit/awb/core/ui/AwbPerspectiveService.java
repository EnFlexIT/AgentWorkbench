package de.enflexit.awb.core.ui;

/**
 * This interface specifies the service definition for the AWB perspective service.
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface AwbPerspectiveService {

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the awb ui configuration.
	 * @return the awb ui configuration
	 */
	public AwbUiConfiguration getAwbUiConfiguration();
}
