package de.enflexit.awb.baseUI.monitor.threading;

import org.jfree.data.xy.XYSeries;

import de.enflexit.awb.simulation.load.threading.storage.ThreadXYDataItem;
import de.enflexit.awb.simulation.load.threading.storage.ThreadXYSeries;

/**
 * The Class ThreadXYSeriesConverter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ThreadXYSeriesConverter {
	
	/**
	 * Converts the specified ThreadXYSeries to a XYSeries of JFreeChart.
	 *
	 * @param threadXySeries the thread xy series
	 * @return the XY series
	 */
	public static XYSeries toXYSeries(ThreadXYSeries threadXySeries) {

		XYSeries xySeries = new XYSeries(threadXySeries.getKey());
		for (ThreadXYDataItem dataItem : threadXySeries.getItems()) {
			xySeries.add(dataItem.getX(), dataItem.getY());
		}
		return xySeries;
	}
	
}
