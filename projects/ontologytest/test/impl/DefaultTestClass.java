package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: TestClass
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultTestClass implements TestClass {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultTestClass() {
    this._internalInstanceName = "";
  }

  public DefaultTestClass(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: FloatTest
   */
   private float floatTest;
   public void setFloatTest(float value) { 
    this.floatTest=value;
   }
   public float getFloatTest() {
     return this.floatTest;
   }

   /**
   * Protege name: StringTest
   */
   private String stringTest;
   public void setStringTest(String value) { 
    this.stringTest=value;
   }
   public String getStringTest() {
     return this.stringTest;
   }

   /**
   * Protege name: IntegerTest
   */
   private int integerTest;
   public void setIntegerTest(int value) { 
    this.integerTest=value;
   }
   public int getIntegerTest() {
     return this.integerTest;
   }

   /**
   * Protege name: BooleanTest
   */
   private boolean booleanTest;
   public void setBooleanTest(boolean value) { 
    this.booleanTest=value;
   }
   public boolean getBooleanTest() {
     return this.booleanTest;
   }

}
