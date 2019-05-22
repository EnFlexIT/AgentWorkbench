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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
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

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.GraphMouseMode;
import org.awb.env.networkModel.controller.ui.messaging.MessagingJInternalFrame;
import org.awb.env.networkModel.controller.ui.toolbar.AbstractCustomToolbarComponent;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentExtension;
import org.awb.env.networkModel.helper.GraphNodePairs;
import org.awb.env.networkModel.prototypes.DistributionNode;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;
import org.awb.env.networkModel.settings.ui.ComponentTypeDialog;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;

/**
 * The Class BasicGraphGuiTools consists on additional visual tools for 
 * the configuration of the Graph- and Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiTools implements ActionListener, Observer {

	public static final String GRAPH_TOOLBAR_EXTENSION_ID = "org.awb.env.graphToolbarExtension";
	public static final Dimension JBUTTON_SIZE = new Dimension(26, 26);

	private final String pathImage = GraphGlobals.getPathImages();

    private boolean isPasteAction = false;
    private KeyAdapter keyAdapterPasteActionStop; 
    
    private JToolBar jToolBarEdit;
    private JToolBar jToolBarView;
    private JToolBar jToolBarLayout;
    
    private JButton jButtonComponents;
    private JButton jButtonNetworkModelInfo;
    private JButton jButtonMessages;
    private JButton jButtonWindows;
    
    private JToggleButton jToggleButtonLayoutToolBar;
    
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
    private JMenuItem jMenuItemNodePositioning;
    private JMenuItem jMenuItemAddComp;
    private JMenuItem jMenuItemSplitNode;
    private JMenuItem jMenuItemNodeProp;

    private GraphEnvironmentController graphController;
    private GraphEnvironmentControllerGUI graphControllerGUI;
    private BasicGraphGui basicGraphGUI;
    
    
    private Vector<CustomToolbarComponentDescription> customToolbarComponentDescriptionAdded;
    
    /**
     * Instantiates a new graph toolbar.
     * @param graphController the current {@link GraphEnvironmentController}
     */
    public BasicGraphGuiTools(GraphEnvironmentController graphController) {
    	if (graphController!=null) {
    		this.graphController = graphController;
    		this.graphController.addObserver(this);
    	}
    	this.proceedCustomToolbarComponentExtensions();
    }

    /**
     * Gets {@link GraphEnvironmentControllerGUI}
     * @return the {@link GraphEnvironmentControllerGUI}
     */
    protected GraphEnvironmentControllerGUI getGraphControllerGUI() {
    	if (this.graphControllerGUI==null) {
    		this.graphControllerGUI = this.graphController.getGraphEnvironmentControllerGUI();
    	}
    	return this.graphControllerGUI;
    }
    /**
     * Gets the current {@link BasicGraphGui}.
     * @return the basic graph GUI
     */
    protected BasicGraphGui getBasicGraphGUI() {
    	if (basicGraphGUI==null) {
    		basicGraphGUI = this.getGraphControllerGUI().getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
    	}
    	return basicGraphGUI;
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
    		jToolBarView.add(getJToggleButtonLayoutToolBar());
    		jToolBarView.addSeparator();
    		
    		jToolBarView.add(getJButtonNetworkModelInfo());
    		jToolBarView.add(getJButtonWindows());
    		jToolBarView.add(getJButtonMessages());
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
     * Gets the Layout toolbar for the BasicGraphGui.
     * @return the toolbar for layout adjustments
     */
    public JToolBar getJToolBarLayout() {
    	if (jToolBarLayout == null) {
    		jToolBarLayout = new BasicGraphGuiToolsLayout(this.graphController, this);
    	}
    	return jToolBarLayout;
    }
    
    // --------------------------------------------------------------
    // --- From here, some production methods can be found ----------
    // --------------------------------------------------------------
    /**
     * Represents an internal factory method for new JButton.
     *
     * @param imageName the image name
     * @param toolTipText the tool tip text
     * @return the new JButton
     */
    protected JButton getNewJButton(String imageName, String toolTipText) {
    	return this.getNewJButton(imageName, toolTipText, null);
    }
    /**
     * Represents an internal factory method for new JButton.
     *
     * @param imageName the image name
     * @param toolTipText the tool tip text
     * @param actionListener the action listener to use. If null, the local listener will be used.
     * @return the new JButton
     */
    protected JButton getNewJButton(String imageName, String toolTipText, ActionListener actionListener) {
    	ImageIcon imageIcon = null;
    	if (imageName!=null && imageName.isEmpty()==false) {
    		imageIcon = new ImageIcon(this.getClass().getResource(this.pathImage + imageName));
    	}
    	return this.getNewJButton(imageIcon, toolTipText, actionListener);
    }
    
    /**
     * Represents an internal factory method for new JButton.
     * @param imageIcon the image icon
     * @param toolTipText the tool tip text
     * @return the new JButton
     */
    protected JButton getNewJButton(ImageIcon imageIcon, String toolTipText) {
    	return this.getNewJButton(imageIcon, toolTipText, null);
    }
    /**
     * Represents an internal factory method for new JButton.
     * @param imageIcon the image icon
     * @param toolTipText the tool tip text
     * @param actionListener the action listener to use. If null, the local listener will be used.
     * @return the new JButton
     */
    protected JButton getNewJButton(ImageIcon imageIcon, String toolTipText, ActionListener actionListener) {
    	JButton newJButton = new JButton();
    	newJButton.setPreferredSize(JBUTTON_SIZE);
    	if (imageIcon!=null) newJButton.setIcon(imageIcon);
    	if (toolTipText!=null && toolTipText.isEmpty()==false) newJButton.setToolTipText(toolTipText);
    	if (actionListener!=null) {
    		newJButton.addActionListener(actionListener);
    	} else {
    		newJButton.addActionListener(this);
    	}
    	return newJButton;
    }

    /**
     * Represents an internal factory method for new JToggleButton.
     *
     * @param imageName the image name
     * @param toolTipText the tool tip text
     * @return the new JButton
     */
    protected JToggleButton getNewJToggleButton(String imageName, String toolTipText) {
    	return this.getNewJToggleButton(imageName, toolTipText, null);
    }
    /**
     * Represents an internal factory method for new JToggleButton.
     *
     * @param imageName the image name
     * @param toolTipText the tool tip text
     * @param actionListener the action listener to use. If null, the local listener will be used.
     * @return the new JButton
     */
    protected JToggleButton getNewJToggleButton(String imageName, String toolTipText, ActionListener actionListener) {
    	JToggleButton newButton = new JToggleButton();
    	newButton.setPreferredSize(JBUTTON_SIZE);
    	if (imageName!=null && imageName.isEmpty()==false) newButton.setIcon(new ImageIcon(this.getClass().getResource(this.pathImage + imageName)));
    	if (toolTipText!=null && toolTipText.isEmpty()==false) newButton.setToolTipText(toolTipText);
    	if (actionListener!=null) {
    		newButton.addActionListener(actionListener);
    	} else {
    		newButton.addActionListener(this);
    	}
    	return newButton;
    }

    /**
     * Represents an internal 'factory method' for a new JMenuItem.
     *
     * @param displayText the display text
     * @param imageName the image name
     * @return the JMenuItem
     */
    protected JMenuItem getNewJMenuItem(String displayText, String imageName) {
    	return this.getNewJMenuItem(displayText, imageName, null);
    }
    /**
     * Represents an internal 'factory method' for a new JMenuItem.
     *
     * @param displayText the display text
     * @param imageName the image name
     * @param actionListener the action listener to use. If null, the local listener will be used.
     * @return the JMenuItem
     */
    protected JMenuItem getNewJMenuItem(String displayText, String imageName, ActionListener actionListener) {
    	
    	JMenuItem newJMenuItem = new JMenuItem();
	    newJMenuItem.setText(displayText);
	    if (imageName!=null && imageName.isEmpty()==false) newJMenuItem.setIcon(new ImageIcon(getClass().getResource(this.pathImage + imageName)));
	    if (actionListener!=null) {
	    	newJMenuItem.addActionListener(actionListener);
	    } else {
	    	newJMenuItem.addActionListener(this);
	    }
	    return newJMenuItem;
    }
    
    
    // --------------------------------------------------------------
    // --- From here, button definitions can be found ---------------
    // --------------------------------------------------------------
    /**
     * This method initializes jButtonClearGraph
     * @return javax.swing.JButton
     */
    private JButton getJButtonClearGraph() {
		if (jButtonClearGraph == null) {
		    jButtonClearGraph = getNewJButton("Remove.png", Language.translate("Clear graph", Language.EN));
		}
		return jButtonClearGraph;
    }
    /**
     * This method initializes jButtonComponents
     * @return javax.swing.JButton
     */
    private JButton getJButtonComponents() {
		if (jButtonComponents == null) {
		    jButtonComponents = getNewJButton("Properties.png", Language.translate("Netzwerk-Komponenten"));
		}
		return jButtonComponents;
    }

    /**
     * Returns the JToggleButton for the layout tool bar.
     * @return the JToggleButton for the layout tool bar
     */
    protected JToggleButton getJToggleButtonLayoutToolBar() {
		if (jToggleButtonLayoutToolBar == null) {
			jToggleButtonLayoutToolBar = this.getNewJToggleButton("LayoutToolbar.png", Language.translate("Layout-Toolbar ein- und ausblenden"));
			
			boolean isShowLayoutToolBar = GraphGlobals.getEclipsePreferences().getBoolean(GraphGlobals.PREF_SHOW_LAYOUT_TOOLBAR, false);
			jToggleButtonLayoutToolBar.setSelected(isShowLayoutToolBar);
		}
		return jToggleButtonLayoutToolBar;
    }
    
    
    /**
     * Return the JButton for the network model info.
     * @return the jbutton network model info
     */
    private JButton getJButtonNetworkModelInfo() {
    	if (jButtonNetworkModelInfo==null) {
    		jButtonNetworkModelInfo = this.getNewJButton(GlobalInfo.getInternalImageIcon("StateInformation.png"), Language.translate("Network Model Information"));
    	}
    	return jButtonNetworkModelInfo;
    }
    /**
     * This method initializes jButtonMessages
     * @return javax.swing.JButton
     */
    private JButton getJButtonMessages() {
		if (jButtonMessages == null) {
			jButtonMessages = this.getNewJButton("Message.png", Language.translate("Messaging", Language.EN));
		}
		return jButtonMessages;
    }
    
    /**
     * This method initializes jButtonWindows
     * @return javax.swing.JButton
     */
    private JButton getJButtonWindows() {
		if (jButtonWindows == null) {
			jButtonWindows = this.getNewJButton("PropertyWindows.png", Language.translate("Komponenten-Eigenschaften ..."));
		}
		return jButtonWindows;
    }

    
    /**
     * This method initializes jButtonZoomReload
     * @return javax.swing.JButton
     */
    private JToggleButton getJToggleButtonSatelliteView() {
		if (jButtonSatelliteView == null) {
		    jButtonSatelliteView = this.getNewJToggleButton("SatelliteView.png", Language.translate("Übersichtsfenster ein- und ausblenden"));
		}
		return jButtonSatelliteView;
    }
    
    /**
     * This method initializes jButtonZoomReload
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomFit2Window() {
		if (jButtonZoomFit2Window == null) {
		    jButtonZoomFit2Window = this.getNewJButton("FitSize.png", Language.translate("An Fenster anpassen"));
		}
		return jButtonZoomFit2Window;
    }

    /**
     * This method initializes jButtonZoomReset
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOne2One() {
		if (jButtonZoomOne2One == null) {
		    jButtonZoomOne2One = this.getNewJButton("One2One.png", "1:1 - Zoom 100%");
		}
		return jButtonZoomOne2One;
    }

    /**
     * This method initializes jButtonFocusComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonFocusNetworkComponent() {
		if (jButtonFocusNetworkComponent == null) {
			jButtonFocusNetworkComponent = this.getNewJButton("FocusComponent.png", Language.translate("Komponente zentrieren und fokussieren"));
		}
		return jButtonFocusNetworkComponent;
    }
    
    /**
     * This method initializes jButtonZoomIn
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomIn() {
		if (jButtonZoomIn == null) {
		    jButtonZoomIn = this.getNewJButton("ZoomIn.png", Language.translate("Vergrößern"));
		}
		return jButtonZoomIn;
    }

    /**
     * This method initializes jButtonZoomOut
     * @return javax.swing.JButton
     */
    private JButton getJButtonZoomOut() {
		if (jButtonZoomOut == null) {
		    jButtonZoomOut = this.getNewJButton("ZoomOut.png", Language.translate("Verkleinern"));
		}
		return jButtonZoomOut;
    }
    
    /**
     * This method initializes jButtonSaveImage
     * @return javax.swing.JButton
     */
    private JButton getJButtonSaveImage() {
		if (jButtonSaveImage == null) {
			jButtonSaveImage = this.getNewJButton("SaveAsImage.png", Language.translate("Als Bild exportieren"));
		}
		return jButtonSaveImage;
    }

    /**
     * This method initializes jToggleMouseTransforming
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMouseTransforming() {
		if (jToggleMouseTransforming == null) {
		    jToggleMouseTransforming = this.getNewJToggleButton("move.png", Language.translate("Switch to Transforming mode", Language.EN));
		}
		return jToggleMouseTransforming;
    }

    /**
     * This method initializes jToggleMousePicking
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleMousePicking() {
		if (jToggleMousePicking == null) {
		    jToggleMousePicking = this.getNewJToggleButton("edit.png", Language.translate("Switch to Picking mode", Language.EN));
		    jToggleMousePicking.setSelected(true);
		}
		return jToggleMousePicking;
    }

    /**
     * This method initializes jButtonAddComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonAddComponent() {
		if (jButtonAddComponent == null) {
		    jButtonAddComponent = this.getNewJButton("ListPlus.png", Language.translate("Add new component", Language.EN));
		}
		return jButtonAddComponent;
    }
    /**
     * This method initializes jButtonRemoveComponent
     * @return javax.swing.JButton
     */
    private JButton getJButtonRemoveComponent() {
		if (jButtonRemoveComponent == null) {
		    jButtonRemoveComponent = this.getNewJButton("ListMinus.png", Language.translate("Remove selected component", Language.EN));
		}
		return jButtonRemoveComponent;
    }

    /**
     * This method initializes jButtonMergeNodes
     * @return javax.swing.JButton
     */
    private JButton getJButtonMergeNodes() {
		if (jButtonMergeNodes == null) {
		    jButtonMergeNodes = this.getNewJButton("Merge.png", Language.translate("Merge nodes", Language.EN));
		}
		return jButtonMergeNodes;
    }

    /**
     * This method initializes jButtonSplitNode
     * @return javax.swing.JButton
     */
    private JButton getJButtonSplitNode() {
		if (jButtonSplitNode == null) {
		    jButtonSplitNode = this.getNewJButton("split.png", Language.translate("Split the node into two nodes", Language.EN));
		}
		return jButtonSplitNode;
    }

    /**
     * Returns the button for the undo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonUndo() {
		if (jButtonUndo == null) {
			jButtonUndo = this.getNewJButton("ActionUndo.png", Language.translate("Undo Action", Language.EN));
		}
		return jButtonUndo;
    }
    /**
     * Returns the button for the redo action.
     * @return javax.swing.JButton
     */
    private JButton getJButtonRedo() {
		if (jButtonRedo == null) {
			jButtonRedo = this.getNewJButton("ActionRedo.png", Language.translate("Redo Action", Language.EN));
		}
		return jButtonRedo;
    }
    
    /**
     * This method initializes jButtonCut
     * @return javax.swing.JButton
     */
    private JButton getJButtonCut() {
		if (jButtonCut == null) {
			jButtonCut = this.getNewJButton("Cut.png", Language.translate("Cut Selection", Language.EN));
		}
		return jButtonCut;
    }
    /**
     * This method initializes jButtonCopy
     * @return javax.swing.JButton
     */
    private JButton getJButtonCopy() {
		if (jButtonCopy == null) {
			jButtonCopy = this.getNewJButton("Copy.png", Language.translate("Copy Selection", Language.EN));
		}
		return jButtonCopy;
    }
    /**
     * This method initializes jButtonPaste
     * @return avax.swing.JButton
     */
    private JButton getJButtonPaste() {
		if (jButtonPaste == null) {
			jButtonPaste = this.getNewJButton("Paste.png", Language.translate("Paste from Clipboard", Language.EN));
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
		    jButtonImportGraph = this.getNewJButton("MBtransImport.png", Language.translate("Import Graph from file", Language.EN));
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
		    	vertexPopup.add(getJMenuItemNodePositioning());
		    	vertexPopup.addSeparator();
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
		    jMenuItemDeleteCompVertex = this.getNewJMenuItem(Language.translate("Delete Component", Language.EN), "ListMinus.png");
		}
		return jMenuItemDeleteCompVertex;
    }
    /**
     * Gets the JMenuItem to delete the currently selected edge.
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemDeleteCompEdge() {
		if (jMenuItemDeleteCompEdge == null) {
			jMenuItemDeleteCompEdge = this.getNewJMenuItem(Language.translate("Delete Component", Language.EN), "ListMinus.png");
		}
		return jMenuItemDeleteCompEdge;
    }
    /**
     * This method initializes jMenuItemNodeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemNodeProp() {
		if (jMenuItemNodeProp == null) {
		    jMenuItemNodeProp = this.getNewJMenuItem(Language.translate("Edit Vertex Properties", Language.EN), "Properties.png");
		}
		return jMenuItemNodeProp;
    }

    /**
     * This method initializes jMenuItemEdgeProp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemEdgeProp() {
		if (jMenuItemEdgeProp == null) {
		    jMenuItemEdgeProp = this.getNewJMenuItem(Language.translate("Edit Properties", Language.EN), "Properties.png");
		}
		return jMenuItemEdgeProp;
    }
    
    /**
     * This method initializes jMenuItemNodePositioning
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemNodePositioning() {
    	if (jMenuItemNodePositioning==null) {
    		jMenuItemNodePositioning = this.getNewJMenuItem(Language.translate("Node Positioning", Language.EN), "Positioning.png", (ActionListener) this.getJToolBarLayout());
    	}
    	return jMenuItemNodePositioning;
    }
    /**
     * This method initializes jMenuItemAddComp
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAddComp() {
		if (jMenuItemAddComp == null) {
		    jMenuItemAddComp = this.getNewJMenuItem(Language.translate("Add component", Language.EN), "ListPlus.png");
		}
		return jMenuItemAddComp;
    }
    /**
     * This method initializes jMenuItemSplitNode
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemSplitNode() {
		if (jMenuItemSplitNode == null) {
		    jMenuItemSplitNode = this.getNewJMenuItem(Language.translate("Split Node", Language.EN), "split.png");
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
     * Show network model information dialog.
     */
    private void showNetworkModelInformationDialog() {
    	NetworkModelInformationDialog nmInfoDialog = new NetworkModelInformationDialog(this.graphController);
    	nmInfoDialog.setVisible(true);
    }

    /**
     * Sets the undo and redo buttons enabled or not. 
     * Additionally the ToolTipText will be set.
     */
    private void setUndoRedoButtonsEnabled() {
    	
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (BasicGraphGuiTools.this.getBasicGraphGUI().getGraphMouseMode()==GraphMouseMode.EdgeEditing) {
					BasicGraphGuiTools.this.getJButtonUndo().setEnabled(false);
					BasicGraphGuiTools.this.getJButtonRedo().setEnabled(false);
				} else {
					UndoManager undoManager = BasicGraphGuiTools.this.graphController.getNetworkModelUndoManager().getUndoManager();
					BasicGraphGuiTools.this.getJButtonUndo().setEnabled(undoManager.canUndo());
					BasicGraphGuiTools.this.getJButtonUndo().setToolTipText(undoManager.getUndoPresentationName());
					BasicGraphGuiTools.this.getJButtonRedo().setEnabled(undoManager.canRedo());
					BasicGraphGuiTools.this.getJButtonRedo().setToolTipText(undoManager.getRedoPresentationName());
				}
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
					final Set<Object> selectedObjects = BasicGraphGuiTools.this.getBasicGraphGUI().getSelectedGraphObject();
					if (selectedObjects==null) {
						// --- No selection -------------------------
						String title = Language.translate("Fehlende Auswahl!");
						String message = Language.translate("Es wurde keine Komponente ausgewählt!");
						JOptionPane.showMessageDialog(getBasicGraphGUI(), message, title, JOptionPane.WARNING_MESSAGE);
						
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
					BasicGraphGuiTools.this.getGraphControllerGUI().getBasicGraphGuiJDesktopPane().closeAllBasicGraphGuiProperties();
					
				} else {
					JInternalFrame frame = BasicGraphGuiTools.this.getGraphControllerGUI().getBasicGraphGuiJDesktopPane().getEditor(ac);
					if (frame instanceof MessagingJInternalFrame) {
						((MessagingJInternalFrame) frame).registerAtDesktopAndSetVisible();
					}
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
    	for (int i = 0; i < customComponents.size(); i++) {
    		CustomToolbarComponentDescription customCopnent = customComponents.get(i);
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
    		CustomToolbarComponentDescription checkDescription = this.getCustomToolbarComponentDescriptionAdded().get(i);
    		if (checkDescription.equals(compDescription)) {
    			return true;
    		}
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
    	case ViewControl:
    		toolBar = this.getJToolBarView();
    		break;

    	case EditControl:
			// --- Nothing to do in case of edit & execution --------
			if (isExecutionTime==true) return;
			toolBar = this.getJToolBarEdit();
			break;
			
		case LayoutControl:
			toolBar = this.getJToolBarLayout();
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
    
    /**
     * Sets that graph mouse was changed edge editing (or not).
     * @param isDoEditing the new graph mouse edge editing
     */
    private void setGraphMouseEdgeEditing(boolean isDoEdgeEditing) {
    	
    	Set<Component> excludeList = new HashSet<>();
    	excludeList.add(this.getJButtonNetworkModelInfo());
    	excludeList.add(this.getJButtonZoomIn());
    	excludeList.add(this.getJButtonZoomOut());
    	excludeList.add(this.getJButtonZoomOne2One());
    	excludeList.add(this.getJButtonZoomFit2Window());
    	excludeList.add(this.getJButtonFocusNetworkComponent());
    	excludeList.add(this.getJToggleButtonSatelliteView());
    	excludeList.add(this.getJButtonSaveImage());
    	
		this.setToolBarEnabled(this.getJToolBarEdit(), excludeList, !isDoEdgeEditing);
		this.setToolBarEnabled(this.getJToolBarView(), excludeList, !isDoEdgeEditing);
		this.setUndoRedoButtonsEnabled();
    }
	
	/**
	 * Sets the specified JToolBar (and its buttons) enabled.
	 *
	 * @param toolBar the tool bar
	 * @param excludeList the exclude list of components that should not be switched
	 * @param enabled the enabled
	 */
	private void setToolBarEnabled(JToolBar toolBar, Set<Component> excludeList, boolean enabled) {
		Component[] compArray = toolBar.getComponents();
		for (int i = 0; i < compArray.length; i++) {
			if (excludeList.contains(compArray[i])==false) {
				compArray[i].setEnabled(enabled);
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
			
			case NetworkModelNotification.NETWORK_MODEL_Add_Action_Do:
			case NetworkModelNotification.NETWORK_MODEL_Paste_Action_Do:
				// --- Set parameter for 'PASTE' action -----------------------
				this.isPasteAction=true;
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_EdgeEditing:
				// --- Start edge edit mode -----------------------------------
				this.setGraphMouseEdgeEditing(true);
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				// --- Stop edge edit mode ------------------------------------
				this.setGraphMouseEdgeEditing(false);
				break;
			}
			
		}
	}
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == getJButtonComponents()) {
			// ------------------------------------------------------
			// --- Edit the ComponentType settings ------------------
			NetworkModel networkModel = this.graphController.getNetworkModelUndoManager().getNetworkModel();
			if (networkModel!=null) {
				
				GeneralGraphSettings4MAS settings = networkModel.getGeneralGraphSettings4MAS();
				
				ComponentTypeDialog ctsDialog = null;
				GlobalInfo globalInfo = Application.getGlobalInfo();
				
				Window ownerWindow = globalInfo.getOwnerFrameForComponent(this.getGraphControllerGUI());
				if (ownerWindow==null) {
					ownerWindow = globalInfo.getOwnerDialogForComponent(this.getGraphControllerGUI());
				}
				ctsDialog = new ComponentTypeDialog(ownerWindow, settings, this.graphController.getProject());
				ctsDialog.setVisible(true);
				// - - - Waiting here - - -
				if (ctsDialog.isCanceled()==false) {
					this.graphController.getNetworkModelUndoManager().setGeneralGraphSettings4MAS(ctsDialog.getGeneralGraphSettings4MAS());
				}
				ctsDialog.dispose();
				ctsDialog = null;
				
			} else {
				// --- No NetworkModel available ----------
				String title = Language.translate("Missing NetworkModel", Language.EN);
				String msg = Language.translate("Currently, no network model is available!", Language.EN);
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), msg, title, JOptionPane.INFORMATION_MESSAGE);
			}
			
		} else if (ae.getSource() == getJToggleButtonLayoutToolBar()) {
			// ------------------------------------------------------
			// --- Show / hide layout toolbar -----------------------
			boolean isShowLayoutToolBar = getJToggleButtonLayoutToolBar().isSelected();
			this.getJToolBarLayout().setVisible(isShowLayoutToolBar);
			// --- Remind this setting ------------------------------
			GraphGlobals.getEclipsePreferences().putBoolean(GraphGlobals.PREF_SHOW_LAYOUT_TOOLBAR, isShowLayoutToolBar);
			GraphGlobals.saveEclipsePreferences();
			
		} else if (ae.getSource() == getJButtonNetworkModelInfo()) {
			// ------------------------------------------------------
			// --- Show the NetworkModel information ----------------
			this.showNetworkModelInformationDialog();
			
		} else if (ae.getSource() == getJButtonMessages()) {
			// ------------------------------------------------------
			// --- Show the message UI ------------------------------
			this.graphController.getUiMessagingController().showMessagingUI();
		
		} else if (ae.getSource() == getJButtonWindows()) {
			// ------------------------------------------------------
			// --- Property Windows ---------------------------------
			this.openJPopupMenu4PropertyWindows();
			
		} else if (ae.getSource() == getJToggleButtonSatelliteView()) {
			// ------------------------------------------------------
			// --- Open satellite view ------------------------------
			this.graphController.getNetworkModelUndoManager().setSatelliteView(getJToggleButtonSatelliteView().isSelected());
			
		} else if (ae.getSource() == getJButtonZoomFit2Window()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelUndoManager().zoomFit2Window();
			
		} else if (ae.getSource() == getJButtonZoomOne2One()) {
			// ------------------------------------------------------
			// --- Button Reset zoom --------------------------------
			this.graphController.getNetworkModelUndoManager().zoomOne2One();
			
		} else if (ae.getSource() == getJButtonFocusNetworkComponent()) {
			// ------------------------------------------------------
			// --- Button Focus Component ---------------------------
			this.graphController.getNetworkModelUndoManager().zoomNetworkComponent();
			
		} else if (ae.getSource() == getJButtonZoomIn()) {
			// ------------------------------------------------------
			// --- Button Zoom in -----------------------------------
			this.graphController.getNetworkModelUndoManager().zoomIn();
			
		} else if (ae.getSource() == getJButtonZoomOut()) {
			// ------------------------------------------------------
			// --- Button Zoom out ----------------------------------
			this.graphController.getNetworkModelUndoManager().zoomOut();

		} else if (ae.getSource() == getJButtonCut()) {
			// ------------------------------------------------------
			// --- Cut Action ---------------------------------------
			Set<GraphNode> nodeSet = this.getBasicGraphGUI().getPickedNodes();
			List<NetworkComponent> selectedComponents = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodeSet);
			if(selectedComponents!=null && selectedComponents.size()>0){
				// --- Copy to clipboard ----------------------------
				this.graphController.copyToClipboard(selectedComponents);
				// --- Remove component and update graph ------------ 
				this.graphController.getNetworkModelUndoManager().removeNetworkComponents(selectedComponents);
			}
			
		} else if (ae.getSource() == getJButtonCopy()) {
			// ------------------------------------------------------
			// --- Copy Action --------------------------------------
			Set<GraphNode> nodeSet = this.getBasicGraphGUI().getPickedNodes();
			List<NetworkComponent> selectedComponents = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodeSet);
			this.graphController.copyToClipboard(selectedComponents);
			
		} else if (ae.getSource() == getJButtonPaste()) {
			// ------------------------------------------------------
			// --- Paste Action -------------------------------------
			if (this.graphController.getClipboardNetworkModel()!=null) {
				this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Paste_Action_Do));	
			}
			
		} else if (ae.getSource() == getJButtonSaveImage()) {
			// ------------------------------------------------------
			// --- Save graph as Image ------------------------------
			this.graphController.getNetworkModelUndoManager().saveAsImage();
			
		} else if (ae.getSource() == getJToggleMouseTransforming()) {
			// ------------------------------------------------------
			// --- Button Transforming Mouse mode -------------------
			this.graphController.getNetworkModelUndoManager().setGraphMouseTransforming();
			
		} else if (ae.getSource() == getJToggleMousePicking()) {
			// ------------------------------------------------------
			// --- Button Picking Mouse mode ------------------------
			this.graphController.getNetworkModelUndoManager().setGraphMousePicking();
			
		} else if (ae.getSource() == getJButtonAddComponent() || ae.getSource() == getJMenuItemAddComp()) {
			// ------------------------------------------------------
			// --- Add Component Button Clicked ---------------------
			this.showAddComponentDialog();
						
		} else if (ae.getSource() == getJButtonRemoveComponent() || ae.getSource() == getJMenuItemDeleteCompVertex() || ae.getSource() == getJMenuItemDeleteCompEdge()) {
			// ------------------------------------------------------
			// --- Remove Component Button clicked ------------------
			boolean removeDistributionNodes = true;
			Set<GraphNode> nodeSet = this.getBasicGraphGUI().getPickedNodes();
			Set<GraphEdge> edgeSet = this.getBasicGraphGUI().getPickedEdges();
			List<NetworkComponent> selectedComponents = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodeSet);
			
			// ------------------------------------------------------
			// --- Capture the case of one edged-NetworkComponent ---  
			// --- and several DistributionNodes are selected    ----
			if (selectedComponents!=null && selectedComponents.size()>1 && edgeSet.size()>=1) {
				// --- Determine NetworkComponents by edges ---------
				HashSet<NetworkComponent> edgeComponents = new HashSet<NetworkComponent>();
				for (GraphEdge edgeSelected : edgeSet) {
					NetworkComponent componentFound = this.graphController.getNetworkModel().getNetworkComponent(edgeSelected);
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
			if (selectedComponents!=null && selectedComponents.size()>0){ 
				// --- Remove component and update graph ------------ 
				this.graphController.getNetworkModelUndoManager().removeNetworkComponents(selectedComponents, removeDistributionNodes);	
			} else {
				// --- Nothing valid picked -------------------------
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Select a valid element first!", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.INFORMATION_MESSAGE);	
			}
			
		} else if (ae.getSource() == getJButtonMergeNodes()) {
			// ------------------------------------------------------
			// --- Merge Nodes Button clicked -----------------------
			Set<GraphNode> nodeSet = this.getBasicGraphGUI().getPickedNodes();
			if(nodeSet.size()>=2){
				boolean mergeError = false;
				GraphNode node2Add2 = null;
				HashSet<GraphNode> nodeHash2Add = new HashSet<GraphNode>();
				for (GraphNode node : nodeSet) {
					if (this.graphController.getNetworkModel().isFreeGraphNode(node)==false) {
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
						this.graphController.getNetworkModelUndoManager().mergeNodes(mergeNodes);	
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
			GraphNode pickedNode = this.getBasicGraphGUI().getPickedSingleNode();
			if(pickedNode!=null){
				// --- One vertex is picked -----
				List<NetworkComponent> components = this.graphController.getNetworkModel().getNetworkComponents(pickedNode);
				NetworkComponent containsDistributionNode = this.graphController.getNetworkModel().getDistributionNode(components);
				if (containsDistributionNode!=null) {
					if(components.size()>=2){
						this.graphController.getNetworkModelUndoManager().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("The select Vertex should be at least between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}

				} else {
					if(components.size()==2){
						this.graphController.getNetworkModelUndoManager().splitNetworkModelAtNode(pickedNode);
					} else {
						JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("The select Vertex should be between two components !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
					}
				}
					
			} else {
				//Multiple vertices are picked
				JOptionPane.showMessageDialog(this.getGraphControllerGUI(), Language.translate("Select one vertex !", Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
			}
		
		} else if (ae.getSource()==getJButtonUndo()) {
			this.graphController.getNetworkModelUndoManager().undo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource()==getJButtonRedo()) {
			this.graphController.getNetworkModelUndoManager().redo();
			this.setUndoRedoButtonsEnabled();
			
		} else if (ae.getSource()==getJButtonImportGraph()) {
			this.graphController.getNetworkModelUndoManager().importNetworkModel();
			
		} else if (ae.getSource()==getJButtonClearGraph()) {
			this.graphController.getNetworkModelUndoManager().clearNetworkModel();
		
		} else if(ae.getSource()==getJMenuItemNodeProp()) {
			// ------------------------------------------------------
			// --- Popup Menu Item Node properties clicked ----------
			GraphNode pickedVertex = this.getBasicGraphGUI().getPickedSingleNode();
			if(pickedVertex!=null){
				NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
				nmNote.setInfoObject(pickedVertex);
				this.graphController.notifyObservers(nmNote);
			}
		
		} else if(ae.getSource() == getJMenuItemEdgeProp()){
			// ------------------------------------------------------
			// --- Popup Menu Item Edge properties clicked ----------
			GraphEdge pickedEdge = this.getBasicGraphGUI().getPickedSingleEdge();
			if(pickedEdge!=null){
				NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
				nmNote.setInfoObject(pickedEdge);
				this.graphController.notifyObservers(nmNote);
			}
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] => Unknow action command from " + ae.getSource());
			
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
