package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Connection
* @author ontology bean generator
* @version 2012/06/26, 14:32:24
*/
public class Connection extends GridComponent{ 

   /**
* Protege name: flowMax
   */
   private ValueType flowMax;
   public void setFlowMax(ValueType value) { 
    this.flowMax=value;
   }
   public ValueType getFlowMax() {
     return this.flowMax;
   }

   /**
* Protege name: currentFlow
   */
   private FlowParameters currentFlow;
   public void setCurrentFlow(FlowParameters value) { 
    this.currentFlow=value;
   }
   public FlowParameters getCurrentFlow() {
     return this.currentFlow;
   }

   /**
* Protege name: to
   */
   private String to;
   public void setTo(String value) { 
    this.to=value;
   }
   public String getTo() {
     return this.to;
   }

   /**
* Protege name: from
   */
   private String from;
   public void setFrom(String value) { 
    this.from=value;
   }
   public String getFrom() {
     return this.from;
   }

   /**
* Protege name: flowMin
   */
   private ValueType flowMin;
   public void setFlowMin(ValueType value) { 
    this.flowMin=value;
   }
   public ValueType getFlowMin() {
     return this.flowMin;
   }

}
