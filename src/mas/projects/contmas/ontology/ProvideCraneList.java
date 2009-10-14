package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProvideCraneList
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class ProvideCraneList implements AgentAction {

   /**
* Protege name: available_cranes
   */
   private List available_cranes = new ArrayList();
   public void addAvailable_cranes(AID elem) { 
     List oldList = this.available_cranes;
     available_cranes.add(elem);
   }
   public boolean removeAvailable_cranes(AID elem) {
     List oldList = this.available_cranes;
     boolean result = available_cranes.remove(elem);
     return result;
   }
   public void clearAllAvailable_cranes() {
     List oldList = this.available_cranes;
     available_cranes.clear();
   }
   public Iterator getAllAvailable_cranes() {return available_cranes.iterator(); }
   public List getAvailable_cranes() {return available_cranes; }
   public void setAvailable_cranes(List l) {available_cranes = l; }

}
