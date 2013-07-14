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
 * The Class DynTable provides an alternative view to the slots of an ontology
 * and is strongly related to the {@link DynForm}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DynTable extends JTable {

	private static final long serialVersionUID = 2625919645764324098L;

	private DynTableJPanel dynTableJPanel = null;
	private DynForm dynForm = null;
	
	private DefaultTableModel myTabelModel = null;
	private TableRowSorter<DefaultTableModel> myRowSorter = null; 
	private RowFilter<DefaultTableModel, Object> myRowFilter = null; 

	private int rowCounter = 0;
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
		this.getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Refreshes the local table model by reloading the data from the DynForm.
	 */
	@SuppressWarnings("unchecked")
	public void refreshTableModel() {
		
		this.rowCounter = 0;
		this.myRowSorter  = null;
		this.myRowFilter  = null;
		int previouslySelectedRow = this.getSelectedRow();
		
		this.getEditableRowsVector().removeAllElements();
		
		DefaultTableModel dtm = (DefaultTableModel) this.getModel();
		Vector<Object> dataVector = this.getDataVector();
		if (dtm.getColumnCount()==0) {

			Vector<String> columnNames = new Vector<String>();
			columnNames.add(" ");
			columnNames.add(" ");
			
			dtm = this.getTableModel();
			dtm.setDataVector(dataVector, columnNames);
			this.setModel(dtm);
			
		} else {
			
			dtm = (DefaultTableModel) this.getModel();
			dtm.getDataVector().removeAllElements();
			dtm.getDataVector().addAll(dataVector);
			dtm.fireTableDataChanged();
			
		}
		this.setRowSorter(this.getMyRowSorter());
		
		// --- Set Renderer, Editors and layout -----------
		TableColumn propColumn = this.getColumnModel().getColumn(0);
		propColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		TableColumn valueColumn = this.getColumnModel().getColumn(1);
		valueColumn.setCellEditor(new DynTableCellRenderEditor(this.dynForm));
		valueColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		this.getColumnModel().getColumn(0).setPreferredWidth(180);
		this.getColumnModel().getColumn(1).setPreferredWidth(80);
		
		if (previouslySelectedRow>-1) {
			this.setRowSelectionInterval(previouslySelectedRow, previouslySelectedRow);
		}
		
	}
	
	/**
	 * Gets the table model.
	 * @return the table model
	 */
	private DefaultTableModel getTableModel(){
		if (this.myTabelModel==null) {
			this.myTabelModel = new DefaultTableModel() {
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
		return this.myTabelModel;
	}
	
	/**
	 * Gets the local RowSorter for the table.
	 * @return the local RowSorter
	 */
	private TableRowSorter<DefaultTableModel> getMyRowSorter() {
		if (myRowSorter==null) {
			myRowSorter = new TableRowSorter<DefaultTableModel>(this.getTableModel()) {
				public void toggleSortOrder(int column) {};
			};
			myRowSorter.setRowFilter(this.getMyRowFilter());
		}
		return myRowSorter;
	}
	
	/**
	 * Sets the filter so that invisible declared slots disappear.
	 */
	private RowFilter<DefaultTableModel, Object> getMyRowFilter() {
		if (myRowFilter==null) {
			this.myRowFilter = new RowFilter<DefaultTableModel, Object>(){
				@Override
				public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
					DynType dynType = (DynType) entry.getValue(0);
					return dynType.isVisibleInTableView();
				};
			};
		}
		return myRowFilter;
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
	 * Sets the OntologyClassVisualsation visible.
	 * @param dynType the new special class visible
	 */
	public void setOntologyClassVisualsationVisible(DynType dynType) {
		this.dynTableJPanel.setOntologyClassVisualsationVisible(dynType);
	}
	
}