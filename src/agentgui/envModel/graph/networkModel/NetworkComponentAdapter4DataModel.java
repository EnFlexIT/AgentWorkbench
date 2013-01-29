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
package agentgui.envModel.graph.networkModel;

import jade.content.Concept;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.envModel.graph.controller.GraphEnvironmentController;

/**
 * The Class NetworkComponentAdapterVisualisation can be used in order  
 * to add a customized data model dialog to a specific {@link NetworkComponent}.<br>
 * Furthermore it can be used to define ontology sub classes (for JADE these
 * are extended {@link Concept} classes) that have to be displayed for component 
 * editing and value changes by using the {@link OntologyInstanceViewer}.<br>     
 * Alternatively, and instead of using ontologies for local data models of
 * {@link NetworkComponent}'s, also individual object structures can be used.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4DataModel {

	private static final long serialVersionUID = -5222516718157004730L;

	private GraphEnvironmentController graphController = null;

	/**
	 * Instantiates a new network component data model adapter.
	 * @param environmentController the environment controller
	 */
	public NetworkComponentAdapter4DataModel(GraphEnvironmentController graphController) {
		this.setGraphEnvironmentController(graphController);
	}
	
	/**
	 * Sets the current GraphEnvironmentController.
	 * @param environmentController the new environment controller
	 */
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Returns the current GraphEnvironmentController.
	 * @return the GraphEnvironmentController
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return this.graphController;
	}

	/**
	 * Returns the JComponent for the visualisation of the data model.
	 * @return the visualisation component
	 */
	public abstract JComponent getVisualisationComponent();
	
	/**
	 * Sets the data model to the visualisation component.
	 * @param dataModel the new data model
	 */
	public abstract void setDataModel(Object dataModel);

	/**
	 * Returns the data model from the visualisation component.
	 * @return the data model
	 */
	public abstract Object getDataModel();
	
	/**
	 * Save the current settings in the visualisation component.
	 */
	public abstract void save();

	
	/**
	 * Returns the data model of a {@link NetworkComponent} as Base64 encoded String.
	 * 
	 * @param dataModel the data model
	 * @return the data model encoded as Base64 String
	 */
	public abstract Vector<String> getDataModelBase64Encoded(Object dataModel);
	
	
	/**
	 * Returns the data model of a {@link NetworkComponent} as Object
	 * specified by a Base64 encoded String.
	 *
	 * @param dataModel the data model as String Vector
	 * @return the data model base64 decode
	 */
	public Object getDataModelBase64Decoded(Vector<String> dataModel) {
		return this.getDataModelBase64Decoded(dataModel, false);
	}
	/**
	 * Returns the data model of a {@link NetworkComponent} as Object
	 * specified by a Base64 encoded String.
	 *
	 * @param dataModel the data model as String Vector
	 * @param avoidGuiUpdate true, if a GUI-update should be avoided 
	 * @return the data model base64 decode
	 */
	public abstract Object getDataModelBase64Decoded(Vector<String> dataModel, boolean avoidGuiUpdate);
	
}
