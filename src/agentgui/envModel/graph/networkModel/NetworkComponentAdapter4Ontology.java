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

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;


/**
 * The Class NetworkComponentAdapter4Ontology.
 */
public abstract class NetworkComponentAdapter4Ontology extends NetworkComponentAdapter4DataModel {

	private static final long serialVersionUID = -5374060290921305401L;
	
	private OntologyVisualisationHelper ovh = null;
	private OntologyInstanceViewer oiv = null;
	private final String base64Seperator = "\n";
	
	
	/**
	 * Define the Vector of the needed Ontologies for
	 * this type of NetworkComponent.
	 *
	 * @return the references to ontologies
	 */
	public abstract Vector<String> getOntologyBaseClasses();
	
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
			this.ovh = new OntologyVisualisationHelper(this.getOntologyBaseClasses());
		}
		return this.ovh;
	}
	
	/**
	 * Returns the ontology instance viewer.
	 * @return the ontology instance viewer
	 */
	private OntologyInstanceViewer getOntologyInstanceViewer() {
		if (this.oiv == null) {
			this.oiv = new OntologyInstanceViewer(this.getOntologyVisualisationHelper(), this.getOntologyClassReferences());
			this.oiv.setAllowViewEnlargement(false);
		}
		return this.oiv;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapterVisualisation#getVisualisation()
	 */
	@Override
	public JComponent getVisualisationComponent() {
		return this.getOntologyInstanceViewer();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapterVisualisation#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		try {
			Object[] dataModelArray = (Object[]) dataModel;
			this.getOntologyInstanceViewer().setConfigurationInstances(dataModelArray);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapterVisualisation#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentAdapterVisualisation#save()
	 */
	@Override
	public void save() {
		this.getOntologyInstanceViewer().save();
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentDataModelAdapter#getDataModelBase64Encoded(java.lang.Object)
	 */
	@Override
	public String getDataModelBase64Encoded(Object dataModel) {
		
		this.setDataModel(dataModel);
		
		String base64 = new String();
		String[] base64Array = this.getOntologyInstanceViewer().getConfigurationXML64();
		for (int i = 0; i < base64Array.length; i++) {
			if (base64.equals("")==false) {
				base64 = base64 + base64Seperator;
			}
			base64 = base64 + base64Array[i];
		} 
		if (base64.equals("")) {
			base64 = null;
		}
		return base64;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.networkModel.NetworkComponentDataModelAdapter#getDataModelBase64Decoded(java.lang.String)
	 */
	@Override
	public Object getDataModelBase64Decoded(String dataModel) {
		String[] base64Array = dataModel.split(base64Seperator);
		this.getOntologyInstanceViewer().setConfigurationXML64(base64Array);
		return this.getOntologyInstanceViewer().getConfigurationInstances();
	}

}