package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: XyValuePair
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultXyValuePair implements XyValuePair {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultXyValuePair() {
    this._internalInstanceName = "";
  }

  public DefaultXyValuePair(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: xValue
   */
   private Simple_Float xValue;
   public void setXValue(Simple_Float value) { 
    this.xValue=value;
   }
   public Simple_Float getXValue() {
     return this.xValue;
   }

   /**
   * Protege name: yValue
   */
   private Simple_Float yValue;
   public void setYValue(Simple_Float value) { 
    this.yValue=value;
   }
   public Simple_Float getYValue() {
     return this.yValue;
   }

}
