package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * assign quay to berthing ship
* Protege name: AssignHarborQuay
* @author ontology bean generator
* @version 2009/10/6, 22:51:49
*/
public class AssignHarborQuay implements AgentAction {

   /**
* Protege name: assigned_quay
   */
   private Quay assigned_quay;
   public void setAssigned_quay(Quay value) { 
    this.assigned_quay=value;
   }
   public Quay getAssigned_quay() {
     return this.assigned_quay;
   }

}
