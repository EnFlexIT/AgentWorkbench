package ontologytest.test.impl;


import ontologytest.test.*;

/**
* This class represents one data series, containing x and y values
* Protege name: DataSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultDataSeries implements DataSeries {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultDataSeries() {
    this._internalInstanceName = "";
  }

  public DefaultDataSeries(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: unit
   */
   private String unit;
   public void setUnit(String value) { 
    this.unit=value;
   }
   public String getUnit() {
     return this.unit;
   }

   /**
   * The data series label
   * Protege name: label
   */
   private String label;
   public void setLabel(String value) { 
    this.label=value;
   }
   public String getLabel() {
     return this.label;
   }

}
