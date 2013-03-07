package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatMaxPmeasurment
* @author ontology bean generator
* @version 2013/03/7, 00:03:01
*/
public class CompStatMaxPmeasurment implements Concept {

   /**
* Protege name: speeMP
   */
   private ValueType speeMP;
   public void setSpeeMP(ValueType value) { 
    this.speeMP=value;
   }
   public ValueType getSpeeMP() {
     return this.speeMP;
   }

   /**
* Protege name: maximalPower
   */
   private ValueType maximalPower;
   public void setMaximalPower(ValueType value) { 
    this.maximalPower=value;
   }
   public ValueType getMaximalPower() {
     return this.maximalPower;
   }

}
