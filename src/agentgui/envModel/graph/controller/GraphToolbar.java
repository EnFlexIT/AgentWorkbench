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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;

public class GraphToolbar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 7033208567874447367L;

	/** Environment model controller, to be passed by the parent GUI. */
	private GraphEnvironmentController controller = null;  //  @jve:decl-index=0:
	
	private final String pathImage = GraphGlobals.getPathImages(); // @jve:decl-index=0:
	private final Dimension jButtonSize = new Dimension(26, 26);  //  @jve:decl-index=0:
	
	private JButton jButtonClearGraph = null;
	private JButton jButtonComponents = null;
	private JButton jButtonZoomIn = null;
	private JButton jButtonZoomOut = null;
	private JButton jButtonZoomReset = null;
	private JButton jButtonAddComponent = null;
	private JToggleButton jToggleMouseTransforming = null;

	private JToggleButton jToggleMousePicking = null;
	private JButton jButtonRemoveComponent = null;
	private JButton jButtonMergeNodes = null;
	private JButton jButtonSplitNode = null;
	private JButton jButtonImportGraph = null;
	
	/**
	 * Instantiates a new graph toolbar.
	 */
	public GraphToolbar(GraphEnvironmentController graphEnvironmentController) {
		this.controller = graphEnvironmentController;
		this.initialize();
	}
	
	/**
	 * Initializes this ToolBar.
	 */
	private void initialize() {
		
		this.setOrientation(JToolBar.VERTICAL);
		this.setFloatable(false);
		this.setPreferredSize(new Dimension(40, 380));

		this.add(getJButtonComponents());
		this.addSeparator();
		
		this.add(getJButtonZoomIn());
		this.add(getJButtonZoomOut());
		this.add(getJButtonZoomReset());
		
		this.addSeparator();
		this.add(getJToggleMouseTransforming());
		this.add(getJToggleMousePicking());
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(jToggleMousePicking);
		bg.add(jToggleMouseTransforming);
		
		// --- In case of editing the simulation setup ----------
		if (this.controller.getProject()!=null) {
			
			this.addSeparator();
			this.add(getJButtonAddComponent());
			this.add(getJButtonRemoveComponent());
			this.add(getJButtonMergeNodes());
			this.add(getJButtonSplitNode());
			
			this.addSeparator();
			this.add(getJButtonClearGraph());
			this.add(getJButtonImportGraph());
		}
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
	 * This method initializes jButtonZoomIn
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonComponents() {
		if (jButtonComponents == null) {
			jButtonComponents = new JButton();
			jButtonComponents.setPreferredSize(jButtonSize);
			jButtonComponents.setIcon(new ImageIcon(getClass().getResource(pathImage + "components.gif")));
			jButtonComponents.setToolTipText(Language.translate("Graph-Komponenten"));
			jButtonComponents.addActionListener(this);
		}
		return jButtonComponents;
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
	 * This method initializes jButtonZoomReset
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomReset() {
		if (jButtonZoomReset == null) {
			jButtonZoomReset = new JButton();
			jButtonZoomReset.setIcon(new ImageIcon(getClass().getResource(pathImage + "Refresh.png")));
			jButtonZoomReset.setPreferredSize(jButtonSize);
			jButtonZoomReset.setToolTipText(Language.translate("Zurücksetzen"));
			jButtonZoomReset.addActionListener(this);
		}
		return jButtonZoomReset;
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
			jButtonMergeNodes.setToolTipText(Language.translate("Merge two nodes", Language.EN));
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
	 * This method initializes jButtonImportGraph
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonImportGraph() {
		if (jButtonImportGraph == null) {
			jButtonImportGraph = new JButton();
			jButtonImportGraph.setIcon(new ImageIcon(getClass().getResource(pathImage + "import.png")));
			jButtonImportGraph.setPreferredSize(jButtonSize);
			jButtonImportGraph.setToolTipText(Language.translate("Import Graph from file", Language.EN));
			jButtonImportGraph.addActionListener(this);
		}
		return jButtonImportGraph;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
