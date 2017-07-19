/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
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
package agentgui.core.ontologies.gui;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import agentgui.core.environment.EnvironmentController;

/**
 * The Class OntologyClassEditorJPanel.
 * 
 * @see OntologyClassVisualisation
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class OntologyClassEditorJPanel extends JPanel {

	private static final long serialVersionUID = -2371432709285440820L;

	private DynForm dynForm = null;  //  @jve:decl-index=0:
	private int startArgIndex = -1;
	

	/**
	 * Instantiates a new ontology class editor JPanel.
	 *
	 * @param dynForm the DynForm
	 * @param startArgIndex the argument index
	 */
	public OntologyClassEditorJPanel(DynForm dynForm, int startArgIndex) {
		super();
		this.dynForm = dynForm;
		this.startArgIndex = startArgIndex;
	}
	
	/**
	 * Returns the configured DynForm.
	 * @return the DynForm
	 */
	public DynForm getDynForm(){
		return this.dynForm;
	}
	/**
	 * Returns the configured argument index.
	 * @return the argument index
	 */
	public int getArgumentIndex() {
		return this.startArgIndex;
	}
	
	/**
	 * Invokes the method {@link #setOntologyClassInstance(Object)} with the new instance of the ontology class.
	 * This method was basically created in order to intercept errors when the method 
	 * {@link #setOntologyClassInstance(Object)} is invoked and in case that an error occurs.<br>
	 * <b>DO NOT OVERWRITE!</b>  
	 * 
	 * @param objectInstance the new object instance
	 */
	public void invokeSetOntologyClassInstance(final Object objectInstance) {
		try {
			setOntologyClassInstance(objectInstance);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Invokes the method {@link #getOntologyClassInstance()} and returns the current instance
	 * of the ontology class. This method was basically created in order to intercept errors when 
	 * the method {@link #getOntologyClassInstance()} is invoked and in case that an error occurs.<br>
	 * <b>DO NOT OVERWRITE!</b>
	 * 
	 * @return the object
	 */
	public Object invokeGetOntologyClassInstance() {
		Object objectInstance = null;
		try {
			objectInstance = this.getOntologyClassInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return objectInstance;
	}
	
	/**
	 * Sets the ontology class instance.
	 * @param objectInstance the new ontology class instance
	 */
	public abstract void setOntologyClassInstance(Object objectInstance);
	/**
	 * Returns the ontology class instance.
	 * @return the current ontology class instance
	 */
	public abstract Object getOntologyClassInstance();
	/**
	 * Gets the JToolBar with the user functions.
	 * @return the JToolBar user functions
	 */
	public abstract JToolBar getJToolBarUserFunctions();

	/**
	 * Returns the current EnvironmentController.
	 * @return the environment controller
	 */
	protected EnvironmentController getEnvironmentController() {
		return this.dynForm.getEnvironmentController();
	}
	/**
	 * Returns the default time format.
	 * @return the default time format
	 */
	public String getDefaultTimeFormat() {
		return this.dynForm.getDefaultTimeFormat();
	}
	
}
