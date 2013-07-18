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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.xyChart.XyDataModel;
/**
 * TableTab-implementation for XY-charts.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class XyTableTab extends TableTab {

	private static final long serialVersionUID = -5737806366707646814L;
	
	
	public XyTableTab(XyDataModel model, ChartEditorJPanel parentChartEditor){
		super(parentChartEditor);
		this.model = model;
		initialize();
	}

	@Override
	protected JTable getTable() {
		return this.getTable(false);
	}

	@Override
	protected JTable getTable(boolean forceRebuild) {
		if(table == null || forceRebuild){
			table = new JTable(){

				private static final long serialVersionUID = 3537626788187543327L;

				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					// Same cell editor for all columns
					return new TableCellEditor4FloatObject();
				}
			};
		}
		table.setModel(model.getTableModel());
		table.setShowGrid(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model.getTableModel());
		if (model.getTableModel().getColumnCount()>0) {
			sorter.setComparator(0, new Comparator<Number>() {
				@Override
				public int compare(Number value1, Number value2) {
					if (value1 instanceof Float || value1 instanceof Double || value2 instanceof Float || value2 instanceof Double) {
						Double dblValue1 = value1.doubleValue();
						Double dblValue2 = value2.doubleValue();
						return dblValue1.compareTo(dblValue2);
						
					} else {
						Long lngValue1 = value1.longValue();
						Long lngValue2 = value2.longValue();
						return lngValue1.compareTo(lngValue2);
					}
				}
			});				
		}
		table.setRowSorter(sorter);
		
		// --- Initially sort by key ------------
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		for (int i = 0; i < table.getColumnCount(); i++) {
		    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
		}
		table.getRowSorter().setSortKeys(sortKeys);
		
		return table;
	}

}
