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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.messaging.GraphUIMessage.GraphUIMessageType;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * The Class MessagingJInternalFrame represents the host for the visualization of UI messages.
 * It consists of three parts that are a toolbar, a message visualization and a state area that
 * can be filled by he usage of {@link GraphUIStateMessage}
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class MessagingJInternalFrame extends BasicGraphGuiJInternalFrame {

	private static final long serialVersionUID = -4190004260052846631L;

	private static final String WIDGET_ORIENTATION_PROPERTY = "GraphEnvironmentMessagingWidgetOrientation";
	private static final String WIDGET_TIME_CONTROLLED_PROPERTY = "GraphEnvironmentMessagingWidgetTimeControlled";
	private static final String WIDGET_WIDTH_HEIGTH = "GraphEnvironmentMessagingWidgetWidthHeight";
	
	public static final Color bgColor = SystemColor.info;
	
	public enum WidgetOrientation {
		Bottom,
		Top,
		Left,
		Right
	}
	
	private WidgetOrientation widgetOrientation;
	private Integer widgetWidthHeight;
	
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> graphVisualizationPanel;
	private ComponentListener graphVisualizationPanelListener;
	
	private JSplitPane jSplitPaneMessagesState;
	private JPanelMessages jPanelMessages;
	private JPanelStates jPanelStates;
	
	private JPanelToolbar jPanelToolbar;
	
	private Boolean timeControlled;
	private Thread closerThread;
	private long closingTime;
	private long visibleTime = 5000;
	
	/**
	 * Instantiates a new messaging frame.
	 * @param controller the controller
	 */
	public MessagingJInternalFrame(GraphEnvironmentController controller) {
		super(controller);
		this.initialize();
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#isRemindAsLastOpenedEditor()
	 */
	@Override
	protected boolean isRemindAsLastOpenedEditor() {
		return false;
	}
	/**
	 * Initialize the internal frame.
	 */
	private void initialize() {
		
		this.setTitle(Language.translate("Messaging", Language.EN));
		
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
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onResizedInternalFrame();
			}
		});
		
		// --- Remove the complete header ------- 
		((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		this.setSizeAndPosition();
		this.setContentPanels();
		
	}
	/**
	 * Sets the content panels.
	 */
	private void setContentPanels() {
		
		if (this.getContentPane().getComponentCount()>0) {
			this.getContentPane().removeAll();
		}
		
		// --- Define general layout ----------------------
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		// --- Assign layouts ----------------------------- 
		GridBagConstraints gbc_jSplitPane = new GridBagConstraints();
		gbc_jSplitPane.anchor = GridBagConstraints.WEST;
		gbc_jSplitPane.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints gbc_jPanelToolbar = new GridBagConstraints();
		gbc_jPanelToolbar.anchor = GridBagConstraints.NORTHWEST;
		gbc_jPanelToolbar.fill = GridBagConstraints.BOTH;
		
		switch (this.getWidgetOrientation()) {
		case Bottom:
		case Top:
			gridBagLayout.columnWeights = new double[]{1.0, 0.0};
			gridBagLayout.rowWeights = new double[]{1.0};

			gbc_jSplitPane.gridx = 0;
			gbc_jSplitPane.gridy = 0;
			
			gbc_jPanelToolbar.gridx = 1;
			gbc_jPanelToolbar.gridy = 0;
			
			this.getJSplitPaneMessagesState().setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			break;

		case Left:
		case Right:
			gridBagLayout.columnWeights = new double[]{1.0};
			gridBagLayout.rowWeights = new double[]{0.0, 1.0};
			
			gbc_jSplitPane.gridx = 0;
			gbc_jSplitPane.gridy = 1;
			
			gbc_jPanelToolbar.gridx = 0;
			gbc_jPanelToolbar.gridy = 0;
			
			this.getJSplitPaneMessagesState().setOrientation(JSplitPane.VERTICAL_SPLIT);
			break;
		}

		// --- Add components -----------------------------
		this.getContentPane().setLayout(gridBagLayout);
		this.getContentPane().add(this.getJSplitPaneMessagesState(), gbc_jSplitPane);
		this.getContentPane().add(this.getJPanelToolbar(), gbc_jPanelToolbar);
		
		this.repaintWidget();
	}
	/**
	 * Repaints widget.
	 */
	private void repaintWidget() {
		this.getJSplitPaneMessagesState().validate();
		this.getJSplitPaneMessagesState().repaint();
		this.getJPanelMessages().validate();
		this.getJPanelMessages().repaint();
		this.getJPanelStates().validate();
		this.getJPanelStates().repaint();
	}
	
	private JPanelToolbar getJPanelToolbar() {
		if (jPanelToolbar == null) {
			jPanelToolbar = new JPanelToolbar(this);
		}
		return jPanelToolbar;
	}
	
	private JSplitPane getJSplitPaneMessagesState() {
		if (jSplitPaneMessagesState==null) {
			jSplitPaneMessagesState = new JSplitPane();
			jSplitPaneMessagesState.setDividerSize(1);
			jSplitPaneMessagesState.setLeftComponent(this.getJPanelStates());
			jSplitPaneMessagesState.setRightComponent(this.getJPanelMessages());
		}
		return jSplitPaneMessagesState;
	}
	private JPanelMessages getJPanelMessages() {
		if (jPanelMessages == null) {
			jPanelMessages = new JPanelMessages(this.graphController);
		}
		return jPanelMessages;
	}
	private JPanelStates getJPanelStates() {
		if (jPanelStates == null) {
			jPanelStates = new JPanelStates(this);
		}
		return jPanelStates;
	}
	
	/**
	 * Returns if the mouse is over the widget.
	 * @return true, if is mouse over widget
	 */
	private boolean isMouseOverWidget() {
		
		if (this.isVisible()==false || this.isShowing()==false) return false;
		
		Point mousePos = MouseInfo.getPointerInfo().getLocation();
		Rectangle bounds = this.getBounds();
		bounds.setLocation(this.getLocationOnScreen());
		return bounds.contains(mousePos);
	}
	
	/**
	 * Sets the widget orientation.
	 * @param widgetOrientation the new widget orientation
	 */
	protected void setWidgetOrientation(WidgetOrientation widgetOrientation) {
		if (widgetOrientation!=null && widgetOrientation!=this.widgetOrientation) {
			this.widgetOrientation = widgetOrientation;
			Application.getGlobalInfo().putStringToConfiguration(WIDGET_ORIENTATION_PROPERTY, this.widgetOrientation.toString());
			this.setSizeAndPosition();
			this.setContentPanels();
			this.getJPanelStates().setWidgetOrientation(widgetOrientation);
			this.resetClosingTime();
		}
	}
	/**
	 * Returns the widget orientation.
	 * @return the widget orientation
	 */
	protected WidgetOrientation getWidgetOrientation() {
		if (widgetOrientation==null) {
			String widgetOrientationString = Application.getGlobalInfo().getStringFromConfiguration(WIDGET_ORIENTATION_PROPERTY, WidgetOrientation.Bottom.toString());
			widgetOrientation = WidgetOrientation.valueOf(widgetOrientationString);
		}
		return widgetOrientation;
	}
	
	/**
	 * Returns the widget width height, which usage depends on the widget orientation.
	 * @return the widget width height
	 */
	protected Integer getWidgetWidthHeight() {
		if (widgetWidthHeight==null) {
			widgetWidthHeight = Application.getGlobalInfo().getIntFromConfiguration(WIDGET_WIDTH_HEIGTH, 0);
		}
		return widgetWidthHeight;
	}
	/**
	 * Sets the widget width height.
	 * @param widgetWidthHeight the new widget width height
	 */
	protected void setWidgetWidthHeight(Integer widgetWidthHeight) {
		this.widgetWidthHeight = widgetWidthHeight;
		Application.getGlobalInfo().putIntToConfiguration(WIDGET_WIDTH_HEIGTH, widgetWidthHeight);
	}
	/**
	 * On resized internal frame remind the dimension.
	 */
	private void onResizedInternalFrame() {
			
		this.resetClosingTime();
		
		int widthHeight = 0;
		switch (this.getWidgetOrientation()) {
		case Left:
		case Right:
			widthHeight = this.getWidth();
			break;
			
		case Top:
		case Bottom:
			widthHeight = this.getHeight();
			break;
		}
		if (widthHeight!=0) {
			this.setWidgetWidthHeight(widthHeight);
		}
	}
	
	/**
	 * Sets if the widget visualization is time controlled.
	 * @param timeControlled the new time controlled
	 */
	protected void setTimeControlled(boolean timeControlled) {
		if (timeControlled!=this.timeControlled) {
			this.timeControlled = timeControlled;
			Application.getGlobalInfo().putBooleanToConfiguration(WIDGET_TIME_CONTROLLED_PROPERTY, this.timeControlled);
			if (this.timeControlled==true) {
				this.restartClosingThread();
			}
		}
	}
	/**
	 * Checks if the widget visualization is time controlled.
	 * @return true, if is time controlled
	 */
	protected boolean isTimeControlled() {
		if (timeControlled==null) {
			timeControlled = Application.getGlobalInfo().getBooleanFromConfiguration(WIDGET_TIME_CONTROLLED_PROPERTY, true);
		}
		return timeControlled;
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
					MessagingJInternalFrame.this.repaintWidget();
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
			double scaleFactor = 0.21;
			int myHeight = (int) (scaleFactor * (double) parentHeight);
			int myWidth = parentWidth;
			
			// --- Get reminded widthHeigth -----
			if (this.getWidgetWidthHeight()!=0) myHeight = this.getWidgetWidthHeight();
			
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
				myWidth = (int) (scaleFactor * (double) parentWidth);
				if (this.getWidgetWidthHeight()!=0) myWidth = this.getWidgetWidthHeight();
				myLocationY = parentPosY;
				break;
				
			case Right:
				myHeight = parentHeight;
				myWidth = (int) (scaleFactor * (double) parentWidth);
				if (this.getWidgetWidthHeight()!=0) myWidth = this.getWidgetWidthHeight();
				myLocationY = parentPosY;
				myLocationX = parentPosX + parentWidth - myWidth;
				break;
			}
			
			// --- Do the positioning -----------------------------------------
			this.setLocation(myLocationX, myLocationY);
			this.setSize(new Dimension(myWidth, myHeight));
			
			// --- Configure the size of the message panel --------------------
			int messageHeight = 0;
			int messageWidth  = 0;
			switch (this.getWidgetOrientation()) {
			case Bottom:
			case Top:
				messageHeight = myHeight;
				messageWidth  = (int) ((double)myWidth * 2.0/3.0);
				break;

			case Left:
			case Right:
				messageHeight = (int) ((double)myHeight* 2.0/3.0);
				messageWidth  = myWidth;
				break;
			}
			this.getJPanelMessages().setSize(new Dimension(messageWidth, messageHeight));
			this.getJPanelMessages().setMaximumSize(new Dimension(messageWidth, messageHeight));
			
		} else {
			this.setLocation(0, 0);
			this.setSize(new Dimension(600, 200));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#registerAtDesktopAndSetVisible()
	 */
	@Override
	public void registerAtDesktopAndSetVisible() {
		if (this.isVisible()==false) {
			super.registerAtDesktopAndSetVisible();
		}
		this.restartClosingThread();
	}
	
	/**
	 * (Re-)Starts the widget closing thread.
	 */
	private void restartClosingThread() {
		if (this.isTimeControlled()==false) return;
		this.resetClosingTime();
		if (this.getCloserThread().isAlive()==false) {
			this.getCloserThread().start();
		}
	}
	/**
	 * Restart visible show time.
	 */
	private void resetClosingTime() {
		this.setClosingTime(System.currentTimeMillis() + this.visibleTime);
	}
	/**
	 * Returns the current closing time.
	 * @return the closing time
	 */
	private long getClosingTime() {
		if (closingTime==0) {
			closingTime = System.currentTimeMillis() + this.visibleTime;
		}
		return closingTime;
	}
	/**
	 * Sets the closing time.
	 * @param closingTime the new closing time
	 */
	public void setClosingTime(long closingTime) {
		this.closingTime = closingTime;
	}
	/**
	 * Returns the thread that will close the this widget after a defined time.
	 * @return the closer thread
	 */
	public Thread getCloserThread() {
		if (closerThread==null) {
			closerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					
					long sleepTimeShort = 100;
					while (true) {
						// --- Determine the sleep time -------------
						long sleepTime = getClosingTime() - System.currentTimeMillis();
						if (sleepTime<=0 && isMouseOverWidget()==true) {
							sleepTime = sleepTimeShort;
						}
						// --- Sleep --------------------------------
						if (sleepTime>0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException inEx) {
								inEx.printStackTrace();
							}
						}
						// --- Reset closing time ? -----------------
						if (sleepTime==sleepTimeShort && isMouseOverWidget()==false) {
							resetClosingTime();
						}
						// --- Close the widget? --------------------
						if (isTimeControlled()==true && isMouseOverWidget()==false && (isVisible()==false || getClosingTime()<=System.currentTimeMillis())) {
							// --- Hide the messaging widget --------
							setVisible(false);
							break;
						}
						if (isTimeControlled()==false) {
							break;
						}
					} // end while
					
					// --- Forget the Thread --------------
					synchronized (closerThread) {
						closerThread = null;
					}
				}
			}, this.getClass().getSimpleName() + "-Closer");
		}
		return closerThread;
	}
	
	/**
	 * Adds the specified GraphUIMessage to the table of messages .
	 * @param graphUiMessage the GraphUIMessage to add
	 */
	public void addMessage(GraphUIMessage graphUiMessage) {
		if (graphUiMessage instanceof GraphUIStateMessage) {
			this.getJPanelStates().addMessage((GraphUIStateMessage) graphUiMessage);
		}
		this.getJPanelMessages().addMessage(graphUiMessage);
	}
	/**
	 * Adds the specified message to the table of messages.
	 *
	 * @param timeStamp the time stamp of the message
	 * @param messageType the message type
	 * @param message the message itself
	 */
	public void addMessage(long timeStamp, GraphUIMessageType messageType, String message) {
		this.getJPanelMessages().addMessage(timeStamp, messageType, message);
	}
	
}
