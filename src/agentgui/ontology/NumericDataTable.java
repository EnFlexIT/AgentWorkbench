package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: NumericDataTable
* @author ontology bean generator
* @version 2012/04/1, 18:52:07
*/
public class NumericDataTable implements Concept {

   /**
* Protege name: tableData
   */
   private List tableData = new ArrayList();
   public void addTableData(NumericDataColumn elem) { 
     List oldList = this.tableData;
     tableData.add(elem);
   }
   public boolean removeTableData(NumericDataColumn elem) {
     List oldList = this.tableData;
     boolean result = tableData.remove(elem);
     return result;
   }
   public void clearAllTableData() {
     List oldList = this.tableData;
     tableData.clear();
   }
   public Iterator getAllTableData() {return tableData.iterator(); }
   public List getTableData() {return tableData; }
   public void setTableData(List l) {tableData = l; }

}
