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
package org.awb.env.networkModel.controller.ui.messaging;

import javax.swing.JPanel;

/**
 * The Class AbstractStateMessageVisualization serves as base for the 
 * visualization of graph states (e.g. traffic lights and other).
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class GraphUIStateMessagePanel extends JPanel {

	private static final long serialVersionUID = -4542890165604477658L;

	/**
	 * Defines the required constructor of the visualization.
	 */
	public GraphUIStateMessagePanel() { }
	
	/**
	 * Adds the specified state message to the current state visualization.
	 * @param stateMessage the state message to display
	 */
	public abstract void addMessage(GraphUIStateMessage stateMessage);
	
}
