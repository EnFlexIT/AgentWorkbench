package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * One single data item in a time series.
* Protege name: TimeSeriesValuePair
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
public class TimeSeriesValuePair extends ValuePair{ 

//////////////////////////// User code
public boolean isEmpty() {
	   if(this.getValue().getFloatValue() == 0.0f && this.getTimestamp().getStringLongValue().length() == 0){
		   return  true;
	   } else {
		   return false;
	   }
   }
   /**
* Protege name: timestamp
   */
   private Simple_Long timestamp;
   public void setTimestamp(Simple_Long value) { 
    this.timestamp=value;
   }
   public Simple_Long getTimestamp() {
     return this.timestamp;
   }

   /**
* Protege name: value
   */
   private Simple_Float value;
   public void setValue(Simple_Float value) { 
    this.value=value;
   }
   public Simple_Float getValue() {
     return this.value;
   }

}
