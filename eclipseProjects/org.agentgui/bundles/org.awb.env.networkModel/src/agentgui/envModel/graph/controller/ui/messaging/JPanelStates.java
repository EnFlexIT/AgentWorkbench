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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.TreeMap;

import javax.swing.JPanel;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.envModel.graph.controller.ui.messaging.MessagingJInternalFrame.WidgetOrientation;

/**
 * The Class JPanleStates represents the visualization for state indications.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class JPanelStates extends JPanel {

	private static final long serialVersionUID = 1110726364983505331L;
	
	private MessagingJInternalFrame messagingFrame;
	private WidgetOrientation widgetOrientation;
	
	private TreeMap<String, GraphUIStateMessagePanel> visualizationMap;
	private JPanel dummyPanel;
	
	
	/**
	 * Default constructor (for WindowBuilder only).
	 */
	public JPanelStates() {
		this.initialize();
	}
	/**
	 * Instantiates a new messaging state panel.
	 * @param messagingFrame the messaging frame
	 */
	public JPanelStates(MessagingJInternalFrame messagingFrame) {
		this.messagingFrame = messagingFrame;
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		this.setBackground(MessagingJInternalFrame.bgColor);
		this.setLayout(new GridBagLayout());
	}
	
	/**
	 * Returns the visualization map.
	 * @return the visualization map
	 */
	public TreeMap<String, GraphUIStateMessagePanel> getVisualizationMap() {
		if (visualizationMap==null) {
			visualizationMap = new TreeMap<>();
		}
		return visualizationMap;
	}
	
	/**
	 * Adds the specified {@link GraphUIStateMessage}.
	 * @param stateMessage the {@link GraphUIStateMessage} 
	 */
	public void addMessage(GraphUIStateMessage stateMessage) {
		
		// --- Get the visualization class of the state Message -----
		Class<? extends GraphUIStateMessagePanel> stateMessageVisClass = stateMessage.getVisualizationClass();
		if (stateMessageVisClass==null) {
			throw new NullPointerException(this.messagingFrame.getClass().getSimpleName() + ": Visualzation class for message was not defined!");
		}
		
		// --- Is this visualization already active? ----------------
		GraphUIStateMessagePanel visualization = this.getVisualizationMap().get(stateMessageVisClass.getName());
		if (visualization==null) {
			try {
				// --- Create new instance of the visualization -----
				visualization = (GraphUIStateMessagePanel) ClassLoadServiceUtility.newInstance(stateMessageVisClass.getName());
				visualization.setBackground(MessagingJInternalFrame.bgColor);
				// --- Remind the visualization for later usage ----- 
				this.getVisualizationMap().put(stateMessageVisClass.getName(), visualization);
				// --- Add the visualization ------------------------
				this.addVisualization(visualization);
			    
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
				System.err.println("[" + this.messagingFrame.getClass().getSimpleName() + "] Visualization class '" + stateMessageVisClass.getName() + "' could not be initiated.");
				//ex.printStackTrace();
			}
		}
		
		// --- Forward the State message to the visualization ------- 
		if (visualization!=null) {
			visualization.addMessage(stateMessage);
		}
	}
	
	/**
	 * Adds the specified visualization to the panel.
	 */
	private void addVisualization(GraphUIStateMessagePanel visualization) {
		
		// --- Remove the dummy panel if available --------
		this.remove(this.getDummyPanel());
		
		// --- Add visualization --------------------------
		GridBagConstraints gbConVis = new GridBagConstraints();
		gbConVis.gridx = 0;
	    gbConVis.gridy = this.getComponentCount();
	    gbConVis.anchor = GridBagConstraints.CENTER;
	    gbConVis.ipady = 10;
	    
	    this.add(visualization, gbConVis);
		
		// --- Add the dummy panel again ? ----------------
	    if (this.getWidgetOrientation()==WidgetOrientation.Top || this.getWidgetOrientation()==WidgetOrientation.Bottom) {
		    this.addDummyWidget();
	    }
	    
	    this.messagingFrame.validate();
		this.messagingFrame.repaint();
		
	}
	/**
	 * Returns the dummy panel.
	 * @return the dummy panel
	 */
	private JPanel getDummyPanel() {
		if (dummyPanel==null) {
			dummyPanel = new JPanel();
			dummyPanel.setMaximumSize(new Dimension(10, 10));
			dummyPanel.setBackground(MessagingJInternalFrame.bgColor);
		}
		return dummyPanel;
	}
	/**
	 * Adds the dummy widget.
	 */
	private void addDummyWidget() {
		GridBagConstraints gbConDummy = new  GridBagConstraints(0, this.getComponentCount(), 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0);
	    this.add(this.getDummyPanel(), gbConDummy);
	}
	
	/**
	 * Returns the current widget orientation.
	 * @return the widget orientation
	 */
	private WidgetOrientation getWidgetOrientation() {
		if (widgetOrientation==null && this.messagingFrame!=null) {
			widgetOrientation = this.messagingFrame.getWidgetOrientation();
		}
		return widgetOrientation;
	}
	/**
	 * Sets the widget orientation.
	 * @param widgetOrientation the new widget orientation
	 */
	public void setWidgetOrientation(WidgetOrientation widgetOrientation) {

		this.widgetOrientation = widgetOrientation;
		if (this.getWidgetOrientation()==WidgetOrientation.Top || this.getWidgetOrientation()==WidgetOrientation.Bottom) {
			if (this.getDummyPanel().isShowing()==false) {
				this.addDummyWidget();
			}
			
		} else {
			this.remove(this.getDummyPanel());
		}
	}
	
}
