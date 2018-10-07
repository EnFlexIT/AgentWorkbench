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
package agentgui.envModel.graph.controller.ui.messaging;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import agentgui.envModel.graph.controller.GraphEnvironmentController;

/**
 * The Class JPanleStates represents the visualization for state indications.
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class JPanelMessages extends JPanel {

	private static final long serialVersionUID = 1110726364983505331L;

	private GraphEnvironmentController graphController;
	
	/**
	 * Instantiates a new messaging state panel.
	 * @param graphController the graph controller
	 */
	public JPanelMessages(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		
		this.setBackground(MessagingJInternalFrame.bgColor);
		
		GridBagLayout gbl_jPanelStates = new GridBagLayout();
		gbl_jPanelStates.columnWidths = new int[]{0};
		gbl_jPanelStates.rowHeights = new int[]{0};
		gbl_jPanelStates.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_jPanelStates.rowWeights = new double[]{Double.MIN_VALUE};
		this.setLayout(gbl_jPanelStates);
	}
	
}
