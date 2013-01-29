package ontologytest.test;


import jade.util.leap.*;

/**
* This class contains all data related to how a chart will be displayed
* Protege name: ChartSettingsGeneral
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface ChartSettingsGeneral extends VisualizationSettings {

   /**
   * Protege name: yAxisLabel
   */
   public void setYAxisLabel(String value);
   public String getYAxisLabel();

   /**
   * Protege name: yAxisLineWidth
   */
   public void addYAxisLineWidth(Float elem);
   public boolean removeYAxisLineWidth(Float elem);
   public void clearAllYAxisLineWidth();
   public Iterator getAllYAxisLineWidth();
   public List getYAxisLineWidth();
   public void setYAxisLineWidth(List l);

   /**
   * Protege name: xAxisLabel
   */
   public void setXAxisLabel(String value);
   public String getXAxisLabel();

   /**
   * Protege name: chartTitle
   */
   public void setChartTitle(String value);
   public String getChartTitle();

   /**
   * The renderer type to be used fpr rendering the plots
   * Protege name: rendererType
   */
   public void setRendererType(String value);
   public String getRendererType();

   /**
   * The RGB values of the colors for each series plot
   * Protege name: yAxisColors
   */
   public void addYAxisColors(String elem);
   public boolean removeYAxisColors(String elem);
   public void clearAllYAxisColors();
   public Iterator getAllYAxisColors();
   public List getYAxisColors();
   public void setYAxisColors(List l);

}
