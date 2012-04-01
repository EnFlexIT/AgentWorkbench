package agentgui.core.charts.timeseries;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeriesCollection;

import agentgui.ontology.NumericDataColum;
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
public class TimeSeriesDataModel extends Observable{
	private DefaultTableModel tableModel = null;
	private TimeSeriesCollection chartModel = null;
	private TimeSeries ontologyModel = null;

	/**
	 * Event ID for observers. This event is triggered when a new series was added to the model. 
	 */
	public static final int TIME_SERIES_ADDED = 1;
	/**
	 * Event ID for observers. This event is triggered when the model settings have been changed.
	 */
	public static final int SETTINGS_CHANGED = 2;
	
	/**
	 * 
	 */
	private static final String DEFAULT_TIME_AXIS_LABEL = "Time";
	private static final String DEFAULT_VALUE_AXIS_LABEL = "Values";
	private static final String DEFAULT_CHART_TITLE = "Time Series Chart";
	/**
	 * Default name for newly added series
	 */
	private static final String DEFAULT_SERIES_NAME = "Data";
	
	private Date startDate = null;
	
	private int numberOfSeries = 0;
	
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
			this.tableModel = new DefaultTableModel();
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
	 * This method imports one time series from a CSV file
	 * @param csvFile The CSV file
	 */
	public void importTimeSeriesFromCSV(File csvFile){
		if(csvFile.exists()){
			
			// Read the CSV data
			BufferedReader csvFileReader;
			System.out.println("Importing CSV data from "+csvFile.getName());
			
			ArrayList timestamps = new ArrayList();
			ArrayList values = new ArrayList();
			
			try {
				
				// Read the data from the file
				csvFileReader = new BufferedReader(new FileReader(csvFile));
				String inBuffer = null;
				
				while((inBuffer = csvFileReader.readLine()) != null){
					// Regex matches two numbers (with or without decimals) separated by a ;
					if(inBuffer.matches("[\\d]+\\.?[\\d]*;[\\d]+\\.?[\\d]*")){
						String[] parts = inBuffer.split(";");
						
						timestamps.add(startDate.getTime() + Float.parseFloat(parts[0])*60000);
						values.add(Float.parseFloat(parts[1]));
					}
				}
			} catch (FileNotFoundException e) {
				// Will not happen, as file existence is checked right before creating the reader  
				System.err.println("Error opening CSV file");
			} catch (IOException e) {
				System.err.println("Error reading CSV data");
			}
			
			// Add the new series to the model
			addTimeSeries(null, timestamps, values);
			
			// Notify observers
			setChanged();
			notifyObservers(new Integer(TIME_SERIES_ADDED));
		}
		
		
	}
	
	/**
	 * This method adds a new time series to the models
	 * @param name The time series' label
	 * @param timestamps The series' timestamps
	 * @param values The series' values
	 */
	public void addTimeSeries(String name, ArrayList timestamps, ArrayList values){
		
		
		
		// If no name is specified, use default name + number
		String newTsName;
		if(name != null && name.length() > 0){
			newTsName = name;
		}else{
			newTsName = DEFAULT_SERIES_NAME + numberOfSeries;
		}
		
		// Add name, line width and color to the ontologyModel
		ontologyModel.getValueAxisDescriptions().add(newTsName);
		ontologyModel.getValueAxisLineWidth().add(new Float(DEFAULT_LINE_WIDTH));
		ontologyModel.getValueAxisColors().add("" + DEFAULT_COLORS[numberOfSeries % DEFAULT_COLORS.length].getRGB());
		
		// Add series data to the three sub-models
		addTimeSeries2ontologyModel(newTsName, timestamps, values);
		buildTableModelFromOntologyModel();	// A complete rebuild is much easier than adding a series to the existing model 
		addTimeSeries2chartModel(newTsName, timestamps, values);
		
		// Some final administrative stuff
		numberOfSeries++;
		setChanged();
		notifyObservers(new Integer(TIME_SERIES_ADDED));
	}
	
