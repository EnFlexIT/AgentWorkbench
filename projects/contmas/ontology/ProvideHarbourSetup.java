package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProvideHarbourSetup
* @author ontology bean generator
* @version 2010/04/20, 14:23:52
*/
public class ProvideHarbourSetup implements AgentAction {

   /**
* Protege name: currently_active_container_holders
   */
   private List currently_active_container_holders = new ArrayList();
   public void addCurrently_active_container_holders(ContainerHolder elem) { 
     List oldList = this.currently_active_container_holders;
     currently_active_container_holders.add(elem);
   }
   public boolean removeCurrently_active_container_holders(ContainerHolder elem) {
     List oldList = this.currently_active_container_holders;
     boolean result = currently_active_container_holders.remove(elem);
     return result;
   }
   public void clearAllCurrently_active_container_holders() {
     List oldList = this.currently_active_container_holders;
     currently_active_container_holders.clear();
   }
   public Iterator getAllCurrently_active_container_holders() {return currently_active_container_holders.iterator(); }
   public List getCurrently_active_container_holders() {return currently_active_container_holders; }
   public void setCurrently_active_container_holders(List l) {currently_active_container_holders = l; }

   /**
* Protege name: current_harbour_layout
   */
   private Domain current_harbour_layout;
   public void setCurrent_harbour_layout(Domain value) { 
    this.current_harbour_layout=value;
   }
   public Domain getCurrent_harbour_layout() {
     return this.current_harbour_layout;
   }

}
