package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Simple_Long
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultSimple_Long implements Simple_Long {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultSimple_Long() {
    this._internalInstanceName = "";
  }

  public DefaultSimple_Long(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

//////////////////////////// User code
public Long getLongValue(){
	   try{
		   return Long.parseLong(getStringLongValue());
	   }catch(NumberFormatException ex){
		   return null;
	   }
   }
   public void setLongValue(long value){
	   setStringLongValue(""+value);
   }
   public void setLongValue(Long value){
	   setStringLongValue(value.toString());
   }

   /**
   * Protege name: StringLongValue
   */
   private String stringLongValue;
   public void setStringLongValue(String value) { 
    this.stringLongValue=value;
   }
   public String getStringLongValue() {
     return this.stringLongValue;
   }

}
