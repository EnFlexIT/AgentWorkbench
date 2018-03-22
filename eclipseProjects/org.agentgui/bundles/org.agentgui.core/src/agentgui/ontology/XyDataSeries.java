package agentgui.ontology;

import jade.util.leap.*;

/**
* Protege name: XyDataSeries
* @author ontology bean generator
* @version 2013/10/8, 13:06:41
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

public void sort() {
	
	java.util.List<XyValuePair> newXyValuePairs = new java.util.ArrayList<XyValuePair>();
	for (int i=0; i<this.xyValuePairs.size(); i++) {
		newXyValuePairs.add((XyValuePair) this.xyValuePairs.get(i));
	}
	java.util.Collections.sort(newXyValuePairs, new java.util.Comparator<XyValuePair>() {
		@Override
		public int compare(XyValuePair vp1, XyValuePair vp2) {
			Float x1 = vp1.getXValue().getFloatValue();
			Float x2 = vp2.getXValue().getFloatValue();
			if (vp1.getXValue().getFloatValue()==vp2.getXValue().getFloatValue()) {
				Float y1 = vp1.getYValue().getFloatValue();
				Float y2 = vp2.getYValue().getFloatValue();
				return y1.compareTo(y2);
			}
			return x1.compareTo(x2);
		}
	});
	this.xyValuePairs.clear();
	for (int i=0; i<newXyValuePairs.size(); i++) {
		this.xyValuePairs.add(newXyValuePairs.get(i));
	}
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

}
