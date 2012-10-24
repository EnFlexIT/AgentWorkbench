package agentgui.core.charts.xyChart;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.DataSeries;
import agentgui.ontology.XyDataSeries;
import agentgui.ontology.XyValuePair;

public class XyChartModel extends XYSeriesCollection implements ChartModel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 8270571170143064082L;

	@Override
	public void addOrUpdateValuePair(int seriesIndex, Number key, Number value)
			throws NoSuchSeriesException {
		if(seriesIndex < this.getSeriesCount()){
			XYSeries series = this.getSeries(seriesIndex);
			series.addOrUpdate(key, value);
		}else{
			throw new NoSuchSeriesException();
		}

	}

	@Override
	public void addSeries(DataSeries series) {
		XYSeries newSeries = new XYSeries(series.getLabel());
		
		jade.util.leap.Iterator valuePairs = ((XyDataSeries)series).getAllXyValuePairs();
		while(valuePairs.hasNext()){
			XyValuePair valuePair = (XyValuePair) valuePairs.next();
			newSeries.add(valuePair.getXValue().getFloatValue(), valuePair.getYValue().getFloatValue());
		}
		
		this.addSeries(newSeries);

	}

	@Override
	public void updateKey(Number oldKey, Number newKey) {
		for(int i=0; i<getSeriesCount(); i++){
			try {
				int itemIndex = getIndexByKey(i, oldKey);
				if(itemIndex >= 0){
					XYSeries series = getSeries(i);
					Number yValue = series.getDataItem(itemIndex).getY();
					series.remove(itemIndex);
					series.addOrUpdate(newKey, yValue);
				}
			} catch (NoSuchSeriesException e) {
				// Should never happen, as i cannot be > getSeriesCount()
				System.err.println("Error accessing series "+i);
				e.printStackTrace();
			}
			
			
		}
		

	}

	@Override
	public void removeValuePair(int seriesIndex, Number key)
			throws NoSuchSeriesException {
		if(seriesIndex < getSeriesCount()){
			int itemIndex = getIndexByKey(seriesIndex, key);
			if(itemIndex >= 0){
				getSeries(seriesIndex).remove(itemIndex);
			}
		}else{
			throw new NoSuchSeriesException();
		}

	}
	
	private int getIndexByKey(int seriesIndex, Number key) throws NoSuchSeriesException{
		if(seriesIndex < getSeriesCount()){
			XYSeries series = getSeries(seriesIndex);
			
			for(int i=0; i < series.getItemCount(); i++){
				if(series.getDataItem(i).getXValue() == key.doubleValue()){
					return i;
				}
			}
			return -1;
		}else{
			throw new NoSuchSeriesException();
		}
	}

}
