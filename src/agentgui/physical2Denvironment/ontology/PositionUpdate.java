package agentgui.physical2Denvironment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PositionUpdate
* @author ontology bean generator
* @version 2011/01/27, 16:56:56
*/
public class PositionUpdate implements AgentAction {

   /**
* Protege name: newPosition
   */
   private Position newPosition;
   public void setNewPosition(Position value) { 
    this.newPosition=value;
   }
   public Position getNewPosition() {
     return this.newPosition;
   }

   /**
* Protege name: customizedParameter
   */
   private Object customizedParameter;
   public void setCustomizedParameter(Object value) { 
    this.customizedParameter=value;
   }
   public Object getCustomizedParameter() {
     return this.customizedParameter;
   }

}
