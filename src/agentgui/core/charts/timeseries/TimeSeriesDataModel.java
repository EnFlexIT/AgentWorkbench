package agentgui.core.charts.timeseries;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeriesCollection;

import agentgui.core.application.Language;
import agentgui.core.charts.ChartDataModel;
import agentgui.ontology.NumericDataColumn;
import agentgui.ontology.NumericDataTable;
import agentgui.ontology.TimeSeries;
/**
 * The data model for a time series chart.
 * This class encapsulates time series data. It provides different representations:
 * - An ontology model
 * - A JFreeChart TimeSeriesCollection
 * - A DefaultTableModel
 * 
 * @author Nils
 *
 */
public class TimeSeriesDataModel extends ChartDataModel{
	private DefaultTableModel tableModel = null;
	private TimeSeriesCollection chartModel = null;
	private TimeSeries ontologyModel = null;
	
	private boolean sumSeriesEnabled = false;
	private int sumSeriesIndex = -1;

	/**
	 * 
	 */
	private static final String DEFAULT_TIME_AXIS_LABEL = "Time";
	private static final String DEFAULT_VALUE_AXIS_LABEL = "Values";
	private static final String DEFAULT_CHART_TITLE = "Time Series Chart";
	private static final String DEFAULT_AGGREGATED_SERIES_LABEL = Language.translate("Summe");
	
	
	Date startDate = null;
	
