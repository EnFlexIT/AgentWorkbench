package ontologytest.test;


import jade.util.leap.*;

/**
* Protege name: TimeSeriesChart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface TimeSeriesChart extends Chart {

   /**
   * The data to be displayed by the chart
   * Protege name: timeSeriesChartData
   */
   public void addTimeSeriesChartData(TimeSeries elem);
   public boolean removeTimeSeriesChartData(TimeSeries elem);
   public void clearAllTimeSeriesChartData();
   public Iterator getAllTimeSeriesChartData();
   public List getTimeSeriesChartData();
   public void setTimeSeriesChartData(List l);

   /**
   * Protege name: timeSeriesAdditionalSettings
   */
   public void setTimeSeriesAdditionalSettings(TimeSeriesAdditionalSettings value);
   public TimeSeriesAdditionalSettings getTimeSeriesAdditionalSettings();

}