	/**
	 * This method adds a new time series to the ontologyModel
	 * @param name The time series name
	 * @param importTimestamps The timestamps (long values, encoded as float for ontology compatibility)
	 * @param importValues The values
	 */
	private void addTimeSeries2ontologyModel(String name, ArrayList importTimestamps, ArrayList importValues){
		
		if(ontologyModel.getTimeAxis().size() == 0){
			
			// Empty model => no problems
			ontologyModel.setTimeAxis(importTimestamps);
			if(ontologyModel.getValueAxisDataTable() == null){
				ontologyModel.setValueAxisDataTable(new NumericDataTable());
			}
			NumericDataColum yVals = new NumericDataColum();
			yVals.setColumnData(importValues);
			ontologyModel.getValueAxisDataTable().addTableData(yVals);
			
		}else{
			
			// Non-empty model => problems
			int currentIndex = 0;
			int modelItemNum = ontologyModel.getTimeAxis().size();
			int importItemNum = importTimestamps.size();
			
			List modelTimestamps = ontologyModel.getTimeAxis();
			
			// Iterate over both timestamp lists till the end of the shorter one is reached
			while(currentIndex < modelItemNum && currentIndex < importItemNum){
				
				long modelTimestamp = ((Float)modelTimestamps.get(currentIndex)).longValue();
				long importTimestamp = ((Float)importTimestamps.get(currentIndex)).longValue();
				
				// Timestamps differ => there is no value for this time in the model or in the import, insert the last one (no change)  
				if(modelTimestamp != importTimestamp){
					if(modelTimestamp < importTimestamp){
						// The value is missing in the import
						if(currentIndex > 0){
							importTimestamps.add(currentIndex, new Float(modelTimestamp));
							float lastVal = (Float) importValues.get(currentIndex-1);
							importValues.add(currentIndex, new Float(lastVal));
						}else{
							// No last one, insert 0
							importTimestamps.add(currentIndex, new Float(modelTimestamp));
							importValues.add(currentIndex, new Float(0));
						}
						importItemNum++;	// The import vector grew 1 longer 
					}else if(importTimestamp < modelTimestamp){
						// The value is missing in the model
						modelTimestamps.add(currentIndex, new Float(importTimestamp));
						Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
						// Iterate over columns
						while(columns.hasNext()){
							NumericDataColum column = (NumericDataColum) columns.next();
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
				float lastVal = (Float) importValues.get(importItemNum-1);
				while(currentIndex < modelItemNum){
					long modelTimestamp = ((Float)modelTimestamps.get(currentIndex)).longValue();
					importTimestamps.add(new Float(modelTimestamp));
					importValues.add(new Float(lastVal));
					currentIndex++;
				}
			}else if(modelItemNum < importItemNum){
				// The import vector is longer, fill the rest of the ontology model
				while(currentIndex < importItemNum){
					long importTimestamp = ((Float)importTimestamps.get(currentIndex)).longValue();
					modelTimestamps.add(currentIndex, new Float(importTimestamp));
					
					// Iterate over the columns and insert the last value
					Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
					while(columns.hasNext()){
						NumericDataColum column = (NumericDataColum) columns.next();
						float lastVal = (Float) column.getColumnData().get(importItemNum-1);
						column.getColumnData().add(currentIndex, new Float(lastVal));
					}
					currentIndex++;
				}
			}
			NumericDataColum yVals = new NumericDataColum();
			yVals.setColumnData(importValues);
			ontologyModel.getValueAxisDataTable().addTableData(yVals);
		}
	}
	
	/**
	 * This method creates a new table model based on the ontologyModel. 
	 */
	private void buildTableModelFromOntologyModel(){
		DefaultTableModel newTableModel = new DefaultTableModel();
		newTableModel.addColumn("Zeit", ontologyModel.getTimeAxis().toArray());
		Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
		
		int colCount = 0;
		while(columns.hasNext()){
			NumericDataColum columnData = (NumericDataColum) columns.next();
			newTableModel.addColumn(ontologyModel.getValueAxisDescriptions().get(colCount++), columnData.getColumnData().toArray());
		}
		tableModel = newTableModel;
	}
	
	/**
	 * This method creates a new TimeSeriesCollection based on the ontologyModel- 
	 */
	private void buildChartModelFromOntologyModel(){
		TimeSeriesCollection newChartModel = new TimeSeriesCollection();
		Iterator columns = ontologyModel.getValueAxisDataTable().getAllTableData();
		
		int colCount = 0;
		while(columns.hasNext()){
			org.jfree.data.time.TimeSeries newTs = new org.jfree.data.time.TimeSeries(ontologyModel.getValueAxisDescriptions().get(colCount++).toString());
			Iterator xVals = ontologyModel.getAllTimeAxis();
			Iterator yVals = ((NumericDataColum)columns.next()).getAllColumnData();
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
	private void addTimeSeries2chartModel(String name, List timestamps, List values){
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
	public void rebuildFromTable(){
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
			tableDataByColumn.add(new NumericDataColum());
		}
		
		// Insert data
		for(int row=0; row<tableModel.getRowCount(); row++){
			for(int col=0; col<tableModel.getColumnCount(); col++){
				((NumericDataColum) tableDataByColumn.get(col)).addColumnData((Float) tableModel.getValueAt(row, col));
			}
		}
		
		// Add the data to the ontologyModel
		ontologyModel.setTimeAxis(((NumericDataColum) tableDataByColumn.get(0)).getColumnData());
		for(int col=1; col<tableDataByColumn.size(); col++){
			ontologyModel.getValueAxisDataTable().addTableData((NumericDataColum) tableDataByColumn.get(col));
			numberOfSeries++;
		}
		
		// Add the data to the chartModel
		for(int i=0; i < numberOfSeries; i++){
			addTimeSeries2chartModel(
					ontologyModel.getValueAxisDescriptions().get(i).toString(), 
					ontologyModel.getTimeAxis(), 
					((NumericDataColum)ontologyModel.getValueAxisDataTable().getTableData().get(i)).getColumnData());
		}
		
		
		
	}

	/**
	 * @return the tableModel
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @return the chartModel
	 */
	public TimeSeriesCollection getChartModel() {
		return chartModel;
	}

	/**
	 * @return the ontologyModel
	 */
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
	 * @return the numberOfSeries
	 */
	public int getNumberOfSeries() {
		return numberOfSeries;
	}

	/**
	 * @param numberOfSeries the numberOfSeries to set
	 */
	public void setNumberOfSeries(int numberOfSeries) {
		this.numberOfSeries = numberOfSeries;
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
}
