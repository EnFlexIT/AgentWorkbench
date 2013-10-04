package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XyDataSeries
* @author ontology bean generator
* @version 2013/10/1, 11:28:40
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
	   }
	   
	   return wrongLabel && noValuePairs;
   }
   /**
* Protege name: avoidDuplicateXValues
   */
   private boolean avoidDuplicateXValues;
   public void setAvoidDuplicateXValues(boolean value) { 
    this.avoidDuplicateXValues=value;
   }
   public boolean getAvoidDuplicateXValues() {
     return this.avoidDuplicateXValues;
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

   /**
* Protege name: autoSort
   */
   private boolean autoSort;
   public void setAutoSort(boolean value) { 
    this.autoSort=value;
   }
   public boolean getAutoSort() {
     return this.autoSort;
   }

}
