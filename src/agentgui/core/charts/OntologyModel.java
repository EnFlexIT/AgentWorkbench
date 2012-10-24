package agentgui.core.charts;

import jade.util.leap.List;
import agentgui.ontology.Chart;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

public abstract class OntologyModel {
	protected Chart chart;
	
	protected DataModel parent;
	
	/**
	 * Gets the list of value pairs for the data series with the specified index 
	 * @param seriesIndex The series index
	 * @return The list of value pairs
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public abstract List getSeriesData(int seriesIndex) throws NoSuchSeriesException;
	
	public abstract void addSeries(DataSeries series);
	
	public abstract void removeSeries(int seriesIndex) throws NoSuchSeriesException;
	
	public abstract int getSeriesCount();
	
	public abstract List getChartData();
	
	public abstract DataSeries getSeries(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Gets the chart settings for this chart
	 * @return
	 */
	public ChartSettingsGeneral getChartSettings(){
		return chart.getVisualizationSettings();
	}
	/**
	 * Gets the index of the value pair with the given key of the series with the given index.
	 * @param seriesIndex The series index
	 * @param key The key
	 * @return The value pair's index
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	protected int getIndexByKey(int seriesIndex, Number key) throws NoSuchSeriesException{
		
		List seriesData = getSeriesData(seriesIndex);
		
		for(int i=0; i<seriesData.size(); i++){
			ValuePair valuePair = (ValuePair) seriesData.get(i);
			if(parent.getKeyFromPair(valuePair).doubleValue() == key.doubleValue()){
				return i;
			}
		}
		return -1;
	}
	
	public void removeValuePair(int seriesIndex, Number key) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			List seriesData = getSeriesData(seriesIndex);
			int index = getIndexByKey(seriesIndex, key);
			if(index >= 0){
				seriesData.remove(index);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			List seriesData = getSeriesData(seriesIndex);
			int valueIndex = getIndexByKey(seriesIndex, key);
			if(valueIndex >= 0){
				ValuePair valuePairToChange = (ValuePair) seriesData.get(valueIndex);
				parent.setValueForPair(value, valuePairToChange);
			}else{
				ValuePair newValuePair = parent.createNewValuePair(key, value);
				seriesData.add(newValuePair);
			}
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Exchanges one key in all series that contain it
	 * @param oldKey The old key
	 * @param newKey The new key
	 */
	public void updateKey(Number oldKey, Number newKey){
		
		for(int i=0; i<getSeriesCount(); i++){
			try {
				
				int vpIndex = getIndexByKey(i, oldKey); 
				if( vpIndex >= 0){
					// There is a pair with this key
					List seriesData = getSeriesData(i);
					ValuePair vp2update = (ValuePair) seriesData.get(vpIndex);
					parent.setKeyForPair(newKey, vp2update);
				}
				
				
			} catch (NoSuchSeriesException e) {
				// Should never happen, as i cannot be > number of series
				System.err.println("Error accessing series "+i);
				e.printStackTrace();
			}
		}
	}
}
