package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_String
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
@SuppressWarnings("unused")
public class Simple_String implements Concept {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
* Protege name: StringValue
   */
   private String stringValue;
   public void setStringValue(String value) { 
    this.stringValue=value;
   }
   public String getStringValue() {
     return this.stringValue;
   }

}
