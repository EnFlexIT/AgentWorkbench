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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;

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
    
    
    private JButton jButtonLayoutSwitch;
    private JToggleButton jToggleButtonEdgeLine;
    private JToggleButton jToggleButtonEdgeQuadCurve;
    private JToggleButton jToggleButtonEdgePolyLine;
    private JToggleButton jToggleButtonEdgeOrthogonal;


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
	}
	
	/**
	 * Builds the layout tool bar.
	 */
	private void buildToolBar() {
		
		this.setOrientation(JToolBar.VERTICAL);
		this.setFloatable(false);
		this.setPreferredSize(new Dimension(30, 30));
		this.setVisible(this.basicGraphGuiTools.getJToggleButtonLayoutToolBar().isSelected());
		
		this.add(this.getJButtonLayoutSwitch());
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
	
	private JButton getJButtonLayoutSwitch() {
		if (jButtonLayoutSwitch == null) {
			jButtonLayoutSwitch = this.basicGraphGuiTools.getNewJButton("LayoutSwitch.png", Language.translate("Switch Layout", Language.EN), this);
		}
		return jButtonLayoutSwitch;
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
		
		// --- TODO
		//System.err.println("[" + this.getClass().getSimpleName() + "] => Received new GraphSelection ....");
		
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
		
		if (ae.getSource()==this.getJButtonLayoutSwitch()) {
			// --- Show Layout switch ---------------------
			System.err.println("[" + this.getClass().getSimpleName() + "] => Show Layout switch");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeLine()) {
			// --- Toggle to line edge --------------------			
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to line edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeQuadCurve()) {
			// --- Toggle to quad curve edge --------------
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to quad curve edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgePolyLine()) {
			// --- Toggle to polyline edge ----------------			
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to polyline edge");
			
		} else if (ae.getSource()==this.getJToggleButtonEdgeOrthogonal()) {
			// --- Toggle to orthogonal edge --------------
			System.err.println("[" + this.getClass().getSimpleName() + "] => Toggle to orthogonal edge");
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] => Unknow action command from " + ae.getSource());
		}
		
	}

}
