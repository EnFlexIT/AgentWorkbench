package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A PropagationPoint is a link between two components of the gas grid.
* Protege name: PropagationPoint
* @author ontology bean generator
* @version 2012/06/5, 16:34:03
*/
public class PropagationPoint implements Concept {

   /**
   * A set of parameters describing the physical properties of a PropagationPoint.
* Protege name: structuralParameters
   */
   private StructuralParameters structuralParameters;
   public void setStructuralParameters(StructuralParameters value) { 
    this.structuralParameters=value;
   }
   public StructuralParameters getStructuralParameters() {
     return this.structuralParameters;
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
   * A set of parameters describing the current gas flow at a PropagationPoint.
* Protege name: flowParameters
   */
   private FlowParameters flowParameters;
   public void setFlowParameters(FlowParameters value) { 
    this.flowParameters=value;
   }
   public FlowParameters getFlowParameters() {
     return this.flowParameters;
   }

}
