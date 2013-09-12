package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Integer
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
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
