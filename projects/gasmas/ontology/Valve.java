package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Valve
* @author ontology bean generator
* @version 2012/08/14, 13:58:40
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
   private int flowDirection;
   public void setFlowDirection(int value) { 
    this.flowDirection=value;
   }
   public int getFlowDirection() {
     return this.flowDirection;
   }

}
