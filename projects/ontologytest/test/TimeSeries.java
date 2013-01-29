package ontologytest.test;


import jade.util.leap.*;

/**
* This class represents a time series, containing time related data.
* Protege name: TimeSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface TimeSeries extends DataSeries {

   /**
   * The data of the time series
   * Protege name: timeSeriesValuePairs
   */
   public void addTimeSeriesValuePairs(TimeSeriesValuePair elem);
   public boolean removeTimeSeriesValuePairs(TimeSeriesValuePair elem);
   public void clearAllTimeSeriesValuePairs();
   public Iterator getAllTimeSeriesValuePairs();
   public List getTimeSeriesValuePairs();
   public void setTimeSeriesValuePairs(List l);

}
