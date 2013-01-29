package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* This class contains all data related to how a chart will be displayed
* Protege name: ChartSettingsGeneral
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultChartSettingsGeneral implements ChartSettingsGeneral {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultChartSettingsGeneral() {
    this._internalInstanceName = "";
  }

  public DefaultChartSettingsGeneral(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: yAxisLabel
   */
   private String yAxisLabel;
   public void setYAxisLabel(String value) { 
    this.yAxisLabel=value;
   }
   public String getYAxisLabel() {
     return this.yAxisLabel;
   }

   /**
   * Protege name: yAxisLineWidth
   */
   private List yAxisLineWidth = new ArrayList();
   public void addYAxisLineWidth(Float elem) { 
     yAxisLineWidth.add(elem);
   }
   public boolean removeYAxisLineWidth(Float elem) {
     boolean result = yAxisLineWidth.remove(elem);
     return result;
   }
   public void clearAllYAxisLineWidth() {
     yAxisLineWidth.clear();
   }
   public Iterator getAllYAxisLineWidth() {return yAxisLineWidth.iterator(); }
   public List getYAxisLineWidth() {return yAxisLineWidth; }
   public void setYAxisLineWidth(List l) {yAxisLineWidth = l; }

   /**
   * Protege name: xAxisLabel
   */
   private String xAxisLabel;
   public void setXAxisLabel(String value) { 
    this.xAxisLabel=value;
   }
   public String getXAxisLabel() {
     return this.xAxisLabel;
   }

   /**
   * Protege name: chartTitle
   */
   private String chartTitle;
   public void setChartTitle(String value) { 
    this.chartTitle=value;
   }
   public String getChartTitle() {
     return this.chartTitle;
   }

   /**
   * The renderer type to be used fpr rendering the plots
   * Protege name: rendererType
   */
   private String rendererType;
   public void setRendererType(String value) { 
    this.rendererType=value;
   }
   public String getRendererType() {
     return this.rendererType;
   }

   /**
   * The RGB values of the colors for each series plot
   * Protege name: yAxisColors
   */
   private List yAxisColors = new ArrayList();
   public void addYAxisColors(String elem) { 
     yAxisColors.add(elem);
   }
   public boolean removeYAxisColors(String elem) {
     boolean result = yAxisColors.remove(elem);
     return result;
   }
   public void clearAllYAxisColors() {
     yAxisColors.clear();
   }
   public Iterator getAllYAxisColors() {return yAxisColors.iterator(); }
   public List getYAxisColors() {return yAxisColors; }
   public void setYAxisColors(List l) {yAxisColors = l; }

}
