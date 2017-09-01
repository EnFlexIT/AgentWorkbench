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
package de.enflexit.common.ontology.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

/**
 * The Class OntologyClassWidget has to be used in order to build
 * a widget for the configuration of a special ontology class.
 * 
 * @see OntologyClassVisualisation
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class OntologyClassWidget extends OntologyClassEditorJPanel {

	private static final long serialVersionUID = -6783920742718563526L;

	
	/**
	 * Instantiates a new ontology class widget.
	 *
	 * @param dynForm the current DynForm
	 * @param startArgIndex the start argument index
	 */
	public OntologyClassWidget(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setSize(new Dimension(315, 250));
	}
	
	/**
	 * Not usable for widgets
	 */
	@Override
	public JToolBar getJToolBarUserFunctions() {
		return null;
	}
	
}
