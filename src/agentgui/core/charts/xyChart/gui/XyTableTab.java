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
package agentgui.core.charts.xyChart.gui;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.charts.gui.FloatInputDialog;
import agentgui.core.charts.gui.KeyInputDialog;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.xyChart.XyDataModel;

public class XyTableTab extends TableTab {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5737806366707646814L;
	
	public XyTableTab(XyDataModel model){
		this.model = model;
		initialize();
	}

	@Override
	protected JTable getTable() {
		if(table == null){
			table = new JTable(){

				/**
				 * Generated serialVersionUID
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					// Same cell editor for all columns
					return new TableCellEditor4FloatObject();
				}
			};
		}
		
		table.setModel(model.getTableModel());
		table.getSelectionModel().addListSelectionListener(this);
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);
		
		return table;
	}

	@Override
	protected KeyInputDialog getKeyInputDialog(String title) {
		return new FloatInputDialog(SwingUtilities.getWindowAncestor(this), title, Language.translate("X Wert"));
	}

}
