package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AssignBerth
* @author ontology bean generator
* @version 2010/06/8, 22:03:06
*/
public class AssignBerth implements AgentAction {

   /**
* Protege name: assigned_berth
   */
   private Object assigned_berth;
   public void setAssigned_berth(Object value) { 
    this.assigned_berth=value;
   }
   public Object getAssigned_berth() {
     return this.assigned_berth;
   }

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
