package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Resistor
* @author ontology bean generator
* @version 2012/06/26, 14:32:24
*/
public class Resistor extends Connection{ 

   /**
* Protege name: Diameter
   */
   private ValueType diameter;
   public void setDiameter(ValueType value) { 
    this.diameter=value;
   }
   public ValueType getDiameter() {
     return this.diameter;
   }

   /**
* Protege name: pressureLoss
   */
   private ValueType pressureLoss;
   public void setPressureLoss(ValueType value) { 
    this.pressureLoss=value;
   }
   public ValueType getPressureLoss() {
     return this.pressureLoss;
   }

   /**
* Protege name: dragFactor
   */
   private float dragFactor;
   public void setDragFactor(float value) { 
    this.dragFactor=value;
   }
   public float getDragFactor() {
     return this.dragFactor;
   }

}
