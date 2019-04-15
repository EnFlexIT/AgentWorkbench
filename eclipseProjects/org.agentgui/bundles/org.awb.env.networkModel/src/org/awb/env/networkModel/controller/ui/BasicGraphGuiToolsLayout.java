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
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.EdgeShape;

import agentgui.core.application.Language;

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
    private JToggleButton jToggleButtonEdgeQuadCurve;
    private JToggleButton jToggleButtonEdgePolyLine;
    private JToggleButton jToggleButtonEdgeOrthogonal;

    private JMenuItem jMenuItemEdgeLine;
    private JMenuItem jMenuItemEdgeQuadCurve;
    private JMenuItem jMenuItemEdgePolyLine;
    private JMenuItem jMenuItemEdgeOrthogonal;
    private javax.swing.JPopupMenu.Separator edgePopupMenuSeparator;

	/**
	 * Instantiates a new basic graph gui tools layout.
	 *
	 * @param graphController the graph controller
	 * @param basicGraphGuiTools the basic graph gui tools
	 */
	public BasicGraphGuiToolsLayout(GraphEnvironmentController graphController, BasicGraphGuiTools basicGraphGuiTools) {
		this.graphController = graphController;
    	this.graphController.addObserver(this);
    	this.basicGraphGuiTools = basicGraphGuiTools;
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
		this.add(this.getJToggleButtonEdgeQuadCurve());
		this.add(this.getJToggleButtonEdgePolyLine());
		this.add(this.getJToggleButtonEdgeOrthogonal());
		this.addSeparator();
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(this.getJToggleButtonEdgeLine());
		bg.add(this.getJToggleButtonEdgeQuadCurve());
		bg.add(this.getJToggleButtonEdgePolyLine());
		bg.add(this.getJToggleButtonEdgeOrthogonal());
		
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
	private JToggleButton getJToggleButtonEdgeOrthogonal() {
		if (jToggleButtonEdgeOrthogonal == null) {
			jToggleButtonEdgeOrthogonal = this.basicGraphGuiTools.getNewJToggleButton("EdgeOrthogonal.png", Language.translate("Orthogonal Line Edge", Language.EN), this);
		}
		return jToggleButtonEdgeOrthogonal;
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
		this.getJToggleButtonEdgeQuadCurve().setEnabled(enableToggleButtons);
		this.getJToggleButtonEdgePolyLine().setEnabled(enableToggleButtons);
		this.getJToggleButtonEdgeOrthogonal().setEnabled(enableToggleButtons);
		
		if (enableToggleButtons==false) {
			// --- Always select the line toggle button -------------
			this.getJToggleButtonEdgeLine().setSelected(true);
			
		} else {
			// --- Select the button that corresponds to the edge ---  
			this.editGraphEdge = graphSelection.getEdgeList().get(0);
			GraphEdgeShapeConfiguration<?> edgeShape = this.editGraphEdge.getEdgeShapeConfiguration();
			if (edgeShape==null) {
				this.getJToggleButtonEdgeLine().setSelected(true);
			} else if (edgeShape instanceof GraphEdgeShapeConfiguration.QuadCurveConfiguration) {
				this.getJToggleButtonEdgeQuadCurve().setSelected(true);
			} else if (edgeShape instanceof GraphEdgeShapeConfiguration.PolylineConfiguration) {
				this.getJToggleButtonEdgePolyLine().setSelected(true);
			} else if (edgeShape instanceof GraphEdgeShapeConfiguration.OrthogonalConfiguration) {
				this.getJToggleButtonEdgeOrthogonal().setSelected(true);
			}
		}
		
	}
	
	
	private JMenuItem getJMenuItemEdgeLine() {
		if (jMenuItemEdgeLine==null) {
			jMenuItemEdgeLine = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Straight Line Edge", Language.EN), "EdgeLine.png", this);
		}
		return jMenuItemEdgeLine;
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
	private JMenuItem getJMenuItemEdgeOrthogonal() {
		if (jMenuItemEdgeOrthogonal==null) {
			jMenuItemEdgeOrthogonal = this.basicGraphGuiTools.getNewJMenuItem(Language.translate("Orthogonal Line Edge", Language.EN), "EdgeOrthogonal.png", this);
		}
		return jMenuItemEdgeOrthogonal;
	}
	private javax.swing.JPopupMenu.Separator getEdgePopupMenuSeparator() {
		if (edgePopupMenuSeparator==null) {
			edgePopupMenuSeparator = new JPopupMenu.Separator(); 
		}
		return edgePopupMenuSeparator;
	}
	/**
	 * Updates the specified edge menu with further layout menu items.
	 *
	 * @param graphEdge the graph edge
	 * @param edgePopUpMenu the edge pop up menu to update
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
		edgePopUpMenu.remove(this.getEdgePopupMenuSeparator());
		
		// ----------------------------------------------------------
		// --- Check if further items are allowed -------------------
		if (graphEdge==null) return;
		
		boolean isConfigurableLine = this.getLayoutSettingEdgeShape()==EdgeShape.ConfigurableLine;
		if (isConfigurableLine==false) return;
		
		
		// ----------------------------------------------------------
		// --- Highlight current setting ----------------------------
		Font currFont = this.getJMenuItemEdgeLine().getFont();
		Font boldFont = new Font(currFont.getName(), Font.BOLD, currFont.getSize());
		Font plainFont = new Font(currFont.getName(), Font.PLAIN, currFont.getSize());
		
		// --- First, set plain font for all ----
		this.getJMenuItemEdgeLine().setFont(plainFont);
		this.getJMenuItemEdgeQuadCurve().setFont(plainFont);
		this.getJMenuItemEdgePolyLine().setFont(plainFont);
		this.getJMenuItemEdgeOrthogonal().setFont(plainFont);
		
		// --- Finally, set the highlight font -- 
		this.editGraphEdge = graphEdge;
		GraphEdgeShapeConfiguration<?> edgeShape = this.editGraphEdge .getEdgeShapeConfiguration();
		if (edgeShape==null) {
			this.getJMenuItemEdgeLine().setFont(boldFont);
		} else if (edgeShape instanceof GraphEdgeShapeConfiguration.QuadCurveConfiguration) {
			this.getJMenuItemEdgeQuadCurve().setFont(boldFont);
		} else if (edgeShape instanceof GraphEdgeShapeConfiguration.PolylineConfiguration) {
			this.getJMenuItemEdgePolyLine().setFont(boldFont);
		} else if (edgeShape instanceof GraphEdgeShapeConfiguration.OrthogonalConfiguration) {
			this.getJMenuItemEdgeOrthogonal().setFont(boldFont);
		}
		
		// ----------------------------------------------------------
		// --- Add the layout menu items ---------------------------- 
		edgePopUpMenu.add(this.getJMenuItemEdgeLine(), 2);
		edgePopUpMenu.add(this.getJMenuItemEdgeQuadCurve(), 3);
		edgePopUpMenu.add(this.getJMenuItemEdgePolyLine(), 4);
		edgePopUpMenu.add(this.getJMenuItemEdgeOrthogonal(), 5);
		edgePopUpMenu.add(this.getEdgePopupMenuSeparator(), 6);
		
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
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to line edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeQuadCurve() || ae.getSource()==this.getJMenuItemEdgeQuadCurve()) {
			// --- Toggle to quad curve edge --------------
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to quad curve edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgePolyLine() || ae.getSource()==this.getJMenuItemEdgePolyLine()) {
			// --- Toggle to polyline edge ----------------			
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to polyline edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeOrthogonal() || ae.getSource()==this.getJMenuItemEdgeOrthogonal()) {
			// --- Toggle to orthogonal edge --------------
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to orthogonal edge");
			
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
