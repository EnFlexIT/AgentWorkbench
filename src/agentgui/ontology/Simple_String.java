package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_String
* @author ontology bean generator
* @version 2013/01/29, 13:35:51
*/
public class Simple_String implements Concept {

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
