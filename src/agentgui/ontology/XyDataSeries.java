package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XyDataSeries
* @author ontology bean generator
* @version 2012/10/23, 17:29:08
*/
public class XyDataSeries extends DataSeries{ 

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
     List oldList = this.xyValuePairs;
     xyValuePairs.add(elem);
   }
   public boolean removeXyValuePairs(XyValuePair elem) {
     List oldList = this.xyValuePairs;
     boolean result = xyValuePairs.remove(elem);
     return result;
   }
   public void clearAllXyValuePairs() {
     List oldList = this.xyValuePairs;
     xyValuePairs.clear();
   }
   public Iterator getAllXyValuePairs() {return xyValuePairs.iterator(); }
   public List getXyValuePairs() {return xyValuePairs; }
   public void setXyValuePairs(List l) {xyValuePairs = l; }

}
