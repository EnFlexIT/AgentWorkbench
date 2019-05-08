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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.commands.AlignNodesAction.Alignment;
import org.awb.env.networkModel.controller.ui.configLines.ConfiguredLineEdit;
import org.awb.env.networkModel.controller.ui.configLines.OrthogonalConfiguration;
import org.awb.env.networkModel.controller.ui.configLines.PolylineConfiguration;
import org.awb.env.networkModel.controller.ui.configLines.QuadCurveConfiguration;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.EdgeShape;

import agentgui.core.application.Language;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * The Class BasicGraphGuiToolsLayout represents the toolbar for layout edits.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class BasicGraphGuiToolsLayout extends JToolBar implements ActionListener, Observer, GraphSelectionListener {

	private static final long serialVersionUID = -1589891707587804360L;

    private GraphEnvironmentController graphController;
    private BasicGraphGuiTools basicGraphGuiTools;
    private boolean isRegisteredGraphSelectionListener;
    
    private LayoutSelectionDialog layoutSelectionDialog;
    private GraphEdge editGraphEdge;
    
    private JToggleButton jToggleButtonLayoutSwitch;
    private JToggleButton jToggleButtonEdgeLine;
    private JToggleButton jToggleButtonEdgeOrthogonal;
    private JToggleButton jToggleButtonEdgeQuadCurve;
    private JToggleButton jToggleButtonEdgePolyLine;
    private JToggleButton jToggleButtonEdgeEdit;

    private JButton jButtonAlignLeft;
    private JButton jButtonAlignRight;
    private JButton jButtonAlignTop;
    private JButton jButtonAlignBottom;
    
    private JMenuItem jMenuItemEdgeLine;
    private JMenuItem jMenuItemEdgeOrthogonal;
    private JMenu jMenuOrthogonalEdgeDirection;
    private JMenuItem jMenuItemEdgeOrthogonalMoveYFirst;
    private JMenuItem jMenuItemEdgeOrthogonalMoveXFirst;
    private JMenuItem jMenuItemEdgeQuadCurve;
    private JMenuItem jMenuItemEdgePolyLine;
    private javax.swing.JPopupMenu.Separator separatorAfterEdgeTypeSelection;
    private JMenuItem jMenuItemEdgeEdit;
    private javax.swing.JPopupMenu.Separator separatorAfterEdgeEdit;
    
    
	/**
	 * Instantiates a new basic graph gui tools layout.
	 *
	 * @param graphController the graph controller
	 * @param basicGraphGuiTools the basic graph gui tools
	 */
	public BasicGraphGuiToolsLayout(GraphEnvironmentController graphController, BasicGraphGuiTools basicGraphGuiTools) {
		if (graphController!=null) {
			this.graphController = graphController;
			this.graphController.addObserver(this);
		}
		if (basicGraphGuiTools!=null) {
			this.basicGraphGuiTools = basicGraphGuiTools;
		}
    	this.buildToolBar();
    	this.onGraphSelectionChanged(null);
	}
	
	/**
	 * Builds the layout tool bar.
	 */
	private void buildToolBar() {
		
		this.setOrientation(JToolBar.VERTICAL);
		this.setFloatable(false);
		this.setPreferredSize(new Dimension(30, 30));
		this.setVisible(this.basicGraphGuiTools.getJToggleButtonLayoutToolBar().isSelected());
		
		this.add(this.getJToggleButtonLayoutSwitch());
		this.addSeparator();
		
		this.add(this.getJToggleButtonEdgeLine());
		this.add(this.getJToggleButtonEdgeOrthogonal());
		this.add(this.getJToggleButtonEdgeQuadCurve());
		this.add(this.getJToggleButtonEdgePolyLine());
		this.addSeparator();
		
		this.add(this.getJToggleButtonEdgeEdit());
		this.addSeparator();
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(this.getJToggleButtonEdgeLine());
		bg.add(this.getJToggleButtonEdgeQuadCurve());
		bg.add(this.getJToggleButtonEdgePolyLine());
		bg.add(this.getJToggleButtonEdgeOrthogonal());
	
		this.add(this.getJButtonAlignLeft());
		this.add(this.getJButtonAlignRight());
		this.add(this.getJButtonAlignTop());
		this.add(this.getJButtonAlignBottom());
		this.addSeparator();
		
	}
	
	private JToggleButton getJToggleButtonLayoutSwitch() {
		if (jToggleButtonLayoutSwitch == null) {
			jToggleButtonLayoutSwitch = this.basicGraphGuiTools.getNewJToggleButton("LayoutSwitch.png", Language.translate("Switch Layout", Language.EN), this);
		}
		return jToggleButtonLayoutSwitch;
	}
	
	private JToggleButton getJToggleButtonEdgeLine() {
		if (jToggleButtonEdgeLine == null) {
			jToggleButtonEdgeLine = this.basicGraphGuiTools.getNewJToggleButton("EdgeLine.png", Language.translate("Straight Line Edge", Language.EN), this);
			jToggleButtonEdgeLine.setSelected(true);
		}
		return jToggleButtonEdgeLine;
	}
	private JToggleButton getJToggleButtonEdgeOrthogonal() {
		if (jToggleButtonEdgeOrthogonal == null) {
			jToggleButtonEdgeOrthogonal = this.basicGraphGuiTools.getNewJToggleButton("EdgeOrthogonal.png", Language.translate("Orthogonal Line Edge", Language.EN), this);
		}
		return jToggleButtonEdgeOrthogonal;
	}
	private JToggleButton getJToggleButtonEdgeQuadCurve() {
		if (jToggleButtonEdgeQuadCurve == null) {
			jToggleButtonEdgeQuadCurve = this.basicGraphGuiTools.getNewJToggleButton("EdgeQuadCurve.png", Language.translate("Quadratic Curve Edge", Language.EN), this);
		}
		return jToggleButtonEdgeQuadCurve;
	}
	private JToggleButton getJToggleButtonEdgePolyLine() {
		if (jToggleButtonEdgePolyLine == null) {
			jToggleButtonEdgePolyLine = this.basicGraphGuiTools.getNewJToggleButton("EdgePolyline.png", Language.translate("Polyline Edge", Language.EN), this);
		}
		return jToggleButtonEdgePolyLine;
	}
	

	private JToggleButton getJToggleButtonEdgeEdit() {
		if (jToggleButtonEdgeEdit == null) {
			jToggleButtonEdgeEdit = this.basicGraphGuiTools.getNewJToggleButton("EdgeEdit.png", Language.translate("Edit Configurable Edge", Language.EN), this);
		}
		return jToggleButtonEdgeEdit;
	}

	private JButton getJButtonAlignLeft() {
		if (jButtonAlignLeft==null) {
			jButtonAlignLeft = this.basicGraphGuiTools.getNewJButton("AlignLeft.png", Language.translate("Align to the left", Language.EN), this);
		}
		return jButtonAlignLeft;
	}
	private JButton getJButtonAlignRight() {
		if (jButtonAlignRight==null) {
			jButtonAlignRight = this.basicGraphGuiTools.getNewJButton("AlignRight.png", Language.translate("Align to the right", Language.EN), this);
		}
		return jButtonAlignRight;
	}
	private JButton getJButtonAlignTop() {
		if (jButtonAlignTop==null) {
			jButtonAlignTop = this.basicGraphGuiTools.getNewJButton("AlignTop.png", Language.translate("Align to the top", Language.EN), this);
		}
		return jButtonAlignTop;
	}
	private JButton getJButtonAlignBottom() {
		if (jButtonAlignBottom==null) {
			jButtonAlignBottom = this.basicGraphGuiTools.getNewJButton("AlignBottom.png", Language.translate("Align to the bottom", Language.EN), this);
		}
		return jButtonAlignBottom;
	}
	
	
	/**
	 * Registers the local class as {@link GraphSelectionListener}.
	 */
	private void registerGraphSelectionListener() {
		if (this.isRegisteredGraphSelectionListener==false) {
	    	this.basicGraphGuiTools.getBasicGraphGUI().addGraphSelectionListener(this);
	    	this.isRegisteredGraphSelectionListener = true;
		}
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.GraphSelectionListener#onGraphSelectionChanged(org.awb.env.networkModel.controller.ui.GraphSelectionListener.GraphSelection)
	 */
	@Override
	public void onGraphSelectionChanged(GraphSelection graphSelection) {

		// ----------------------------------------------------------
		// --- Act on the line edit buttons -------------------------
		// ----------------------------------------------------------
		
		// --- Reset current GraphEdge that is to be edited ---------
		this.editGraphEdge = null;
		
		// --- Check if a single configurable line was selected -----  
		boolean isSingleEdgeSeelction = false;
		if (graphSelection!=null) {
			isSingleEdgeSeelction = graphSelection.getEdgeList().size()==1;
		}
		boolean isConfigurableLine = this.getLayoutSettingEdgeShape()==EdgeShape.ConfigurableLine;
		boolean enableToggleButtons = isConfigurableLine & isSingleEdgeSeelction;
		
		// --- Enable / disable toggle buttons ----------------------
		this.getJToggleButtonEdgeLine().setEnabled(enableToggleButtons);
		this.getJToggleButtonEdgeOrthogonal().setEnabled(enableToggleButtons);
		this.getJToggleButtonEdgeQuadCurve().setEnabled(enableToggleButtons);
		this.getJToggleButtonEdgePolyLine().setEnabled(enableToggleButtons);
		// --- Reset edit button ------------------------------------
		this.getJToggleButtonEdgeEdit().setEnabled(enableToggleButtons);
		
		if (enableToggleButtons==false) {
			// --- Always select the line toggle button -------------
			this.getJToggleButtonEdgeLine().setSelected(true);
			
		} else {
			// --- Select the button that corresponds to the edge ---  
			this.editGraphEdge = graphSelection.getEdgeList().get(0);
			GraphEdgeShapeConfiguration<?> edgeShape = this.editGraphEdge.getEdgeShapeConfiguration();
			if (edgeShape==null) {
				this.getJToggleButtonEdgeLine().setSelected(true);
				this.getJToggleButtonEdgeEdit().setEnabled(false); // --- Disable edit button here ---
			} else if (edgeShape instanceof OrthogonalConfiguration) {
				this.getJToggleButtonEdgeOrthogonal().setSelected(true);
				this.getJToggleButtonEdgeEdit().setEnabled(false); // --- Disable edit button here ---
			} else if (edgeShape instanceof QuadCurveConfiguration) {
				this.getJToggleButtonEdgeQuadCurve().setSelected(true);
			} else if (edgeShape instanceof PolylineConfiguration) {
				this.getJToggleButtonEdgePolyLine().setSelected(true);
			}
		}
		
		// ----------------------------------------------------------
		// --- Set the alignment button enabled ---------------------
		// ----------------------------------------------------------
		this.setAlignmentButtonsEnabled(graphSelection);
		
	}
	
	/**
	 * Sets the alignment buttons enabled (or not).
	 * @param graphSelection the new alignment buttons
	 */
	private void setAlignmentButtonsEnabled(GraphSelection graphSelection) {
		
		boolean enabledAlignment = graphSelection!=null && graphSelection.getNodeList().size()>1;
		this.getJButtonAlignRight().setEnabled(enabledAlignment);
		this.getJButtonAlignLeft().setEnabled(enabledAlignment);
		this.getJButtonAlignTop().setEnabled(enabledAlignment);
		this.getJButtonAlignBottom().setEnabled(enabledAlignment);
	}
	
	
	private JMenuItem getJMenuItemEdgeLine() {
		if (jMenuItemEdgeLine==null) {
			jMenuItemEdgeLine = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Straight Line Edge", Language.EN), "EdgeLine.png", this);
		}
		return jMenuItemEdgeLine;
	}
	
	private JMenuItem getJMenuItemEdgeOrthogonal() {
		if (jMenuItemEdgeOrthogonal==null) {
			jMenuItemEdgeOrthogonal = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Orthogonal Line Edge", Language.EN), "EdgeOrthogonal.png", this);
		}
		return jMenuItemEdgeOrthogonal;
	}
	public JMenu getJMenuOrthogonalEdgeDirection(OrthogonalConfiguration orthConfig) {
		if (jMenuOrthogonalEdgeDirection==null) {
			jMenuOrthogonalEdgeDirection = new JMenu(Language.translate("Orthogonal Line Edge", Language.EN));
			jMenuOrthogonalEdgeDirection.setIcon(GraphGlobals.getImageIcon("EdgeOrthogonal.png"));
			Font currFont = this.getJMenuItemEdgeLine().getFont();
			Font boldFont = new Font(currFont.getName(), Font.BOLD, currFont.getSize());
			jMenuOrthogonalEdgeDirection.setFont(boldFont);
		}
		if (orthConfig!=null) {
			// --- Re-configure the sub menu -------------- 
			jMenuOrthogonalEdgeDirection.remove(this.getjMenuItemEdgeOrthogonalMoveXFirst());
			jMenuOrthogonalEdgeDirection.remove(this.getjMenuItemEdgeOrthogonalMoveYFirst());
			if (orthConfig.isFirstMoveInY()==true) {
				jMenuOrthogonalEdgeDirection.add(this.getjMenuItemEdgeOrthogonalMoveXFirst());
			} else {
				jMenuOrthogonalEdgeDirection.add(this.getjMenuItemEdgeOrthogonalMoveYFirst());
			}
		}
		return jMenuOrthogonalEdgeDirection;
	}
	private JMenuItem getjMenuItemEdgeOrthogonalMoveXFirst() {
		if (jMenuItemEdgeOrthogonalMoveXFirst==null) {
			jMenuItemEdgeOrthogonalMoveXFirst = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Move to x-direction first", Language.EN), "EdgeOrthogonalX-First.png", this);
		}
		return jMenuItemEdgeOrthogonalMoveXFirst;
	}
	private JMenuItem getjMenuItemEdgeOrthogonalMoveYFirst() {
		if (jMenuItemEdgeOrthogonalMoveYFirst==null) {
			jMenuItemEdgeOrthogonalMoveYFirst = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Move to y-direction first", Language.EN), "EdgeOrthogonalY-First.png", this);
		}
		return jMenuItemEdgeOrthogonalMoveYFirst;
	}
	
	private JMenuItem getJMenuItemEdgeQuadCurve() {
		if (jMenuItemEdgeQuadCurve==null) {
			jMenuItemEdgeQuadCurve = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Quadratic Curve Edge", Language.EN), "EdgeQuadCurve.png", this);
		}
		return jMenuItemEdgeQuadCurve;
	}
	private JMenuItem getJMenuItemEdgePolyLine() {
		if (jMenuItemEdgePolyLine==null) {
			jMenuItemEdgePolyLine = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Polyline Edge", Language.EN), "EdgePolyline.png", this);
		}
		return jMenuItemEdgePolyLine;
	}
	private javax.swing.JPopupMenu.Separator getSeparatorAfterEdgeTypeSelection() {
		if (separatorAfterEdgeTypeSelection==null) {
			separatorAfterEdgeTypeSelection = new JPopupMenu.Separator(); 
		}
		return separatorAfterEdgeTypeSelection;
	}
	private JMenuItem getJMenuItemEdgeEdit() {
		if (jMenuItemEdgeEdit==null) {
			jMenuItemEdgeEdit = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Edit Configurable Edge", Language.EN), "EdgeEdit.png", this);
		}
		return jMenuItemEdgeEdit;
	}
	private javax.swing.JPopupMenu.Separator getSeparatorAfterEdgeEdit() {
		if (separatorAfterEdgeEdit==null) {
			separatorAfterEdgeEdit = new JPopupMenu.Separator(); 
		}
		return separatorAfterEdgeEdit;
	}
	/**
	 * Updates the specified edge menu with further layout menu items.
	 * @param graphEdge the graph edge
	 */
	public void updateEdgeMenu(GraphEdge graphEdge) {

		// ----------------------------------------------------------
		// --- Reset current GraphEdge that is to be edited ---------
		this.editGraphEdge = null;

		// ----------------------------------------------------------
		// --- First, remove all menu items from the popup menu -----
		JPopupMenu edgePopUpMenu = this.basicGraphGuiTools.getEdgePopup();
		edgePopUpMenu.remove(this.getJMenuItemEdgeLine());
		edgePopUpMenu.remove(this.getJMenuItemEdgeQuadCurve());
		edgePopUpMenu.remove(this.getJMenuItemEdgePolyLine());
		edgePopUpMenu.remove(this.getJMenuItemEdgeOrthogonal());
		edgePopUpMenu.remove(this.getJMenuOrthogonalEdgeDirection(null));
		edgePopUpMenu.remove(this.getSeparatorAfterEdgeTypeSelection());
		edgePopUpMenu.remove(this.getJMenuItemEdgeEdit());
		edgePopUpMenu.remove(this.getSeparatorAfterEdgeEdit());
		
		// ----------------------------------------------------------
		// --- Check if further items are allowed -------------------
		if (graphEdge==null || this.isVisible()==false) return;
		
		boolean isConfigurableLine = this.getLayoutSettingEdgeShape()==EdgeShape.ConfigurableLine;
		if (isConfigurableLine==false) return;
		
		
		// ----------------------------------------------------------
		// --- Highlight current setting ----------------------------
		Font currFont = this.getJMenuItemEdgeLine().getFont();
		Font boldFont = new Font(currFont.getName(), Font.BOLD, currFont.getSize());
		Font plainFont = new Font(currFont.getName(), Font.PLAIN, currFont.getSize());
		
		// --- First, set plain font for all ----
		this.getJMenuItemEdgeLine().setFont(plainFont);
		this.getJMenuItemEdgeOrthogonal().setFont(plainFont);
		this.getJMenuItemEdgeQuadCurve().setFont(plainFont);
		this.getJMenuItemEdgePolyLine().setFont(plainFont);
		
		// --- Finally, set the highlight font --
		this.editGraphEdge = graphEdge;
		GraphEdgeShapeConfiguration<?> edgeShapeConfiguration = this.editGraphEdge .getEdgeShapeConfiguration();
		if (edgeShapeConfiguration==null) {
			this.getJMenuItemEdgeLine().setFont(boldFont);
		} else if (edgeShapeConfiguration instanceof OrthogonalConfiguration) {
			this.getJMenuItemEdgeOrthogonal().setFont(boldFont);
		} else if (edgeShapeConfiguration instanceof QuadCurveConfiguration) {
			this.getJMenuItemEdgeQuadCurve().setFont(boldFont);
		} else if (edgeShapeConfiguration instanceof PolylineConfiguration) {
			this.getJMenuItemEdgePolyLine().setFont(boldFont);
		}
		
		// ----------------------------------------------------------
		// --- Add the layout menu items ----------------------------
		int i = 2;
		edgePopUpMenu.add(this.getJMenuItemEdgeLine(), i++);
		if (edgeShapeConfiguration instanceof OrthogonalConfiguration) {
			edgePopUpMenu.add(this.getJMenuOrthogonalEdgeDirection((OrthogonalConfiguration) edgeShapeConfiguration), i++);
		} else {
			edgePopUpMenu.add(this.getJMenuItemEdgeOrthogonal(), i++);
		}
		edgePopUpMenu.add(this.getJMenuItemEdgeQuadCurve(), i++);
		edgePopUpMenu.add(this.getJMenuItemEdgePolyLine(), i++);
		edgePopUpMenu.add(this.getSeparatorAfterEdgeTypeSelection(), i++);
		if (edgeShapeConfiguration!=null && !(edgeShapeConfiguration instanceof OrthogonalConfiguration)) {
			edgePopUpMenu.add(this.getJMenuItemEdgeEdit(), i++);
			edgePopUpMenu.add(this.getSeparatorAfterEdgeEdit(), i++);
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (updateObject instanceof NetworkModelNotification) {

			NetworkModelNotification nmNotification = (NetworkModelNotification) updateObject;
			int reason = nmNotification.getReason();
			switch (reason) {
			case NetworkModelNotification.NETWORK_MODEL_Reload:
				this.registerGraphSelectionListener();
				break;
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				this.getJToggleButtonEdgeEdit().setSelected(false);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJToggleButtonLayoutSwitch()) {
			// --- Show Layout switch ---------------------
			boolean isSetVisible = this.getJToggleButtonLayoutSwitch().isSelected();
			this.getLayoutSelectionDialog();
			if (isSetVisible==false) {
				this.getLayoutSelectionDialog().dispose();
				this.setLayoutSelectionDialog(null);
			}
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeLine() || ae.getSource()==this.getJMenuItemEdgeLine()) {
			// --- Toggle to line edge --------------------			
			this.setGraphConfiguration(null);
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeOrthogonal() || ae.getSource()==this.getJMenuItemEdgeOrthogonal()) {
			// --- Toggle to orthogonal edge --------------
			this.setGraphConfiguration(new OrthogonalConfiguration());

		} else if (ae.getSource()==this.getjMenuItemEdgeOrthogonalMoveXFirst()) {
			// --- Set configuration to x-First -----------
			this.setGraphConfiguration(new OrthogonalConfiguration(false));
			
		} else if (ae.getSource()==this.getjMenuItemEdgeOrthogonalMoveYFirst()) {
			// --- Set configuration to y-First -----------
			this.setGraphConfiguration(new OrthogonalConfiguration(true));
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeQuadCurve() || ae.getSource()==this.getJMenuItemEdgeQuadCurve()) {
			// --- Toggle to quad curve edge --------------
			this.setGraphConfiguration(new QuadCurveConfiguration());
			
		} else if (ae.getSource()==this.getJToggleButtonEdgePolyLine() || ae.getSource()==this.getJMenuItemEdgePolyLine()) {
			// --- Toggle to polyline edge ----------------			
			this.setGraphConfiguration(new PolylineConfiguration());
			
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeEdit()) {
			// --- Do edge editing (from toolbar) ---------
			this.switchEdgeEdit(this.getJToggleButtonEdgeEdit().isSelected(), null);
			
		} else if (ae.getSource()==this.getJMenuItemEdgeEdit()) {
			// --- Do edge editing (from context menu) ----
			this.switchEdgeEdit(true, null);
			
		} else if (ae.getSource()==this.getJButtonAlignLeft()) {
			// --- Align marked nodes to the left ---------
			this.graphController.getNetworkModelUndoManager().alignNodes(this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer(), Alignment.Left);
			
		} else if (ae.getSource()==this.getJButtonAlignRight()) {
			// --- Align marked nodes to the right --------
			this.graphController.getNetworkModelUndoManager().alignNodes(this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer(),Alignment.Right);
			
		} else if (ae.getSource()==this.getJButtonAlignTop()) {
			// --- Align marked nodes to the top ----------
			this.graphController.getNetworkModelUndoManager().alignNodes(this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer(),Alignment.Top);
			
		} else if (ae.getSource()==this.getJButtonAlignBottom()) {
			// --- Align marked nodes to the bottom -------
			this.graphController.getNetworkModelUndoManager().alignNodes(this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer(),Alignment.Bottom);
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] => Unknow action command from " + ae.getSource());
		}
		
	}

	/**
	 * Returns the layout selection dialog.
	 * @return the layout selection dialog
	 */
	private LayoutSelectionDialog getLayoutSelectionDialog() {
		if (layoutSelectionDialog==null) {
			layoutSelectionDialog = new LayoutSelectionDialog(this.graphController);
		}
		return layoutSelectionDialog;
	}
	/**
	 * Sets the layout selection dialog.
	 * @param layoutSelectionDialog the new layout selection dialog
	 */
	public void setLayoutSelectionDialog(LayoutSelectionDialog layoutSelectionDialog) {
		this.layoutSelectionDialog = layoutSelectionDialog;
	}
	
	/**
	 * Sets the specified graph edge shape configuration to the local {@link #editGraphEdge}.
	 * @param edgeShapeConfiguration the new graph configuration
	 */
	private void setGraphConfiguration(GraphEdgeShapeConfiguration<?> edgeShapeConfiguration) {
		
		if (this.editGraphEdge==null) return;
		
		// --- Remind old configuration before the new will be applied --------
		ConfiguredLineEdit initialConfLineEdit = this.getInitialConfiguredLineEdit();
		
		// --- Set edge configuration and redraw edge -------------------------
		this.editGraphEdge.setEdgeShapeConfiguration(edgeShapeConfiguration);
		this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer().getPickedEdgeState().pick(editGraphEdge, false);
		this.basicGraphGuiTools.getBasicGraphGUI().getVisualizationViewer().getPickedEdgeState().pick(editGraphEdge, true);
		
		// --- Enable edge edit -----------------------------------------------
		boolean isEnableEdgeEdit = (edgeShapeConfiguration!=null && !(edgeShapeConfiguration instanceof OrthogonalConfiguration));
		this.getJToggleButtonEdgeEdit().setEnabled(isEnableEdgeEdit);
		this.switchEdgeEdit(isEnableEdgeEdit, initialConfLineEdit);
		
	}
	
	/**
	 * Start or stops the edge editing mode.
	 *
	 * @param isStartEdit start=true, stop=false
	 * @param initialConfLineEdit the initial {@link ConfiguredLineEdit} description that contains the initial situation before the edit
	 */
	private void switchEdgeEdit(boolean isStartEdit, ConfiguredLineEdit initialConfLineEdit) {
		
		if (initialConfLineEdit==null) {
			// --- Remind old edge configuration ----------------------------------------
			initialConfLineEdit = this.getInitialConfiguredLineEdit();
		}
		
		// --- Start edge editing -------------------------------------------------------
		if (this.getJToggleButtonEdgeEdit().isSelected()==false) this.getJToggleButtonEdgeEdit().setSelected(true);
		this.graphController.getNetworkModelUndoManager().setGraphMouseEdgeEditing(initialConfLineEdit);
		if (isStartEdit==false) {
			// --- Stop edge editing ----------------------------------------------------
			if (this.getJToggleButtonEdgeEdit().isSelected()==true) this.getJToggleButtonEdgeEdit().setSelected(false);
			this.graphController.getNetworkModelUndoManager().setGraphMousePicking();
		}
		
		// --- Disable (or enable) buttons that are not required for the edge editing ---
		this.getJToggleButtonLayoutSwitch().setEnabled(! isStartEdit);
		
	}
	
	/**
	 * Return description {@link ConfiguredLineEdit} that will remind the old edge setting 
	 * for a possible later undo action. 
	 * @return the configured line edit
	 */
	private ConfiguredLineEdit getInitialConfiguredLineEdit() {
		
		GraphNode graphNodeOldStateFrom = null;
		GraphNode graphNodeOldStateTo = null;
		
		Graph<GraphNode, GraphEdge> graph = this.graphController.getNetworkModel().getGraph();
		if (graph.getEdgeType(this.editGraphEdge)==EdgeType.DIRECTED) {
			graphNodeOldStateFrom = graph.getSource(this.editGraphEdge);
			graphNodeOldStateTo   = graph.getDest(this.editGraphEdge);
			
		} else {
			Pair<GraphNode> nodePair = graph.getEndpoints(this.editGraphEdge);
			graphNodeOldStateFrom = nodePair.getFirst();
			graphNodeOldStateTo   = nodePair.getSecond();
		}
		return new ConfiguredLineEdit(this.editGraphEdge.getCopy(), graphNodeOldStateFrom.getCopy(), graphNodeOldStateTo.getCopy());
	}
	
	/**
	 * Returns the current layout setting.
	 * @return the layout setting
	 */
	private LayoutSettings getLayoutSetting() {
		return this.graphController.getNetworkModel().getLayoutSettings();
	}
	/**
	 * Returns the currently configured edge shape from the LayoutSettings.
	 * @return the layout setting edge shape
	 */
	private EdgeShape getLayoutSettingEdgeShape() {
		EdgeShape shape = EdgeShape.Line; // --- Default --- 
		LayoutSettings ls = this.getLayoutSetting();
		if (ls!=null) {
			shape = ls.getEdgeShape();
		}
		return shape;
	}
	
	
}
