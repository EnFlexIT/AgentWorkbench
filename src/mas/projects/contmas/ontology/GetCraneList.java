package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: GetCraneList
* @author ontology bean generator
* @version 2009/10/6, 22:51:49
*/
public class GetCraneList implements AgentAction {

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

   /**
* Protege name: required_turnover_capacity
   */
   private LoadList required_turnover_capacity;
   public void setRequired_turnover_capacity(LoadList value) { 
    this.required_turnover_capacity=value;
   }
   public LoadList getRequired_turnover_capacity() {
     return this.required_turnover_capacity;
   }

}
