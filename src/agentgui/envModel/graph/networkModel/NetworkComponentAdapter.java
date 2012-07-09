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

import javax.swing.JMenuItem;

import agentgui.core.ontologies.OntologyVisualisationHelper;
import agentgui.core.ontologies.gui.OntologyInstanceViewer;

/**
 * The Class NetworkComponentAdapter can be used in order to extend the local 
 * data model and its visual representation of a {@link NetworkComponent}.
 * Also further functionalities can be added for context menus and so on.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class NetworkComponentAdapter {

	/** The OntologyVisualisationHelper, for ontologies. */
	private OntologyVisualisationHelper ovHelper = null;
	
	
	/**
	 * Returns the NetworkComponentAdapter4DataModel for the application.<br>
	 * DO NOT OVERRIDE !!!
	 * 
	 * @return the NetworkComponentAdapter4DataModel 
	 */
	public NetworkComponentAdapter4DataModel invokeGetDataModelAdapter() {
		
		// --- Get the DataModel Adapter in a regular manner ----------------------------
		NetworkComponentAdapter4DataModel nca4dm = this.getDataModelAdapter();
		if (nca4dm instanceof NetworkComponentAdapter4Ontology) {

			NetworkComponentAdapter4Ontology nca4Onto = (NetworkComponentAdapter4Ontology) nca4dm;
			if (this.ovHelper!=null) {
				nca4Onto.setOntologyVisualisationHelper(this.ovHelper);
			} else {
				// --- Remind this for later ------------------------------------------------ 
				OntologyInstanceViewer oiv = (OntologyInstanceViewer) nca4Onto.getVisualisationComponent();
				this.ovHelper = oiv.getOntologyVisualisationHelper();	
			}
		}
		return nca4dm;
	}
	
	/**
	 * Returns the data model adapter for the {@link NetworkComponent}.
	 * @return the adapter visualisation
	 */
	public abstract NetworkComponentAdapter4DataModel getDataModelAdapter();
	
	
	/**
	 * Returns the JPopup menu elements for this kind of NetworkComponent.
	 * @return the JPopup menu elements
	 */
	public abstract Vector<JMenuItem> getJPopupMenuElements();
	
	
}
