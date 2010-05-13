package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ActiveContainerHolder
* @author ontology bean generator
* @version 2010/05/13, 15:13:19
*/
public class ActiveContainerHolder extends ContainerHolder{ 

   /**
* Protege name: capable_of
   */
   private List capable_of = new ArrayList();
   public void addCapable_of(Domain elem) { 
     List oldList = this.capable_of;
     capable_of.add(elem);
   }
   public boolean removeCapable_of(Domain elem) {
     List oldList = this.capable_of;
     boolean result = capable_of.remove(elem);
     return result;
   }
   public void clearAllCapable_of() {
     List oldList = this.capable_of;
     capable_of.clear();
   }
   public Iterator getAllCapable_of() {return capable_of.iterator(); }
   public List getCapable_of() {return capable_of; }
   public void setCapable_of(List l) {capable_of = l; }

   /**
* Protege name: scheduled_movements
   */
   private List scheduled_movements = new ArrayList();
   public void addScheduled_movements(Movement elem) { 
     List oldList = this.scheduled_movements;
     scheduled_movements.add(elem);
   }
   public boolean removeScheduled_movements(Movement elem) {
     List oldList = this.scheduled_movements;
     boolean result = scheduled_movements.remove(elem);
     return result;
   }
   public void clearAllScheduled_movements() {
     List oldList = this.scheduled_movements;
     scheduled_movements.clear();
   }
   public Iterator getAllScheduled_movements() {return scheduled_movements.iterator(); }
   public List getScheduled_movements() {return scheduled_movements; }
   public void setScheduled_movements(List l) {scheduled_movements = l; }

}
