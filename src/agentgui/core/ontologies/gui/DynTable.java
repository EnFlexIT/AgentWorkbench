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

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Application;

/**
 * The Class DynTable.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTable extends JTable {

	private static final long serialVersionUID = 2625919645764324098L;

	private DynTableJPanel dynTableJPanel = null;
	private DynForm dynForm = null;
	private DefaultTableModel tabelModel = null;
	
	private int rowCounter = 0;
	private TableRowSorter<DefaultTableModel> rowSorter = null; 
	private Vector<Integer> editableRows = null;
	
	
	/**
	 * Instantiates a new DynTable.
	 * @param dynForm the current DynForm
	 */
	public DynTable(DynForm dynForm, DynTableJPanel dynTableJPanel) {
		super();
		this.dynForm = dynForm;
		this.dynTableJPanel = dynTableJPanel;
		this.initialize();
	}
	
	/** 
	 * Initialize this. 
	 */
	private void initialize() {
		this.setSize(200, 200);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.refreshTableModel();
	}
	
	/**
	 * Refreshes the local table model by reloading the data from the DynForm.
	 */
	public void refreshTableModel() {
		
		this.rowCounter=0;
		this.tabelModel=null;
		this.getEditableRowsVector().removeAllElements();
		
		this.setModel(this.getTableModel());	
		this.setRowSorter(this.getMyRowSorter());
		this.setFilterInvisibleSlots();
		
		this.getTableHeader().setReorderingAllowed(false);
		
		// --- Set Renderer, Editors and layout -----------
		TableColumn propColumn = this.getColumnModel().getColumn(0);
		propColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		TableColumn valueColumn = this.getColumnModel().getColumn(1);
		valueColumn.setCellEditor(new DynTableCellRenderEditor(this.dynForm));
		valueColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		this.getColumnModel().getColumn(0).setPreferredWidth(170);
		this.getColumnModel().getColumn(1).setPreferredWidth(60);
		
	}
	
	/**
	 * Gets the table model.
	 * @return the table model
	 */
	private DefaultTableModel getTableModel(){
		if (this.tabelModel==null) {
			
			Vector<String> columnNames = new Vector<String>();
			columnNames.add(" ");
			columnNames.add(" ");
			
			this.tabelModel = new DefaultTableModel(this.getDataVector(), columnNames) {
				private static final long serialVersionUID = 1217406328326262128L;
				public boolean isCellEditable(int row, int column) {
					if (column==0) {
						return false;
					} else {
						if (getEditableRowsVector().contains(row)==true) {
							return true;
						} else {
							return false;
						}
					}
				};
			};
		}
		return this.tabelModel;
	}
	
	/**
	 * Gets the local RowSorter for the table.
	 * @return the local RowSorter
	 */
	public TableRowSorter<DefaultTableModel> getMyRowSorter() {
		if (rowSorter==null) {
			rowSorter = new TableRowSorter<DefaultTableModel>(this.getTableModel()) {
				public void toggleSortOrder(int column) {};
			};
		}
		return rowSorter;
	}
	
	/**
	 * Sets the filter so that invisible declared slots disappear.
	 */
	public void setFilterInvisibleSlots() {
		RowFilter<DefaultTableModel, Integer> rowFilter = new RowFilter<DefaultTableModel, Integer>(){
			@Override
			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
				return ((DynType) entry.getValue(0)).isVisibleInTableView();
			};
		};
		this.getMyRowSorter().setRowFilter(rowFilter);
	}

	/**
	 * Gets the editable rows vector.
	 * @return the editable rows vector
	 */
	public Vector<Integer> getEditableRowsVector() {
		if (this.editableRows==null) {
			this.editableRows = new Vector<Integer>();
		}
		return this.editableRows;
	}
	
	/**
	 * Returns the data vector for the table.
	 * @return the data vector
	 */
	private Vector<Object> getDataVector() {
		
		Vector<Object> dataVector = new Vector<Object>();
		DefaultTreeModel objectTree = this.dynForm.getObjectTree();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) objectTree.getRoot();
		
		Vector<Object> rows = this.getChildNodeVector(rootNode, true);
		dataVector.addAll(rows);
		
		return dataVector;
	}
	
	/**
	 * Gets the child node vector.
	 * @return the child node vector
	 */
	private Vector<Object> getChildNodeVector(DefaultMutableTreeNode parentNode, boolean visibleInTableView) {
		
		Vector<Object> childVector = new Vector<Object>();
		boolean childNodesVisible = true;
		for (int i=0; i<parentNode.getChildCount(); i++) {

			// --------------------------------------------
			// --- Create data row for this node ----------
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			DynType dynType = (DynType) childNode.getUserObject();
			dynType.setVisibleInTableView(visibleInTableView);
			
			if (visibleInTableView==true) {
				// --- Are child slots visible in the table ---
				if (Application.getGlobalInfo().isOntologyClassVisualisation(dynType.getClassName())) {
					childNodesVisible = false;
				} else {
					childNodesVisible = true;
				}
				
			} else {
				// --- nodes and sub node are invisible ---
				childNodesVisible = visibleInTableView;
			}
			
			// --- Create data row ------------------------
			Vector<Object> dataRow = new Vector<Object>();			
			dataRow.add(dynType);
			dataRow.add(dynType);
			// --- Add to mainVector ----------------------
			childVector.add(dataRow);
			
			// --------------------------------------------
			// --- Remind the row number as editable!? ----
			if (dynType.getTypeName().equals(DynType.typeRawType)) {
				this.getEditableRowsVector().add(this.rowCounter);
			}
			this.rowCounter++;
			
			// --------------------------------------------
			// --- Add the Child nodes, if available ------
			if (childNode.getChildCount()!=0) {
				// --- get child nodes first --------------
				childVector.addAll(this.getChildNodeVector(childNode, childNodesVisible));
			}
			
		}
		return childVector;
	}
	
	/**
	 * Sets the special class visible.
	 * @param dynTyp the new special class visible
	 */
	public void setSpecialClassVisible(DynType dynType) {
		
		DefaultMutableTreeNode currNode = this.dynForm.getTreeNodeByDynType(dynType);;
		JComponent comp = this.dynForm.getFormComponent4AgentGUISpecialClass(currNode);
		Object ontoClassInstance=null; 
		
		if (comp instanceof OntologyClassWidget) {
			OntologyClassWidget ontoClassWidget = (OntologyClassWidget) comp;
			ontoClassInstance = ontoClassWidget.getOntologyClassInstance();
		}
		
		if (ontoClassInstance==null) {
			
		} else {
			System.out.println(ontoClassInstance.toString());
			System.out.println(dynType.getClassName());	
		}
		
		this.dynTableJPanel.setSpecialClassVisible(true);
		
	}
	
}