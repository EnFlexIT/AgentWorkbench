package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AssignHarborQuay
* @author ontology bean generator
* @version 2010/03/9, 21:12:43
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
