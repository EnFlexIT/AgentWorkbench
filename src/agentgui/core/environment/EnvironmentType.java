package agentgui.core.environment;

import agentgui.core.application.Language;


public class EnvironmentType {

	private String internalKey = null;
	private String displayName = null;
	private Class<? extends EnvironmentPanel> displayPanel = null;
		
	/**
	 * Constructor for this class
	 */
	public EnvironmentType(String key, String displayName, Class<? extends EnvironmentPanel> panel) {
		this.internalKey = key;
		this.displayName = displayName;
		this.displayPanel = panel;
	}
	
	/**
	 * @return the displayName as representation for this EnvironmentType 
	 */
	@Override
	public String toString() {
		return Language.translate(this.displayName);
	}
	/**
	 * @return the internalKey
	 */
	public String getInternalKey() {
		return internalKey;
	}
	/**
	 * @param internalKey the internalKey to set
	 */
	public void setInternalKey(String internalKey) {
		this.internalKey = internalKey;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayPanel
	 */
	public Class<? extends EnvironmentPanel> getDisplayPanel() {
		return displayPanel;
	}
	/**
	 * @param displayPanel the displayPanel to set
	 */
	public void setDisplayPanel(Class<? extends EnvironmentPanel> displayPanel) {
		this.displayPanel = displayPanel;
	}
	
}
