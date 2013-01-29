package ontologytest.test.impl;


import ontologytest.test.*;

/**
* One single data item in a time series.
* Protege name: TimeSeriesValuePair
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultTimeSeriesValuePair implements TimeSeriesValuePair {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultTimeSeriesValuePair() {
    this._internalInstanceName = "";
  }

  public DefaultTimeSeriesValuePair(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: timestamp
   */
   private Simple_Long timestamp;
   public void setTimestamp(Simple_Long value) { 
    this.timestamp=value;
   }
   public Simple_Long getTimestamp() {
     return this.timestamp;
   }

   /**
   * Protege name: value
   */
   private Simple_Float value;
   public void setValue(Simple_Float value) { 
    this.value=value;
   }
   public Simple_Float getValue() {
     return this.value;
   }

}
