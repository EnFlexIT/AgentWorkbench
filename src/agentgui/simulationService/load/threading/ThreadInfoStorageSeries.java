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
package agentgui.simulationService.load.threading;

import java.util.HashMap;

import org.jfree.data.xy.XYSeries;

/**
 * Basic class for storing Thread-Load data series
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageSeries {
	
	protected String name;
	private boolean isSelected;
	public final String DELIMITER   = "<>";
	
	/** The xy series hashmap that Contains multiple data-series  */
	private HashMap<String, XYSeries> xySeriesMap;

	public ThreadInfoStorageSeries(String name) {
		this.name = name;
		this.isSelected = false;
		xySeriesMap = new HashMap<String, XYSeries>();
	}
	
	/**
	 * Sets the xy series.
	 * @param key the key
	 * @param series the series
	 */
	public void setXYSeries(String key, XYSeries series){
		this.xySeriesMap.put(key,series);
	}
	
	/**
	 * Gets the XY series.
	 * @param key the key
	 * @return the XY series
	 */
	public XYSeries getXYSeries(String key){
		return this.xySeriesMap.get(key);
	}
	
	/**
	 * Gets the last delta for xy series.
	 * @param series the series
	 * @return the last delta for xy series
	 */
	public double getLastDeltaForXYSeries(XYSeries series) {
		double from, to;
		int itemIndex = series.getItemCount()-1;
		
		if(itemIndex >=1){
			from = (Double) series.getDataItem(itemIndex-1).getY();
			to = (Double) series.getDataItem(itemIndex).getY();
			return  (to -from);
		}
		return 0;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
    public String toString(){
         return this.name;
    }

	/**
	 * @return the xySeriesMap
	 */
	public HashMap<String, XYSeries> getXYSeriesMap() {
		return xySeriesMap;
	}

	/**
	 * @param xySeriesMap the xySeriesMap to set
	 */
	public void setXYSeriesMap(HashMap<String, XYSeries> xySeriesMap) {
		this.xySeriesMap = xySeriesMap;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
