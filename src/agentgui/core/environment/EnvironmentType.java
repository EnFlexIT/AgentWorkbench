/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.environment;

import jade.core.Agent;
import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;

/**
 * This class has to be used in order to hand over a predefined environment model
 * with its visual representation and tools to Agent.GUI.<br>
 * In order to add such constructions to Agent.GUI use the methods in the class 
 * GlobalInfo (accessible at runtime by using Application.RunInfo) 
 * 
 * @see Application#RunInfo
 * @see GlobalInfo#addEnvironmentType(EnvironmentType)
 * @see GlobalInfo#removeEnvironmentType(EnvironmentType)
 * @see GlobalInfo#removeEnvironmentType(String)
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentType {

	private String internalKey = null;
	private String displayName = null;
	private Class<? extends EnvironmentPanel> displayPanelClass = null;
	private Class<? extends Agent> displayAgentClass = null;
	
	/**
	 * Constructor for this class.
	 * 
	 * @param key A unique identifier for the environment model type  
	 * @param displayName A name that will be displayed later on
	 * @param panelClass The panel on which all components have to be placed in order to allow end users to define their own environment model
	 */
	public EnvironmentType(String key, String displayName, Class<? extends EnvironmentPanel> panelClass, Class<? extends Agent> agentClass) {
		this.internalKey = key;
		this.displayName = displayName;
		this.displayPanelClass = panelClass;
		this.displayAgentClass = agentClass;
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
	 * @return the displayPanelClass
	 */
	public Class<? extends EnvironmentPanel> getDisplayPanelClass() {
		return displayPanelClass;
	}
	/**
	 * @param displayPanelClass the displayPanel to set
	 */
	public void setDisplayPanelClass(Class<? extends EnvironmentPanel> displayPanelClass) {
		this.displayPanelClass = displayPanelClass;
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
