package agentgui.ontology;


import jade.content.*;

/**
* Protege name: Simple_Object
* @author ontology bean generator
* @version 2013/10/8, 13:06:42
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
