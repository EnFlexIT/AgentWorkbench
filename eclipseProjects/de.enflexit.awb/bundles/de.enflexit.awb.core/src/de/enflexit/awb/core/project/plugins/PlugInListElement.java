package de.enflexit.awb.core.project.plugins;

/**
 * This class is used as a displayable element in JLists, as for example in the project tab 'Resources'.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInListElement {

	private String plugInClassReference;
	private String plugInName;
	
	private String plugInLoadMessage;
	
	
	/**
	 * Constructor that needs the Name of a PlugIn and the reference to its class.
	 *
	 * @param name the plugin name
	 * @param classReference2PlugIn the class reference of the AWB-plugin
	 */
	public PlugInListElement(String name, String classReference2PlugIn) {
		this.plugInClassReference = classReference2PlugIn;
		this.plugInName = name;
	}

	/**
	 * Sets the plug in class reference.
	 * @param classReference2PlugIn the plugInClassReference to set
	 */
	public void setPlugInClassReference(String classReference2PlugIn) {
		this.plugInClassReference = classReference2PlugIn;
	}
	/**
	 * Gets the plug in class reference.
	 * @return the plugInClassReference
	 */
	public String getPlugInClassReference() {
		return plugInClassReference;
	}

	/**
	 * Sets the plug in name.
	 * @param plugInName the plugInName to set
	 */
	public void setPlugInName(String plugInName) {
		this.plugInName = plugInName;
	}
	/**
	 * Gets the plug in name.
	 * @return the plugInName
	 */
	public String getPlugInName() {
		return plugInName;
	}
	
	/**
	 * Gets the plug in load message.
	 * @return the plug in load message
	 */
	public String getPlugInLoadMessage() {
		return plugInLoadMessage;
	}
	/**
	 * Sets the plug in load message.
	 * @param plugInLoadMessage the new plug in load message
	 */
	public void setPlugInLoadMessage(String plugInLoadMessage) {
		this.plugInLoadMessage = plugInLoadMessage;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String displayText = this.getPlugInName() + " [" + this.getPlugInClassReference() + "]";
		if (this.getPlugInLoadMessage()!=null && this.getPlugInLoadMessage().length()>0) {
			displayText += " - " + this.getPlugInLoadMessage();
		}
		return displayText; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareInstance) {
		if (compareInstance instanceof PlugInListElement) {
			PlugInListElement pile = (PlugInListElement) compareInstance;
			if (this.getPlugInName().equals(pile.getPlugInName()) && this.getPlugInClassReference().equals(pile.getPlugInClassReference())) {
				return true;
			}
		}
		return false;
	}
	
}
