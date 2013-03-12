package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatSECmeasurment
* @author ontology bean generator
* @version 2013/03/10, 21:16:56
*/
public class CompStatSECmeasurment implements Concept {

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

}
