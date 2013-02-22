package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatSECmeasurment
* @author ontology bean generator
* @version 2013/02/22, 16:57:41
*/
public class CompStatSECmeasurment implements Concept {

   /**
* Protege name: fuelConsumption
   */
   private ValueType fuelConsumption;
   public void setFuelConsumption(ValueType value) { 
    this.fuelConsumption=value;
   }
   public ValueType getFuelConsumption() {
     return this.fuelConsumption;
   }

   /**
* Protege name: comressorPower
   */
   private ValueType comressorPower;
   public void setComressorPower(ValueType value) { 
    this.comressorPower=value;
   }
   public ValueType getComressorPower() {
     return this.comressorPower;
   }

}
