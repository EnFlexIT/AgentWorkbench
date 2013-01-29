package ontologytest.test.impl;


import ontologytest.test.*;

/**
* Protege name: VisualizationSettings
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultVisualizationSettings implements VisualizationSettings {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultVisualizationSettings() {
    this._internalInstanceName = "";
  }

  public DefaultVisualizationSettings(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

}
