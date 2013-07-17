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

import jade.content.onto.Ontology;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;
import agentgui.envModel.graph.controller.GraphEnvironmentController;

/**
 * The Class NetworkComponentAdapter4Ontology.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4Ontology extends NetworkComponentAdapter4DataModel {

	private static final long serialVersionUID = -5374060290921305401L;
	
	private OntologyVisualisationHelper ovh = null;
	private OntologyInstanceViewer oiv = null;


	/**
	 * Instantiates a new network component adapter4 ontology.
	 *
	 * @param graphController the graph controller
	 */
	public NetworkComponentAdapter4Ontology(GraphEnvironmentController graphController) {
		super(graphController);
	}

	/**
	 * Define the Vector of the needed Ontologies for this type of NetworkComponent.
	 * @return the Vector of ontology classes 
	 */
	public abstract Vector<Class<? extends Ontology>> getOntologyBaseClasses();
	
	/**
	 * Returns the ontology base class references.
	 * @return the ontology base class references
	 */
	public Vector<String> getOntologyBaseClassReferences() {
		Vector<String> classReferences = null;
		if (this.getOntologyBaseClasses()!=null) {
			classReferences = new Vector<String>();
			for (Class<? extends Ontology> ontoClass : this.getOntologyBaseClasses()) {
				classReferences.add(ontoClass.getName());
			}
		}
		return classReferences;
	}
	
	/**
	 * Gets the ontology class references.
	 * @return the ontology class references
	 */
	public abstract String[] getOntologyClassReferences();
	
	
	/**
	 * Sets the ontology visualisation helper.
	 * @param ontologyVisualisationHelper the new ontology visualisation helper
	 */
	public void setOntologyVisualisationHelper(OntologyVisualisationHelper ontologyVisualisationHelper) {
		this.ovh = ontologyVisualisationHelper;
	}
	/**
	 * Returns the ontology visualisation helper.
	 * @return the ontology visualisation helper
	 */
	public OntologyVisualisationHelper getOntologyVisualisationHelper() {
		if (this.ovh==null) {
			if (this.getOntologyBaseClasses()==null) {
				throw new NullPointerException("The ontology base classes of the NetworkComponentAdapter were not defined!");
			} else {
				this.ovh = new OntologyVisualisationHelper(this.getOntologyBaseClassReferences());	
			}
		}
		return this.ovh;
	}
	
	/**
	 * Returns the ontology instance viewer.
	 * @return the ontology instance viewer
	 */
	private OntologyInstanceViewer getOntologyInstanceViewer() {
		if (this.oiv == null) {
			if (this.getOntologyClassReferences()==null) {
				throw new NullPointerException("The references to the classes out of the configured ontologies are not set!");
			} else {
				this.oiv = new OntologyInstanceViewer(this.getGraphEnvironmentController(), this.getOntologyVisualisationHelper(), this.getOntologyClassReferences());
				this.oiv.setAllowViewEnlargement(false);	
			}
		}
		return this.oiv;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#save()
	 */
	@Override
	public void save() {
		this.getOntologyInstanceViewer().save();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getVisualisationComponent()
	 */
	@Override
	public JComponent getVisualisationComponent() {
		return this.getOntologyInstanceViewer();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#setDataModel(java.lang.Object, boolean)
	 */
	@Override
	public void setDataModel(Object dataModel, boolean avoidGuiUpdate) {
		Object[] dataModelArray = (Object[]) dataModel;
		this.getOntologyInstanceViewer().setConfigurationInstances(dataModelArray, avoidGuiUpdate);
	}
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel#getDataModelBase64Encoded(java.lang.Object, boolean)
	 */
	@Override
	public Vector<String> getDataModelBase64Encoded(Object dataModel, boolean avoidGuiUpdate) {
		
		this.setDataModel(dataModel, avoidGuiUpdate);
		
		Vector<String> base64Vector = new Vector<String>();
		String[] base64Array = this.getOntologyInstanceViewer().getConfigurationXML64();
		for (int i = 0; i < base64Array.length; i++) {
			if (base64Array[i]!=null) {
				if (base64Array[i].equals("")==false) {
					base64Vector.addElement(base64Array[i]);
				}
			}
		} 
		if (base64Vector.isEmpty()) {
			base64Vector=null;
		} else if (base64Vector.size()==0) {
			base64Vector=null;
		}
		return base64Vector;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentDataModelAdapter#getDataModelBase64Decoded(java.lang.String)
	 */
	@Override
	public Object getDataModelBase64Decoded(Vector<String> dataModel, boolean avoidGuiUpdate) {
		String[] base64Array = new String[this.getOntologyClassReferences().length];
		dataModel.toArray(base64Array);
		this.getOntologyInstanceViewer().setConfigurationXML64(base64Array, avoidGuiUpdate);
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}

}