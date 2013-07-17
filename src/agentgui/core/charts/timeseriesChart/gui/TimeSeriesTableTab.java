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
package agentgui.core.charts.timeseriesChart.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableCellEditor4Time;
import agentgui.core.charts.gui.TableCellRenderer4Time;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

/**
 * The Class TimeSeriesTableTab.
 */
public class TimeSeriesTableTab extends TableTab {
	
	private static final long serialVersionUID = 9156505881049717567L;

	/**
	 * Create the TimeSeriesTableTab.
	 */
	public TimeSeriesTableTab(TimeSeriesDataModel model, ChartEditorJPanel parentChartEditor) {
		super(parentChartEditor);
		this.model = model;
		initialize();
	}
	@Override
	protected JTable getTable() {
		return this.getTable(true);
	}
	@Override
	protected JTable getTable(boolean forceRebuild) {
		if (table == null || forceRebuild) {
			table = new JTable(){

				private static final long serialVersionUID = 4259474557660027403L;

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellEditor(int, int)
				 */
				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					if(column == 0){
						return new TableCellEditor4Time(((TimeSeriesDataModel)model).getTimeFormat());
					}else{
						return new TableCellEditor4FloatObject();
					}
				}

				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellRenderer(int, int)
				 */
				@Override
				public TableCellRenderer getCellRenderer(int row, int column) {
					if(column == 0){
						return new TableCellRenderer4Time(((TimeSeriesDataModel)model).getTimeFormat());
					}else{
						return super.getCellRenderer(row, column);
					}
				}
				
				

				
			};
			table.setModel(model.getTableModel());
			table.setShowGrid(false);
//			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setCellSelectionEnabled(true);
			table.setRowSelectionAllowed(true);
			table.setColumnSelectionAllowed(true);
			table.getTableHeader().setReorderingAllowed(false);
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
			
		}
		return table;
	}
		
}
