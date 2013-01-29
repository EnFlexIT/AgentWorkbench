package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Simple_String
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultSimple_String implements Simple_String {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultSimple_String() {
    this._internalInstanceName = "";
  }

  public DefaultSimple_String(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: StringValue
   */
   private String stringValue;
   public void setStringValue(String value) { 
    this.stringValue=value;
   }
   public String getStringValue() {
     return this.stringValue;
   }

}
