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

import java.util.TreeMap;

import agentgui.ontology.Simple_Float;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

/**
 * The Class XySeriesHelper provides some methods to work with {@link XyDataSeries}.
 */
public class XySeriesHelper extends TreeMap<Float, XyValuePair>{

	private static final long serialVersionUID = 5313715263015405675L;

	private XyDataSeries xySeries = null;
	
	
	/**
	 * Instantiates a new XySeriesHelper.
	 */
	public XySeriesHelper() {	}
	/**
	 * Instantiates a new XySeriesHelper.
	 * @param xySeries the current XyDataSeries
	 */
	public XySeriesHelper(XyDataSeries xySeries) {
		this.setXySeries(xySeries);	
	}
	
	/**
	 * Sets the current XyDataSeries.
	 * @param xySeries the new XyDataSeries
	 */
	public void setXySeries(XyDataSeries xySeries) {
		this.xySeries = xySeries;
		this.putToTreeMap();
	}
	/**
	 * Gets the current XyDataSeries.
	 * @return the XyDataSeries
	 */
	public XyDataSeries getXySeries() {
		if (this.xySeries==null) {
			this.xySeries = new XyDataSeries();
			this.xySeries.setLabel("New XY-Data Seriues");
		}
		return xySeries;
	}
	/**
	 * Returns a copy of the current XyDataSeries.
	 * @return the time series copy
	 */
	public XyDataSeries getXySeriesCopy() {
		XyDataSeries copy = null;
		if (this.xySeries!=null) {
			
			copy = new XyDataSeries();
			copy.setLabel(this.xySeries.getLabel() + " (Copy)");
			copy.setUnit(this.xySeries.getUnit());

			for (int i = 0; i < this.xySeries.getXyValuePairs().size(); i++) {
				XyValuePair xyVpOld = (XyValuePair) this.xySeries.getXyValuePairs().get(i);
				
				Simple_Float xValue = new Simple_Float();
				xValue.setFloatValue(xyVpOld.getXValue().getFloatValue());
				Simple_Float yValue = new Simple_Float();
				yValue.setFloatValue(xyVpOld.getYValue().getFloatValue());
				
				XyValuePair xyVpNew = new XyValuePair();
				xyVpNew.setXValue(xValue);
				xyVpNew.setYValue(yValue);
				
				copy.addXyValuePairs(xyVpNew);
			}
		}
		return copy;
	}

	/**
	 * Puts the current XyDataSeries into the local TreeMap.
	 */
	private void putToTreeMap() {
		this.clear();
		List valuePairs = this.xySeries.getXyValuePairs();
		for (int i = 0; i < valuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) valuePairs.get(i);
			Float xyStamp = xyVp.getXValue().getFloatValue();
			this.put(xyStamp, xyVp);
		}
	}
	
	/**
	 * Resets the current XyDataSeries from local TreeMap.
	 */
	private void resetXySeriesFromTreeMap() {
		
		// --- Clear current XyDataSeries ---------
		this.getXySeries().getXyValuePairs().clear();
		
		// --- rebuild  XyDataSeries --------------
		Long[] keys = new Long[this.size()];
		this.keySet().toArray(keys);
		for (int i = 0; i < keys.length; i++) {
			this.getXySeries().getXyValuePairs().add(this.get(keys[i]));
		}
		
	}
	
	/**
	 * Adds new series data to the current XyDataSeries, if the concrete data is new.
	 * If the data (xy stamp) is already there, it will not be overwritten.
	 *
	 * @param additionalXySeries the XySereis containing new data
	 * @return the xy series containing the value pairs that were added
	 */
	public XyDataSeries addSeriesData(XyDataSeries additionalXySeries) {
		return this.addSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Adds new series data to the current XyDataSeries, if the concrete data is new.
	 * If the data (xy stamp) is already there, it will not be overwritten.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 */
	public XyDataSeries addSeriesData(List listOfXySeriesValuePairs) {
		XyDataSeries addedValuePairs = new XyDataSeries();
		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xyStamp = xyVp.getXValue().getFloatValue();
			if (this.containsKey(xyStamp)==false) {
				this.getXySeries().getXyValuePairs().add(xyVp);
				this.put(xyStamp, xyVp);
				addedValuePairs.addXyValuePairs(xyVp);
			}
		}
		if (addedValuePairs.getXyValuePairs().size()==0) {
			addedValuePairs=null;
		}
		return addedValuePairs;
	}
	
	/**
	 * Adds the or exchanges series data with the data of the specified XyDataSeries.
	 * @param additionalXySeries the additional xy series
	 */
	public void addOrExchangeSeriesData(XyDataSeries additionalXySeries) {
		this.addOrExchangeSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Adds the or exchanges series data with the specified .
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 */
	public void addOrExchangeSeriesData(List listOfXySeriesValuePairs) {
		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xyStamp = xyVp.getXValue().getFloatValue();
			this.put(xyStamp, xyVp);
		}
		this.resetXySeriesFromTreeMap();
	}

	/**
	 * Exchanges series data, if the concrete xy stamps are available.
	 * @param additionalXySeries the additional xy series
	 * @return the xy series of value pairs that were exchanged
	 */
	public XyDataSeries exchangeSeriesData(XyDataSeries additionalXySeries) {
		return this.exchangeSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Exchanges series data, if the concrete xy stamps are available.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 * @return the xy series of value pairs that were exchanged
	 */
	public XyDataSeries exchangeSeriesData(List listOfXySeriesValuePairs) {
		XyDataSeries exchangedXySeries = new XyDataSeries();
		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xyStamp = xyVp.getXValue().getFloatValue();
			if (this.containsKey(xyStamp)) {
				this.put(xyStamp, xyVp);
				exchangedXySeries.addXyValuePairs(xyVp);
			}
		}
		this.resetXySeriesFromTreeMap();
		if (exchangedXySeries.getXyValuePairs().size()==0) {
			exchangedXySeries = null;
		}
		return exchangedXySeries;
	}
	
	/**
	 * Removes the series data specified by the xy stamps.
	 * @param additionalXySeries the additional xy series
	 */
	public void removeSeriesData(XyDataSeries additionalXySeries) {
		this.removeSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Removes the series data specified by the xy stamps.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 */
	public void removeSeriesData(List listOfXySeriesValuePairs) {
		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xyStamp = xyVp.getXValue().getFloatValue();
			this.remove(xyStamp);
		}
		this.resetXySeriesFromTreeMap();
	}
	

}
