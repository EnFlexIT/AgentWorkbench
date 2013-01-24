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
package agentgui.core.charts.timeseriesChart;

import java.util.TimeZone;

import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * Data model for the JFreeChart representation of a time series
 * @author Nils
 *
 */
public class TimeSeriesChartModel extends TimeSeriesCollection implements ChartModel{
	
	public TimeSeriesChartModel(){
		super(TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 2315036788757231999L;
	
	public void addSeries(agentgui.ontology.DataSeries series){
		org.jfree.data.time.TimeSeries newSeries = new org.jfree.data.time.TimeSeries(series.getLabel());
		
		jade.util.leap.Iterator valuePairs = ((agentgui.ontology.TimeSeries)series).getAllTimeSeriesValuePairs();
		while(valuePairs.hasNext()){
			TimeSeriesValuePair valuePair = (TimeSeriesValuePair) valuePairs.next();
			TimeSeriesDataItem newItem = new TimeSeriesDataItem(new FixedMillisecond(valuePair.getTimestamp().getLongValue()), valuePair.getValue().getFloatValue());
			newSeries.add(newItem);
		}
		
		this.addSeries(newSeries);
	}
	
	@Override
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value)
			throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = this.getSeries(seriesIndex);
			series.addOrUpdate(new FixedMillisecond(key.longValue()), value.floatValue());
		}else{
			throw new NoSuchSeriesException();
		}
		
	}


	@Override
	public void updateKey(Number oldKey, Number newKey) {
		@SuppressWarnings("unchecked")
		java.util.Iterator<org.jfree.data.time.TimeSeries> allSeries = getSeries().iterator();
		
		// Iterate over all series
		while(allSeries.hasNext()){
			org.jfree.data.time.TimeSeries series = allSeries.next();
			
			// Try to find a value pair with the old time stamp
			TimeSeriesDataItem oldValuePair = series.getDataItem(new FixedMillisecond(oldKey.longValue()));
			
			// If found, remove it and add a new one with the new time stamp and the old value
			if(oldValuePair != null){
				
				series.delete(new FixedMillisecond(oldKey.longValue()));
				series.addOrUpdate(new FixedMillisecond(newKey.longValue()), oldValuePair.getValue());
				
			}
		}
		
	}


	@Override
	public void removeValuePair(int seriesIndex, Number key)
			throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			org.jfree.data.time.TimeSeries series = (TimeSeries) getSeries().get(seriesIndex);
			series.delete(new FixedMillisecond(key.longValue()));
		}else{
			throw new NoSuchSeriesException();
		}
		
	}

}
