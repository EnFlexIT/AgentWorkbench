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
package agentgui.envModel.graph.controller;

import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;


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
 * @see #registerAtDesktopAndSetVisible()
 * @see #registerAtDesktopAndSetVisible(Object)
 * @see AddComponentDialog
 * @see BasicGraphGuiProperties
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class BasicGraphGuiJInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = -2417300638615468552L;

    protected GraphEnvironmentController graphController = null;
    protected GraphEnvironmentControllerGUI graphControllerGUI = null;
    protected BasicGraphGuiJDesktopPane graphDesktop = null;
    protected BasicGraphGui basicGraphGui = null;
    
	/**
	 * Instantiates a new JInternalFrame that can register at the BasicGraphGuiJDesktopPane.
	 *
	 * @param controller the current GraphEnvironmentController
	 */
	public BasicGraphGuiJInternalFrame(GraphEnvironmentController controller) {
		this.graphController = controller;
		this.graphControllerGUI = (GraphEnvironmentControllerGUI)controller.getEnvironmentPanel();
		this.graphDesktop = this.graphControllerGUI.getBasicGraphGuiJDesktopPane();
		this.basicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
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
	protected void registerAtDesktopAndSetVisible(Object jDesktopLayer) {
		
		// --- Does the dialog for that component already exists? ---------
		JInternalFrame compProps = this.graphDesktop.getEditor(this.getTitle()); 
		if (compProps!=null) {
			super.dispose();
			
			compProps.moveToFront();
			try {
				compProps.setSelected(true);
			} catch (PropertyVetoException pve) {
				pve.printStackTrace();
			}
			
		} else {
			this.graphDesktop.add(this, jDesktopLayer);
			this.graphDesktop.registerEditor(this);
			
			this.setVisible(true);	
		}
		
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
