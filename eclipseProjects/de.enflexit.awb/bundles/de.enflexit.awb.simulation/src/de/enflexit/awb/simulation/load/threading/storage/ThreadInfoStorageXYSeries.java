package de.enflexit.awb.simulation.load.threading.storage;

import java.util.HashMap;

import org.jfree.data.xy.XYSeries;

/**
 * Base class for storing Thread-Load data series
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageXYSeries {
	
	/** The name. */
	private String name;
	
	/** The XY series hash map that Contains multiple data-series  */
	private HashMap<String, XYSeries> xySeriesMap;

	/**
	 * Instantiates a new thread info storage series.
	 * @param name the name
	 */
	public ThreadInfoStorageXYSeries(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the last delta for XY series.
	 * @param series the series
	 * @return the last delta for XY series
	 */
	public double getLastDeltaForXYSeries(XYSeries series) {
		double from, to;
		int itemIndex = series.getItemCount()-1;
		
		if(itemIndex >=1){
			from = (Double) series.getDataItem(itemIndex-1).getY();
			to = (Double) series.getDataItem(itemIndex).getY();
			return  Math.abs(to - from);
		}
		return 0.0d;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString(){
		/*
		 * returns the first substring of the name.
		 * example:
		 * name: agent@container@JVM@machine@cluster
		 * returns agent
		 */
		int x = name.indexOf("@");
		
		if(x != -1){
			return name.substring(0, x);
		}
		return name;
		
    }

	/**
	 * Gets the XY series map.
	 * @return the xySeriesMap
	 */
	public HashMap<String, XYSeries> getXYSeriesMap() {
		if(xySeriesMap == null){
			xySeriesMap = new HashMap<String, XYSeries>();
		}
		return xySeriesMap;
	}

	/**
	 * Sets the XY series map.
	 * @param xySeriesMap the xySeriesMap to set
	 */
	public void setXYSeriesMap(HashMap<String, XYSeries> xySeriesMap) {
		this.xySeriesMap = xySeriesMap;
	}
}
