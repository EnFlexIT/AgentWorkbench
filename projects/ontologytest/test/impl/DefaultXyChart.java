package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* Protege name: XyChart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultXyChart implements XyChart {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultXyChart() {
    this._internalInstanceName = "";
  }

  public DefaultXyChart(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

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
   * The data to be displayed by the chart
   * Protege name: xyChartData
   */
   private List xyChartData = new ArrayList();
   public void addXyChartData(XyDataSeries elem) { 
     xyChartData.add(elem);
   }
   public boolean removeXyChartData(XyDataSeries elem) {
     boolean result = xyChartData.remove(elem);
     return result;
   }
   public void clearAllXyChartData() {
     xyChartData.clear();
   }
   public Iterator getAllXyChartData() {return xyChartData.iterator(); }
   public List getXyChartData() {return xyChartData; }
   public void setXyChartData(List l) {xyChartData = l; }

}
