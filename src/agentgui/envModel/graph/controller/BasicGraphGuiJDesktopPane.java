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

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * The Class BasicGraphGuiJDesktopPane.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiJDesktopPane extends JDesktopPane {

	private static final long serialVersionUID = 5733873560749370049L;
	
	private HashMap<String, JInternalFrame> editorFrames = null;  //  @jve:decl-index=0:
	private JInternalFrame lastOpenedEditor = null;
	private ComponentListener myComponentAdapter = null;  //  @jve:decl-index=0:
	
	
	/**
	 * Instantiates a new BasicGraphGuiJLayeredPane.
	 */
	public BasicGraphGuiJDesktopPane() {
		this.initialize();
	}	
	/**
	 * Initialize this.
	 */
	private void initialize() {
		this.setSize(new Dimension(300, 300));
		this.setAutoscrolls(false);
		this.addComponentListener(this.getComponentListener());
		this.setDesktopManager(new BasicGraphGuiGraphWorkspaceDesktopManager());
	}

	/**
	 * Returns a HashMap with all open editor windows.
	 * @return the local a HashMap with all open property windows
	 */
	public HashMap<String, JInternalFrame> getHashMapEditorFrames() {
		if (this.editorFrames==null) {
			editorFrames = new HashMap<String, JInternalFrame>();
		}
		return editorFrames;
	}
	/**
	 * Can be used in order to register a property window for components or nodes.
	 * @param internalFrame the JInternalFrame to register
	 */
	public void registerEditor(JInternalFrame internalFrame) {
		this.getHashMapEditorFrames().put(internalFrame.getTitle(), internalFrame);
		this.setLastOpenedEditor(internalFrame);
	}
	/**
	 * Unregisters a property window for components or nodes
	 * @param jInternalFrame the JInternalFrame to unregister
	 */
	public void unregisterEditor(JInternalFrame internalFrame) {
		this.getHashMapEditorFrames().remove(internalFrame.getTitle());
		if (internalFrame==this.getLastOpenedEditor()) {
			this.setLastOpenedEditor(null);
		}
	}
	/**
	 * Returns, if available, the JInternalFrame for an object like GraphNode, NetworkComponet 
	 * @param editorTitle the graph object (NetworkComponent or Node)
	 * @return the instances of the related JInternalFrame, if available
	 */
	public JInternalFrame getEditor(String editorTitle) {
		return this.getHashMapEditorFrames().get(editorTitle);
	}

	/**
	 * Sets the last opened JInternalFrame.
	 * @param lastOpenedEditorFrame the new last opened JInternalFrame
	 */
	private void setLastOpenedEditor(JInternalFrame lastOpenedEditorFrame) {
		this.lastOpenedEditor = lastOpenedEditorFrame;
	}
	/**
	 * Gets the last opened JInternalFrame.
	 * @return the last opened JInternalFrame
	 */
	public JInternalFrame getLastOpenedEditor() {
		return lastOpenedEditor;
	}
	
	/**
	 * Will close all open property windows for GraphNodes and NetworkCoponents.
	 */
	public void closeAllBasicGraphGuiProperties() {
		Vector<String> internalFramesTitles = new Vector<String>(this.getHashMapEditorFrames().keySet());
		for (String internalFrameTitles : internalFramesTitles) {
			JInternalFrame internalFrame = this.getHashMapEditorFrames().get(internalFrameTitles);
			if (internalFrame instanceof BasicGraphGuiProperties) {
				try {
					internalFrame.setClosed(true);
				} catch (PropertyVetoException pve) {
					pve.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Gets the component listener.
	 * @return the component listener
	 */
	private ComponentListener getComponentListener() {
		if (myComponentAdapter==null) {
			myComponentAdapter = new ComponentAdapter() {
				public void componentResized(java.awt.event.ComponentEvent ce) {

				};
			};
		}
		return myComponentAdapter;
	}
	
	
	/**
	 * The Class BasicGraphGuiGraphWorkspaceDesktopManager.
	 * 
	 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
	 */
	private class BasicGraphGuiGraphWorkspaceDesktopManager extends DefaultDesktopManager {

		private static final long serialVersionUID = -7839103861123261921L;

		/* (non-Javadoc)
		 * @see javax.swing.DefaultDesktopManager#iconifyFrame(javax.swing.JInternalFrame)
		 */
		@Override
		public void iconifyFrame(JInternalFrame f) {
			super.iconifyFrame(f);
		}
		
	}
	
}
