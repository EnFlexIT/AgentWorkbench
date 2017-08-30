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
package agentgui.core.charts.xyChart;

import jade.util.leap.List;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

import agentgui.core.application.Language;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.TableModel;
import agentgui.core.charts.TableModelDataVector;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;


/**
 * The Class XyTableModel.
 */
public class XyTableModel extends TableModel {

	private static final long serialVersionUID = -8873141815861732107L;
	
	private static final String DEFAULT_NUMBER_COLUMN_TITLE = "Row No";
	private static final String DEFAULT_KEY_COLUMN_TITLE = "X Value";
	
	private JTable myJTable = null;
	private int currSeriesSelection = -1;
	private Vector<TableModelDataVector> seriesTableModels = null;
	
	
	/**
	 * Instantiates a new XyTableModel.
	 * @param parent the parent
	 */
	public XyTableModel(JTable myJTable, XyDataModel parent){
		this.myJTable = myJTable;
		this.parentDataModel = parent;
		this.initilizeTabelModel();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#initilizeTabelModel()
	 */
	public void initilizeTabelModel() {
		this.columnTitles = new Vector<String>();
		this.columnTitles.add(DEFAULT_NUMBER_COLUMN_TITLE);
		this.columnTitles.add(DEFAULT_KEY_COLUMN_TITLE);
		this.currSeriesSelection = -1;
		this.seriesTableModels = new Vector<TableModelDataVector>();
		this.tableModelDataVector = new TableModelDataVector(XyDataSeries.class.getSimpleName(), true, 0);
		this.addTableModelListener(this);
		this.setTabTitle();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#rebuildTableModel()
	 */
	@Override
	public void rebuildTableModel() {
		
		// Remember the current width of the table columns
		Vector<Integer> columnWidths = this.getColumnWidths();
		
		// Remember column widths
		int seriesSelected = this.getFocusedSeriesIndex();
		
		// Rebuild the table model
		this.seriesTableModels = null;
		this.tableModelDataVector = null;
		List chartDataSeries = this.parentDataModel.getOntologyModel().getChartData();
		for (int i = 0; i < chartDataSeries.size(); i++) {
			DataSeries chartData = (DataSeries) chartDataSeries.get(i);
			this.addSeries(chartData);
		}
		
		// Select previously selected series
		if (seriesSelected!=-1) {
			this.focusSeries(seriesSelected);
		}
		
		// Set the width of the columns like before
		this.setColumnWidths(columnWidths);
	}
	
	/**
	 * Rebuilds the table model for the specified XYDataSeries.
	 * @param seriesIndex the series index of the XyDataSeries to change
	 */
	public void rebuildXyDataSeries(int seriesIndex) {
		
		try {
			XyDataSeries xyDataSeries = (XyDataSeries) this.parentDataModel.getOntologyModel().getSeries(seriesIndex);
			TableModelDataVector tmdv = this.getTableModelDataVectorFromXyDataSeries(xyDataSeries);
			this.seriesTableModels.set(seriesIndex, tmdv);
			if (seriesIndex==this.getFocusedSeriesIndex()) {
				this.focusSeries(seriesIndex);	
			}
			xyDataSeries = this.getXyDataSeriesFromData();
			this.getOntologyModel().exchangeSeries(seriesIndex, xyDataSeries);
			this.getChartModel().exchangeSeries(seriesIndex, xyDataSeries);
		
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
	}
	
	/**
	 * Gets the chart model.
	 * @return the chart model
	 */
	private XyChartModel getChartModel() {
		return (XyChartModel) this.parentDataModel.getChartModel();
	}
	/**
	 * Gets the ontology model.
	 * @return the ontology model
	 */
	private XyOntologyModel getOntologyModel() {
		return (XyOntologyModel) this.parentDataModel.getOntologyModel();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) {
			return Integer.class;
		} 
		return Float.class;
	}
	
	/**
	 * @return the defaultTimeColumnTitle
	 */
	public static String getDefaultKeyColumnTitle() {
		return DEFAULT_KEY_COLUMN_TITLE;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#setSeriesLabel(int, java.lang.String)
	 */
	@Override
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex==this.getFocusedSeriesIndex()){
			if (columnTitles.size()==3){
				columnTitles.set(2, label);	
			} else {
				columnTitles.add(2, label);
			}
			fireTableStructureChanged();
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the series table models.
	 * @return the series table models
	 */
	public Vector<TableModelDataVector> getSeriesTableModels() {
		if (this.seriesTableModels==null) {
			this.seriesTableModels = new Vector<TableModelDataVector>();
		}
		return this.seriesTableModels;
	}
	
	/**
	 * Gets the focused series index.
	 * @return the focused series index
	 */
	public int getFocusedSeriesIndex() {
		return this.currSeriesSelection;
	}
	/**
	 * Focuses the specified series.
	 *
	 * @param targetSeriesIndex the target series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public void focusSeries(int targetSeriesIndex) {
		
		try {
			if (targetSeriesIndex<0) {
				targetSeriesIndex = 0;
			} else if (targetSeriesIndex>(this.getSeriesTableModels().size()-1)) {
				targetSeriesIndex = this.getSeriesTableModels().size()-1;
			}
			this.currSeriesSelection = targetSeriesIndex;
			// --- Get label from series ------------------
			DataSeries series = this.parentDataModel.getOntologyModel().getSeries(this.currSeriesSelection);
			this.tableModelDataVector = this.getSeriesTableModels().get(this.currSeriesSelection);
			// --- Set series name ------------------------
			if (this.columnTitles.size()==3) {
				this.columnTitles.set(2, series.getLabel());	
			} else {
				this.columnTitles.add(2, series.getLabel());
			}
			// --- Refresh model --------------------------
			this.myJTable.setModel(this);
			this.fireTableStructureChanged();
			
			// --- Set tab-title --------------------------
			this.setTabTitle();
			
		} catch (NoSuchSeriesException nsse) {
			nsse.printStackTrace();
		}
	}
	
	/**
	 * Sets the tab title.
	 */
	private void setTabTitle() {
		
		String tabTitle = "";
		if (this.getSeriesTableModels().size()==0) {
			tabTitle = "Table (0)";
		} else {
			tabTitle = "Table (" + (currSeriesSelection+1) + "/" +  this.getSeriesTableModels().size() + ")";
		}
		Container tableTab = this.myJTable.getParent().getParent().getParent();
		JTabbedPane editorTabbedPane =(JTabbedPane) tableTab.getParent();
		if (editorTabbedPane!=null) {
			int tabPosIndex = 0;
			for (tabPosIndex=0; tabPosIndex<editorTabbedPane.getTabCount(); tabPosIndex++) {
				if (editorTabbedPane.getComponentAt(tabPosIndex)==tableTab) {
					break;
				}
			}
			editorTabbedPane.setTitleAt(tabPosIndex, tabTitle);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#addEmptyRow(javax.swing.JTable)
	 */
	@Override
	public void addEmptyRow(JTable jTable){
		
		// --- Get current position in the table model ----
		Vector<Number> rowSelected = null;
		Vector<Number> newRow = null;
		Number currKey = 0;
		float newXValue = 0f;
		float newYValue = 0f;
		
		// --- Get the current or the last row ------------
		int modelLineSelected = 0;
		int modelLineSelectedNew = 0;
		int tableRowSelected = 0;
		int tableRowSelectedNew = 0;
			
		tableRowSelected = jTable.getSelectedRow();
		if (tableRowSelected==-1 || tableRowSelected>jTable.getRowCount()) {
			tableRowSelected = 0;
		}
		modelLineSelected=jTable.convertRowIndexToModel(tableRowSelected);
		if (modelLineSelected>this.tableModelDataVector.size()-1) {
			modelLineSelected = this.tableModelDataVector.size()-1; 
		}
		
		if (tableModelDataVector.size()==0) {
			// --- Create new data series -----------------
			DataSeries newSeries = parentDataModel.createNewDataSeries(parentDataModel.getDefaultSeriesLabel());
			
			ValuePair initialValuePair = parentDataModel.createNewValuePair(newXValue, newYValue);
			parentDataModel.getValuePairsFromSeries(newSeries).add(initialValuePair);
			parentDataModel.addSeries(newSeries);
			
		} else {
			// --- Get current selection ------------------
			rowSelected = tableModelDataVector.get(modelLineSelected);
			currKey = rowSelected.get(tableModelDataVector.getKeyColumnIndex());
			newRow = new Vector<Number>();

			// --- Case separation according to the key ---
			if (tableModelDataVector.getKeyColumnIndex()==0) {
				// ------------------------------
				// --- Key by row number --------
				// ------------------------------
				// --- Find new index -----------
				modelLineSelectedNew = modelLineSelected;
				// --- Add to new row -----------
				newRow.add(currKey);
				newRow.add(newXValue);
				
			} else {
				// ------------------------------
				// --- Key by X Value -----------
				// ------------------------------
				newXValue = currKey.floatValue() + 1f;
				while (tableModelDataVector.getKeyRowVectorTreeMap().get(newXValue)!=null) {
					newXValue = newXValue + 1f;
				}
				// --- Find new index -----------
				modelLineSelectedNew = tableModelDataVector.size();
				for (int i=0; i < tableModelDataVector.size(); i++) {
					Vector<Number> row = tableModelDataVector.get(i);
					Number key = row.get(tableModelDataVector.getKeyColumnIndex());
					if (key.doubleValue()>newXValue) {
						modelLineSelectedNew = i;
						break;
					}
				}
				// --- Add to new row -----------
				newRow.add(modelLineSelectedNew+1);
				newRow.add(newXValue);
			}
			newRow.add(newYValue);
			
			this.getOntologyModel().addXyValuePair(this.getFocusedSeriesIndex(), modelLineSelectedNew, newXValue, newYValue);
			this.getChartModel().addXyDataItem(this.getFocusedSeriesIndex(), modelLineSelectedNew, newXValue, newYValue);
			
			// --- Add new row to table model ---
			tableModelDataVector.add(modelLineSelectedNew, newRow);
			
		}
		fireTableRowsInserted(0, getRowCount()-1);
		
		// --- Set new selection ----------------
		tableRowSelectedNew = jTable.convertRowIndexToView(modelLineSelectedNew);
		if(tableRowSelectedNew >= 0){
			jTable.setRowSelectionInterval(tableRowSelectedNew, tableRowSelectedNew);
			jTable.changeSelection(tableRowSelectedNew, 0, false, false);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#removeRow(javax.swing.JTable)
	 */
	@Override
	public void removeRow(JTable jTable){

		int rowIndexTable = jTable.getSelectedRow();
		
		int rowIndexModel = 0;
		int rowIndexTableNew = 0;
		int rowIndexModelNew = 0;
		
		if(jTable.getRowCount()==1 && parentDataModel.getSeriesCount()==1){
			// ---- Last row of the last data series ------
			while(parentDataModel.getSeriesCount()>0){
				try {
					parentDataModel.removeSeries(0);
				} catch (NoSuchSeriesException nsse) {
					nsse.printStackTrace();  
				}
			}
			rowIndexModelNew = -1;
			rowIndexTableNew = -1;
		
		} else if (jTable.getRowCount()==1 && parentDataModel.getSeriesCount()>1) {
			// --- Last row of a data series --------------
			try {
				parentDataModel.removeSeries(this.getFocusedSeriesIndex());
			} catch (NoSuchSeriesException nsse) {
				nsse.printStackTrace();
			}
			
		} else{
			// --- One of several rows are selected -------
			if ((rowIndexTable+1) > jTable.getRowCount()) {
				rowIndexTable = jTable.getRowCount()-1;
			}
			rowIndexModel = jTable.convertRowIndexToModel(rowIndexTable);
			
			this.tableModelDataVector.remove(rowIndexModel);
			this.getOntologyModel().removeXyValuePair(this.getFocusedSeriesIndex(), rowIndexModel);
			this.getChartModel().setXYSeriesAccordingToOntologyModel(this.getFocusedSeriesIndex());
			this.fireTableRowsDeleted(rowIndexModel, rowIndexModel);
			
			rowIndexModelNew = rowIndexModel;
			if (rowIndexModelNew>=jTable.getRowCount()) {
				rowIndexModelNew = jTable.getRowCount()-1;
			}
			rowIndexTableNew = jTable.convertRowIndexToView(rowIndexModelNew);
			
		}
		
		// --- Set new selection ------------
		if(rowIndexTableNew==-1){
			jTable.clearSelection();
		} else {
			jTable.setRowSelectionInterval(rowIndexTableNew, rowIndexTableNew);
			jTable.changeSelection(rowIndexTableNew, 0, false, false);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries newSeries){

		if (newSeries==null) return;
		XyDataSeries xyDataSeries = (XyDataSeries) newSeries;
		if (xyDataSeries.getAutoSort()==true) {
			xyDataSeries.sort();	
		}
		
		// --- Do some initial work on the data series ----
		TableModelDataVector tmdv = this.getTableModelDataVectorFromXyDataSeries(xyDataSeries);

		// --- Add TableModels and set focus --------------
		this.getSeriesTableModels().add(tmdv);
		this.focusSeries(this.getSeriesTableModels().size()-1);
		this.fireTableStructureChanged();
		
		if (xyDataSeries.getAvoidDuplicateXValues()==true && tmdv.size()!=xyDataSeries.getXyValuePairs().size()) {
			// ---------------------------------------------------------------- 
			// --- Some rows are filtered because of duplicate X-Values. ------ 
			// --- Correct the ontology and chart model					 ------
			// ----------------------------------------------------------------
			xyDataSeries = this.getXyDataSeriesFromData();
			this.getOntologyModel().getChartData().remove(this.getFocusedSeriesIndex());
			this.getOntologyModel().getChartData().add(this.getFocusedSeriesIndex(), xyDataSeries);
			try {
				this.getChartModel().exchangeSeries(this.getFocusedSeriesIndex(), xyDataSeries);
			} catch (NoSuchSeriesException nsse) {
				nsse.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * Exchanges the specified data series with a new data series.
	 *
	 * @param seriesIndex the series index
	 * @param newSeries the new series
	 */
	public void exchangeSeries(int seriesIndex, DataSeries newSeries) {
		XyDataSeries xyDataSeries = (XyDataSeries) newSeries;
		TableModelDataVector tmdv = this.getTableModelDataVectorFromXyDataSeries(xyDataSeries);
		if (seriesIndex>-1 && seriesIndex<this.seriesTableModels.size()) {
			this.seriesTableModels.set(seriesIndex, tmdv);
			if (seriesIndex==this.getFocusedSeriesIndex()) {
				this.focusSeries(seriesIndex);	
			}
		}
	}
	
	/**
	 * Removes a XY Series from the table model
	 * @param seriesIndex The index of the series to be removed
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException {

		if (this.getSeriesTableModels().size()==1) {
			// --- The last series has to be removed ------
			this.initilizeTabelModel();
			
		} else {
			if (this.getSeriesTableModels().size()==0 || seriesIndex>this.getSeriesTableModels().size()-1) {
				throw new NoSuchSeriesException();
				
			} else {
				this.getSeriesTableModels().remove(seriesIndex);
				// --- Focus next series ------------------
				if (this.getSeriesTableModels().size()>0) {
					if (seriesIndex<(this.getSeriesTableModels().size()-1)) {
						this.focusSeries(seriesIndex);	
					} else {
						this.focusSeries(this.getSeriesTableModels().size()-1);
					}
				} else {
					this.initilizeTabelModel();
				}
			}
		}
		fireTableStructureChanged();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent tme) {
		
		if(tme.getSource()==this && tme.getFirstRow()>=0){
			
			if(tme.getType()==TableModelEvent.UPDATE){
				// --- Update Events in the table ---------------------------------------
				try {
					int rowIndex = tme.getFirstRow();
					int colIndex = tme.getColumn();
					Vector<Number> rowVector = this.getRow(rowIndex);
					Float xValue = (Float) rowVector.get(1);
					Float yValue = (Float) rowVector.get(2);
					
					// --- Do we have changes? ------------------------------------------
					if (colIndex==1) {
						if (((Float)this.latestChangedValue).equals(xValue)) return;
						if (this.getOntologyModel().getSeries(this.getFocusedSeriesIndex()).getAvoidDuplicateXValues()==true) {
							// --- Risk of duplicate x values ? -------------------------
							if (this.tableModelDataVector.getRowByValue(colIndex, xValue, rowIndex)!=null) {
								// --- Another row contains this value, undo ------------
								String title = "Doppelte X-Werte nicht zulässig!";
								String msg = "Doppelte X-Werte sind derzeit nicht zulässig!\n";
								msg += "Die Aktion wird rückgängig gemacht.";
								
								JOptionPane.showMessageDialog(this.myJTable, Language.translate(msg), Language.translate(title), JOptionPane.WARNING_MESSAGE, null);
								xValue = (Float) this.latestChangedValue;
								this.tableModelDataVector.get(rowIndex).set(colIndex, xValue);
								return;
							}
						}
					} else if (colIndex==2) {
						if (((Float)this.latestChangedValue).equals(yValue)) return;
					}
					
					// --- A value of a series was edited -------------------------------
					if(xValue!=null && yValue!=null){
						// --- Update new entry in chart and ontology model --------
						this.getOntologyModel().updateXyValuePair(this.getFocusedSeriesIndex(), rowIndex, xValue, yValue);
						if (this.getOntologyModel().getSeries(this.getFocusedSeriesIndex()).getAutoSort()==true) {
							this.tableModelDataVector.sort();
							this.getOntologyModel().getSeries(this.getFocusedSeriesIndex()).sort();
							// --- Find new position of the row vector -------------  
							rowIndex = this.tableModelDataVector.indexOf(rowVector);
						}
						this.getChartModel().setXYSeriesAccordingToOntologyModel(this.getFocusedSeriesIndex());
						if (rowIndex!=-1) {
							int rowIndexTable = this.myJTable.convertRowIndexToView(rowIndex);
							this.myJTable.setRowSelectionInterval(rowIndexTable, rowIndexTable);
							this.myJTable.changeSelection(rowIndexTable, 0, false, false);
						}
						
					} else {
						// --- We have an empty yValue, delete ValuePair ------------
						this.tableModelDataVector.remove(rowIndex);
						if (this.getRowCount()==0) {
							this.parentDataModel.removeSeries(this.getFocusedSeriesIndex());
						} else {
							this.getOntologyModel().removeXyValuePair(this.getFocusedSeriesIndex(), rowIndex);
							this.getChartModel().setXYSeriesAccordingToOntologyModel(this.getFocusedSeriesIndex());
						}
					}
					
				} catch (NoSuchSeriesException e) {
					System.err.println("Error updating data model: Series "+this.getFocusedSeriesIndex()+" does mot exist!");
					e.printStackTrace();
				}
				
			} else {
				// --- Insert or Delete events in the table ---------
			}
		}

	}
	
	/**
	 * Returns a TableModelDataVector from a given XyDataSeries.
	 * @param xyDataSeries the xy data series
	 * @return the table model data vector from xy data series
	 */
	private TableModelDataVector getTableModelDataVectorFromXyDataSeries(XyDataSeries xyDataSeries) {
		
		int keyColumnIndex = 0;
		if (xyDataSeries.getAvoidDuplicateXValues()==true) {
			keyColumnIndex = 1;
		}
		if (xyDataSeries.getAutoSort()==true) {
			xyDataSeries.sort();
		}
		// --- Do some initial work on the data series ----
		TableModelDataVector tmdv = new TableModelDataVector(XyDataSeries.class.getSimpleName(), true, keyColumnIndex);
		
		// --- Run through the list of value pairs --------
		List valuePairs = parentDataModel.getValuePairsFromSeries(xyDataSeries); 
		for (int i=0; i<valuePairs.size(); i++) {
			
			ValuePair vp = (ValuePair) valuePairs.get(i);
			Number xValue = parentDataModel.getXValueFromPair(vp);
			Number yValue = parentDataModel.getYValueFromValuePair(vp);
			
			Vector<Number> newRow = new Vector<Number>();
			if (tmdv.size()==0) {
				newRow.add(1);
			} else {
				newRow.add(tmdv.size()+1);
			}
			newRow.add(xValue);
			newRow.add(yValue);
			
			tmdv.add(newRow);
		}
		return tmdv;
	}
	
	/**
	 * Gets the XyDataSeries from the current data.
	 * @return the XyDataSeries from data
	 */
	private XyDataSeries getXyDataSeriesFromData() {
		
		XyDataSeries xyDataSeries = null;
		if (this.tableModelDataVector!=null && this.tableModelDataVector.size()!=0) {
			
			XyDataModel model = (XyDataModel) this.parentDataModel;
			XyDataSeries xyDataSeriesCurrent = (XyDataSeries) this.getOntologyModel().getChartData().get(this.getFocusedSeriesIndex());
			// --- Get settings from current Series -------
			xyDataSeries = new XyDataSeries();
			xyDataSeries.setLabel(xyDataSeriesCurrent.getLabel());
			xyDataSeries.setAutoSort(xyDataSeriesCurrent.getAutoSort());
			xyDataSeries.setAvoidDuplicateXValues(xyDataSeriesCurrent.getAvoidDuplicateXValues());
			// --- Add data from local table model --------
			for (int i=0; i < this.tableModelDataVector.size(); i++) {
				Vector<Number> row = this.tableModelDataVector.get(i);
				XyValuePair vp = (XyValuePair) model.createNewValuePair(row.get(1), row.get(2));	
				xyDataSeries.addXyValuePairs(vp);
			}
		}
		return xyDataSeries;
	}
	
	/**
	 * Moves the current selection according to the direction.
	 * @param direction the direction to move
	 */
	public void move(JTable jTable, int direction) {
		
		if (jTable.getSelectedRow()!=-1 && direction!=0) {
			
			int selectedRowTableNew; 
			int selectedRowModelNew;
			int selectedRowTable = jTable.getSelectedRow();
			int selectedRowModel = jTable.convertRowIndexToModel(selectedRowTable);
			
			if (!((selectedRowModel==0 && direction<0) || (selectedRowModel==this.tableModelDataVector.size()-1 && direction>0))) {
				Vector<Number> rowSelected = this.tableModelDataVector.remove(selectedRowModel);
				XyValuePair xyValuePair = (XyValuePair) this.getOntologyModel().removeXyValuePair(this.getFocusedSeriesIndex(), selectedRowModel);
				
				if (direction>0) {
					// --- move down ------------
					selectedRowModelNew = selectedRowTable+1; 
					this.tableModelDataVector.add(selectedRowModelNew, rowSelected);
					this.getOntologyModel().addXyValuePair(this.getFocusedSeriesIndex(), selectedRowModelNew, xyValuePair);
					this.getChartModel().setXYSeriesAccordingToOntologyModel(this.getFocusedSeriesIndex());
					fireTableRowsInserted(selectedRowModel, selectedRowModelNew);
					
				} else {
					// --- move up --------------
					selectedRowModelNew = selectedRowTable-1; 
					this.tableModelDataVector.add(selectedRowModelNew, rowSelected);
					this.getOntologyModel().addXyValuePair(this.getFocusedSeriesIndex(), selectedRowModelNew, xyValuePair);
					this.getChartModel().setXYSeriesAccordingToOntologyModel(this.getFocusedSeriesIndex());
					fireTableRowsInserted(selectedRowModelNew, selectedRowModel);
				}
				
				selectedRowTableNew = jTable.convertRowIndexToView(selectedRowModelNew);
				if(selectedRowTableNew >= 0){
					jTable.setRowSelectionInterval(selectedRowTableNew, selectedRowTableNew);
					jTable.changeSelection(selectedRowTableNew, 0, false, false);
				}
			}
		}
		
	}

}
