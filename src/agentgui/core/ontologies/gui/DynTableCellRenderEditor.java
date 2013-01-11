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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import agentgui.core.application.Application;
import agentgui.core.ontologies.OntologySingleClassSlotDescription;
import agentgui.core.project.AgentStartArgument;
import agentgui.envModel.graph.GraphGlobals;

/**
 * The Class DynTableCellRenderer.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTableCellRenderEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = 7472132949696264592L;
	
	private DynForm dynForm = null;
	private DynTable dynTable = null;
	private DynType dynType = null;  //  @jve:decl-index=0:
	private boolean isSelected = false;
	private int rowTable = -1;
	private int rowModel = -1;
	
	private JPanel jPanelToDisplay = null;
	private JComponent displayComponent = null;
	
	/**
	 * Instantiates a new cell renderer and editor for the DynTable.
	 * @param dynForm the current DynForm
	 */
	public DynTableCellRenderEditor(DynForm dynForm) {
		this.dynForm = dynForm;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		this.dynTable = (DynTable) table;
		this.dynType  = (DynType) value;
		this.isSelected = isSelected;
		this.rowTable = row;
		this.rowModel = table.convertRowIndexToModel(row);

		if (column==0) {
			return this.getFirstColumnDisplay();
		} else {
			return this.getSecondColumnDisplayAndEditPanel();	
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		this.dynTable = (DynTable) table;
		this.dynType  = (DynType) value;
		this.isSelected = isSelected;
		this.rowTable = row;
		this.rowModel = table.convertRowIndexToModel(row);
		
		return this.getSecondColumnDisplayAndEditPanel();	
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		this.setValueOfDynFormComponent(this.displayComponent);
		return this.dynType;
	}
	
	
	/**
	 * Gets the display for the first column (index=0).
	 * @return the first display
	 */
	private JLabel getFirstColumnDisplay() {
	
		Vector<AgentStartArgument> classReferenceList = this.dynForm.getOntologyClassReferenceList();
		
		DefaultMutableTreeNode dynFormTreeNode = this.dynForm.getTreeNodeByDynType(this.dynType);;
		String displayNamePrefix = null;
		String displayName = null;
		String clazzName = null;
		if (this.dynType.getOntologySingleClassSlotDescription()==null) {
			// --- These are the base classes added to the root node ----------
			int startArgumentCounter = dynFormTreeNode.getParent().getIndex(dynFormTreeNode);
			AgentStartArgument startArgument = classReferenceList.get(startArgumentCounter);
			displayNamePrefix = startArgument.getPosition() + ": ";
			if (startArgument.getDisplayTitle()!=null) {
				displayNamePrefix += startArgument.getDisplayTitle() + " ";
			}
			
			clazzName = this.dynType.getClassName();
			clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
			displayName = "(" + clazzName + ")";
			
		} else {
			// --- These are normal classes lower then level 2 ----------------
			if (dynFormTreeNode==null) {
				displayNamePrefix = "";
			} else {
				displayNamePrefix = "" + this.repeatChar("    ", dynFormTreeNode.getLevel()-1);	
			}
			clazzName = this.dynType.getClassName();
			if (clazzName.contains(".")) {
				clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
			}
			displayName = this.dynType.getFieldName() + " [" + clazzName + "]";
			
		}
		
		// --- Create final display element ------------------------- 
		JLabel jLabelDisplay = new JLabel(displayNamePrefix + displayName);
		if (this.dynType.getTypeName().equals(DynType.typeRawType)==true) {
			jLabelDisplay.setFont(new Font("Arial", Font.PLAIN, 12));
		} else {
			jLabelDisplay.setFont(new Font("Arial", Font.BOLD, 12));
		}
		GraphGlobals.Colors.setTableCellRendererColors(jLabelDisplay, this.rowTable, this.isSelected);
		
		return jLabelDisplay;
	}
	
	/**
	 * Repeats a character nTimes.
	 *
	 * @param stringToRepeat the string to repeat
	 * @param nTimes the n times
	 * @return the string
	 */
	private String repeatChar(String stringToRepeat,int nTimes) {
		return new String(new char[nTimes]).replace("\0", stringToRepeat);
	}
	
	/**
	 * Gets the display and edit panel for the second column (index=0).
	 * @return the display and edit panel
	 */
	private JPanel getSecondColumnDisplayAndEditPanel() {
		
		this.jPanelToDisplay = new JPanel();
		this.jPanelToDisplay.setLayout(new BorderLayout());
		this.jPanelToDisplay.setSize(new Dimension(10, 10));
		
		OntologySingleClassSlotDescription oscsd = this.dynType.getOntologySingleClassSlotDescription();

		if (this.dynType.getTypeName().equals(DynType.typeRawType)) {
			// --- A field for the user input is needed -----------------------
			this.displayComponent = this.dynForm.getVisualComponent(oscsd.getSlotVarType());
			//this.displayComponent.setOpaque(true); // Here we have a Nimbus-bug and its workaround !!
			this.displayComponent.setBorder(BorderFactory.createEmptyBorder());
			this.displayComponent.setBackground(new Color(0,0,0,0));
			
			this.setValueOfDisplayComponent(this.displayComponent);

			this.jPanelToDisplay.add(this.displayComponent, BorderLayout.CENTER);
		
		} else {
			// --- Are special classes for visualisation ? --------------------
			if (Application.getGlobalInfo().isOntologyClassVisualisation(this.dynType.getClassName())) {
				this.displayComponent = this.getJButtonOntologyClassVisualsation(this.dynType);
				this.jPanelToDisplay.add(this.displayComponent, BorderLayout.CENTER);
				this.dynTable.getEditableRowsVector().add(this.rowModel);
			}
			
		}
		
		// --- Multiple Button ? ----------------------------------------------
		if (oscsd!=null) {
			if (oscsd.isSlotCardinalityIsMultiple()==true) {
				JButton jButtonMultiple = getJButtonMultiple();
				if (jButtonMultiple!=null) {
					this.jPanelToDisplay.add(jButtonMultiple, BorderLayout.EAST);
					// --- Ensure that this cell is editable ----------------------
					this.dynTable.getEditableRowsVector().add(this.rowModel);	
				}
			}
		}
		
		GraphGlobals.Colors.setTableCellRendererColors(this.jPanelToDisplay, this.rowTable, this.isSelected);
		return this.jPanelToDisplay;
	}

	/**
	 * Returns, if exists the multiple (+|-) JButton multiple of the DynForm.
	 *
	 * @param dynType the DynType
	 * @return the JButton multiple of the DynForm
	 */
	private JButton getJButtonMultipleOnDynForm(DynType dynType) {
		JButton jButtonMultipleOnDynForm = null;
		if (dynType!=null) {
			JPanel dynFormPanel = this.dynType.getPanel();
			for (int i=0; i < dynFormPanel.getComponentCount(); i++) {
				if (dynFormPanel.getComponent(i) instanceof JButton) {
					jButtonMultipleOnDynForm = (JButton) dynFormPanel.getComponent(i);
					break;
				}
			}
		}
		return jButtonMultipleOnDynForm;
	}
	
	/**
	 * This method initializes jButtonMultiple	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonMultiple() {
			
		JButton jButtonMultiple = null;

		final JButton jButtonMultipleOnDynForm = this.getJButtonMultipleOnDynForm(this.dynType);
		if (jButtonMultipleOnDynForm==null) {
			jButtonMultiple=null;

		} else {
			jButtonMultiple = new JButton(jButtonMultipleOnDynForm.getText());
			jButtonMultiple.setBounds(new Rectangle(new Dimension(25, 15)));
			jButtonMultiple.setFont(new Font("Monospaced", Font.BOLD, 12));
			jButtonMultiple.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					jButtonMultipleOnDynForm.doClick();
					dynTable.refreshTableModel();
				}
			});

		}
		return jButtonMultiple;
	}

	/**
	 * Returns the JButton for a OntologyClassVisualsation like for TimeSeries or XyChart's.
	 * @param text the text to set on the Button
	 * @return the JButton for special class
	 */
	private JButton getJButtonOntologyClassVisualsation(DynType dynTypeInstance) {
		
		final DynType dynTypeCurrent = dynTypeInstance;
		
		JButton jButtonSpecialClass = new JButton();
		jButtonSpecialClass.setFont(new Font("Arial", Font.BOLD, 11));
		jButtonSpecialClass.setText("Edit");
		jButtonSpecialClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dynTable.setOntologyClassVisualsationVisible(dynTypeCurrent);
			}
		});
		return jButtonSpecialClass;
	}
	
	/**
	 * Sets the value of display component.
	 * @param displComponent the new valoe of display component
	 */
	private void setValueOfDisplayComponent(JComponent displayComponent) {
		JComponent dynFormComponent = this.dynType.getFieldDisplay();
		if (displayComponent instanceof JTextField) {
			((JTextField) displayComponent).setText(((JTextField) dynFormComponent).getText());
		} else if (displayComponent instanceof JCheckBox) {
			((JCheckBox)displayComponent).setSelected(((JCheckBox)dynFormComponent).isSelected());
		}
	}

	/**
	 * Sets the new value to the DynForm component.
	 * @param displayComponent the current displayComponent
	 */
	private void setValueOfDynFormComponent(JComponent displayComponent) {
		JComponent dynFormComponent = this.dynType.getFieldDisplay();
		if (displayComponent instanceof JTextField) {
			((JTextField) dynFormComponent).setText(((JTextField) displayComponent).getText());
		} else if (displayComponent instanceof JCheckBox) {
			((JCheckBox)dynFormComponent).setSelected(((JCheckBox)displayComponent).isSelected());
		}
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
