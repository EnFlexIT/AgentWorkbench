package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XyChart
* @author ontology bean generator
* @version 2019/02/12, 14:08:01
*/
public class XyChart extends Chart{ 

//////////////////////////// User code
public boolean isEmpty() {
 boolean wrongChartTitle = false;
 if (this.getXySeriesVisualisationSettings().getChartTitle() == null|| this.getXySeriesVisualisationSettings().getChartTitle().length()==0) {
  wrongChartTitle = true;
 }
 boolean wrongXAxisLabel = false;
 if (this.getXySeriesVisualisationSettings().getXAxisLabel()==null || this.getXySeriesVisualisationSettings().getXAxisLabel().length()==0) {
  wrongXAxisLabel = true;
 }
 boolean wrongYAxisLabel = false;
 if (this.getXySeriesVisualisationSettings().getYAxisLabel()==null || this.getXySeriesVisualisationSettings().getYAxisLabel().length()==0) {
  wrongYAxisLabel= true;
 }

 if (wrongChartTitle && wrongXAxisLabel && wrongYAxisLabel) {
  return true;
 } else {
  return false;
 }
}
   /**
* Protege name: xySeriesVisualisationSettings
   */
   private XySeriesChartSettings xySeriesVisualisationSettings;
   public void setXySeriesVisualisationSettings(XySeriesChartSettings value) { 
    this.xySeriesVisualisationSettings=value;
   }
   public XySeriesChartSettings getXySeriesVisualisationSettings() {
     return this.xySeriesVisualisationSettings;
   }

   /**
   * The data to be displayed by the chart
* Protege name: xyChartData
   */
   private List xyChartData = new ArrayList();
   public void addXyChartData(XyDataSeries elem) { 
     List oldList = this.xyChartData;
     xyChartData.add(elem);
   }
   public boolean removeXyChartData(XyDataSeries elem) {
     List oldList = this.xyChartData;
     boolean result = xyChartData.remove(elem);
     return result;
   }
   public void clearAllXyChartData() {
     List oldList = this.xyChartData;
     xyChartData.clear();
   }
   public Iterator getAllXyChartData() {return xyChartData.iterator(); }
   public List getXyChartData() {return xyChartData; }
   public void setXyChartData(List l) {xyChartData = l; }

}
