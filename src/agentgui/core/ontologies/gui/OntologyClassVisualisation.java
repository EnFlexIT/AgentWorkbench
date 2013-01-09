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

import agentgui.core.config.GlobalInfo;

/**
 * The Class OntologyClassVisualisation can be used in order to name a 
 * class out of an Ontology that can be configured with customized 
 * widgets or dialog.<br>
 * In order to register your own OntologyClassVisualisation use the register method
 * in the GlobalInfo of the Application (see links below).
 * 
 * @see GlobalInfo#registerOntologyClassVisualisation(OntologyClassVisualisation)
 * @see GlobalInfo#getKnownOntologyClassVisualisations()
 * @see GlobalInfo#isOntologyClassVisualisation(Object)
 * @see GlobalInfo#getOntologyClassVisualisation(Object)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class OntologyClassVisualisation {

	
	/**
	 * Returns the class out of an ontology that can be visualized .
	 * @return the ontology class
	 */
	public abstract Class<?> getOntologyClass();

	/**
	 * Returns the class of the widget that extends a OntologyClassWidget that extends a JPanel.<br>
	 * Should be a small JPanel with a dimension of approx width=315 and height=250. 
	 * @return the widget class
	 */
	public abstract Class<? extends OntologyClassWidget> getWidgetClass();

	
	/**
	 * Returns the OntologyClassEditorJPanel class that extends JPanel.
	 * @return the editor OntologyClassEditorJPanel class
	 */
	public abstract Class<? extends OntologyClassEditorJPanel> getEditorJPanelClass();
	
	
	/**
	 * Returns the OntologyClassEditorJDialog class that extends JDialog.
	 * @return the OntologyClassEditorJDialog class
	 */
	public abstract Class<? extends OntologyClassEditorJDialog> getEditorJDialogClass();
	
	
}
