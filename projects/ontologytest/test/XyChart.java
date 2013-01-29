package ontologytest.test;


import jade.util.leap.*;

/**
* Protege name: XyChart
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface XyChart extends Chart {

   /**
   * The data to be displayed by the chart
   * Protege name: xyChartData
   */
   public void addXyChartData(XyDataSeries elem);
   public boolean removeXyChartData(XyDataSeries elem);
   public void clearAllXyChartData();
   public Iterator getAllXyChartData();
   public List getXyChartData();
   public void setXyChartData(List l);

}
