package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * One single data item in a time series.
* Protege name: TimeSeriesValuePair
* @author ontology bean generator
* @version 2012/09/21, 17:08:25
*/
public class TimeSeriesValuePair extends ValuePair{ 

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
