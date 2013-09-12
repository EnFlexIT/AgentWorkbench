package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Float
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
*/
public class Simple_Float implements Concept {

   /**
* Protege name: FloatValue
   */
   private float floatValue;
   public void setFloatValue(float value) { 
    this.floatValue=value;
   }
   public float getFloatValue() {
     return this.floatValue;
   }

}
