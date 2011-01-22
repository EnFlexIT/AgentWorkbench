package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class defines links between two grid component. The link itself is not part of the modelled grid.
* Protege name: GridLink
* @author ontology bean generator
* @version 2011/01/21, 21:34:17
*/
public class GridLink implements Concept {

   /**
   * The ID of this edge's target component.
* Protege name: targetID
   */
   private String targetID;
   public void setTargetID(String value) { 
    this.targetID=value;
   }
   public String getTargetID() {
     return this.targetID;
   }

   /**
   * The ID of this edge's source component.
* Protege name: sourceID
   */
   private String sourceID;
   public void setSourceID(String value) { 
    this.sourceID=value;
   }
   public String getSourceID() {
     return this.sourceID;
   }

}
