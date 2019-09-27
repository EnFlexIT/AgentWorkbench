package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Object
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
public class Simple_Object implements Concept {

   /**
* Protege name: ObjectValue
   */
   private Object objectValue;
   public void setObjectValue(Object value) { 
    this.objectValue=value;
   }
   public Object getObjectValue() {
     return this.objectValue;
   }

}
