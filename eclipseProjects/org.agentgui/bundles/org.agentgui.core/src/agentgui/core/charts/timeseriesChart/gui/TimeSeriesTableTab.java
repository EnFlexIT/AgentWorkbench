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

import java.awt.event.ActionEvent;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.TableModel;
import agentgui.core.charts.TableModelDataVector;
import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.gui.TableCellEditor4FloatObject;
import agentgui.core.charts.gui.TableCellEditor4Time;
import agentgui.core.charts.gui.TableCellRenderer4Time;
import agentgui.core.charts.gui.TableTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesTableModel;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * TableTab-implementation for time series charts.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class TimeSeriesTableTab extends TableTab {
	
	private static final long serialVersionUID = 9156505881049717567L;

	/**
	 * Create the TimeSeriesTableTab.
	 */
	public TimeSeriesTableTab(TimeSeriesDataModel model, ChartEditorJPanel parentChartEditor) {
		super(parentChartEditor);
		this.dataModelLocal = model;
		initialize();
	}
	
	@Override
	protected JTable getTable() {
		if (jTable == null) {
			
			jTable = new JTable(){
				private static final long serialVersionUID = 4259474557660027403L;
				/* (non-Javadoc)
				 * @see javax.swing.JTable#getCellEditor(int, int)
				 */
				@Override
				public TableCellEditor getCellEditor(int row, int column) {
					if(column == 0){
						return new TableCellEditor4Time(((TimeSeriesDataModel)dataModelLocal).getTimeFormat());
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
						return new TableCellRenderer4Time(((TimeSeriesDataModel)dataModelLocal).getTimeFormat());
					}else{
						return super.getCellRenderer(row, column);
					}
				}
			};
			
			jTable.setModel(dataModelLocal.getTableModel());
			((TimeSeriesTableModel)dataModelLocal.getTableModel()).setTable(jTable);
			jTable.setShowGrid(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setCellSelectionEnabled(true);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(true);
			jTable.getTableHeader().setReorderingAllowed(false);
			
			jTable.getSelectionModel().addListSelectionListener(this);
			jTable.getColumnModel().getSelectionModel().addListSelectionListener(this);
			
			
			final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dataModelLocal.getTableModel());
			if (dataModelLocal.getTableModel().getColumnCount()>0) {
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
			// --- Initially sort by key ------------
			jTable.setRowSorter(sorter);
//			jTable.getRowSorter().toggleSortOrder(0);
			
//			List<SortKey> sortKeys = new ArrayList<SortKey>();
//			for (int i = 0; i < jTable.getColumnCount(); i++) {
//			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
//			}
			
		}
		return jTable;
	}
		
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// --- Stop Cell editing --------------------------
		if (this.getTable().isEditing()) {
			this.getTable().getCellEditor().stopCellEditing();	
		}
		
		// --- Case separation ActionEvent ----------------
		if(e.getSource()==getBtnAddColumn() || (e.getSource()==getBtnAddRow() && getTable().getRowCount()==0)){
			
			String seriesLabel = (String) JOptionPane.showInputDialog(this, Language.translate("Label"), Language.translate("Neue Datenreihe"), JOptionPane.QUESTION_MESSAGE, null, null, dataModelLocal.getDefaultSeriesLabel());
			if(seriesLabel != null){
				// --- As JFreeChart doesn't work with empty data series, one value pair must be added ------ 
				DataSeries newSeries = this.dataModelLocal.createNewDataSeries(seriesLabel);
				ValuePair initialValuePair;
				if(this.dataModelLocal.getSeriesCount() > 0){
					// -- If there is already data in the model, use the first existing key -----------------
					TableModelDataVector dataVector = this.dataModelLocal.getTableModel().getTableModelDataVector();
					int keyColumn = dataVector.getKeyColumnIndex();
					for (int row=0; row<dataVector.size(); row++) {
						initialValuePair = this.dataModelLocal.createNewValuePair((Number) this.dataModelLocal.getTableModel().getValueAt(row, keyColumn), 0);
						this.dataModelLocal.getValuePairsFromSeries(newSeries).add(initialValuePair);
					}
					
				}else{
					// --- If not, use 0 as key value -------------------------------------------------------
					initialValuePair = this.dataModelLocal.createNewValuePair(0, 0);
					this.dataModelLocal.getValuePairsFromSeries(newSeries).add(initialValuePair);
				}
				this.dataModelLocal.addSeries(newSeries);
			}
			
		} else if(e.getSource() == getBtnAddRow()){
			this.dataModelLocal.getTableModel().addEmptyRow(this.getTable());
			
		} else if(e.getSource() == getBtnRemoveRow()){
			this.dataModelLocal.getTableModel().removeRow(this.getTable());

		} else if(e.getSource() == getBtnRemoveColumn()){
			
			if(jTable.getSelectedColumn() > 0){
				TableModelDataVector tmdv = ((TableModel) jTable.getModel()).getTableModelDataVector();
				int seriesIndex = jTable.getSelectedColumn()-tmdv.getNoOfPrefixColumns();
				try {
					this.dataModelLocal.removeSeries(seriesIndex);

				} catch (NoSuchSeriesException nsse) {
					System.err.println("Error removing series " + seriesIndex);
					nsse.printStackTrace();
				}
			}
			
		} 
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		this.setButtonsEnabledToSituation();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.TableTab#setButtonsEnabledToSituation()
	 */
	@Override
	public void setButtonsEnabledToSituation() {
		// Enable btnRemoveRow if a row is selected
		getBtnRemoveRow().setEnabled(jTable.getSelectedRow() >= 0);
		// Enable btnRemoveColumn if a non-key column is selected
		getBtnRemoveColumn().setEnabled(jTable.getSelectedColumn() > 0);
	}

	
}
