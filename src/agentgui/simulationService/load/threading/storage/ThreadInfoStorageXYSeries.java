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
package agentgui.simulationService.load.threading.storage;

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
