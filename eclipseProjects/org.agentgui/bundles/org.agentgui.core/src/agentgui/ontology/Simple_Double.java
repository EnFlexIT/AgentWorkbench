package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Double
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
public class Simple_Double implements Concept {

//////////////////////////// User code
public Double getDoubleValue() {
	   try {
		   return Double.parseDouble(this.getStringDoubleValue());
	   } catch(NumberFormatException ex){
		   return null;
	   }
   }
   public void setDoubleValue(double doubleValue) {
	   this.setStringDoubleValue("" + doubleValue);;
   }
   public void setDoubleValue(Double doubleValue) {
	   this.setStringDoubleValue(doubleValue.toString());
   }
   /**
* Protege name: StringDoubleValue
   */
   private String stringDoubleValue;
   public void setStringDoubleValue(String value) { 
    this.stringDoubleValue=value;
   }
   public String getStringDoubleValue() {
     return this.stringDoubleValue;
   }

}
