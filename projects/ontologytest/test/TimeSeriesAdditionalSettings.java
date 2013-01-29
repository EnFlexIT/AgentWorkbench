package ontologytest.test;


import jade.util.leap.*;

/**
* Protege name: TimeSeriesAdditionalSettings
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface TimeSeriesAdditionalSettings extends ChartSettingSpecial {

   /**
   * Protege name: timeFormat
   */
   public void addTimeFormat(String elem);
   public boolean removeTimeFormat(String elem);
   public void clearAllTimeFormat();
   public Iterator getAllTimeFormat();
   public List getTimeFormat();
   public void setTimeFormat(List l);

}
