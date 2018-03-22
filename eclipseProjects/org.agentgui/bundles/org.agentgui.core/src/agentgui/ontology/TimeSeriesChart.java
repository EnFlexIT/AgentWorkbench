package agentgui.ontology;

import jade.util.leap.*;

/**
* Protege name: TimeSeriesChart
* @author ontology bean generator
* @version 2013/10/8, 13:06:41
*/
public class TimeSeriesChart extends Chart{ 

//////////////////////////// User code
public boolean isEmpty() {
 boolean wrongChartTitle = false;
 if (this.getTimeSeriesVisualisationSettings().getChartTitle() == null|| this.getTimeSeriesVisualisationSettings().getChartTitle().length()==0) {
  wrongChartTitle = true;
 }
 boolean wrongXAxisLabel = false;
 if (this.getTimeSeriesVisualisationSettings().getXAxisLabel()==null || this.getTimeSeriesVisualisationSettings().getXAxisLabel().length()==0) {
  wrongXAxisLabel = true;
 }
 boolean wrongYAxisLabel = false;
 if (this.getTimeSeriesVisualisationSettings().getYAxisLabel()==null || this.getTimeSeriesVisualisationSettings().getYAxisLabel().length()==0) {
  wrongYAxisLabel= true;
 }

 if (wrongChartTitle && wrongXAxisLabel && wrongYAxisLabel) {
  return true;
 } else {
  return false;
 }
}
   /**
   * The data to be displayed by the chart
* Protege name: timeSeriesChartData
   */
   private List timeSeriesChartData = new ArrayList();
   public void addTimeSeriesChartData(TimeSeries elem) { 
     List oldList = this.timeSeriesChartData;
     timeSeriesChartData.add(elem);
   }
   public boolean removeTimeSeriesChartData(TimeSeries elem) {
     List oldList = this.timeSeriesChartData;
     boolean result = timeSeriesChartData.remove(elem);
     return result;
   }
   public void clearAllTimeSeriesChartData() {
     List oldList = this.timeSeriesChartData;
     timeSeriesChartData.clear();
   }
   public Iterator getAllTimeSeriesChartData() {return timeSeriesChartData.iterator(); }
   public List getTimeSeriesChartData() {return timeSeriesChartData; }
   public void setTimeSeriesChartData(List l) {timeSeriesChartData = l; }

   /**
* Protege name: timeSeriesVisualisationSettings
   */
   private TimeSeriesChartSettings timeSeriesVisualisationSettings;
   public void setTimeSeriesVisualisationSettings(TimeSeriesChartSettings value) { 
    this.timeSeriesVisualisationSettings=value;
   }
   public TimeSeriesChartSettings getTimeSeriesVisualisationSettings() {
     return this.timeSeriesVisualisationSettings;
   }

}
