package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: IndicateContainer
* @author ontology bean generator
* @version 2009/10/6, 22:51:49
*/
public class IndicateContainer implements AgentAction {

   /**
* Protege name: found
   */
   private List found = new ArrayList();
   public void addFound(Container elem) { 
     List oldList = this.found;
     found.add(elem);
   }
   public boolean removeFound(Container elem) {
     List oldList = this.found;
     boolean result = found.remove(elem);
     return result;
   }
   public void clearAllFound() {
     List oldList = this.found;
     found.clear();
   }
   public Iterator getAllFound() {return found.iterator(); }
   public List getFound() {return found; }
   public void setFound(List l) {found = l; }

}
