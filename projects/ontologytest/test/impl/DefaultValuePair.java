package ontologytest.test.impl;


import ontologytest.test.*;

/**
* General superclass for value pairs. This class is used for unified handling of value pairs only. As the type of the values can be different for the different types of value pairs (and Protégé has problems handling more specialized member types in suclasses), this is a simple marker class without any properties.
* Protege name: ValuePair
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultValuePair implements ValuePair {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultValuePair() {
    this._internalInstanceName = "";
  }

  public DefaultValuePair(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

}
