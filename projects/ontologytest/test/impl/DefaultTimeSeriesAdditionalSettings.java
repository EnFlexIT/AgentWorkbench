package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* Protege name: TimeSeriesAdditionalSettings
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultTimeSeriesAdditionalSettings implements TimeSeriesAdditionalSettings {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultTimeSeriesAdditionalSettings() {
    this._internalInstanceName = "";
  }

  public DefaultTimeSeriesAdditionalSettings(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: timeFormat
   */
   private List timeFormat = new ArrayList();
   public void addTimeFormat(String elem) { 
     timeFormat.add(elem);
   }
   public boolean removeTimeFormat(String elem) {
     boolean result = timeFormat.remove(elem);
     return result;
   }
   public void clearAllTimeFormat() {
     timeFormat.clear();
   }
   public Iterator getAllTimeFormat() {return timeFormat.iterator(); }
   public List getTimeFormat() {return timeFormat; }
   public void setTimeFormat(List l) {timeFormat = l; }

}
