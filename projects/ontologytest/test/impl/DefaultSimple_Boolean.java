package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Simple_Boolean
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultSimple_Boolean implements Simple_Boolean {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultSimple_Boolean() {
    this._internalInstanceName = "";
  }

  public DefaultSimple_Boolean(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: BooleanValue
   */
   private boolean booleanValue;
   public void setBooleanValue(boolean value) { 
    this.booleanValue=value;
   }
   public boolean getBooleanValue() {
     return this.booleanValue;
   }

}
