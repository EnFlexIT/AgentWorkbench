package agentgui.ontology;


import jade.content.*;

/**
* Protege name: Simple_Boolean
* @author ontology bean generator
* @version 2013/10/8, 13:06:41
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
