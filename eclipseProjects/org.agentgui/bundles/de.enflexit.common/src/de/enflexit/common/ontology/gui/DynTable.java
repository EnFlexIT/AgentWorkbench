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
package de.enflexit.common.ontology.gui;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

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
	
	private DynTableDataVector dataVector = null;
	private DefaultTableModel myTabelModel = null;
	private TableRowSorter<DefaultTableModel> myRowSorter = null; 
	private RowFilter<DefaultTableModel, Object> myRowFilter = null; 
	
	
	/**
	 * Instantiates a new DynTable.
	 *
	 * @param dynForm the current DynForm
	 * @param dynTableJPanel the DynTableJPanel
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
	public void refreshTableModel() {
		
		int rowIndexSelected = this.getSelectedRow();
		
		DefaultTableModel dtm = (DefaultTableModel) this.getModel();
		if (dtm.getColumnCount()==0) {
			// --- Create a new table model ---------------
			Vector<String> columnNames = new Vector<String>();
			columnNames.add(" ");
			columnNames.add(" ");
			
			dtm = this.getTableModel();
			dtm.setDataVector(this.getDataVector(), columnNames);
			this.setModel(dtm);
			this.setRendererAndEditors();
			this.setRowSorter(this.getMyRowSorter());
			
		} else {
			// --- Exchange or re-new the data model ------
			DynTableDataVector newDataVector = new DynTableDataVector(this.dynForm);
			if (newDataVector.isEqualStructure(dtm.getDataVector())==false) {
				// --- exchange data completely -----------
				this.setDataVector(newDataVector);
				dtm.getDataVector().removeAllElements();
				dtm.getDataVector().addAll(this.getDataVector());
				dtm.fireTableDataChanged();
				this.setRowSorter(this.getMyRowSorter());
				
			} else {
				// --- Same structure! -------------------- 
				// --- => Just exchange different values --
				
// 				// --- THIS IS JUST A BACKUP SOLUTION ----- 				
//				for (int i=0; i < this.getDataVector().size(); i++) {
//					
//					Object objectThisRow = this.getDataVector().get(i);
//					Vector<Object> rowThis = (Vector<Object>) objectThisRow;
//					DynType dynTypeThis = (DynType) rowThis.get(0);
//					JComponent jCompThis = dynTypeThis.getFieldDisplay();
//					
//					Object objectNewRow = newDataVector.get(i);
//					Vector<Object> rowNew = (Vector<Object>) objectNewRow;
//					DynType dynTypeNew = (DynType) rowNew.get(0);
//					JComponent jCompNew = dynTypeNew.getFieldDisplay();
//					
//					if ((jCompThis!=null && jCompNew!=null) && jCompThis.getClass().getName().equals(jCompNew.getClass().getName())==true) {
//						if (jCompThis instanceof JTextField) {
//							((JTextField) jCompThis).setText(((JTextField) jCompNew).getText());
//						} else if (jCompThis instanceof JCheckBox) {
//							((JCheckBox) jCompThis).setSelected(((JCheckBox) jCompNew).isSelected());
//						}
//					}
//					
//				}
				dtm.fireTableDataChanged();
				
			}
			
		}
		
		// --- Set new selection --------------------------
		int rowCount = this.getRowCount();
		if (rowIndexSelected>(rowCount-1)) {
			this.setRowSelectionInterval(rowCount-1, rowCount-1);
		} else if (rowIndexSelected>-1) {
			this.setRowSelectionInterval(rowIndexSelected, rowIndexSelected);
		}
	}
	
	/**
	 * Sets the renderer and editor for the table.
	 */
	private void setRendererAndEditors() {
		// --- Set Renderer, Editors and layout -----------
		TableColumn propColumn = this.getColumnModel().getColumn(0);
		propColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		TableColumn valueColumn = this.getColumnModel().getColumn(1);
		valueColumn.setCellEditor(new DynTableCellRenderEditor(this.dynForm));
		valueColumn.setCellRenderer(new DynTableCellRenderEditor(this.dynForm));
		
		this.getColumnModel().getColumn(0).setPreferredWidth(180);
		this.getColumnModel().getColumn(1).setPreferredWidth(80);
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
	 * Sets the data vector.
	 * @param newDynTableDataVector the new data vector
	 */
	private void setDataVector(DynTableDataVector newDynTableDataVector) {
		this.dataVector = newDynTableDataVector;
	}
	/**
	 * Returns the data vector for the table.
	 * @return the data vector
	 */
	private DynTableDataVector getDataVector() {
		if (dataVector==null) {
			dataVector = new DynTableDataVector(this.dynForm);
		}
		return dataVector;
	}
	/**
	 * Gets the editable rows vector.
	 * @return the editable rows vector
	 */
	public Vector<Integer> getEditableRowsVector() {
		return this.getDataVector().getEditableRowsVector();
	}
	
	/**
	 * Sets the OntologyClassVisualsation visible.
	 * @param dynType the new special class visible
	 */
	public void setOntologyClassVisualsationVisible(DynType dynType) {
		this.dynTableJPanel.setOntologyClassVisualsationVisible(dynType);
	}
	
}