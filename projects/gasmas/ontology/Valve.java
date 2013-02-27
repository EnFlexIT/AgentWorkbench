package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Valve
* @author ontology bean generator
* @version 2013/02/26, 16:41:10
*/
public class Valve extends Connection{ 

   /**
* Protege name: pressureDifferentialMax
   */
   private ValueType pressureDifferentialMax;
   public void setPressureDifferentialMax(ValueType value) { 
    this.pressureDifferentialMax=value;
   }
   public ValueType getPressureDifferentialMax() {
     return this.pressureDifferentialMax;
   }

   /**
* Protege name: flowDirection
   */
   private boolean flowDirection;
   public void setFlowDirection(boolean value) { 
    this.flowDirection=value;
   }
   public boolean getFlowDirection() {
     return this.flowDirection;
   }

   /**
* Protege name: value
   */
   private boolean value;
   public void setValue(boolean value) { 
    this.value=value;
   }
   public boolean getValue() {
     return this.value;
   }

}
