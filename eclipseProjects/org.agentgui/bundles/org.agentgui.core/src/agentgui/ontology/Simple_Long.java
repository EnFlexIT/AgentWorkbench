package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Simple_Long
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
@SuppressWarnings("unused")
public class Simple_Long implements Concept {

//////////////////////////// User code
public Long getLongValue(){
	   try{
		   return Long.parseLong(getStringLongValue());
	   }catch(NumberFormatException ex){
		   return null;
	   }
   }
   public void setLongValue(long value){
	   setStringLongValue(""+value);
   }
   public void setLongValue(Long value){
	   setStringLongValue(value.toString());
   }
   /**
* Protege name: StringLongValue
   */
   private String stringLongValue;
   public void setStringLongValue(String value) { 
    this.stringLongValue=value;
   }
   public String getStringLongValue() {
     return this.stringLongValue;
   }

}
