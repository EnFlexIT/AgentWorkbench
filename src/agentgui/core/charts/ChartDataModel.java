package agentgui.core.charts;

import java.util.Observable;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jfree.data.general.Dataset;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
/**
 * Generic superclass for handling chart data
 * @author Nils
 *
 */
public abstract class ChartDataModel extends Observable implements TableModelListener{
	
	/**
	 * Event ID for observers. This event is triggered when a new data series was added to the model. 
	 */
	public static final int DATA_ADDED = 1;
	/**
	 * Event ID for observers. This event is triggered when the chart settings have been changed.
	 */
	public static final int SETTINGS_CHANGED = 2;
	
	/**
	 * Default name for newly added series
	 */
	protected static final String DEFAULT_SERIES_NAME = "Data";
	
	
	/**
	 * The Ontology representation of the data
	 */
	protected Concept ontologyModel;
	/**
	 * The JFreeChart representation of the data
	 */
	protected Dataset chartModel;
	/**
	 * The table representation of the data for editing
	 */
	protected TableModel tableModel;
	/**
	 * The number of data series in this chart
	 */
	protected int numberOfSeries = 0;
	/**
	 * @return The ontology representation of the data
	 */
	public Concept getOntologyModel() {
		return ontologyModel;
	}
	/**
	 * @return The JFreeChart representation of the data
	 */
	public Dataset getChartModel() {
		return chartModel;
	}
	/**
	 * @return The table representation of the data
	 */
	public TableModel getTableModel() {
		return tableModel;
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
	
	public void addDataSeries(String name, ArrayList xValues, ArrayList yValues){
		// If no name is specified, use default name + number
		String newTsName;
		if(name != null && name.length() > 0){
			newTsName = name;
		}else{
			newTsName = DEFAULT_SERIES_NAME + numberOfSeries;
		}
		
		// Add series data to the ontology model
		addDataSeries2ontologyModel(newTsName, xValues, yValues);
		// For the table model, a complete rebuild is much easier than adding a series to the existing model
		buildTableModelFromOntologyModel();	 
		addDataSeries2chartModel(newTsName, xValues, yValues);
		numberOfSeries++;
		
		// Notify observers
		setChanged();
		notifyObservers(new Integer(DATA_ADDED));
	}
	
	protected abstract void addDataSeries2ontologyModel(String name, List xValues, List yValues);
	protected abstract void buildTableModelFromOntologyModel();
	protected abstract void buildChartModelFromOntologyModel();
	protected abstract void addDataSeries2chartModel(String name, List timestamps, List values);
}
