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
package org.agentgui.gui.swing.project;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.common.ontology.OntologyClassTreeObject;

import java.awt.Font;

/**
 * This JPanel can be used in order to display the slots of an ontology class.<br>
 * It is used in the tab 'Configuration' - 'Ontologies' in project window.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyTabClassView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel jLabelClassNameCaption = null;
	private JTextField jTextFieldClassName = null;
	private JTable jTableClassSlots = null;
	private JScrollPane JScrollPaneClassSlots = null;
	private JLabel jLabelClassDescCaption = null;
	private JTextField jTextFieldClassDescription1 = null;
	private JLabel jLabelDummy = null;
	
	private DefaultMutableTreeNode currOntoNode;
	private OntologyClassTreeObject currOntoObject;

	
	/**
	 * This is the default constructor
	 */
	public OntologyTabClassView(DefaultMutableTreeNode Node) {
		super();
		currOntoNode = Node;
		currOntoObject = (OntologyClassTreeObject) currOntoNode.getUserObject();
		initialize();
		
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.insets = new Insets(10, 0, 10, 10);
		gridBagConstraints1.weightx = 1.0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.insets = new Insets(10, 10, 10, 5);
		gridBagConstraints.gridy = 0;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.gridwidth = 5;
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.weighty = 1.0;
		gridBagConstraints6.insets = new Insets(0, 10, 0, 10);
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridx = 1;
		gridBagConstraints5.gridy = 0;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.anchor = GridBagConstraints.CENTER;
		gridBagConstraints5.weighty = 0.0;
		gridBagConstraints5.insets = new Insets(10, 0, 10, 10);
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new Insets(10, 10, 10, 5);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridx = 0;

		jLabelDummy = new JLabel();
		jLabelDummy.setText(" ");

		jLabelClassDescCaption = new JLabel();
		jLabelClassDescCaption.setText("Text");
		jLabelClassDescCaption.setVisible(false);

		jLabelClassNameCaption = new JLabel();
		jLabelClassNameCaption.setText("Name:");
		jLabelClassNameCaption.setFont(new Font("Dialog", Font.BOLD, 12));

		this.setSize(392, 238);
		this.setLayout(new GridBagLayout());
		this.add(jLabelClassNameCaption, gridBagConstraints4);
		this.add(getJTextFieldClassName(), gridBagConstraints5);
		this.add(getJScrollPaneClassSlots(), gridBagConstraints6);
		this.add(jLabelClassDescCaption, gridBagConstraints);
		this.add(getJTextFieldClassDescription1(), gridBagConstraints1);
		this.add(jLabelDummy, gridBagConstraints2);
	}

	/**
	 * This method initializes ClassName	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldClassName() {
		if (jTextFieldClassName == null) {
			jTextFieldClassName = new JTextField();
			jTextFieldClassName.setPreferredSize(new Dimension(120, 26));
			jTextFieldClassName.setEditable(false);
			jTextFieldClassName.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldClassName.setText(this.currOntoObject.getClassReference() );
		}
		return jTextFieldClassName;
	}
	/**
	 * This method initializes ClassSlots	
	 * @return javax.swing.JTable	
	 */
	private JScrollPane getJScrollPaneClassSlots() {
		if (JScrollPaneClassSlots == null) {
			JScrollPaneClassSlots = new JScrollPane();
			JScrollPaneClassSlots.setViewportView(getJTableClassSlots());
		}
		return JScrollPaneClassSlots;
	}
	/**
	 * This method initializes ClassSlots	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableClassSlots() {
		if (jTableClassSlots == null) {
			jTableClassSlots = new JTable(this.currOntoObject.getTableModel4Slot());
			jTableClassSlots.setEnabled(true);
			jTableClassSlots.setShowGrid(false);
		}
		return jTableClassSlots;
	}
	/**
	 * This method initializes ClassDescription1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldClassDescription1() {
		if (jTextFieldClassDescription1 == null) {
			jTextFieldClassDescription1 = new JTextField();
			jTextFieldClassDescription1.setPreferredSize(new Dimension(120, 26));
			jTextFieldClassDescription1.setVisible(false);
		}
		return jTextFieldClassDescription1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
