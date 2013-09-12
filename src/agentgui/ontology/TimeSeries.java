package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents a time series, containing time related data.
* Protege name: TimeSeries
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
*/
public class TimeSeries extends DataSeries{ 

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
     List oldList = this.timeSeriesValuePairs;
     timeSeriesValuePairs.add(elem);
   }
   public boolean removeTimeSeriesValuePairs(TimeSeriesValuePair elem) {
     List oldList = this.timeSeriesValuePairs;
     boolean result = timeSeriesValuePairs.remove(elem);
     return result;
   }
   public void clearAllTimeSeriesValuePairs() {
     List oldList = this.timeSeriesValuePairs;
     timeSeriesValuePairs.clear();
   }
   public Iterator getAllTimeSeriesValuePairs() {return timeSeriesValuePairs.iterator(); }
   public List getTimeSeriesValuePairs() {return timeSeriesValuePairs; }
   public void setTimeSeriesValuePairs(List l) {timeSeriesValuePairs = l; }

}
