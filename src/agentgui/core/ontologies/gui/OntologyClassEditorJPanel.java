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

import java.util.Observable;
import java.util.Observer;

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
public abstract class OntologyClassEditorJPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -2371432709285440820L;

	protected DynForm dynForm = null;  //  @jve:decl-index=0:
	protected int startArgIndex = -1;
	
	private boolean pauseObserver = false;
	
	
	/**
	 * Instantiates a new ontology class editor JPanel.
	 *
	 * @param dynForm the DynForm
	 * @param startArgIndex the start argument index
	 */
	public OntologyClassEditorJPanel(DynForm dynForm, int startArgIndex) {
		super();
		this.dynForm = dynForm;
		this.dynForm.addObserver(this);
		this.startArgIndex = startArgIndex;
		
		Object[] startArgs = this.dynForm.getOntoArgsInstance();
		Object newOntologyClassInstance = null;
		if (startArgs!=null) {
			newOntologyClassInstance =  startArgs[this.startArgIndex];
		}
		this.setOntologyClassInstance(newOntologyClassInstance);
	}
	
	/**
	 * Sets the new ontology class instance to the DynForm.
	 * @param newOntologyClassInstance the new new ontology class instance
	 */
	protected void setNewOntologyClassInstance(Object newOntologyClassInstance) {
		this.setPauseObserver(true);
		Object[] startArgs = this.dynForm.getOntoArgsInstance();
		startArgs[this.startArgIndex] = newOntologyClassInstance;
		this.dynForm.setOntoArgsInstance(startArgs);
		this.setPauseObserver(false);
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
	

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		
		if (this.isPauseObserver()==true) return;
		
		if (object instanceof String) {
			String notification = (String) object;
			if (notification.equals(DynForm.UPDATED_DataModel)==true) {
				Object[] startArgs = this.dynForm.getOntoArgsInstance();
				Object newOntologyClassInstance =  startArgs[this.startArgIndex];
				this.setOntologyClassInstance(newOntologyClassInstance);	
			}
		}
	}

	/**
	 * Sets the observer to be paused or not.
	 * @param pauseNow the new pause observer
	 */
	protected void setPauseObserver(boolean pauseNow) {
		this.pauseObserver = pauseNow;
	}
	/**
	 * Checks if is pause observer.
	 * @return true, if the observer is paused 
	 */
	protected boolean isPauseObserver() {
		return this.pauseObserver;
	}

	/**
	 * Removes this widget from the DynForm observer.
	 */
	public void removeFromObserver() {
		this.dynForm.deleteObserver(this);
	}

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
