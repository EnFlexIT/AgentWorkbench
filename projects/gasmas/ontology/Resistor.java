package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Resistor
* @author ontology bean generator
* @version 2012/10/24, 18:22:29
*/
public class Resistor extends Connection{ 

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

}
