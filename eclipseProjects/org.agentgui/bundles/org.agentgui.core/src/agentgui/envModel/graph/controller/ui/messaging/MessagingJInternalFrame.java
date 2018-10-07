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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	private static final String WIDGET_TIME_CONTROLLED_PROPERTY = "GraphEnvironmentMessagingWidgetTimeControlled";
	
	public static final Color bgColor = SystemColor.info;
	
	public enum WidgetOrientation {
		Bottom,
		Top,
		Left,
		Right
	}
	
	private WidgetOrientation widgetOrientation;
	
	private MouseListener mouseListener;
	private boolean mouseOverWidget;
	
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> graphVisualizationPanel;
	private ComponentListener graphVisualizationPanelListener;
	
	private JPanelMessages jPanelMessages;
	private JPanelStates jPanelStates;
	private JPanelMenu jPanelMenu;
	
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
		this.addMouseListener(this.getMouseListener());
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
			jPanelMessages.addMouseListener(this.getMouseListener());
		}
		return jPanelMessages;
	}
	private JPanelStates getJPanelStates() {
		if (jPanelStates == null) {
			jPanelStates = new JPanelStates(this.graphController);
			jPanelStates.addMouseListener(this.getMouseListener());
		}
		return jPanelStates;
	}
	private JPanelMenu getJPanelMenu() {
		if (jPanelMenu == null) {
			jPanelMenu = new JPanelMenu(this);
			jPanelMenu.addMouseListener(this.getMouseListener());
		}
		return jPanelMenu;
	}
	public MouseListener getMouseListener() {
		if (mouseListener==null) {
			mouseListener = new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent me) {
					resetClosingTime();
					setMouseOverWidget(false);
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					setMouseOverWidget(true);
				}
			};
		}
		return mouseListener;
	}
	
	/**
	 * Sets that the mouse is over the widget.
	 * @param mouseOverWidget the new mouse over widget
	 */
	private void setMouseOverWidget(boolean mouseOverWidget) {
		this.mouseOverWidget = mouseOverWidget;
	}
	/**
	 * Returns if the mouse is over the widget.
	 * @return true, if is mouse over widget
	 */
	private boolean isMouseOverWidget() {
		return mouseOverWidget;
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
					
					while (true) {
						// --- Determine the sleep time -------------
						long sleepTime = getClosingTime() - System.currentTimeMillis();
						if (sleepTime<=0 && isMouseOverWidget()==true) {
							sleepTime = 100;
						}
						if (sleepTime>0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException inEx) {
								inEx.printStackTrace();
							}
						}
						// --- Close the widget? --------------------
						if (isMouseOverWidget()==false && (isVisible()==false || getClosingTime()<=System.currentTimeMillis())) {
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
	
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.ui.BasicGraphGuiJInternalFrame#isRemindAsLastOpenedEditor()
	 */
	@Override
	protected boolean isRemindAsLastOpenedEditor() {
		return false;
	}
	
}
