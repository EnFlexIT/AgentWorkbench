package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;

/**
* Protege name: ClientAvailableMachinesReply
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class ClientAvailableMachinesReply implements AgentAction {

   /**
* Protege name: availableMachines
   */
   private List availableMachines = new ArrayList();
   public void addAvailableMachines(MachineDescription elem) { 
     List oldList = this.availableMachines;
     availableMachines.add(elem);
   }
   public boolean removeAvailableMachines(MachineDescription elem) {
     List oldList = this.availableMachines;
     boolean result = availableMachines.remove(elem);
     return result;
   }
   public void clearAllAvailableMachines() {
     List oldList = this.availableMachines;
     availableMachines.clear();
   }
   public Iterator getAllAvailableMachines() {return availableMachines.iterator(); }
   public List getAvailableMachines() {return availableMachines; }
   public void setAvailableMachines(List l) {availableMachines = l; }

}
