package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesChart
* @author ontology bean generator
* @version 2019/02/25, 13:33:15
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
* Protege name: timeSeriesVisualisationSettings
   */
   private TimeSeriesChartSettings timeSeriesVisualisationSettings;
   public void setTimeSeriesVisualisationSettings(TimeSeriesChartSettings value) { 
    this.timeSeriesVisualisationSettings=value;
   }
   public TimeSeriesChartSettings getTimeSeriesVisualisationSettings() {
     return this.timeSeriesVisualisationSettings;
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
   * Indicates if this chart displays real time data, i.e. length restrictions are applied
* Protege name: realTime
   */
   private boolean realTime;
   public void setRealTime(boolean value) { 
    this.realTime=value;
   }
   public boolean getRealTime() {
     return this.realTime;
   }

}
