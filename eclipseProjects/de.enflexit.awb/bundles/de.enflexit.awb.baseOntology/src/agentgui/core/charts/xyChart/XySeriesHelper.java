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
import java.util.Vector;

import agentgui.ontology.Simple_Float;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

/**
 * The Class XySeriesHelper provides some methods to work with {@link XyDataSeries}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
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
			this.xySeries.setLabel("New XY-Data Series");
		}
		return xySeries;
	}
	
	/**
	 * Returns all value pairs that contain the specified x and y value.
	 * @param xValueToSearch the x value to search
	 * @param yValueToSearch the y value to search
	 * @return the Vector of {@link XyValuePair} with the specified x and y values
	 */
	public Vector<XyValuePair> getAllValuePairsContaining(Float xValueToSearch, Float yValueToSearch) {
		Vector<XyValuePair> xyValuePairsFound = null;
		for (int i=0; i<this.getXySeries().getXyValuePairs().size(); i++) {
			XyValuePair currValuePair = (XyValuePair) this.getXySeries().getXyValuePairs().get(i);
			float currXValue = currValuePair.getXValue().getFloatValue();
			float currYValue = currValuePair.getYValue().getFloatValue();
			if (xValueToSearch.equals(currXValue) && yValueToSearch.equals(currYValue)) {
				if (xyValuePairsFound==null) {
					xyValuePairsFound = new Vector<XyValuePair>();
				}
				xyValuePairsFound.addElement(currValuePair);
			}
		}
		return xyValuePairsFound;
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
			copy.setAutoSort(this.xySeries.getAutoSort());
			copy.setAvoidDuplicateXValues(this.xySeries.getAvoidDuplicateXValues());
			
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
			Float xValue = xyVp.getXValue().getFloatValue();
			this.put(xValue, xyVp);
		}
	}
	
	/**
	 * Adds new series data to the current XyDataSeries, if the concrete data is new.
	 * ATTENTION: If duplicate x values are allowed, all {@link XyValuePair} elements 
	 * will just be added. If duplicate x values are NOT allowed only {@link XyValuePair} 
	 * with new x values will be added.
	 * @param additionalXySeries the XySereis containing new data
	 * @return a XyDataSeries containing the value pairs that were actually added
	 */
	public XyDataSeries addSeriesData(XyDataSeries additionalXySeries) {
		return this.addSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Adds new series data to the current XyDataSeries, if the concrete data is new.
	 * ATTENTION: If duplicate x values are allowed, all {@link XyValuePair} elements 
	 * will just be added. If duplicate x values are NOT allowed only {@link XyValuePair} 
	 * with new x values will be added.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 * @return a XyDataSeries containing the value pairs that were actually added
	 */
	public XyDataSeries addSeriesData(List listOfXySeriesValuePairs) {
		XyDataSeries addedValuePairs = new XyDataSeries();
		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xValue = xyVp.getXValue().getFloatValue();
			boolean addValuePair = true;
			if (this.getXySeries().getAvoidDuplicateXValues() && this.containsKey(xValue)) {
				addValuePair = false;
			} 
			if (addValuePair==true) {
				this.getXySeries().getXyValuePairs().add(xyVp);
				this.put(xValue, xyVp);
				addedValuePairs.addXyValuePairs(xyVp);
			}
		}
		if (addedValuePairs.getXyValuePairs().size()==0) {
			addedValuePairs=null;
		} else {
			if (this.getXySeries().getAutoSort()==true) {
				this.getXySeries().sort();
			}
		}
		return addedValuePairs;
	}
	
	/**
	 * Adds or exchanges series data with the data of the specified XyDataSeries.
	 * ATTENTION: If duplicate x values are allowed in the current series, the 
	 * new data will be just added.
	 * @param additionalXySeries the additional xy series
	 */
	public void addOrExchangeSeriesData(XyDataSeries additionalXySeries) {
		this.addOrExchangeSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Adds or exchanges series data with the specified List of XyValuePairs.
	 * ATTENTION: If duplicate x values are allowed in the current series, the 
	 * new data will be just added.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 */
	public void addOrExchangeSeriesData(List listOfXySeriesValuePairs) {
		if (this.getXySeries().getAvoidDuplicateXValues()==true) {
			for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
				XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
				Float xValue = xyVp.getXValue().getFloatValue();
				XyValuePair xyVpLocal = this.get(xValue);
				if (xyVpLocal!=null) {
					xyVpLocal.setYValue(xyVp.getYValue());
				} else {
					this.getXySeries().getXyValuePairs().add(xyVp);
					this.put(xValue, xyVp);
				}
			}
			if (this.getXySeries().getAutoSort()==true) {
				this.getXySeries().sort();
			}
		} else {
			this.addSeriesData(listOfXySeriesValuePairs);
		}
	}

	/**
	 * Exchanges series data, if the concrete x values are available and
	 * if duplicate x values are forbidden.
	 * ATTENTION: If duplicate x values are allowed this method will simply 
	 * exchange nothing.
	 * @param additionalXySeries the additional xy series
	 * @return the XyDataSeries of value pairs that were exchanged
	 */
	public XyDataSeries exchangeSeriesData(XyDataSeries additionalXySeries) {
		return this.exchangeSeriesData(additionalXySeries.getXyValuePairs());
	}
	/**
	 * Exchanges series data, if the concrete x values are available and if duplicate 
	 * x values are forbidden.
	 * ATTENTION: If duplicate x values are allowed this method will exchange nothing.
	 * @param listOfXySeriesValuePairs the list of xy series value pairs
	 * @return the XyDataSeries of value pairs that were exchanged
	 */
	public XyDataSeries exchangeSeriesData(List listOfXySeriesValuePairs) {
		XyDataSeries exchangedXySeries = null;
		if (this.getXySeries().getAvoidDuplicateXValues()==true) {
			exchangedXySeries = new XyDataSeries();
			for (int i=0; i<listOfXySeriesValuePairs.size(); i++) {
				XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
				Float xValue = xyVp.getXValue().getFloatValue();
				XyValuePair xyVpLocal = this.get(xValue);
				if (xyVpLocal!=null) {
					xyVpLocal.setYValue(xyVp.getYValue());
					exchangedXySeries.addXyValuePairs(xyVp);
				}
			}
			if (exchangedXySeries.getXyValuePairs().size()==0) {
				exchangedXySeries = null;
			}
		}
		return exchangedXySeries;
	}
	
	/**
	 * Removes the series data specified by the list of {@link XyValuePair}.
	 * @param xyDataSeriesToRemove the XyDataSeries that contains the value pairs to remove
	 */
	public void removeSeriesData(XyDataSeries xyDataSeriesToRemove) {
		this.removeSeriesData(xyDataSeriesToRemove.getXyValuePairs());
	}
	/**
	 * Removes the series data specified by the xy stamps.
	 * ATTENTION: If duplicate x values are forbidden this method just searches for 
	 * x values to remove. If duplicates are allowed, the method searches for a match
	 * of x and y value (which can be time consuming).
	 * @param listOfXySeriesValuePairs the list of {@link XyValuePair}
	 */
	public void removeSeriesData(List listOfXySeriesValuePairs) {

		for (int i = 0; i < listOfXySeriesValuePairs.size(); i++) {
			XyValuePair xyVp = (XyValuePair) listOfXySeriesValuePairs.get(i);
			Float xValue = xyVp.getXValue().getFloatValue();
			Float yValue = xyVp.getYValue().getFloatValue();
			if (this.getXySeries().getAvoidDuplicateXValues()==true) {
				// --- duplicate x values are forbidden -------
				XyValuePair xyVpLocal = this.get(xValue);
				if (xyVpLocal!=null) {
					this.getXySeries().removeXyValuePairs(xyVpLocal);
					this.remove(xValue);
				}
			} else {
				// --- duplicate x values are allowed ---------
				Vector<XyValuePair> xyVPsLocal = this.getAllValuePairsContaining(xValue, yValue);
				if (xyVPsLocal!=null) {
					for (int j = 0; j < xyVPsLocal.size(); j++) {
						XyValuePair xyVpLocal = xyVPsLocal.get(j);
						this.getXySeries().removeXyValuePairs(xyVpLocal);
					}
					this.remove(xValue);
				}
			}
		}//--- end for ---
	}

}
