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
package org.awb.env.networkModel.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.adapter.dataModel.DataModelStorageHandlerOntology;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties;

import de.enflexit.common.ontology.OntologyVisualizationHelper;
import de.enflexit.common.ontology.gui.OntologyInstanceViewer;
import jade.content.onto.Ontology;

/**
 * The Class NetworkComponentAdapter4Ontology.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter4Ontology extends NetworkComponentAdapter4DataModel {

	public static final String DATA_MODEL_TYPE_ONTOLOGY = "OntologyDataModel"; 
	
	private List<String> dataModelTypes;
	
	private OntologyVisualizationHelper ovh;
	private OntologyInstanceViewer oiv;
	
	private DataModelStorageHandlerOntology storageHandler;
	
	/**
	 * Instantiates a new network component adapter for ontologies.
	 * @param graphController the graph controller
	 */
	public NetworkComponentAdapter4Ontology(GraphEnvironmentController graphController) {
		super(graphController);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModelTypes()
	 */
	@Override
	protected List<String> getDataModelTypes() {
		if (dataModelTypes==null) {
			dataModelTypes = new ArrayList<String>();
			dataModelTypes.add(DATA_MODEL_TYPE_ONTOLOGY);
		}
		return dataModelTypes;
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
	 * Sets the ontology visualization helper.
	 * @param ontologyVisualizationHelper the new ontology visualization helper
	 */
	public void setOntologyVisualizationHelper(OntologyVisualizationHelper ontologyVisualizationHelper) {
		this.ovh = ontologyVisualizationHelper;
	}
	/**
	 * Returns the ontology visualization helper.
	 * @return the ontology visualization helper
	 */
	public OntologyVisualizationHelper getOntologyVisualizationHelper() {
		if (this.ovh==null) {
			if (this.getOntologyBaseClasses()==null) {
				throw new NullPointerException("The ontology base classes of the NetworkComponentAdapter were not defined!");
			} else {
				this.ovh = new OntologyVisualizationHelper(this.getOntologyBaseClassReferences());	
			}
		}
		return this.ovh;
	}
	
	/**
	 * Returns the ontology instance viewer.
	 * @return the ontology instance viewer
	 */
	protected OntologyInstanceViewer getOntologyInstanceViewer() {
		if (this.oiv==null) {
			if (this.getOntologyClassReferences()==null) {
				throw new NullPointerException("The references to the classes out of the configured ontologies are not set!");
			} else {
				this.oiv = new OntologyInstanceViewer(this.getOntologyVisualizationHelper(), this.getOntologyClassReferences());
				this.oiv.setAllowViewEnlargement(false);	
			}
		}
		return this.oiv;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponentAdapter4DataModel#save()
	 */
	@Override
	public boolean save() {
		this.getOntologyInstanceViewer().save();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponentAdapter4DataModel#setVisualizationComponent(javax.swing.JComponent)
	 */
	@Override
	public final void setVisualizationComponent(JComponent visualizationComponent) {
		// --- Nothing to do in case of an Ontology -------
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponentAdapter4DataModel#getVisualizationComponent(org.awb.env.networkModel.controller.BasicGraphGuiProperties)
	 */
	@Override
	public JComponent getVisualizationComponent(BasicGraphGuiProperties internalPropertyFrame) {
		return this.getOntologyInstanceViewer();
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponentAdapter4DataModel#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		Object[] dataModelArray = null;
		if (dataModel!=null && dataModel.getClass().isArray()) {
			dataModelArray = (Object[]) dataModel;
			
		} else if (dataModel instanceof TreeMap<?, ?>) {
			TreeMap<?, ?> dmTreeMap = (TreeMap<?, ?>) dataModel;
			String domain = this.getDomain();
			if (domain!=null) {
				dataModelArray = (Object[]) dmTreeMap.get(domain);
			}
			
		}
		this.getOntologyInstanceViewer().setConfigurationInstances(dataModelArray);	
	}
	/**
	 * returns the current domain.
	 * @return the domain
	 */
	private String getDomain() {
		String domain = null; 
		if (this.getNetworkComponent()!=null) {
			domain = this.getGraphEnvironmentController().getNetworkModel().getDomain(this.getNetworkComponent());
		} else if (this.getGraphNode()!=null) {
			List<String> domainList = this.getGraphEnvironmentController().getNetworkModel().getDomain(this.getGraphNode());
			if (domainList.size()==1) {
				domain = domainList.get(0);
			}
		}
		return domain;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponentAdapter4DataModel#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}
	/**
	 * Returns the ontology data model as array.
	 * @return the data model as array
	 */
	public Object[] getDataModelAsArray() {
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModelStorageHandler()
	 */
	@Override
	protected AbstractDataModelStorageHandler getDataModelStorageHandler() {
		if (storageHandler==null) {
			storageHandler = new DataModelStorageHandlerOntology(this, this.getPartModelID());
		}
		return storageHandler;
	}

	
}