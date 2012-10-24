package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesChart
* @author ontology bean generator
* @version 2012/10/23, 17:29:08
*/
public class TimeSeriesChart extends Chart{ 

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

}