	private String rendererType = "XYStepRenderer"; 
	/**
	 * Default colors for newly added series 
	 */
	static final Color[] DEFAULT_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW};
	static final float DEFAULT_LINE_WIDTH = 1.0f;
	
	public TimeSeriesDataModel(TimeSeries ts){
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 0, 1, 0, 0, 0);
		this.startDate = cal.getTime();
		
		if(ts == null || ts.getValueAxisDescriptions().get(0).equals("")){
			// No or empty model passed, create new one
			this.ontologyModel = new TimeSeries();
			this.ontologyModel.setChartTitle(DEFAULT_CHART_TITLE);
			this.ontologyModel.setTimeAxisLabel(DEFAULT_TIME_AXIS_LABEL);
			this.ontologyModel.setValueAxisLabel(DEFAULT_VALUE_AXIS_LABEL);
			this.tableModel = new TimeSeriesTableModel(this);
			this.chartModel = new TimeSeriesCollection();
			
		}else{
			// Use ontology model from parameter
			this.ontologyModel = ts;
			// Create chart and table model from the ontology model
			buildTableModelFromOntologyModel();
			buildChartModelFromOntologyModel();
			this.numberOfSeries = ts.getValueAxisDataTable().getTableData().size();
		}
	}
	
	/**
	 * This method adds a new time series to the models
	 * @param name The time series' label
	 * @param xValues The series' timestamps
	 * @param yValues The series' values
	 */
	public void addDataSeries(String name, ArrayList xValues, ArrayList yValues){
		
		// Remove aggregated series, if present
		if(sumSeriesIndex >= 0){
			hideSumSeries();
		}
		
		// Transfer x values to timestamps related to the start date
		ArrayList xTimestamps = new ArrayList();
		Iterator xIter = xValues.iterator();
		while(xIter.hasNext()){
			long xValStamp = ((Float) xIter.next()).longValue() * 60000;	// Minutes -> Milliseconds
			xTimestamps.add(new Float(startDate.getTime() + xValStamp));	// Relative to startDate
		}
		
		// Add the series
		super.addDataSeries(name, xTimestamps, yValues);
		
		// If more than one series, calculate the aggregated values and add them to the chart
		if(numberOfSeries > 1 && sumSeriesEnabled){
			showSumSeries();
		}
		
		
	}
	
	/**
	 * This method adds a new time series to the ontologyModel
	 * @param name The time series name
	 * @param xValues The timestamps (long values, encoded as float for ontology compatibility)
	 * @param yValues The values
	 */
	@Override
	protected void addDataSeries2ontologyModel(String name, List xValues, List yValues){
		
		// Add name, line width and color to the ontologyModel
		ontologyModel.getValueAxisDescriptions().add(name);
		ontologyModel.getValueAxisLineWidth().add(new Float(DEFAULT_LINE_WIDTH));
		ontologyModel.getValueAxisColors().add("" + DEFAULT_COLORS[numberOfSeries % DEFAULT_COLORS.length].getRGB());
		
		if(ontologyModel.getTimeAxis().size() == 0){
			
			// Empty model => no problems
			ontologyModel.setTimeAxis(xValues);
			if(ontologyModel.getValueAxisDataTable() == null){
				ontologyModel.setValueAxisDataTable(new NumericDataTable());
			}
			NumericDataColumn yVals = new NumericDataColumn();
			yVals.setColumnData(yValues);
			ontologyModel.getValueAxisDataTable().addTableData(yVals);
			
		}else{
			
			// Non-empty model => problems
			int currentIndex = 0;
			int modelItemNum = ontologyModel.getTimeAxis().size();
			int importItemNum = xValues.size();
			
			List modelTimestamps = ontologyModel.getTimeAxis();
			
			// Iterate over both timestamp lists till the end of the shorter one is reached
			while(currentIndex < modelItemNum && currentIndex < importItemNum){
				
				long modelTimestamp = ((Float)modelTimestamps.get(currentIndex)).longValue();
				long importTimestamp = ((Float)xValues.get(currentIndex)).longValue();
				
				// Timestamps differ => there is no value for this time in the model or in the import, insert the last one (no change)  
				if(modelTimestamp != importTimestamp){
					if(modelTimestamp < importTimestamp){
						// The value is missing in the import
						if(currentIndex > 0){
							xValues.add(currentIndex, new Float(modelTimestamp));
							float lastVal = (Float) yValues.get(currentIndex-1);
							yValues.add(currentIndex, new Float(lastVal));
						}else{
							// No last one, insert 0
							xValues.add(currentIndex, new Float(modelTimestamp));
							yValues.add(currentIndex, new Float(0));
						}
						importItemNum++;	// The import vector grew 1 longer 
					}else if(importTimestamp < modelTimestamp){
						// The value is missing in the model
						modelTimestamps.add(currentIndex, new Float(importTimestamp));
						Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
						// Iterate over columns
						while(columns.hasNext()){
							NumericDataColumn column = (NumericDataColumn) columns.next();
							if(currentIndex > 0){
								// Insert last value
								float lastVal = (Float) column.getColumnData().get(currentIndex-1);
								column.getColumnData().add(currentIndex, new Float(lastVal));
							}else{
								// No last value, insert 0
								column.getColumnData().add(currentIndex, new Float(0));
							}
						}
						modelItemNum++;		// The model grew 1 longer
					}
				}
				currentIndex++;
			}
			// Now the end of the shorter one is reached
			
			if(importItemNum < modelItemNum){
				// The ontology model is longer, fill the rest of the import vector
				float lastVal = (Float) yValues.get(importItemNum-1);
				while(currentIndex < modelItemNum){
					long modelTimestamp = ((Float)modelTimestamps.get(currentIndex)).longValue();
					xValues.add(new Float(modelTimestamp));
					yValues.add(new Float(lastVal));
					currentIndex++;
				}
			}else if(modelItemNum < importItemNum){
				// The import vector is longer, fill the rest of the ontology model
				while(currentIndex < importItemNum){
					long importTimestamp = ((Float)xValues.get(currentIndex)).longValue();
					modelTimestamps.add(currentIndex, new Float(importTimestamp));
					
					// Iterate over the columns and insert the last value
					Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
					while(columns.hasNext()){
						NumericDataColumn column = (NumericDataColumn) columns.next();
						float lastVal = (Float) column.getColumnData().get(importItemNum-1);
						column.getColumnData().add(currentIndex, new Float(lastVal));
					}
					currentIndex++;
				}
			}
			NumericDataColumn yVals = new NumericDataColumn();
			yVals.setColumnData(yValues);
			ontologyModel.getValueAxisDataTable().addTableData(yVals);
		}
	}
	
	/**
	 * This method creates a new table model based on the ontologyModel. 
	 */
	@Override
	protected void buildTableModelFromOntologyModel(){
		DefaultTableModel newTableModel = new TimeSeriesTableModel(this);
		newTableModel.addColumn("Zeit", ontologyModel.getTimeAxis().toArray());
		Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
		
		int colCount = 0;
		while(columns.hasNext()){
			NumericDataColumn columnData = (NumericDataColumn) columns.next();
			newTableModel.addColumn(ontologyModel.getValueAxisDescriptions().get(colCount++), columnData.getColumnData().toArray());
		}
		tableModel = newTableModel;
		tableModel.addTableModelListener(this);
	}
	
	/**
	 * This method creates a new TimeSeriesCollection based on the ontologyModel- 
	 */
	@Override
	protected void buildChartModelFromOntologyModel(){
		TimeSeriesCollection newChartModel = new TimeSeriesCollection();
		Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
		
		int colCount = 0;
		while(columns.hasNext()){
			org.jfree.data.time.TimeSeries newTs = new org.jfree.data.time.TimeSeries(ontologyModel.getValueAxisDescriptions().get(colCount++).toString());
			Iterator xVals = ontologyModel.getAllTimeAxis();
			Iterator yVals = ((NumericDataColumn)columns.next()).getAllColumnData();
			while(xVals.hasNext() && yVals.hasNext()){
				newTs.add(new FixedMillisecond(((Float)xVals.next()).longValue()), ((Float)yVals.next()).longValue());
			}
			newChartModel.addSeries(newTs);
		}
		chartModel = newChartModel;
	}
	
	/**
	 * This method adds a new time series to the TimeSeriesCollection
	 * @param name The series label
	 * @param timestamps The series timestamps
	 * @param values The series values
	 */
	@Override
	protected void addDataSeries2chartModel(String name, List timestamps, List values){
		org.jfree.data.time.TimeSeries newTs = new org.jfree.data.time.TimeSeries(name);
		Iterator xVals = timestamps.iterator();
		Iterator yVals = values.iterator();
		while(xVals.hasNext() && yVals.hasNext()){
			newTs.addOrUpdate(new FixedMillisecond(((Float)xVals.next()).longValue()), ((Float)yVals.next()).longValue());
		}
		chartModel.addSeries(newTs);
	}
	
	/**
	 * This method rebuilds ontologyModel and chartModel after the tableModel has been edited.
	 */
	private void rebuildFromTable(){
		// Remove old data
		ontologyModel.clearAllTimeAxis();
		ontologyModel.getValueAxisDataTable().clearAllTableData();
		chartModel.removeAllSeries();
		numberOfSeries = 0;
		
		/*
		 * The DefaultTableModel is row based, but the two others need column based
		 * data. So the data has to be re-arranged column-wise before adding.  
		 */
		ArrayList tableDataByColumn = new ArrayList();

		// Add columns
		for(int col=0; col<tableModel.getColumnCount(); col++){
			tableDataByColumn.add(new NumericDataColumn());
		}
		
		// Insert data
		for(int row=0; row<tableModel.getRowCount(); row++){
			for(int col=0; col<tableModel.getColumnCount(); col++){
				((NumericDataColumn) tableDataByColumn.get(col)).addColumnData((Float) tableModel.getValueAt(row, col));
			}
		}
		
		// Add the data to the ontologyModel
		ontologyModel.setTimeAxis(((NumericDataColumn) tableDataByColumn.get(0)).getColumnData());
		for(int col=1; col<tableDataByColumn.size(); col++){
			ontologyModel.getValueAxisDataTable().addTableData((NumericDataColumn) tableDataByColumn.get(col));
			numberOfSeries++;
		}
		
		// Add the data to the chartModel
		for(int i=0; i < numberOfSeries; i++){
			addDataSeries2chartModel(
					ontologyModel.getValueAxisDescriptions().get(i).toString(), 
					ontologyModel.getTimeAxis(), 
					((NumericDataColumn)ontologyModel.getValueAxisDataTable().getTableData().get(i)).getColumnData());
		}
	}
	
	private List calculateSumSeries(){
		List totalValues = new ArrayList();
		int colNum = ontologyModel.getValueAxisDataTable().getTableData().size();
		int rowNum = ((NumericDataColumn)ontologyModel.getValueAxisDataTable().getTableData().get(0)).getColumnData().size();
		for(int i=0; i<rowNum; i++){
			float rowSum = 0;
			for(int j=0; j<colNum; j++){
				NumericDataColumn column = (NumericDataColumn) ontologyModel.getValueAxisDataTable().getTableData().get(j);
				rowSum += (Float)column.getColumnData().get(i); 
			}
			totalValues.add(new Float(rowSum));
		}
		
		return totalValues;
	};

	/**
	 * @return the tableModel
	 */
	@Override
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @return the chartModel
	 */
	@Override
	public TimeSeriesCollection getChartModel() {
		return chartModel;
	}

	/**
	 * @return the ontologyModel
	 */
	@Override
	public TimeSeries getOntologyModel() {
		return ontologyModel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return ontologyModel.getChartTitle();
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		ontologyModel.setChartTitle(title);
	}

	/**
	 * @return the xAxisLabel
	 */
	public String getxAxisLabel() {
		return ontologyModel.getTimeAxisLabel();
	}

	/**
	 * @param xAxisLabel the xAxisLabel to set
	 */
	public void setxAxisLabel(String xAxisLabel) {
		ontologyModel.setTimeAxisLabel(xAxisLabel);
	}

	/**
	 * @return the yAxisLabel
	 */
	public String getyAxisLabel() {
		return ontologyModel.getValueAxisLabel();
	}

	/**
	 * @param yAxisLabel the yAxisLabel to set
	 */
	public void setyAxisLabel(String yAxisLabel) {
		ontologyModel.setValueAxisLabel(yAxisLabel);
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * This method has to be called after all settings changes have been made to notify the observers.
	 * To avoid unnecessary notifications, this is  not done when a model change occurs but has to be 
	 * triggered separately when all changes have been made.  
	 */
	public void triggerNotifySettingsChanged(){
		setChanged();
		notifyObservers(new Integer(SETTINGS_CHANGED));
	}

	/**
	 * @return the rendererType
	 */
	public String getRendererType() {
		return rendererType;
	}

	/**
	 * @param rendererType the rendererType to set
	 */
	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getSource() == tableModel){
			rebuildFromTable();
			if(sumSeriesEnabled){
				// TODO eleganter lösen
				hideSumSeries();
				showSumSeries();
			}
		}
	}


	/**
	 * @return the sumSeriesEnabled
	 */
	public boolean isSumSeriesEnabled() {
		return sumSeriesEnabled;
	}

	/**
	 * @param sumSeriesEnabled the sumSeriesEnabled to set
	 */
	public void setSumSeriesEnabled(boolean sumSeriesEnabled) {
		this.sumSeriesEnabled = sumSeriesEnabled;
		if(sumSeriesEnabled){
			showSumSeries();
		}else{
			hideSumSeries();
		}
	}

	/**
	 * @return the sumSeriesIndex
	 */
	public int getSumSeriesIndex() {
		return sumSeriesIndex;
	}
	
	void showSumSeries(){
		if(numberOfSeries > 1){
			List sumSeries = calculateSumSeries();
			addDataSeries2ontologyModel(DEFAULT_AGGREGATED_SERIES_LABEL, ontologyModel.getTimeAxis(), sumSeries);
			addDataSeries2chartModel(DEFAULT_AGGREGATED_SERIES_LABEL, ontologyModel.getTimeAxis(), sumSeries);
			tableModel.removeTableModelListener(this);
			tableModel.addColumn(DEFAULT_AGGREGATED_SERIES_LABEL, sumSeries.toArray());
			tableModel.addTableModelListener(this);
			sumSeriesIndex = numberOfSeries++;
			
			setChanged();
			notifyObservers(ChartDataModel.DATA_ADDED);
		}
	}
	
	void hideSumSeries(){
		if(sumSeriesIndex >= 0){
			ontologyModel.getValueAxisDataTable().getTableData().remove(sumSeriesIndex);
			ontologyModel.getValueAxisColors().remove(sumSeriesIndex);
			ontologyModel.getValueAxisDescriptions().remove(sumSeriesIndex);
			chartModel.removeSeries(sumSeriesIndex);
			sumSeriesIndex = -1;
			numberOfSeries--;
			buildTableModelFromOntologyModel();
			
			setChanged();
			notifyObservers(ChartDataModel.DATA_ADDED);
		}
	}
}
