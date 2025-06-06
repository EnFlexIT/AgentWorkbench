package de.enflexit.awb.core.environment;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.language.Language;
import jade.core.Agent;

/**
 * This class has to be used in order to hand over a predefined environment model
 * with its visual representation and tools to Agent.Workbench.<br>
 * In order to add such constructions to Agent.GUI use the methods in the class 
 * GlobalInfo (accessible at runtime by using Application.RunInfo) 
 * 
 * @see Application#getGlobalInfo()
 * @see GlobalInfo#addEnvironmentType(EnvironmentType)
 * @see GlobalInfo#removeEnvironmentType(EnvironmentType)
 * @see GlobalInfo#removeEnvironmentType(String)
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentType {

	private String internalKey = null;
	private String displayName = null;
	private String displayNameLanguage = null;
	private Class<? extends EnvironmentController> environmentController = null;
	private Class<? extends Agent> displayAgentClass = null;
	
	/**
	 * Constructor for this class.
	 *
	 * @param key a unique identifier for the environment model type
	 * @param displayName A name that will be displayed later on
	 * @param displayNameLanguage the display name language
	 * @param controller the controller class of the environment 
	 * @param agentClass the agent class for displaying the environment during runtime
	 */
	public EnvironmentType(String key, String displayName, String displayNameLanguage, Class<? extends EnvironmentController> controller, Class<? extends Agent> agentClass) {
		this.internalKey = key;
		this.displayName = displayName;
		this.displayNameLanguage = displayNameLanguage;
		this.environmentController = controller;
		this.displayAgentClass = agentClass;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
	
		if (object instanceof EnvironmentType) {
			EnvironmentType envType = (EnvironmentType) object;
			if (envType.getInternalKey().equals(this.getInternalKey())==false) {
				return false; 
			}
			if (envType.getDisplayName().equals(this.getDisplayName())==false) {
				return false; 
			}
			if (!(envType.getEnvironmentControllerClass()==null & this.getEnvironmentControllerClass()==null)) {
				if (envType.getEnvironmentControllerClass().equals(this.getEnvironmentControllerClass())==false) {
					return false; 
				}
			}
			if (!(envType.getDisplayAgentClass()==null & this.getDisplayAgentClass()==null)) {
				if (envType.getDisplayAgentClass().equals(this.getDisplayAgentClass())==false) {
					return false; 
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @return the displayName as representation for this EnvironmentType 
	 */
	@Override
	public String toString() {
		if (this.displayNameLanguage==null) {
			return this.displayName;
		} else {
			return Language.translate(this.displayName, this.displayNameLanguage);
		}
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
	 * @return the displayPanelClass
	 */
	public Class<? extends EnvironmentController> getEnvironmentControllerClass() {
		return environmentController;
	}
	
	/**
	 * Sets the environment controller class.
	 * @param controllerClass the new environment controller class
	 */
	public void setEnvironmentControllerClass(Class<? extends EnvironmentController> controllerClass) {
		this.environmentController = controllerClass;
	}

	/**
	 * @param displayAgentClass the displayAgent to set
	 */
	public void setDisplayAgentClass(Class<? extends Agent> displayAgentClass) {
		this.displayAgentClass = displayAgentClass;
	}
	/**
	 * @return the displayAgent
	 */
	public Class<? extends Agent> getDisplayAgentClass() {
		return displayAgentClass;
	}
	
}
