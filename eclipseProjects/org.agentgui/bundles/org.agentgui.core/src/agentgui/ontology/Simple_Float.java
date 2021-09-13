package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Float
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
@SuppressWarnings("unused")
public class Simple_Float implements Concept {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
