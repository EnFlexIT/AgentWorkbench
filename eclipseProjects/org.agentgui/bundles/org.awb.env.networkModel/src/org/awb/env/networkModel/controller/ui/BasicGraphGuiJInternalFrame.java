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
package org.awb.env.networkModel.controller.ui;

import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;


/**
 * This class can be used as base class for dialogs (JInternalFrames) that are used in the context
 * of the graph and network environment.
 * The class basically provides protected access to important instances of the environment model, like 
 * the {@link GraphEnvironmentController}, the {@link GraphEnvironmentControllerGUI} and the {@link BasicGraphGuiJDesktopPane}. 
 * Furthermore it provides methods to register your dialog (JInternFrame) at the current 
 * {@link BasicGraphGuiJDesktopPane}, which makes the dialog also visible. To avoid the opening
 * of dialogs that are dealing with the same content (e. g. the same {@link GraphNode} or the 
 * same {@link NetworkComponent}), mechanisms are available that prevent such issues.
 * Example can be found in the classes {@link AddComponentDialog} or {@link BasicGraphGuiProperties}.
 * 
 * @see GraphEnvironmentController
 * @see GraphEnvironmentControllerGUI
 * @see BasicGraphGuiJDesktopPane
 * @see AddComponentDialog
 * @see BasicGraphGuiProperties
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class BasicGraphGuiJInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = -2417300638615468552L;

    protected GraphEnvironmentController graphController;
    protected GraphEnvironmentControllerGUI graphControllerGUI;
    protected BasicGraphGuiJDesktopPane graphDesktop;
    protected BasicGraphGui basicGraphGui;
    
	/**
	 * Instantiates a new JInternalFrame that can register at the BasicGraphGuiJDesktopPane.
	 *
	 * @param controller the current GraphEnvironmentController
	 */
	public BasicGraphGuiJInternalFrame(GraphEnvironmentController controller) {
		this.graphController = controller;
		if (this.graphController!=null) {
			this.graphControllerGUI = this.graphController.getGraphEnvironmentControllerGUI();
			this.graphDesktop = this.graphControllerGUI.getBasicGraphGuiJDesktopPane();
			this.basicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
		}
	}
	
	/**
	 * Resets the setting for 'action on top' with a time delay (of 100 ms).
	 * @param isActionOnTop the is action on top
	 */
	protected void resetActionOnTopWithTimeDelay(boolean isActionOnTop) {
		if (this.basicGraphGui!=null) {
			this.basicGraphGui.getVisualizationViewer().resetActionOnTopWithTimeDelay(isActionOnTop);
		}
	}
	/**
	 * Resets the setting for 'action on top' with a time delay (of 'delayMillis').
	 *
	 * @param isActionOnTop the is action on top
	 * @param delayMillis the delay milliseconds
	 */
	protected void resetActionOnTopWithTimeDelay(boolean isActionOnTop, int delayMillis) {
		if (this.basicGraphGui!=null) {
			this.basicGraphGui.getVisualizationViewer().resetActionOnTopWithTimeDelay(isActionOnTop, delayMillis);
		}
	}
	/**
	 * Sets that there is an 'action on top' of the graphs visualization viewer (or not).
	 * If set <code>true</code>, the graph rendering will be hindered to accelerate the 
	 * visualization update (repaint) in the superpositioned frame or component.
	 * @param isActionOnTop the new action on top
	 */
	protected void setActionOnTop(boolean isActionOnTop) {
		if (this.basicGraphGui!=null) {
			this.basicGraphGui.getVisualizationViewer().setActionOnTop(isActionOnTop);
		}
	}
	/**
	 * Checks if there is currently an action on top of the graphs visualization viewer 
	 * that possibly hinders to repaint the graph.
	 * @return true, if there is an action on top
	 * @see #setActionOnTop(boolean)
	 */
	protected boolean isActionOnTop() {
		if (this.basicGraphGui==null) return false;
		return this.basicGraphGui.getVisualizationViewer().isActionOnTop();
	}
	
	
	/**
	 * Register at the graph desktop and set this extended JInternalFrame visible.
	 */
	protected void registerAtDesktopAndSetVisible() {
		this.registerAtDesktopAndSetVisible(JDesktopPane.PALETTE_LAYER);
	}
	/**
	 * Register at the graph desktop and set this extended JInternalFrame visible.
	 * @see JDesktopPane
	 * @param jDesktopLayer the layer in which the JInternaFrame has to be added (e.g. JDesktopPane.PALETTE_LAYER) 
	 */
	protected void registerAtDesktopAndSetVisible(Integer jDesktopLayer) {
		
		// --- Does the dialog for that component already exists? ---------
		JInternalFrame compProps = this.graphDesktop.getEditor(this.getTitle()); 
		if (compProps!=null) {
			try {
				// --- Make visible, if invisible --------- 
				if (compProps.isVisible()==false) compProps.setVisible(true);
				// --- Move to front ----------------------
				compProps.moveToFront();
				// --- Set selected -----------------------
				compProps.setSelected(true);
				
			} catch (PropertyVetoException pve) {
				pve.printStackTrace();
			}
			
		} else {
			this.graphDesktop.add(this, jDesktopLayer);
			this.graphDesktop.registerEditor(this, this.isRemindAsLastOpenedEditor());
			
			this.setVisible(true);	
		}
		
	}
	
	/**
	 * Checks if the position of this frame should be reminded as a last opened editor.
	 * if this method returns true (which is the default case), the internal frame will 
	 * be reminded and a next internal frame will be adjusted relative to the frame position.
	 * Override this method if you don't want to remind this internal frame as a 'last opened editor'
	 *  
	 * @return true, if the position of this frame should be reminded
	 */
	protected boolean isRemindAsLastOpenedEditor() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JInternalFrame#dispose()
	 */
	@Override
	public void dispose() {
		this.graphDesktop.unregisterEditor(this);
		super.dispose();
	}
	
}
