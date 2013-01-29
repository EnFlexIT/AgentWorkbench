package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* Protege name: XyDataSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultXyDataSeries implements XyDataSeries {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultXyDataSeries() {
    this._internalInstanceName = "";
  }

  public DefaultXyDataSeries(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

//////////////////////////// User code
public boolean isEmpty() {
	   boolean wrongLabel = false;
	   if(this.getLabel() == null || this.getLabel().length() == 0){
		   wrongLabel = true;
	   }
	   boolean noValuePairs = false;
	   if(getXyValuePairs().size() == 0){
		   noValuePairs = true;
	   }else{
		   if(getXyValuePairs().size() == 1){
			   XyValuePair vp = (XyValuePair) getXyValuePairs().get(0);
			   if(vp.getXValue().getFloatValue() == 0.0f && vp.getYValue().getFloatValue() == 0.0f){
				   noValuePairs = true;
			   }
		   }
	   }
	   
	   return wrongLabel && noValuePairs;
   }

   /**
   * The data of the series
   * Protege name: xyValuePairs
   */
   private List xyValuePairs = new ArrayList();
   public void addXyValuePairs(XyValuePair elem) { 
     xyValuePairs.add(elem);
   }
   public boolean removeXyValuePairs(XyValuePair elem) {
     boolean result = xyValuePairs.remove(elem);
     return result;
   }
   public void clearAllXyValuePairs() {
     xyValuePairs.clear();
   }
   public Iterator getAllXyValuePairs() {return xyValuePairs.iterator(); }
   public List getXyValuePairs() {return xyValuePairs; }
   public void setXyValuePairs(List l) {xyValuePairs = l; }

   /**
   * Protege name: unit
   */
   private String unit;
   public void setUnit(String value) { 
    this.unit=value;
   }
   public String getUnit() {
     return this.unit;
   }

   /**
   * The data series label
   * Protege name: label
   */
   private String label;
   public void setLabel(String value) { 
    this.label=value;
   }
   public String getLabel() {
     return this.label;
   }

}
