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

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.components.ComponentTypeDialog;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.prototypes.DistributionNode;

/**
 * The Class BasicGraphGuiTools consists on additional visual tools for 
 * the configuration of the Graph- and Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiTools implements ActionListener, Observer {

	private static final String GRAPH_TOOLBAR_EXTENSION_ID = "org.awb.env.graphToolbarExtension";
	
    private final String pathImage = GraphGlobals.getPathImages();
    private final Dimension jButtonSize = new Dimension(26, 26);

    private boolean isPasteAction = false;
    private KeyAdapter keyAdapterPasteActionStop; 
    
    private JToolBar jToolBarEdit;
    private JToolBar jToolBarView;
    
    private JButton jButtonComponents;
    private JButton jButtonWindows;
    private JToggleButton jButtonSatelliteView;
    private JButton jButtonZoomFit2Window;
    private JButton jButtonZoomOne2One;
    private JButton jButtonFocusNetworkComponent= null;
    private JButton jButtonZoomIn;
    private JButton jButtonZoomOut;
    private JButton jButtonSaveImage;
    private JToggleButton jToggleMouseTransforming;
    private JToggleButton jToggleMousePicking;
    private JButton jButtonAddComponent;
    private JButton jButtonRemoveComponent;
    private JButton jButtonMergeNodes;
    private JButton jButtonSplitNode;
    private JButton jButtonRedo;
    private JButton jButtonUndo;
    private JButton jButtonClearGraph;
    private JButton jButtonImportGraph;
    private JButton jButtonCut;
    private JButton jButtonCopy;
    private JButton jButtonPaste;
    
    private JPopupMenu edgePopup;
    private JMenuItem jMenuItemDeleteCompVertex;
    private JMenuItem jMenuItemDeleteCompEdge;
    private JMenuItem jMenuItemEdgeProp;

    private JPopupMenu vertexPopup;
    private JMenuItem jMenuItemNodeProp;
    private JMenuItem jMenuItemAddComp;
    private JMenuItem jMenuItemSplitNode;

    private GraphEnvironmentController graphController;
    private GraphEnvironmentControllerGUI graphControllerGUI;
    private BasicGraphGui basicGraphGui;

    
    private Vector<CustomToolbarComponentDescription> customToolbarComponentDescriptionAdded;
    
    /**
     * Instantiates a new graph toolbar.
     */
    public BasicGraphGuiTools(GraphEnvironmentController graphEnvironmentController) {
    	this.graphController = graphEnvironmentController;
    	this.graphController.addObserver(this);
    	this.proceedCustomToolbarComponentExtensions();
    }

    /**
     * Gets {@link GraphEnvironmentControllerGUI}
     * @return the {@link GraphEnvironmentControllerGUI}
     */
    private GraphEnvironmentControllerGUI getGraphControllerGUI() {
    	if (this.graphControllerGUI==null) {
    		this.graphControllerGUI = this.graphController.getGraphEnvironmentControllerGUI();
    	}
    	return this.graphControllerGUI;
    }
    /**
     * Gets the key adapter paste action stop.
     * @return the key adapter paste action stop
     */
    private KeyAdapter getKeyAdapterPasteActionStop() {
    	if (keyAdapterPasteActionStop==null) {
    		keyAdapterPasteActionStop= new KeyAdapter() {
    			@Override
    			public void keyTyped(KeyEvent ke) {
    				if (isPasteAction==true) {
    					isPasteAction = false;
    					NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Paste_Action_Stop);
    					graphController.notifyObservers(nmNote);
    				}
    			}
    		};	
    	}
    	return keyAdapterPasteActionStop;
    }

    /**
     * Gets the Edit toolbar for the BasicGraphGui.
     * @return the toolbar
     */
    public JToolBar getJToolBarEdit() {
    	if (jToolBarEdit == null) {
    		jToolBarEdit = new JToolBar();
    		jToolBarEdit.setOrientation(JToolBar.VERTICAL);
    		jToolBarEdit.setFloatable(false);
    		jToolBarEdit.setPreferredSize(new Dimension(30, 30));
    		jToolBarEdit.addKeyListener(this.getKeyAdapterPasteActionStop());
    		
    		jToolBarEdit.add(getJButtonUndo());
    		jToolBarEdit.add(getJButtonRedo());
			this.setUndoRedoButtonsEnabled();

    		jToolBarEdit.addSeparator();
    		jToolBarEdit.add(getJButtonAddComponent());
    		jToolBarEdit.add(getJButtonRemoveComponent());

    		jToolBarEdit.addSeparator();
    		jToolBarEdit.add(getJButtonCut());
    		jToolBarEdit.add(getJButtonCopy());
    		jToolBarEdit.add(getJButtonPaste());
    		
    		jToolBarEdit.addSeparator();
    		jToolBarEdit.add(getJButtonMergeNodes());
    		jToolBarEdit.add(getJButtonSplitNode());

			jToolBarEdit.addSeparator();
			jToolBarEdit.add(getJButtonImportGraph());
    		jToolBarEdit.add(getJButtonClearGraph());
	    	
    	}
    	return jToolBarEdit;
    }

    /**
     * Gets the View toolbar for the BasicGraphGui.
     * @return the toolbar
     */
    public JToolBar getJToolBarView() {
    	if (jToolBarView == null) {
    		jToolBarView = new JToolBar();
    		jToolBarView.setOrientation(JToolBar.VERTICAL);
    		jToolBarView.setFloatable(false);
    		jToolBarView.setPreferredSize(new Dimension(30, 30));

    		jToolBarView.add(getJButtonComponents());
    		jToolBarView.addSeparator();
    		
    		jToolBarView.add(getJButtonWindows());
    		jToolBarView.addSeparator();
    		
    		jToolBarView.add(getJToggleMousePicking());
    		jToolBarView.add(getJToggleMouseTransforming());

  		    ButtonGroup bg = new ButtonGroup();
  		    bg.add(getJToggleMousePicking());
  		    bg.add(getJToggleMouseTransforming());
  		    
  		    jToolBarView.addSeparator();
  		    jToolBarView.add(getJButtonZoomIn());
		    jToolBarView.add(getJButtonZoomOut());

  		    jToolBarView.addSeparator();
  		    jToolBarView.add(getJButtonZoomFit2Window());
  		    jToolBarView.add(getJButtonZoomOne2One());
  		    jToolBarView.add(getJButtonFocusNetworkComponent());

  		    jToolBarView.addSeparator();
  		    jToolBarView.add(getJToggleButtonSatelliteView());

  		    jToolBarView.addSeparator();
  		    jToolBarView.add(getJButtonSaveImage());
  		    
    	}
    	return jToolBarView;
    }
    
    /**
     * This method initializes jButtonClearGraph
     * @return javax.swing.JButton
     */
    private JButton getJButtonClearGraph() {
		if (jButtonClearGraph == null) {
		    jButtonClearGraph = new JButton();
		    jButtonClearGraph.setPreferredSize(jButtonSize);
		    jButtonClearGraph.setIcon(new ImageIcon(getClass().getResource(pathImage + "Remove.png")));
		    jButtonClearGraph.setToolTipText(Language.translate("Clear graph", Language.EN));
		    jButtonClearGraph.addActionListener(this);
		}
		return jButtonClearGraph;
    }

    /**
     * This method initializes jButtonComponents
     * @return javax.swing.JButton
     */
    private JButton getJButtonComponents() {
		if (jButtonComponents == null) {
		    jButtonComponents = new JButton();
		    jButtonComponents.setPreferredSize(jButtonSize);
		    jButtonComponents.setIcon(new ImageIcon(getClass().getResource(pathImage + "Properties.png")));
		    jButtonComponents.setToolTipText(Language.translate("Netzwerk-Komponenten"));
		    jButtonComponents.addActionListener(this);
		}
		return jButtonComponents;
    }

    /**
     * This method initializes jButtonWindows
     * @return javax.swing.JButton
     */
    private JButton getJButtonWindows() {
		if (jButtonWindows == null) {
			jButtonWindows = new JButton();
			jButtonWindows.setPreferredSize(jButtonSize);
			jButtonWindows.setIcon(new ImageIcon(getClass().getResource(pathImage + "PropertyWindows.png")));
			jButtonWindows.setToolTipText(Language.translate("Komponenten-Eigenschaften ..."));
			jButtonWindows.addActionListener(this);
		}
		return jButtonWindows;
    }

    
    /**
     * This method initializes jButtonZoomReload
     * @return javax.swing.JButton
     */
    private JToggleButton getJToggleButtonSatelliteView() {
		if (jButtonSatelliteView == null) {
		    jButtonSatelliteView = new JToggleButton();
		    jButtonSatelliteView.setIcon(new ImageIcon(getClass().getResource(pathImage + "SatelliteView.png")));
		    jButtonSatelliteView.setPreferredSize(jButtonSize);
		    jButtonSatelliteView.setToolTipText(Language.translate("Übersichtsfenster ein- und ausblenden"));
		    jButtonSatelliteView.addActionListener(this);
		}
		return jButtonSatelliteView;
    }
    
    /**
     * This method initializes jButtonZoomReload
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomFit2Window() {
		if (jButtonZoomFit2Window == null) {
		    jButtonZoomFit2Window = new JButton();
		    jButtonZoomFit2Window.setIcon(new ImageIcon(getClass().getResource(pathImage + "FitSize.png")));
		    jButtonZoomFit2Window.setPreferredSize(jButtonSize);
		    jButtonZoomFit2Window.setToolTipText(Language.translate("An Fenster anpassen"));
		    jButtonZoomFit2Window.addActionListener(this);
		}
		return jButtonZoomFit2Window;
    }

    /**
     * This method initializes jButtonZoomReset
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOne2One() {
		if (jButtonZoomOne2One == null) {
		    jButtonZoomOne2One = new JButton();
		    jButtonZoomOne2One.setIcon(new ImageIcon(getClass().getResource(pathImage + "One2One.png")));
		    jButtonZoomOne2One.setPreferredSize(jButtonSize);
		    jButtonZoomOne2One.setToolTipText("1:1 - Zoom 100%");
		    jButtonZoomOne2One.addActionListener(this);
		}
		return jButtonZoomOne2One;
    }

    /**
     * This method initializes jButtonFocusComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonFocusNetworkComponent() {
		if (jButtonFocusNetworkComponent == null) {
			jButtonFocusNetworkComponent = new JButton();
			jButtonFocusNetworkComponent.setIcon(new ImageIcon(getClass().getResource(pathImage + "FocusComponent.png")));
			jButtonFocusNetworkComponent.setPreferredSize(jButtonSize);
			jButtonFocusNetworkComponent.setToolTipText(Language.translate("Komponente zentrieren und fokussieren"));
			jButtonFocusNetworkComponent.addActionListener(this);
		}
		return jButtonFocusNetworkComponent;
    }
    
    /**
     * This method initializes jButtonZoomIn
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomIn() {
		if (jButtonZoomIn == null) {
		    jButtonZoomIn = new JButton();
		    jButtonZoomIn.setPreferredSize(jButtonSize);
		    jButtonZoomIn.setIcon(new ImageIcon(getClass().getResource(pathImage + "ZoomIn.png")));
		    jButtonZoomIn.setToolTipText(Language.translate("Vergrößern"));
		    jButtonZoomIn.addActionListener(this);
		}
		return jButtonZoomIn;
    }

    /**
     * This method initializes jButtonZoomOut
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOut() {
		if (jButtonZoomOut == null) {
		    jButtonZoomOut = new JButton();
		    jButtonZoomOut.setIcon(new ImageIcon(getClass().getResource(pathImage + "ZoomOut.png")));
		    jButtonZoomOut.setPreferredSize(jButtonSize);
		    jButtonZoomOut.setToolTipText(Language.translate("Verkleinern"));
		    jButtonZoomOut.addActionListener(this);
		}
		return jButtonZoomOut;
    }
    
    /**
     * This method initializes jButtonSaveImage
     * @return javax.swing.JButton
     */
    private JButton getJButtonSaveImage() {
		if (jButtonSaveImage == null) {
			jButtonSaveImage = new JButton();
			jButtonSaveImage.setIcon(new ImageIcon(getClass().getResource(pathImage + "SaveAsImage.png")));
			jButtonSaveImage.setPreferredSize(jButtonSize);
			jButtonSaveImage.setToolTipText(Language.translate("Als Bild exportieren"));
			jButtonSaveImage.addActionListener(this);
		}
		return jButtonSaveImage;
    }
   
    /**
     * This method initializes jButtonAddComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonAddComponent() {
		if (jButtonAddComponent == null) {
		    jButtonAddComponent = new JButton();
		    jButtonAddComponent.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
		    jButtonAddComponent.setPreferredSize(jButtonSize);
		    jButtonAddComponent.setToolTipText(Language.translate("Add new component", Language.EN));
		    jButtonAddComponent.addActionListener(this);
		}
		return jButtonAddComponent;
    }

    /**
     * This method initializes jToggleMouseTransforming
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMouseTransforming() {
		if (jToggleMouseTransforming == null) {
		    jToggleMouseTransforming = new JToggleButton();
		    jToggleMouseTransforming.setIcon(new ImageIcon(getClass().getResource(pathImage + "move.png")));
		    jToggleMouseTransforming.setPreferredSize(jButtonSize);
		    jToggleMouseTransforming.setToolTipText(Language.translate("Switch to Transforming mode", Language.EN));
		    jToggleMouseTransforming.setSize(new Dimension(36, 36));
		    jToggleMouseTransforming.addActionListener(this);
		}
		return jToggleMouseTransforming;
    }

    /**
     * This method initializes jToggleMousePicking
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMousePicking() {
		if (jToggleMousePicking == null) {
		    jToggleMousePicking = new JToggleButton();
		    jToggleMousePicking.setIcon(new ImageIcon(getClass().getResource(pathImage + "edit.png")));
		    jToggleMousePicking.setPreferredSize(jButtonSize);
		    jToggleMousePicking.setToolTipText(Language.translate("Switch to Picking mode", Language.EN));
		    jToggleMousePicking.addActionListener(this);
		    jToggleMousePicking.setSelected(true);
		}
		return jToggleMousePicking;
    }

    /**
     * This method initializes jButtonRemoveComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonRemoveComponent() {
		if (jButtonRemoveComponent == null) {
		    jButtonRemoveComponent = new JButton();
		    jButtonRemoveComponent.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
		    jButtonRemoveComponent.setPreferredSize(jButtonSize);
		    jButtonRemoveComponent.setToolTipText(Language.translate("Remove selected component", Language.EN));
		    jButtonRemoveComponent.addActionListener(this);
		}
		return jButtonRemoveComponent;
    }

    /**
     * This method initializes jButtonMergeNodes
     * @return javax.swing.JButton
     */
    private JButton getJButtonMergeNodes() {
		if (jButtonMergeNodes == null) {
		    jButtonMergeNodes = new JButton();
		    jButtonMergeNodes.setIcon(new ImageIcon(getClass().getResource(pathImage + "Merge.png")));
		    jButtonMergeNodes.setPreferredSize(jButtonSize);
		    jButtonMergeNodes.setToolTipText(Language.translate("Merge nodes", Language.EN));
		    jButtonMergeNodes.addActionListener(this);
		}
		return jButtonMergeNodes;
    }

    /**
     * This method initializes jButtonSplitNode
     * @return javax.swing.JButton
     */
    private JButton getJButtonSplitNode() {
		if (jButtonSplitNode == null) {
		    jButtonSplitNode = new JButton();
		    jButtonSplitNode.setIcon(new ImageIcon(getClass().getResource(pathImage + "split.png")));
		    jButtonSplitNode.setPreferredSize(jButtonSize);
		    jButtonSplitNode.setToolTipText(Language.translate("Split the node into two nodes", Language.EN));
		    jButtonSplitNode.addActionListener(this);
		}
		return jButtonSplitNode;
    }

    /**
     * Returns the button for the undo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonUndo() {
		if (jButtonUndo == null) {
			jButtonUndo = new JButton();
		    jButtonUndo.setIcon(new ImageIcon(getClass().getResource(pathImage + "ActionUndo.png")));
		    jButtonUndo.setPreferredSize(jButtonSize);
		    jButtonUndo.setToolTipText(Language.translate("Undo Action", Language.EN));
		    jButtonUndo.addActionListener(this);
		}
		return jButtonUndo;
    }
    /**
     * Returns the button for the redo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonRedo() {
		if (jButtonRedo == null) {
			jButtonRedo = new JButton();
			jButtonRedo.setIcon(new ImageIcon(getClass().getResource(pathImage + "ActionRedo.png")));
			jButtonRedo.setPreferredSize(jButtonSize);
			jButtonRedo.setToolTipText(Language.translate("Redo Action", Language.EN));
			jButtonRedo.addActionListener(this);
		}
		return jButtonRedo;
    }
    
    /**
     * This method initializes jButtonCut
     * @return javax.swing.JButton
     */
    private JButton getJButtonCut() {
		if (jButtonCut == null) {
			jButtonCut = new JButton();
			jButtonCut.setIcon(new ImageIcon(getClass().getResource(pathImage + "Cut.png")));
			jButtonCut.setPreferredSize(jButtonSize);
			jButtonCut.setToolTipText(Language.translate("Cut Selection", Language.EN));
			jButtonCut.addActionListener(this);
		}
		return jButtonCut;
    }
    /**
     * This method initializes jButtonCopy
     * @return javax.swing.JButton
     */
    private JButton getJButtonCopy() {
		if (jButtonCopy == null) {
			jButtonCopy = new JButton();
			jButtonCopy.setIcon(new ImageIcon(getClass().getResource(pathImage + "Copy.png")));
			jButtonCopy.setPreferredSize(jButtonSize);
			jButtonCopy.setToolTipText(Language.translate("Copy Selection", Language.EN));
			jButtonCopy.addActionListener(this);
		}
		return jButtonCopy;
    }
    /**
     * This method initializes jButtonPaste
     * @return avax.swing.JButton
     */
    private JButton getJButtonPaste() {
		if (jButtonPaste == null) {
			jButtonPaste = new JButton();
			jButtonPaste.setIcon(new ImageIcon(getClass().getResource(pathImage + "Paste.png")));
			jButtonPaste.setPreferredSize(jButtonSize);
			jButtonPaste.setToolTipText(Language.translate("Paste from Clipboard", Language.EN));
			jButtonPaste.addActionListener(this);
			jButtonPaste.addKeyListener(this.getKeyAdapterPasteActionStop());
		}
		return jButtonPaste;
    }
    
    /**
     * This method initializes jButtonImportGraph
     * @return javax.swing.JButton
     */
    private JButton getJButtonImportGraph() {
		if (jButtonImportGraph == null) {
		    jButtonImportGraph = new JButton();
		    jButtonImportGraph.setIcon(new ImageIcon(getClass().getResource(pathImage + "MBtransImport.png")));
		    jButtonImportGraph.setPreferredSize(jButtonSize);
		    jButtonImportGraph.setToolTipText(Language.translate("Import Graph from file", Language.EN));
		    jButtonImportGraph.addActionListener(this);
		}
		return jButtonImportGraph;
    }

    /**
     * This method initializes edgePopup The menu is displayed when an edge is right clicked
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getEdgePopup() {
		if (edgePopup == null) {
		    edgePopup = new JPopupMenu();
		    if (this.graphController.getProject()!=null) {
		    	edgePopup.add(getJMenuItemDeleteCompEdge());
		    	edgePopup.addSeparator();
		    }
		    edgePopup.add(getJMenuItemEdgeProp());
		}
		return edgePopup;
    }

    /**
     * This method initializes vertexPopup The menu is displayed when a vertex is right clicked
     * @return javax.swing.JPopupMenu
     */
    public JPopupMenu getVertexPopup() {
		if (vertexPopup == null) {
		    vertexPopup = new JPopupMenu();
		    if (this.graphController.getProject()!=null) {
		    	vertexPopup.add(getJMenuItemAddComp());
			    vertexPopup.add(getJMenuItemSplitNode());
		    	vertexPopup.addSeparator();
			    vertexPopup.add(getJMenuItemDeleteCompVertex());
			    vertexPopup.addSeparator();
		    }
		    vertexPopup.add(getJMenuItemNodeProp());
		}
		return vertexPopup;
    }

    /**
     * Gets the JMenuItem to delete the currently selected vertex.
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemDeleteCompVertex() {
		if (jMenuItemDeleteCompVertex == null) {
		    jMenuItemDeleteCompVertex = new JMenuItem();
		    jMenuItemDeleteCompVertex.setText(Language.translate("Delete Component", Language.EN));
		    jMenuItemDeleteCompVertex.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
		    jMenuItemDeleteCompVertex.addActionListener(this);
		}
		return jMenuItemDeleteCompVertex;
    }
    /**
     * Gets the JMenuItem to delete the currently selected edge.
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemDeleteCompEdge() {
		if (jMenuItemDeleteCompEdge == null) {
			jMenuItemDeleteCompEdge = new JMenuItem();
			jMenuItemDeleteCompEdge.setText(Language.translate("Delete Component", Language.EN));
			jMenuItemDeleteCompEdge.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
			jMenuItemDeleteCompEdge.addActionListener(this);
		}
		return jMenuItemDeleteCompEdge;
    }
    /**
     * This method initializes jMenuItemNodeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemNodeProp() {
		if (jMenuItemNodeProp == null) {
		    jMenuItemNodeProp = new JMenuItem();
		    jMenuItemNodeProp.setText(Language.translate("Edit Vertex Properties", Language.EN));
		    jMenuItemNodeProp.setIcon(new ImageIcon(getClass().getResource(pathImage + "Properties.png")));
		    jMenuItemNodeProp.addActionListener(this);
		}
		return jMenuItemNodeProp;
    }

    /**
     * This method initializes jMenuItemEdgeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemEdgeProp() {
		if (jMenuItemEdgeProp == null) {
		    jMenuItemEdgeProp = new JMenuItem();
		    jMenuItemEdgeProp.setText(Language.translate("Edit Properties", Language.EN));
		    jMenuItemEdgeProp.setIcon(new ImageIcon(getClass().getResource(pathImage + "Properties.png")));
		    jMenuItemEdgeProp.addActionListener(this);
		}
		return jMenuItemEdgeProp;
    }

    /**
     * This method initializes jMenuItemAddComp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAddComp() {
		if (jMenuItemAddComp == null) {
		    jMenuItemAddComp = new JMenuItem();
		    jMenuItemAddComp.setText(Language.translate("Add component", Language.EN));
		    jMenuItemAddComp.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
		    jMenuItemAddComp.addActionListener(this);
		}
		return jMenuItemAddComp;
    }

    /**
     * This method initializes jMenuItemSplitNode
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemSplitNode() {
		if (jMenuItemSplitNode == null) {
		    jMenuItemSplitNode = new JMenuItem();
		    jMenuItemSplitNode.setText(Language.translate("Split Node", Language.EN));
		    jMenuItemSplitNode.setIcon(new ImageIcon(getClass().getResource(pathImage + "split.png")));
		    jMenuItemSplitNode.addActionListener(this);
		}
		return jMenuItemSplitNode;
    }

    /**
     * Opens the AddComponentDialog. 
     */
    private void showAddComponentDialog() {
		AddComponentDialog addComponentDialog = new AddComponentDialog(this.graphController);
		addComponentDialog.setVisible(true);
    }

    /**
     * Sets locally the GUI elements for the graph.
     */
    private void setGraphGuiElements() {
    	this.basicGraphGui = this.getGraphControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui();	
    }
    
    /**
     * Sets the undo and redo buttons enabled or not. 
     * Additionally the ToolTipText will be set.
     */
    private void setUndoRedoButtonsEnabled() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				UndoManager undoManager = graphController.getNetworkModelAdapter().getUndoManager();
				
				getJButtonUndo().setEnabled(undoManager.canUndo());
				getJButtonUndo().setToolTipText(undoManager.getUndoPresentationName());
				
				getJButtonRedo().setEnabled(undoManager.canRedo());
				getJButtonRedo().setToolTipText(undoManager.getRedoPresentationName());
				
			}
		});
    }
	
    /**
     * Open the JPopupMenu for the property windows.
     */
    private void openJPopupMenu4PropertyWindows() {
    	
    	// ----------------------------------------------------------
    	// --- Create the ActionListener first ----------------------
    	// ----------------------------------------------------------
    	ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				String ac = ae.getActionCommand();
				if (ac.equals("PropWindowOpenSelection")) {
					// --- Open properties of current selection -----
					final Set<Object> selectedObjects = basicGraphGui.getSelectedGraphObject();
					if (selectedObjects==null) {
						// --- No selection -------------------------
						String title = Language.translate("Fehlende Auswahl!");
						String message = Language.translate("Es wurde keine Komponente ausgewählt!");
						JOptionPane.showMessageDialog(basicGraphGui, message, title, JOptionPane.WARNING_MESSAGE);
						
					} else {
						// --- Open the property dialog(s) ----------
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								for (Object selectedObject : selectedObjects) {
									NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
									nmn.setInfoObject(selectedObject);
									graphController.notifyObservers(nmn);						
								}
							}
						});
					}
					
				} else if (ac.equals("PropWindowCloseAll")) {
					getGraphControllerGUI().getBasicGraphGuiJDesktopPane().closeAllBasicGraphGuiProperties();
					
				} else {
					JInternalFrame frame = getGraphControllerGUI().getBasicGraphGuiJDesktopPane().getEditor(ac);
					frame.moveToFront();
					
				}
			}
		}; // end ActionListener
    	
    	// ----------------------------------------------------------
		// --- Create the popup menu --------------------------------
    	// ----------------------------------------------------------
		JPopupMenu pMenue = new JPopupMenu();
		
		JMenuItem item = new JMenuItem(Language.translate("Komponenten-Eigenschaften bearbeiten"));
    	item.setActionCommand("PropWindowOpenSelection");
    	item.addActionListener(al);
    	pMenue.add(item);
    	
    	// --- List all displayed Frames ----------------------------
    	BasicGraphGuiJDesktopPane desktopPane = this.getGraphControllerGUI().getBasicGraphGuiJDesktopPane();
    	JInternalFrame[] intFrames = desktopPane.getAllFrames();
    	
		boolean separatorSet = false;
    	for (int i = 0; i < intFrames.length; i++) {
    		JInternalFrame intFrame = intFrames[i];
			if ( ! (intFrame instanceof AddComponentDialog || intFrame instanceof BasicGraphGuiRootJSplitPane)) {
				// --- Property frame listed ------------------------
				if (separatorSet==false) {
					// --- Add item to close all open windows ------- 
					pMenue.addSeparator();
			    	item = new JMenuItem(Language.translate("Alle Eigenschaftsfenster schließen"));
			    	item.setActionCommand("PropWindowCloseAll");
			    	item.addActionListener(al);
			    	pMenue.add(item);
					pMenue.addSeparator();
					separatorSet = true;
				}
				// --- Add menu item for the window -----------------
				item = new JMenuItem(intFrame.getTitle());
    	    	item.setActionCommand(intFrame.getTitle());
    	    	item.addActionListener(al);
    	    	if (i==0) {
    	    		item.setFont(new Font("Dialog", Font.BOLD, 12));
    	    	}
    	    	pMenue.add(item); 
    	    	
			}
		}
    	
    	// --- Show popup menu --------------------------------------
    	pMenue.show(this.getJButtonWindows(), this.getJButtonWindows().getWidth(), 0);
    	
    }
    
    /**
     * Returns the vector of CustomToolbarComponentDescription's that were already added.
     * @return the custom toolbar component description added
     */
    private Vector<CustomToolbarComponentDescription> getCustomToolbarComponentDescriptionAdded() {
    	if (customToolbarComponentDescriptionAdded==null) {
    		customToolbarComponentDescriptionAdded = new Vector<>();
    	}
    	return customToolbarComponentDescriptionAdded;
    }
    
    /**
     * Rebuild custom toolbar component.
     */
    private void rebuildCustomToolbarComponent() {
    	Vector<CustomToolbarComponentDescription> customComponents = this.graphController.getNetworkModel().getGeneralGraphSettings4MAS().getCustomToolbarComponentDescriptions();
    	for (CustomToolbarComponentDescription customCopnent : customComponents) {
    		if (this.isAlreadyAdded(customCopnent)==false) {
    			this.addCustomToolbarComponent(customCopnent);
    		}
    	}
    }
    /**
     * Checks if the specified {@link CustomToolbarComponentDescription} is already added.
     *
     * @param compDescription the CustomToolbarComponentDescription
     * @return true, if is already added
     */
    private boolean isAlreadyAdded(CustomToolbarComponentDescription compDescription) {
    	for (int i = 0; i < this.getCustomToolbarComponentDescriptionAdded().size(); i++) {
			if (this.getCustomToolbarComponentDescriptionAdded().get(i).equals(compDescription)) return true;
		}
    	return false;
    }
    /**
     * Adds the specified custom toolbar component.
     * @param compDescription the component description
     */
    private void addCustomToolbarComponent(CustomToolbarComponentDescription compDescription) {
    	
    	if (compDescription==null || compDescription.getToolBarType()==null || compDescription.getToolBarSurrounding()==null) return;
    	// --------------------------------------------------------------------
    	// --- Check if the component should currently be added ---------------
    	// --------------------------------------------------------------------
    	boolean isExecutionTime = this.graphController.getProject()==null;
    	boolean addComponent = false;
    	switch (compDescription.getToolBarSurrounding()) {
		case Both:
			addComponent = true;
			break;
		case ConfigurationOnly:
			if (isExecutionTime==false) addComponent = true;
			break;
		case RuntimeOnly:
			if (isExecutionTime==true) addComponent = true;
			break;
		}
    	if (addComponent==false) return;
    	
    	// --------------------------------------------------------------------
    	// --- Get the needed JToolBar ----------------------------------------
    	// --------------------------------------------------------------------
    	JToolBar toolBar = null;
    	switch (compDescription.getToolBarType()) {
		case EditControl:
			// --- Nothing to do in case of edit & execution --------
			if (isExecutionTime==true) return;
			toolBar = this.getJToolBarEdit();
			break;

		case ViewControl:
			toolBar = this.getJToolBarView();
			break;
		}

    	// --------------------------------------------------------------------
    	// --- Add the component, if not already there ------------------------
    	// --------------------------------------------------------------------
    	AbstractCustomToolbarComponent componentClass = compDescription.getToolBarComponent(this.graphController);
    	// --- Avoid double creation ------------------------------------------
    	if (componentClass!=null && componentClass.getCreatedCustomComponent()==null) {
			// --- Create and remind the custom component ---------------------
    		JComponent customComponent = componentClass.getCustomComponent();
			componentClass.setCreatedCustomComponent(customComponent);
        	// --- Does the component already exists --------------------------
			if (toolBar.getComponentIndex(customComponent)==-1) {
        		// --- Add a JSeparator first, if wished ----------------------
        		if (compDescription.isAddSeparatorFirst()==true) {
        			JToolBar.Separator separator = new JToolBar.Separator(); 
        			if (compDescription.getIndexPosition()==null) {
            			toolBar.add(separator);
            		} else {
            			toolBar.add(separator, (int)compDescription.getIndexPosition());
            		}	
        		}
        		// --- Add new component --------------------------------------
        		if (compDescription.getIndexPosition()==null) {
        			toolBar.add(customComponent);
        		} else {
        			if (compDescription.isAddSeparatorFirst()==true) {
        				toolBar.add(customComponent, (int)compDescription.getIndexPosition()+1);
        			} else {
        				toolBar.add(customComponent, (int)compDescription.getIndexPosition());
        			}
        		}
        		toolBar.validate();
        		toolBar.repaint();
        		// --- Remind added component ---------------------------------
        		this.getCustomToolbarComponentDescriptionAdded().add(compDescription);
        	}
    	}
    	
    }
    
    
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
	
		if (object instanceof NetworkModelNotification) {
			
			this.setUndoRedoButtonsEnabled();
			
			NetworkModelNotification nmNotification = (NetworkModelNotification) object;
			int reason = nmNotification.getReason();
			Object infoObject = nmNotification.getInfoObject();
			
			switch (reason) {
			case NetworkModelNotification.NETWORK_MODEL_Reload:
				// --- Check for customised JButtons  -------------------------
				this.rebuildCustomToolbarComponent();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_AddedCustomToolbarComponentDescription:
				// --- A BasicGraphGuiCustomJButtonDescription was added ------
				CustomToolbarComponentDescription compDescription = (CustomToolbarComponentDescription) infoObject;
				this.addCustomToolbarComponent(compDescription);	
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Satellite_View:
				// --- Toggle satellite view button ---------------------------
				Boolean visible = (Boolean) infoObject;
				if (this.getJToggleButtonSatelliteView().isSelected()==true && visible==false) {
					this.getJToggleButtonSatelliteView().setSelected(false);
				} else if (this.getJToggleButtonSatelliteView().isSelected()==false && visible==true) {
					this.getJToggleButtonSatelliteView().setSelected(true);
				}
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_Paste_Action_Do:
				// --- Set parameter for 'PASTE' action -----------------------
				this.isPasteAction=true;
				break;
			}
			
		}
	}
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
		
    	this.setGraphGuiElements();
    	
		if (ae.getSource() == getJButtonComponents()) {
			// ------------------------------------------------------
			// --- Edit the ComponentType settings ------------------
			NetworkModel networkModel = this.graphController.getNetworkModelAdapter().getNetworkModel();
			if (networkModel!=null) {
				
				GeneralGraphSettings4MAS settings = networkModel.getGeneralGraphSettings4MAS();
				
				ComponentTypeDialog ctsDialog = null;
				GlobalInfo globalInfo = Application.getGlobalInfo();
				Frame ownerFrame = globalInfo.getOwnerFrameForComponent(this.getGraphControllerGUI());
				if (ownerFrame!=null) {
					ctsDialog = new ComponentTypeDialog(ownerFrame, settings, this.graphController.getProject());
				} else {
					Dialog ownerDialog = globalInfo.getOwnerDialogForComponent(this.getGraphControllerGUI());
					if (ownerDialog!=null) {
						ctsDialog = new ComponentTypeDialog(ownerDialog, settings, this.graphController.getProject());
					} else {
						ctsDialog = new ComponentTypeDialog(settings, this.graphController.getProject());		
					}
				}
				ctsDialog.setVisible(true);
				// - - - Waiting here - - -
				if (ctsDialog.isCanceled()==false) {
					this.graphController.getNetworkModelAdapter().setGeneralGraphSettings4MAS(ctsDialog.getGeneralGraphSettings4MAS());
					this.graphController.getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui().setEdgeShapeTransformer();
				}
				ctsDialog.dispose();
				ctsDialog = null;
				
			} else {
				// --- No NetworkModel available ----------
				String title = Language.translate("Missing NetworkModel", Language.EN);
				String msg = Language.translate("Currently, no network model is available!", Language.EN);
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), msg, title, JOptionPane.INFORMATION_MESSAGE);
			}
			
		} else if (ae.getSource() == getJButtonWindows()) {
			// ------------------------------------------------------
			// --- Property Windows ---------------------------------
			this.openJPopupMenu4PropertyWindows();
			
		} else if (ae.getSource() == getJToggleButtonSatelliteView()) {
			// ------------------------------------------------------
			// --- Open satellite view ------------------------------
			this.graphController.getNetworkModelAdapter().setSatelliteView(getJToggleButtonSatelliteView().isSelected());
			
		} else if (ae.getSource() == getJButtonZoomFit2Window()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelAdapter().zoomFit2Window();
			
		} else if (ae.getSource() == getJButtonZoomOne2One()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelAdapter().zoomOne2One();
			
		} else if (ae.getSource() == getJButtonFocusNetworkComponent()) {
			// ------------------------------------------------------
			// --- Button Focus Component ---------------------------
			this.graphController.getNetworkModelAdapter().zoomNetworkComponent();
			
		} else if (ae.getSource() == getJButtonZoomIn()) {
			// ------------------------------------------------------
			// --- Button Zoom in -----------------------------------
			this.graphController.getNetworkModelAdapter().zoomIn();
			
		} else if (ae.getSource() == getJButtonZoomOut()) {
			// ------------------------------------------------------
			// --- Button Zoom out ----------------------------------
			this.graphController.getNetworkModelAdapter().zoomOut();

		} else if (ae.getSource() == getJButtonCut()) {
			// ------------------------------------------------------
			// --- Cut Action ---------------------------------------
			Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
			HashSet<NetworkComponent> selectedComponents = this.graphController.getNetworkModelAdapter().getNetworkComponentsFullySelected(nodeSet);
			if(selectedComponents!=null && selectedComponents.size()>0){
				// --- Copy to clipboard ----------------------------
				this.graphController.copyToClipboard(selectedComponents);
				// --- Remove component and update graph ------------ 
				this.graphController.getNetworkModelAdapter().removeNetworkComponents(selectedComponents);
			}
			
		} else if (ae.getSource() == getJButtonCopy()) {
			// ------------------------------------------------------
			// --- Copy Action --------------------------------------
			Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
			HashSet<NetworkComponent> selectedComponents = this.graphController.getNetworkModelAdapter().getNetworkComponentsFullySelected(nodeSet);
			this.graphController.copyToClipboard(selectedComponents);
			
		} else if (ae.getSource() == getJButtonPaste()) {
			// ------------------------------------------------------
			// --- Paste Action -------------------------------------
			if (this.graphController.getClipboardNetworkModel()!=null) {
				NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Paste_Action_Do);
				this.graphController.notifyObservers(nmNote);	
			}
			
		} else if (ae.getSource() == getJButtonSaveImage()) {
			// ------------------------------------------------------
			// --- Save graph as Image ------------------------------
			this.graphController.getNetworkModelAdapter().saveAsImage();
			
		} else if (ae.getSource() == getJToggleMouseTransforming()) {
			// ------------------------------------------------------
			// --- Button Transforming Mouse mode -------------------
			this.graphController.getNetworkModelAdapter().setGraphMouseTransforming();
			
		} else if (ae.getSource() == getJToggleMousePicking()) {
			// ------------------------------------------------------
			// --- Button Picking Mouse mode ------------------------
			this.graphController.getNetworkModelAdapter().setGraphMousePicking();
			
		} else if (ae.getSource() == getJButtonAddComponent() || ae.getSource() == getJMenuItemAddComp()) {
			// ------------------------------------------------------
			// --- Add Component Button Clicked ---------------------
			this.showAddComponentDialog();
						
		} else if (ae.getSource() == getJButtonRemoveComponent() || ae.getSource() == getJMenuItemDeleteCompVertex() || ae.getSource() == getJMenuItemDeleteCompEdge()) {
			// ------------------------------------------------------
			// --- Remove Component Button clicked ------------------
			boolean removeDistributionNodes = true;
			Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
			Set<GraphEdge> edgeSet = this.basicGraphGui.getPickedEdges();
			HashSet<NetworkComponent> selectedComponents = this.graphController.getNetworkModelAdapter().getNetworkComponentsFullySelected(nodeSet);
			
			// ------------------------------------------------------
			// --- Capture the case of one edged-NetworkComponent ---  
			// --- and several DistributionNodes are selected    ----
			if (selectedComponents!=null && selectedComponents.size()>1 && edgeSet.size()>=1) {
				// --- Determine NetworkComponents by edges ---------
				HashSet<NetworkComponent> edgeComponents = new HashSet<NetworkComponent>();
				for (GraphEdge edgeSelected : edgeSet) {
					NetworkComponent componentFound = this.graphController.getNetworkModelAdapter().getNetworkComponent(edgeSelected);
					if (componentFound!=null && edgeComponents.contains(componentFound)==false) {
						edgeComponents.add(componentFound);
						if (edgeComponents.size()>1) {
							break;
						}
					}
				}
				// --- Only if we could find a single component -----
				if (edgeComponents.size()==1) {
					NetworkComponent edgeComponent = edgeComponents.iterator().next();
					if (selectedComponents.contains(edgeComponent)==true) {
						// --- Get remaining list of selections  ----
						HashSet<NetworkComponent> disNodeComponents = new HashSet<NetworkComponent>(selectedComponents);
						disNodeComponents.remove(edgeComponent);
						// --- Are there DistributionNodes? ---------
						for(NetworkComponent disNodeSearch : disNodeComponents) {
							if (disNodeSearch.getPrototypeClassName().equals(DistributionNode.class.getName())==false) {
								disNodeComponents.remove(disNodeSearch);
							}
						}
						// --- Prepare user request -----------------
						String title = Language.translate("Also remove node components?", Language.EN);
						String msg = Language.translate("The selection contains also NetworkComponets that are single nodes.", Language.EN) + "\n";
						msg += Language.translate("These are:", Language.EN) + "\n";
						for(NetworkComponent distributionNode : disNodeComponents) {
							msg += "- " + distributionNode.getType() + " (" + distributionNode.getId() + ")\n";
						}
						msg += Language.translate("Should they be removed too?", Language.EN);
						int userAnswer = JOptionPane.showConfirmDialog(this.getGraphControllerGUI(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (userAnswer==JOptionPane.NO_OPTION) {
							removeDistributionNodes = false;
							for(NetworkComponent distributionNode : disNodeComponents) {
								selectedComponents.remove(distributionNode);
							}
						}
					}
				}
			}
			// --- Proceed with deleting, if applicable -------------  
			if(selectedComponents!=null && selectedComponents.size()>0){ 
				// --- Remove component and update graph ------------ 
				this.graphController.getNetworkModelAdapter().removeNetworkComponents(selectedComponents, removeDistributionNodes);	
			} else {
				// --- Nothing valid picked -------------------------
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Select a valid element first!", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.INFORMATION_MESSAGE);	
			}
			
		} else if (ae.getSource() == getJButtonMergeNodes()) {
			// ------------------------------------------------------
			// --- Merge Nodes Button clicked -----------------------
			Set<GraphNode> nodeSet = this.basicGraphGui.getPickedNodes();
			if(nodeSet.size()>=2){
				boolean mergeError = false;
				GraphNode node2Add2 = null;
				HashSet<GraphNode> nodeHash2Add = new HashSet<GraphNode>();
				for (GraphNode node : nodeSet) {
					if (this.graphController.getNetworkModelAdapter().isFreeGraphNode(node)==false) {
						mergeError=true;
						break;
					}
					if (node2Add2 == null) {
						node2Add2 = node;
					} else {
						nodeHash2Add.add(node);
					}
				}
				
				if (mergeError==false) {
					// --- Valid nodes are picked -------------------
					GraphNodePairs mergeNodes = new GraphNodePairs(node2Add2, nodeHash2Add);
					mergeNodes = this.graphController.getNetworkModel().getValidGraphNodePairConfig4Merging(mergeNodes);
					if (mergeNodes==null) {
						String msg = "Invalid node selection!";
						JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);

					} else {
						this.graphController.getNetworkModelAdapter().mergeNodes(mergeNodes);	
					}
					
				} else {
					// --- Invalid nodes are picked -----------------
					JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Select at least two valid vertices!", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
				}
			} else {
				// --- At least two nodes are required --------------
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Use Shift and click on two vertices at least!", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}	
			
			
		} else if (ae.getSource() == getJButtonSplitNode() || ae.getSource() == getJMenuItemSplitNode()) {
			// ------------------------------------------------------
			// --- Button Split node --------------------------------
			GraphNode pickedNode = basicGraphGui.getPickedSingleNode();
			if(pickedNode!=null){
				// --- One vertex is picked -----
				HashSet<NetworkComponent> components = this.graphController.getNetworkModelAdapter().getNetworkComponents(pickedNode);
				NetworkComponent containsDistributionNode = this.graphController.getNetworkModelAdapter().containsDistributionNode(components);
				if (containsDistributionNode!=null) {
					if(components.size()>=2){
						this.graphController.getNetworkModelAdapter().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("The select Vertex should be at least between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}

				} else {
					if(components.size()==2){
						this.graphController.getNetworkModelAdapter().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("The select Vertex should be between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
					
			} else {
				//Multiple vertices are picked
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Select one vertex !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}
		
		} else if (ae.getSource()==getJButtonUndo()) {
			this.graphController.getNetworkModelAdapter().undo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource()==getJButtonRedo()) {
			this.graphController.getNetworkModelAdapter().redo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource()==getJButtonImportGraph()) {
			this.graphController.getNetworkModelAdapter().importNetworkModel();
			
		} else if (ae.getSource()==getJButtonClearGraph()) {
			this.graphController.getNetworkModelAdapter().clearNetworkModel();
		
		} else if(ae.getSource()==getJMenuItemNodeProp()) {
			// ------------------------------------------------------
			// --- Popup Menu Item Node properties clicked ----------
			GraphNode pickedVertex = basicGraphGui.getPickedSingleNode();
			if(pickedVertex!=null){
				NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
				nmNote.setInfoObject(pickedVertex);
				this.graphController.notifyObservers(nmNote);
			}
		
		} else if(ae.getSource() == getJMenuItemEdgeProp()){
			// ------------------------------------------------------
			// --- Popup Menu Item Edge properties clicked ----------
			GraphEdge pickedEdge = basicGraphGui.getPickedSingleEdge();
			if(pickedEdge!=null){
				NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
				nmNote.setInfoObject(pickedEdge);
				this.graphController.notifyObservers(nmNote);
			}
		}		
		
	} // end actionPerformed()

    
    // ----------------------------------------------------------------------------------
    // --- From here, the handling of the CustomToolbarComponentExtension can be found --
    // ----------------------------------------------------------------------------------
    /**
	 * Proceeds the toolbar extensions that are defines by the corresponding extension point.
	 */
	private void proceedCustomToolbarComponentExtensions() {
		
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(GRAPH_TOOLBAR_EXTENSION_ID);
		try {
			for (int i = 0; i < configElements.length; i++) {
				IConfigurationElement configElement = configElements[i]; 
				Object execExt = configElement.createExecutableExtension("class");
				if (execExt instanceof CustomToolbarComponentExtension) {
					this.proceedCustomToolbarComponentExtension((CustomToolbarComponentExtension) execExt);
				}
			} 

		} catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
	}
	/**
	 * Proceeds a single {@link CustomToolbarComponentExtension}.
	 * @param mwExtension the toolbar extension to proceed
	 */
	private void proceedCustomToolbarComponentExtension(CustomToolbarComponentExtension toolbarExtension) {
		
		if (toolbarExtension==null) return;

		try {
			// --- Get the list  -----------------
			List<CustomToolbarComponentDescription> ctcDescriptionList = toolbarExtension.getCustomToolbarComponentDescriptionList();
			if (ctcDescriptionList!=null) {
				for (int i = 0; i < ctcDescriptionList.size(); i++) {
					CustomToolbarComponentDescription ctcDescription = ctcDescriptionList.get(i);
					if (ctcDescription!=null) {
						this.graphController.addCustomToolbarComponentDescription(ctcDescription);
					}
				}
			}
			
		} catch (Exception ex) {
			System.err.println(toolbarExtension.getClass().getName() + ": Error while initializing the MainWindowExtension.");
			ex.printStackTrace();
		}
	}
    
}
