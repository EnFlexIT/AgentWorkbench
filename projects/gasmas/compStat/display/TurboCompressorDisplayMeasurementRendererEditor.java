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
package gasmas.compStat.display;

import gasmas.ontology.ValueType;

import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import agentgui.envModel.graph.GraphGlobals;

public class TurboCompressorDisplayMeasurementRendererEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = -7991361823642444067L;

	private TurboCompressorDisplayMeasurements measurementsDisplay = null;
	
	private int currColumn = -1;
	private String rowHeader =null;
	private ValueType valueType=null;
	
	
	/**
	 * Instantiates a new turbo compressor display measurement renderer editor.
	 * @param measurementsDisplay the display for the measurements
	 */
	public TurboCompressorDisplayMeasurementRendererEditor(TurboCompressorDisplayMeasurements measurementsDisplay) {
		this.measurementsDisplay = measurementsDisplay;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JComponent displayComponent = null;
		
		this.currColumn = column;
		switch (this.currColumn) {
		case 0:
			this.rowHeader = (String) value;
			displayComponent = this.getJLabel4Display(this.rowHeader, column, row, isSelected);
			break;
		default:
			this.valueType = (ValueType) value;
			String displayText = getDisplayText4ValueType(this.valueType, column);
			displayComponent = this.getJLabel4Display(displayText, column, row, isSelected);
			break;
		}
		return displayComponent;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		this.currColumn = column;
		switch (this.currColumn) {
		case 0:
			this.rowHeader = (String) value;
			break;
		default:
			this.valueType = (ValueType) value;
			break;
		}
		
		// --- Highlight selected row ---------------------
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.setSelectionInterval(row, row);
		
		// --- Get the row of the model -------------------
		int tableModelRowSelected = table.convertRowIndexToModel(row);
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		// --- Build editable Vector for the dialog ------- 
		Vector<Object> editRow = new Vector<Object>();
		editRow.add(tableModel.getValueAt(tableModelRowSelected, 0));
		editRow.add(tableModel.getValueAt(tableModelRowSelected, 1));
		editRow.add(tableModel.getValueAt(tableModelRowSelected, 2));
		editRow.add(tableModel.getValueAt(tableModelRowSelected, 3));
		
		// --- Open edit diealog --------------------------
		TurboCompressorMeasurmentEdit tcme = new TurboCompressorMeasurmentEdit(editRow);
		tcme.setVisible(true);
		// --- Wait until the end of the user interaction -----------
		if (tcme.isCanceled()==true) {
			tcme.dispose();
			tcme = null;
			return null;
		}
		
		// --- Get edits and transfer it to the table -----
		editRow = tcme.getMeasurement();
		tableModel.setValueAt(editRow.get(0), tableModelRowSelected, 0);
		tableModel.setValueAt(editRow.get(1), tableModelRowSelected, 1);
		tableModel.setValueAt(editRow.get(2), tableModelRowSelected, 2);
		tableModel.setValueAt(editRow.get(3), tableModelRowSelected, 3);
		
		// --- Set the new current value ------------------
		switch (this.currColumn) {
		case 0:
			this.rowHeader = (String) editRow.get(0);
			break;
		default:
			this.valueType = (ValueType) editRow.get(this.currColumn);
			break;
		}
		
		// --- Inform listener about changes -------------- 
		this.measurementsDisplay.setMeasurements2TurboCompressor();
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		Object returnValue = null;
		switch (this.currColumn) {
		case 0:
			returnValue = this.rowHeader;
			break;
		default:
			returnValue = this.valueType;;
			break;
		}
		return returnValue;
	}
	
	/**
	 * Gets the display text4 value type.
	 *
	 * @param valueType the value type
	 * @param column the column
	 * @return the display text4 value type
	 */
	private String getDisplayText4ValueType(ValueType valueType, int column) {
		
		String displayText = null;
		if (valueType==null) return displayText;
		
		displayText = ((Float)valueType.getValue()).toString();
		String unit = valueType.getUnit();
		
		switch (column) {
		case 1:
			// --- Speed ------------------------ 
			if (unit==null) {
				unit = "PER_MIN";
				valueType.setUnit(unit);
			} else  if (unit.equalsIgnoreCase("PER_MIN")==false) {
				displayText += "  [" + unit + "]";
			}
			break;

		case 2:
			// --- Volumetric flow rate ---------
			if (unit==null) {
				unit = "m_cube_per_s";
				valueType.setUnit(unit);
			} else  if (unit.equalsIgnoreCase("m_cube_per_s")==false) {
				displayText += "  [" + unit + "]";
			}
			break;
			
		case 3:
			// --- Adiabatic Head ---------------
			if (unit==null) {
				unit = "kJ_per_kg";
				valueType.setUnit(unit);
			} else  if (unit.equalsIgnoreCase("kJ_per_kg")==false) {
				displayText += "  [" + unit + "]";
			}
			break;
		}
		return displayText;
	}
	
	/**
	 * Gets the JLabel for displaying the current value.
	 *
	 * @param headerText the header text
	 * @param column the column
	 * @param row the row
	 * @param isSelected the is selected
	 * @return the j label4 display
	 */
	private JLabel getJLabel4Display(String headerText, int column, int row, boolean isSelected) {
		JLabel jLabelHeaderText = new JLabel("  " + headerText + "  ");
		jLabelHeaderText.setFont(new Font("Dialog", Font.PLAIN, 12));
		if (column>0) {
			jLabelHeaderText.setHorizontalAlignment(JLabel.RIGHT);	
		}
		// --- Set color for the display component --------
		GraphGlobals.Colors.setTableCellRendererColors(jLabelHeaderText, row, isSelected);
		return jLabelHeaderText;
	}
	
	
}
