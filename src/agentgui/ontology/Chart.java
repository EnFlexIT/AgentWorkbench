package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Chart
* @author ontology bean generator
* @version 2012/09/21, 17:08:25
*/
public class Chart implements Concept {

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
