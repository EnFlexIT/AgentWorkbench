package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* Protege name: TimeSeriesChart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultTimeSeriesChart implements TimeSeriesChart {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultTimeSeriesChart() {
    this._internalInstanceName = "";
  }

  public DefaultTimeSeriesChart(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * The data to be displayed by the chart
   * Protege name: timeSeriesChartData
   */
   private List timeSeriesChartData = new ArrayList();
   public void addTimeSeriesChartData(TimeSeries elem) { 
     timeSeriesChartData.add(elem);
   }
   public boolean removeTimeSeriesChartData(TimeSeries elem) {
     boolean result = timeSeriesChartData.remove(elem);
     return result;
   }
   public void clearAllTimeSeriesChartData() {
     timeSeriesChartData.clear();
   }
   public Iterator getAllTimeSeriesChartData() {return timeSeriesChartData.iterator(); }
   public List getTimeSeriesChartData() {return timeSeriesChartData; }
   public void setTimeSeriesChartData(List l) {timeSeriesChartData = l; }

   /**
   * This defines how the chart will be displayed
   * Protege name: visualizationSettings
   */
   private ChartSettingsGeneral visualizationSettings;
   public void setVisualizationSettings(ChartSettingsGeneral value) { 
    this.visualizationSettings=value;
   }
   public ChartSettingsGeneral getVisualizationSettings() {
     return this.visualizationSettings;
   }

   /**
   * Protege name: timeSeriesAdditionalSettings
   */
   private TimeSeriesAdditionalSettings timeSeriesAdditionalSettings;
   public void setTimeSeriesAdditionalSettings(TimeSeriesAdditionalSettings value) { 
    this.timeSeriesAdditionalSettings=value;
   }
   public TimeSeriesAdditionalSettings getTimeSeriesAdditionalSettings() {
     return this.timeSeriesAdditionalSettings;
   }

}
