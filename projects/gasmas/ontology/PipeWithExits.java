package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PipeWithExits
* @author ontology bean generator
* @version 2011/04/5, 11:14:58
*/
public class PipeWithExits extends Pipe{ 

   /**
* Protege name: exitList
   */
   private List exitList = new ArrayList();
   public void addExitList(PropagationPoint elem) { 
     List oldList = this.exitList;
     exitList.add(elem);
   }
   public boolean removeExitList(PropagationPoint elem) {
     List oldList = this.exitList;
     boolean result = exitList.remove(elem);
     return result;
   }
   public void clearAllExitList() {
     List oldList = this.exitList;
     exitList.clear();
   }
   public Iterator getAllExitList() {return exitList.iterator(); }
   public List getExitList() {return exitList; }
   public void setExitList(List l) {exitList = l; }

   /**
* Protege name: entryList
   */
   private List entryList = new ArrayList();
   public void addEntryList(PropagationPoint elem) { 
     List oldList = this.entryList;
     entryList.add(elem);
   }
   public boolean removeEntryList(PropagationPoint elem) {
     List oldList = this.entryList;
     boolean result = entryList.remove(elem);
     return result;
   }
   public void clearAllEntryList() {
     List oldList = this.entryList;
     entryList.clear();
   }
   public Iterator getAllEntryList() {return entryList.iterator(); }
   public List getEntryList() {return entryList; }
   public void setEntryList(List l) {entryList = l; }

}
