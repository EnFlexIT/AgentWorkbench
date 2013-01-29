package ontologytest.test;



/**
* Protege name: Chart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface Chart extends jade.content.Concept {

   /**
   * This defines how the chart will be displayed
   * Protege name: visualizationSettings
   */
   public void setVisualizationSettings(ChartSettingsGeneral value);
   public ChartSettingsGeneral getVisualizationSettings();

}
