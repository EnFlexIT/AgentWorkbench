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

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;

import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.envModel.graph.controller.BasicGraphGuiJDesktopPane;
import agentgui.envModel.graph.controller.GraphEnvironmentController;


/**
 * The Class NetworkComponentAdapter4DataModel can be used in order  
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

	private GraphEnvironmentController graphController;

	/**
	 * Instantiates a new network component data model adapter.
	 * @param graphController the GraphEnvironmentController
	 */
	public NetworkComponentAdapter4DataModel(GraphEnvironmentController graphController) {
		this.setGraphEnvironmentController(graphController);
	}
	
	
	/**
	 * Sets the current GraphEnvironmentController.
	 * @param graphController the new GraphEnvironmentController
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
	 * Returns the default size for the visualisation of the data model.
	 * Override this method in order to get control over the window size 
	 * of your own, customised visualisation. 
	 * By default this method just returns null.
	 *
	 * @param graphDesktop the current desktop instance that will be the host of the visualisation 
	 * @return the Dimension of the visualisation
	 */
	public Dimension getSizeOfVisualisation(BasicGraphGuiJDesktopPane graphDesktop) {
		return null;
	}
	
	
	/**
	 * Save the current settings in the visualisation component.
	 */
	public abstract void save();
	
	/**
	 * Sets the data model of a NetworkComponent to the visualisation component.
	 * @param networkComponent the NetworkComponent
	 */
	public void setDataModel(NetworkComponent networkComponent) {
		try {
			this.setDataModel(networkComponent.getDataModel());
		} catch (Exception ex) {
			System.err.println("Error while setting data model of NetworkComponent '" + networkComponent.getId() + "'!");
			ex.printStackTrace();
		}
	}
	/**
	 * Sets the data model of a GraphNode to the visualisation component.
	 * @param graphNode the GraphNode
	 */
	public void setDataModel(GraphNode graphNode) {
		try {
			this.setDataModel(graphNode.getDataModel());
		} catch (Exception ex) {
			System.err.println("Error while setting data model of GraphNode '" + graphNode.getId() + "'!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets the data model to the visualisation component and will refresh the visualisation.
	 * @param dataModel the new data model
	 */
	public abstract void setDataModel(Object dataModel);
	
	/**
	 * Returns the data model from the visualisation component.
	 * @return the data model
	 */
	public abstract Object getDataModel();
	
	/**
	 * Returns the data model of a {@link NetworkComponent} as Vector of Base64 encoded String.
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
	public abstract Object getDataModelBase64Decoded(Vector<String> dataModel);
	
}
