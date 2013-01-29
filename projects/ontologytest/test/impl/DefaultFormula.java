package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: Formula
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultFormula implements Formula {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultFormula() {
    this._internalInstanceName = "";
  }

  public DefaultFormula(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: formula
   */
   private String formula;
   public void setFormula(String value) { 
    this.formula=value;
   }
   public String getFormula() {
     return this.formula;
   }

}
