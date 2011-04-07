package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PropagationPoint
* @author ontology bean generator
* @version 2011/04/5, 11:14:58
*/
public class PropagationPoint implements Concept {

   /**
* Protege name: flowParameters
   */
   private FlowParameters flowParameters;
   public void setFlowParameters(FlowParameters value) { 
    this.flowParameters=value;
   }
   public FlowParameters getFlowParameters() {
     return this.flowParameters;
   }

   /**
* Protege name: ID
   */
   private String iD;
   public void setID(String value) { 
    this.iD=value;
   }
   public String getID() {
     return this.iD;
   }

   /**
* Protege name: structuralParameters
   */
   private StructuralParameters structuralParameters;
   public void setStructuralParameters(StructuralParameters value) { 
    this.structuralParameters=value;
   }
   public StructuralParameters getStructuralParameters() {
     return this.structuralParameters;
   }

}
