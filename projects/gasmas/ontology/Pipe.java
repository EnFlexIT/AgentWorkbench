package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Pipe
* @author ontology bean generator
* @version 2013/02/20, 13:32:45
*/
public class Pipe extends Connection{ 

   /**
* Protege name: lineOfSight
   */
   private ValueType lineOfSight;
   public void setLineOfSight(ValueType value) { 
    this.lineOfSight=value;
   }
   public ValueType getLineOfSight() {
     return this.lineOfSight;
   }

   /**
* Protege name: pressureMax
   */
   private ValueType pressureMax;
   public void setPressureMax(ValueType value) { 
    this.pressureMax=value;
   }
   public ValueType getPressureMax() {
     return this.pressureMax;
   }

   /**
* Protege name: roughness
   */
   private ValueType roughness;
   public void setRoughness(ValueType value) { 
    this.roughness=value;
   }
   public ValueType getRoughness() {
     return this.roughness;
   }

   /**
* Protege name: heatTransferCoefficient
   */
   private ValueType heatTransferCoefficient;
   public void setHeatTransferCoefficient(ValueType value) { 
    this.heatTransferCoefficient=value;
   }
   public ValueType getHeatTransferCoefficient() {
     return this.heatTransferCoefficient;
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

   /**
* Protege name: Length
   */
   private ValueType length;
   public void setLength(ValueType value) { 
    this.length=value;
   }
   public ValueType getLength() {
     return this.length;
   }

}
