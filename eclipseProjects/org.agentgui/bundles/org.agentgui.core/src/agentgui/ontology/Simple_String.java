package agentgui.ontology;


import jade.content.*;

/**
* Protege name: Simple_String
* @author ontology bean generator
* @version 2013/10/8, 13:06:42
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
