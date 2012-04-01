package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: NumericDataColumn
* @author ontology bean generator
* @version 2012/04/1, 18:52:07
*/
public class NumericDataColumn implements Concept {

   /**
* Protege name: columnData
   */
   private List columnData = new ArrayList();
   public void addColumnData(Float elem) { 
     List oldList = this.columnData;
     columnData.add(elem);
   }
   public boolean removeColumnData(Float elem) {
     List oldList = this.columnData;
     boolean result = columnData.remove(elem);
     return result;
   }
   public void clearAllColumnData() {
     List oldList = this.columnData;
     columnData.clear();
   }
   public Iterator getAllColumnData() {return columnData.iterator(); }
   public List getColumnData() {return columnData; }
   public void setColumnData(List l) {columnData = l; }

}
