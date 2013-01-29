package ontologytest.test.impl;


import jade.util.leap.*;
import ontologytest.test.*;

/**
* This class represents a time series, containing time related data.
* Protege name: TimeSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public class DefaultTimeSeries implements TimeSeries {

  private static final long serialVersionUID = 168012930009999740L;

  private String _internalInstanceName = null;

  public DefaultTimeSeries() {
    this._internalInstanceName = "";
  }

  public DefaultTimeSeries(String instance_name) {
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
	   if(getTimeSeriesValuePairs().size() == 0){
		   noValuePairs = true;
	   }else{
		   if(getTimeSeriesValuePairs().size() == 1){
			   TimeSeriesValuePair vp = (TimeSeriesValuePair) getTimeSeriesValuePairs().get(0);
			   if(vp.getValue().getFloatValue() == 0.0f && vp.getTimestamp().getStringLongValue().length() == 0){
				   noValuePairs = true;
			   }
		   }
	   }
	   
	   return wrongLabel && noValuePairs;
   }

   /**
   * The data of the time series
   * Protege name: timeSeriesValuePairs
   */
   private List timeSeriesValuePairs = new ArrayList();
   public void addTimeSeriesValuePairs(TimeSeriesValuePair elem) { 
     timeSeriesValuePairs.add(elem);
   }
   public boolean removeTimeSeriesValuePairs(TimeSeriesValuePair elem) {
     boolean result = timeSeriesValuePairs.remove(elem);
     return result;
   }
   public void clearAllTimeSeriesValuePairs() {
     timeSeriesValuePairs.clear();
   }
   public Iterator getAllTimeSeriesValuePairs() {return timeSeriesValuePairs.iterator(); }
   public List getTimeSeriesValuePairs() {return timeSeriesValuePairs; }
   public void setTimeSeriesValuePairs(List l) {timeSeriesValuePairs = l; }

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
