package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Simple_Float
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultSimple_Float implements Simple_Float {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultSimple_Float() {
    this._internalInstanceName = "";
  }

  public DefaultSimple_Float(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: FloatValue
   */
   private float floatValue;
   public void setFloatValue(float value) { 
    this.floatValue=value;
   }
   public float getFloatValue() {
     return this.floatValue;
   }

}
