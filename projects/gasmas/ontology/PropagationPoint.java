package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A PropagationPoint is a link between two components of the gas grid.
* Protege name: PropagationPoint
* @author ontology bean generator
* @version 2012/05/21, 12:49:00
*/
public class PropagationPoint implements Concept {

   /**
   * The AIDs of the agents the propagationpoint connects.
* Protege name: adjacentAgents
   */
   private List adjacentAgents = new ArrayList();
   public void addAdjacentAgents(AID elem) { 
     List oldList = this.adjacentAgents;
     adjacentAgents.add(elem);
   }
   public boolean removeAdjacentAgents(AID elem) {
     List oldList = this.adjacentAgents;
     boolean result = adjacentAgents.remove(elem);
     return result;
   }
   public void clearAllAdjacentAgents() {
     List oldList = this.adjacentAgents;
     adjacentAgents.clear();
   }
   public Iterator getAllAdjacentAgents() {return adjacentAgents.iterator(); }
   public List getAdjacentAgents() {return adjacentAgents; }
   public void setAdjacentAgents(List l) {adjacentAgents = l; }

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

}
