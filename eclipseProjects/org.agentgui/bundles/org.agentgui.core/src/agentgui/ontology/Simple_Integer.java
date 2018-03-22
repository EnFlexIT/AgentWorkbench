package agentgui.ontology;


import jade.content.*;

/**
* Protege name: Simple_Integer
* @author ontology bean generator
* @version 2013/10/8, 13:06:42
*/
public class Simple_Integer implements Concept {

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
