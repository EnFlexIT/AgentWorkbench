package ontologytest.test;


import jade.util.leap.*;

/**
* Protege name: XyDataSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface XyDataSeries extends DataSeries {

   /**
   * The data of the series
   * Protege name: xyValuePairs
   */
   public void addXyValuePairs(XyValuePair elem);
   public boolean removeXyValuePairs(XyValuePair elem);
   public void clearAllXyValuePairs();
   public Iterator getAllXyValuePairs();
   public List getXyValuePairs();
   public void setXyValuePairs(List l);

}
