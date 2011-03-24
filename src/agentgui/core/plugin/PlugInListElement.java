package agentgui.core.plugin;

/**
 * 
 * @author Christian Derksen
 */
public class PlugInListElement {

	private String plugInClassReference = null;
	private String plugInName = null;
	
	/**
	 * Constructor that needs the Name of a PlugIn and the 
	 * reference to its class
	 */
	public PlugInListElement(String name, String ref) {
		this.plugInClassReference = ref;
		this.plugInName = name;
	}

	/**
	 * Returns the String representation for a plugIn
	 */
	@Override
	public String toString() {
		return plugInName + " [" + plugInClassReference + "]"; 
	}
	
	/**
	 * Will do the object comparison 
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			PlugInListElement pile = (PlugInListElement) obj;
			if (this.getPlugInName().equals(pile.getPlugInName()) 
				&& this.getPlugInClassReference().equals(pile.getPlugInClassReference())) {
				return true;
			}
			
		} catch (Exception err) {
			err.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param plugInClassReference the plugInClassReference to set
	 */
	public void setPlugInClassReference(String plugInClassReference) {
		this.plugInClassReference = plugInClassReference;
	}

	/**
	 * @return the plugInClassReference
	 */
	public String getPlugInClassReference() {
		return plugInClassReference;
	}

	/**
	 * @param plugInName the plugInName to set
	 */
	public void setPlugInName(String plugInName) {
		this.plugInName = plugInName;
	}

	/**
	 * @return the plugInName
	 */
	public String getPlugInName() {
		return plugInName;
	}
}
