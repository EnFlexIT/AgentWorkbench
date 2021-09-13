package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Integer
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
@SuppressWarnings("unused")
public class Simple_Integer implements Concept {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
* Protege name: IntegerValue
   */
   private int integerValue;
   public void setIntegerValue(int value) { 
    this.integerValue=value;
   }
   public int getIntegerValue() {
     return this.integerValue;
   }

}
