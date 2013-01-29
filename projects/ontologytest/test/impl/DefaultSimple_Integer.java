package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Simple_Integer
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultSimple_Integer implements Simple_Integer {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultSimple_Integer() {
    this._internalInstanceName = "";
  }

  public DefaultSimple_Integer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: IntegerValue
   */
   private int integerValue;
   public void setIntegerValue(int value) { 
    this.integerValue=value;
   }
   public int getIntegerValue() {
     return this.integerValue;
   }

}
