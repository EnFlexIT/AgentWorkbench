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
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import agentgui.core.application.Application;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.visualisation.notifications.DataModelNotification;
import agentgui.envModel.graph.visualisation.notifications.UpdateDataSeries;
import agentgui.envModel.graph.visualisation.notifications.UpdateDataSeriesException;

/**
 * The Class BasicGraphGuiJDesktopPane.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiJDesktopPane extends JDesktopPane {

	private static final long serialVersionUID = 5733873560749370049L;
	
	private GraphEnvironmentController graphController;
	
	private HashMap<String, JInternalFrame> editorFrames;  
	private Vector<JInternalFrame> lastOpenedEditor = new Vector<JInternalFrame>();
	private ComponentListener myComponentAdapter; 

	
	/**
	 * Instantiates a new BasicGraphGuiJLayeredPane.
	 */
	public BasicGraphGuiJDesktopPane(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		this.initialize();
	}	
	/**
	 * Initialize this.
	 */
	private void initialize() {
		
		if (Application.isOperatingHeadless()==false && Application.getMainWindow()!=null) {
			Dimension desktopSize = Application.getMainWindow().getJDesktopPane4Projects().getSize();
			this.setSize(desktopSize);
			this.setPreferredSize(desktopSize);	
		}
		
		this.setDoubleBuffered(true);
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
	 * Returns, if available, the JInternalFrame for an object like GraphNode, NetworkComponet 
	 * @param editorTitle the graph object (NetworkComponent or Node)
	 * @return the instances of the related JInternalFrame, if available
	 */
	public JInternalFrame getEditor(String editorTitle) {
		return this.getHashMapEditorFrames().get(editorTitle);
	}
	
	/**
	 * Can be used in order to register a property window for components or nodes.
	 * @param internalFrame the JInternalFrame to register
	 * @param remindAsLastOpenedEditor indicator to remind as last opened editor or not
	 */
	public void registerEditor(JInternalFrame internalFrame, boolean remindAsLastOpenedEditor ) {
		this.getHashMapEditorFrames().put(internalFrame.getTitle(), internalFrame);
		if (remindAsLastOpenedEditor==true) {
			this.setLastOpenedEditor(internalFrame);
		}
	}
	/**
	 * Unregisters a property window for components or nodes.
	 * @param internalFrame the JInternalFrame to unregister
	 */
	public void unregisterEditor(JInternalFrame internalFrame) {
		this.getHashMapEditorFrames().remove(internalFrame.getTitle());
		this.lastOpenedEditor.remove(internalFrame);
	}

	/**
	 * Sets the last opened JInternalFrame.
	 * @param lastOpenedEditorFrame the new last opened JInternalFrame
	 */
	private void setLastOpenedEditor(JInternalFrame lastOpenedEditorFrame) {
		this.lastOpenedEditor.add(lastOpenedEditorFrame);
	}
	/**
	 * Gets the last opened JInternalFrame.
	 * @return the last opened JInternalFrame
	 */
	public JInternalFrame getLastOpenedEditor() {
		if (this.lastOpenedEditor.size()==0) {
			return null;	
		} 
		return lastOpenedEditor.lastElement();
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
		 * @see javax.swing.DefaultDesktopManager#beginDraggingFrame(javax.swing.JComponent)
		 */
		@Override
		public void beginDraggingFrame(JComponent f) {
			if (getBasicGraphGuiVisViewer()!=null) {
				getBasicGraphGuiVisViewer().setActionOnTop(true);	
			}
			super.beginDraggingFrame(f);
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultDesktopManager#endDraggingFrame(javax.swing.JComponent)
		 */
		@Override
		public void endDraggingFrame(JComponent f) {
			if (getBasicGraphGuiVisViewer()!=null) {
				getBasicGraphGuiVisViewer().setActionOnTop(false);	
			}
			super.endDraggingFrame(f);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.DefaultDesktopManager#beginResizingFrame(javax.swing.JComponent, int)
		 */
		@Override
		public void beginResizingFrame(JComponent f, int direction) {
			if (getBasicGraphGuiVisViewer()!=null) {
				getBasicGraphGuiVisViewer().setActionOnTop(true);	
			}
			super.beginResizingFrame(f, direction);
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultDesktopManager#endResizingFrame(javax.swing.JComponent)
		 */
		@Override
		public void endResizingFrame(JComponent f) {
			if (getBasicGraphGuiVisViewer()!=null) {
				getBasicGraphGuiVisViewer().setActionOnTop(false);	
			}
			super.endResizingFrame(f);
		}
		/**
		 * Gets the current BasicGraphGuiVisViewer.
		 * @return the current BasicGraphGuiVisViewer
		 */
		private BasicGraphGuiVisViewer<GraphNode, GraphEdge> getBasicGraphGuiVisViewer() {
			BasicGraphGuiVisViewer<GraphNode, GraphEdge> basicGraphGuiVisViewer = null;
			try {
				basicGraphGuiVisViewer = graphController.getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui().getVisualizationViewer();
			} catch (Exception ex) {
//				ex.printStackTrace();
			}
			return basicGraphGuiVisViewer;
		}
		
	}

	/**
	 * Sets the data model update.
	 * @param dataModelNotification the new data model update
	 */
	public void setDataModelNotification(final DataModelNotification dataModelNotification) {
		
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {

				Vector<String> internalFramesTitles = new Vector<String>(getHashMapEditorFrames().keySet());
				for (String internalFrameTitles : internalFramesTitles) {
					JInternalFrame internalFrame = getHashMapEditorFrames().get(internalFrameTitles);
					if (internalFrame instanceof BasicGraphGuiProperties) {
						// --- Put notification into the property dialog ---- 
						BasicGraphGuiProperties basicProperties = (BasicGraphGuiProperties) internalFrame;
						if (basicProperties.setDataModelNotification(dataModelNotification)==true) return; // --- Done ! ---
					}
				}			
			
//			}
//		});
		
	}
	
	/**
	 * Sets an {@link UpdateDataSeries} to a data model.
	 * @param updateDataSeries the new update data series
	 */
	public void setUpdateDataSeries(final UpdateDataSeries updateDataSeries) {
		
		// ----------------------------------------------------------
		// --- Apply the update to the NetworkModel first -----------
		try {
			updateDataSeries.applyToNetworkModelOnly(this.graphController.getNetworkModel());
			
		} catch (UpdateDataSeriesException udse) {
			udse.printStackTrace();
			return;
		}

		// ----------------------------------------------------------
		// --- Apply to an open property window, if there is one ----
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
				
				Vector<String> internalFramesTitles = new Vector<String>(getHashMapEditorFrames().keySet());
				for (String internalFrameTitles : internalFramesTitles) {
					JInternalFrame internalFrame = getHashMapEditorFrames().get(internalFrameTitles);
					if (internalFrame instanceof BasicGraphGuiProperties) {
						// --- Put notification into the property dialog ---- 
						BasicGraphGuiProperties basicProperties = (BasicGraphGuiProperties) internalFrame;
						if (basicProperties.setUpdateDataSeries(updateDataSeries)==true) break;// --- Done ! ---
					}
				}
				
//			}
//		});
	
	}
	
}
