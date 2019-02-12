package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class contains all data related to how a chart will be displayed
* Protege name: ChartSettingsGeneral
* @author ontology bean generator
* @version 2019/02/12, 13:38:42
*/
public class ChartSettingsGeneral extends VisualizationSettings{ 

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
   * The RGB values of the colors for each series plot
* Protege name: yAxisColors
   */
   private List yAxisColors = new ArrayList();
   public void addYAxisColors(String elem) { 
     List oldList = this.yAxisColors;
     yAxisColors.add(elem);
   }
   public boolean removeYAxisColors(String elem) {
     List oldList = this.yAxisColors;
     boolean result = yAxisColors.remove(elem);
     return result;
   }
   public void clearAllYAxisColors() {
     List oldList = this.yAxisColors;
     yAxisColors.clear();
   }
   public Iterator getAllYAxisColors() {return yAxisColors.iterator(); }
   public List getYAxisColors() {return yAxisColors; }
   public void setYAxisColors(List l) {yAxisColors = l; }

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
* Protege name: yAxisLineWidth
   */
   private List yAxisLineWidth = new ArrayList();
   public void addYAxisLineWidth(Float elem) { 
     List oldList = this.yAxisLineWidth;
     yAxisLineWidth.add(elem);
   }
   public boolean removeYAxisLineWidth(Float elem) {
     List oldList = this.yAxisLineWidth;
     boolean result = yAxisLineWidth.remove(elem);
     return result;
   }
   public void clearAllYAxisLineWidth() {
     List oldList = this.yAxisLineWidth;
     yAxisLineWidth.clear();
   }
   public Iterator getAllYAxisLineWidth() {return yAxisLineWidth.iterator(); }
   public List getYAxisLineWidth() {return yAxisLineWidth; }
   public void setYAxisLineWidth(List l) {yAxisLineWidth = l; }

}
