package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Chart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultChart implements Chart {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultChart() {
    this._internalInstanceName = "";
  }

  public DefaultChart(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

//////////////////////////// User code
public boolean isEmpty() {
	   
	   boolean wrongChartTitle = false;
	   if (this.getVisualizationSettings().getChartTitle() == null|| this.getVisualizationSettings().getChartTitle().length()==0) {
		   wrongChartTitle = true;
	   }
	   boolean wrongXAxisLabel = false;
	   if (this.getVisualizationSettings().getXAxisLabel()==null || this.getVisualizationSettings().getXAxisLabel().length()==0) {
		   wrongXAxisLabel = true;
	   }
	   boolean wrongYAxisLabel = false;
	   if (this.getVisualizationSettings().getYAxisLabel()==null || this.getVisualizationSettings().getYAxisLabel().length()==0) {
		   wrongYAxisLabel= true;
	   }
	   
	   if (wrongChartTitle && wrongXAxisLabel && wrongYAxisLabel) {
		   return true;
	   }  else {
		   return false;
	   }
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

}
