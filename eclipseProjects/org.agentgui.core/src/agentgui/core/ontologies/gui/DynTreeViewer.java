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
package agentgui.core.ontologies.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JSplitPane;

/**
 * This dialog is used through the {@link DynForm} class in order to show the structure of 
 * ontology classes during debugging processes.
 * 
 * @see DynForm
 *
 * @author Marvin Steinberg - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTreeViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JTree jTreeDynForm = null;
	
	private DefaultTreeModel treeModel = null;
	private JSplitPane jSplitPane = null;
	private JScrollPane jScrollPaneRight = null;
	

	/**
	 * Instantiates a new dyn tree viewer.
	 *
	 * @param objectTree the object tree
	 */
	public DynTreeViewer(DefaultTreeModel objectTree) {
		super();
		treeModel = objectTree;
		initialize();
		this.jTreeDynForm.setModel(treeModel);
	}

	/**
	 * Adds a specified component to the right.
	 *
	 * @param component the component
	 */
	public void addPanel(JComponent component) {
		jScrollPaneRight.setViewportView(component);
		jScrollPaneRight.validate();
		jScrollPaneRight.repaint();
	}
	
	/**
	 * This method initializes this.
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(900, 300);
		this.setContentPane(getJSplitPane());
		this.setTitle("DynForm - SlotView");
	}

	/**
	 * This method initializes jContentPane.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTreeDynForm());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTreeDynForm.
	 *
	 * @return javax.swing.JTree
	 */
	private JTree getJTreeDynForm() {
		if (jTreeDynForm == null) {
			jTreeDynForm = new JTree();
		}
		return jTreeDynForm;
	}

	/**
	 * This method initializes jSplitPane.
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(395);
			jSplitPane.setRightComponent(getJScrollPaneRight());
			jSplitPane.setLeftComponent(getJContentPane());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jScrollPaneRight.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneRight() {
		if (jScrollPaneRight == null) {
			jScrollPaneRight = new JScrollPane();
		}
		return jScrollPaneRight;
	}

}
