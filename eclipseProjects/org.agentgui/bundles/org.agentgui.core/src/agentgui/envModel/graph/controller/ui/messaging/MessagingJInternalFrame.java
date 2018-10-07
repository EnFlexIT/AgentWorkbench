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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.ui.BasicGraphGuiJInternalFrame;
import agentgui.envModel.graph.controller.ui.BasicGraphGuiVisViewer;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;

/**
 * The Class MessagingJInternalFrame represents the host for the visualization of UI messages.
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class MessagingJInternalFrame extends BasicGraphGuiJInternalFrame {

	private static final long serialVersionUID = -4190004260052846631L;

	private static final String WIDGET_ORIENTATION_PROPERTY = "GraphEnvironmentMessagingWidgetOrientation";
	public static final Color bgColor = SystemColor.info;
	
	public enum WidgetOrientation {
		Bottom,
		Top,
		Left,
		Right
	}
	
	private WidgetOrientation widgetOrientation;
	
	
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> graphVisualizationPanel;
	private ComponentListener graphVisualizationPanelListener;
	
	private JPanelMessages jPanelMessages;
	private JPanelStates jPanelStates;
	private JPanelMenu jPanelMenu;
	
	
	/**
	 * Instantiates a new messaging frame.
	 * @param controller the controller
	 */
	public MessagingJInternalFrame(GraphEnvironmentController controller) {
		super(controller);
		this.initialize();
	}
	/**
	 * Initialize the internal frame.
	 */
	private void initialize() {
		
		this.setTitle(Language.translate("Notifications", Language.EN));
		
		this.setAutoscrolls(true);
		this.getContentPane().setBackground(MessagingJInternalFrame.bgColor);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(false);

		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent ife) {
				MessagingJInternalFrame.this.setVisible(false);
			}
		});

		// --- Remove the complete header ------- 
		((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		this.setSizeAndPosition();
		// --- Set the content panels -----------
		this.getContentPane().setLayout(new BorderLayout(5, 5));
		this.setContentPanels();
		
	}
	/**
	 * Sets the content panels.
	 */
	private void setContentPanels() {
		
		if (this.getContentPane().getComponentCount()>0) {
			this.getContentPane().removeAll();
		}
		
		switch (this.getWidgetOrientation()) {
		case Bottom:
		case Top:
			this.getContentPane().add(getJPanelStates(), BorderLayout.WEST);
			this.getContentPane().add(getJPanelMessages(), BorderLayout.CENTER);
			this.getContentPane().add(getJPanelMenu(), BorderLayout.EAST);
			break;

		case Left:
		case Right:
			this.getContentPane().add(getJPanelMenu(), BorderLayout.NORTH);
			this.getContentPane().add(getJPanelMessages(), BorderLayout.CENTER);
			this.getContentPane().add(getJPanelStates(), BorderLayout.SOUTH);
			break;
		}
		
		this.getContentPane().validate();
		this.getContentPane().repaint();
		
	}
	private JPanelMessages getJPanelMessages() {
		if (jPanelMessages == null) {
			jPanelMessages = new JPanelMessages(this.graphController);
		}
		return jPanelMessages;
	}
	private JPanelStates getJPanelStates() {
		if (jPanelStates == null) {
			jPanelStates = new JPanelStates(this.graphController);
		}
		return jPanelStates;
	}
	private JPanelMenu getJPanelMenu() {
		if (jPanelMenu == null) {
			jPanelMenu = new JPanelMenu(this);
		}
		return jPanelMenu;
	}

	/**
	 * Sets the widget orientation.
	 * @param widgetOrientation the new widget orientation
	 */
	public void setWidgetOrientation(WidgetOrientation widgetOrientation) {
		if (widgetOrientation!=null && widgetOrientation!=this.widgetOrientation) {
			this.widgetOrientation = widgetOrientation;
			Application.getGlobalInfo().putStringToConfiguration(WIDGET_ORIENTATION_PROPERTY, this.widgetOrientation.toString());
			this.setSizeAndPosition();
			this.setContentPanels();
		}
	}
	/**
	 * Returns the widget orientation.
	 * @return the widget orientation
	 */
	public WidgetOrientation getWidgetOrientation() {
		if (widgetOrientation==null) {
			String widgetOrientationString = Application.getGlobalInfo().getStringFromConfiguration(WIDGET_ORIENTATION_PROPERTY, WidgetOrientation.Bottom.toString());
			widgetOrientation = WidgetOrientation.valueOf(widgetOrientationString);
		}
		return widgetOrientation;
	}
	
	/**
	 * Returns the graph visualization panel.
	 * @return the graph visualization panel
	 */
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> getGraphVisualizationPanel() {
		if (graphVisualizationPanel==null) {
			graphVisualizationPanel = this.basicGraphGui.getVisualizationViewer();
			graphVisualizationPanel.addComponentListener(this.getGraphVisualizationPanelListener());
		}
		return graphVisualizationPanel;
	}
	/**
	 * Returns the graph visualization panel listener.
	 * @return the graph visualization panel listener
	 */
	private ComponentListener getGraphVisualizationPanelListener() {
		if (graphVisualizationPanelListener==null) {
			graphVisualizationPanelListener = new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					MessagingJInternalFrame.this.setSizeAndPosition();
				}
			};
		}
		return graphVisualizationPanelListener;
	}
	
	/**
	 * Sets the initial size and position of the frame.
	 */
	private void setSizeAndPosition() {

		if (this.graphDesktop!=null && this.getGraphVisualizationPanel()!=null) {
			// --- Get the initial x-position of the property window ----------
			int posBasicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getLocation().x;
			int posVisViewOnBasicGraphGui = this.getGraphVisualizationPanel().getParent().getLocation().x; 

			int parentPosX = posBasicGraphGui + posVisViewOnBasicGraphGui;
			int parentPosY = (int) this.getGraphVisualizationPanel().getLocation().getX();
			int parentHeight = this.getGraphVisualizationPanel().getHeight();
			int parentWidth = this.getGraphVisualizationPanel().getWidth();
			
			// --- Do positioning depending on the orientation ---------------- 
			int myHeight = (int) (0.2 * (double) parentHeight);
			int myWidth = parentWidth;
			
			int myLocationX = parentPosX;
			int myLocationY = parentPosY + parentHeight - myHeight;

			switch (this.getWidgetOrientation()) {
			case Bottom:
				// --- Nothing to do here -------
				break;
				
			case Top:
				myLocationY = parentPosY;
				break;

			case Left:
				myHeight = parentHeight;
				myWidth = (int) (0.2 * (double) parentWidth);
				myLocationY = parentPosY;
				break;
				
			case Right:
				myHeight = parentHeight;
				myWidth = (int) (0.2 * (double) parentWidth);
				myLocationY = parentPosY;
				myLocationX = parentPosX + parentWidth - myWidth;
				break;
			}
			
			// --- Do the positioning -----------------------------------------
			this.setLocation(myLocationX, myLocationY);
			this.setSize(new Dimension(myWidth, myHeight));
			
		} else {
			this.setLocation(0, 0);
			this.setSize(new Dimension(600, 200));
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.BasicGraphGuiJInternalFrame#registerAtDesktopAndSetVisible()
	 */
	@Override
	public void registerAtDesktopAndSetVisible() {
		super.registerAtDesktopAndSetVisible();
	}
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.BasicGraphGuiJInternalFrame#isRemindAsLastOpenedEditor()
	 */
	@Override
	protected boolean isRemindAsLastOpenedEditor() {
		return false;
	}
	
}
