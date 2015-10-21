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

import java.awt.Paint;
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
	/** The line color map contains color information for a XYSeries */
	private HashMap<String, Paint> xySeriesLineColorMap;

	/**
	 * Instantiates a new thread info storage series.
	 * @param name the name
	 */
	public ThreadInfoStorageSeries(String name) {
		this.name = name;
		this.isSelected = false;
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
			return  Math.abs(to -from);
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
         return name;
    }

	/**
	 * @return the xySeriesMap
	 */
	public HashMap<String, XYSeries> getXYSeriesMap() {
		if(xySeriesMap == null){
			this.xySeriesMap = new HashMap<String, XYSeries>();
		}
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

	/**
	 * @return the xySeriesLineColorMap
	 */
	public HashMap<String, Paint> getXySeriesLineColorMap() {
		if(xySeriesLineColorMap == null){
			this.xySeriesLineColorMap = new HashMap<String, Paint>();
		}
		return xySeriesLineColorMap;
	}
}
