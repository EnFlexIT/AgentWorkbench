package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XyChart
* @author ontology bean generator
* @version 2013/01/29, 13:35:51
*/
public class XyChart extends Chart{ 

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
