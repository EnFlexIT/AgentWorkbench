package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Boolean
* @author ontology bean generator
* @version 2013/07/2, 23:36:43
*/
public class Simple_Boolean implements Concept {

   /**
* Protege name: BooleanValue
   */
   private boolean booleanValue;
   public void setBooleanValue(boolean value) { 
    this.booleanValue=value;
   }
   public boolean getBooleanValue() {
     return this.booleanValue;
   }

}
