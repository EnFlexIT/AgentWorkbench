package de.enflexit.awb.core.ui;

/**
 * This interface specifies the service definition for the AWB perspective service.
 * 
 * @author Daniel Bormann - EnFlex.IT GmbH
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbPerspectiveService {

	/**
	 * Has to return the human-readable name of the perspective.
	 * @return the name of the perspective
	 */
	public String getName();
	
	/**
	 * Has to return the AwbUiConfiguration to be used with the perspective.
	 * @return the AwbUiConfiguration to be used or <code>null</code>
	 */
	public AwbUiConfiguration getAwbUiConfiguration();
	
	/**
	 * May return an AwbUiExtension (as for example a MainWindowExtension) to be used for the perspective.
	 * @return the AwbUiExtension to be used or <code>null</code>
	 */
	public AwbUiExtension getAwbUiExtension();
	
}
