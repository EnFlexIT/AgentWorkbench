package agentgui.core.charts.xyChart;

import java.util.Vector;

import jade.util.leap.List;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XySeriesChartSettings;
import agentgui.ontology.XyValuePair;

/**
 * The Class XyChartModel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class XyChartModel extends ChartModel {

	private XyDataModel parent;
	private XYSeriesCollection<String> xySeriesCollection;
	
	/**
	 * Instantiates a new XYChartModel.
	 * @param parent the current XyDataModel
	 */
	public XyChartModel(XyDataModel parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getChartSettings()
	 */
	@Override
	public XySeriesChartSettings getChartSettings() {
		return (XySeriesChartSettings) parent.getOntologyModel().getChartSettings();
	}

	/**
	 * Converts and returns a XYSeries from a XyDataSeries.
	 * @param xyDataSeries the XyDataSeries
	 * @return the converted 
	 */
	public XYSeries<String> getXYSeriesFromXyDataSeries(XyDataSeries xyDataSeries) {
	
		XYSeries<String> newSeries = new XYSeries<>(xyDataSeries.getLabel(), xyDataSeries.getAutoSort(), !xyDataSeries.getAvoidDuplicateXValues());
		List valuePairs = xyDataSeries.getXyValuePairs();
		for (int i = 0; i < valuePairs.size(); i++) {
			XyValuePair valuePair = (XyValuePair) valuePairs.get(i);
			newSeries.add(valuePair.getXValue().getFloatValue(), valuePair.getYValue().getFloatValue());
		}
		return newSeries;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#addSeries(agentgui.ontology.DataSeries)
	 */
	@Override
	public void addSeries(DataSeries series) {
		
		this.getXySeriesCollection().addSeries(this.getXYSeriesFromXyDataSeries((XyDataSeries)series));
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_ADDED);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#exchangeSeries(int, agentgui.ontology.DataSeries)
	 */
	@Override
	public void exchangeSeries(int seriesIndex, DataSeries series) throws NoSuchSeriesException {
		
		if (seriesIndex < 0 ||  seriesIndex >= this.getSeriesCount()) {
			throw new NoSuchSeriesException();
		}
		// --- Create series that exchanges the specified series ----
		this.exchangeSeries(seriesIndex, this.getXYSeriesFromXyDataSeries((XyDataSeries)series), true);
	}
	/**
	 * Exchanges the specified TimeSeries in the local {@link #getTimeSeriesCollection()}.
	 *
	 * @param seriesIndex the series index to replace
	 * @param tsExchange the time series to exchange
	 * @param setDirtyAndNotify the set dirty and notify
	 * @throws NoSuchSeriesException the no such series exception
	 */
	private void exchangeSeries(int seriesIndex, XYSeries<String> xyExchange, boolean setDirtyAndNotify) {
		
		// --- Replace the edit series with the new series ------------
		Vector<XYSeries<String>> currSerieses = new Vector<>();
		for (int i=0; i < this.getSeriesCount(); i++) {
			currSerieses.add(this.getSeries(i));
		}
		currSerieses.set(seriesIndex, xyExchange);

		this.getXySeriesCollection().removeAllSeries();
		for (int i = 0; i < currSerieses.size(); i++) {
			this.getXySeriesCollection().addSeries(currSerieses.get(i));
		}
		
		if (setDirtyAndNotify==true) {
			this.setChanged();
			this.notifyObservers(ChartModel.EventType.SERIES_ADDED);
		}
	}
	
	/**
	 * Sets a XY series according to the ontology model.
	 * @param seriesIndex the new XY series according to ontology model
	 */
	public void setXYSeriesAccordingToOntologyModel(int seriesIndex) {
		
		try {
			List xyData = this.parent.getOntologyModel().getSeriesData(seriesIndex);
			XYSeries<String> xySeries = this.getSeries(seriesIndex);	
			xySeries.clear();
			
			for (int i=0; i<xyData.size()-1; i++) {
				XyValuePair vp = (XyValuePair) xyData.get(i);
				xySeries.add(vp.getXValue().getFloatValue(), vp.getYValue().getFloatValue(), false);
			}
			if (xyData.size()>0) {
				XyValuePair vp = (XyValuePair) xyData.get(xyData.size()-1);
				xySeries.add(vp.getXValue().getFloatValue(), vp.getYValue().getFloatValue());
			}
			
		} catch (NoSuchSeriesException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the XYDataItem.
	 *
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @return the xY data item
	 */
	public XYDataItem getXYDataItem(int seriesIndex, int indexPosition) {
		return this.getSeries(seriesIndex).getDataItem(indexPosition);
	}
	
	/**
	 * Adds the specified values as XYDataItem to the specified position.
	 * @param seriesIndex the series index
	 * @param indexPosition the index position
	 * @param newXValue the new x value
	 * @param newYValue the new y value
	 */
	public void addXyDataItem(int seriesIndex, int indexPosition, float newXValue, float newYValue) {
		XYSeries<String> xySeries = this.getSeries(seriesIndex);
		if (xySeries.getAutoSort()==true) {
			// --- Add new value ------------------------------------
			xySeries.add(newXValue, newYValue);
		} else {
			// --- Reset this series in order to keep the order -----
			this.setXYSeriesAccordingToOntologyModel(seriesIndex);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getSeries(int)
	 */
	@Override
	public XYSeries<String> getSeries(int seriesIndex) {
		return this.getXySeriesCollection().getSeries(seriesIndex);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#getSeries(java.lang.String)
	 */
	@Override
	public XYSeries<String> getSeries(String seriesLabel) {
		return this.getXySeriesCollection().getSeries(seriesLabel);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#removeSeries(int)
	 */
	@Override
	public void removeSeries(int seriesIndex) {
		this.getXySeriesCollection().removeSeries(seriesIndex);
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_REMOVED);
	}
	
	/**
	 * Gets the JFreeChart data model for this chart
	 * @return The JFreeChart data model for this chart
	 */
	public XYSeriesCollection<String> getXySeriesCollection(){
		if(this.xySeriesCollection == null){
			this.xySeriesCollection = new XYSeriesCollection<>();
		}
		return this.xySeriesCollection;
	}
	
	/**
	 * Returns the number of series in this chart.
	 * @return the number of series 
	 */
	public int getSeriesCount(){
		return this.getXySeriesCollection().getSeriesCount();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.ChartModel#setSeriesLabel(int, java.lang.String)
	 */
	@Override
	public void setSeriesLabel(int seriesIndex, String newLabel) {
		
		// --- Try getting the current series -------------
		XYSeries<String> oldSeries = this.getSeries(seriesIndex);
		if (oldSeries==null || oldSeries.getKey().equals(newLabel)==true) return;
		
		// --- Create a new series ------------------------
		XYSeries<String> newSeries = new XYSeries<String>(newLabel);
		for (int i = 0; i < oldSeries.getItemCount(); i++) {
			newSeries.add(oldSeries.getDataItem(i));
		}
		
		// --- Exchange the series ------------------------
		this.exchangeSeries(seriesIndex, newSeries, false);
		
		// --- Notify -------------------------------------
		this.setChanged();
		this.notifyObservers(ChartModel.EventType.SERIES_RENAMED);
	}
	
}
