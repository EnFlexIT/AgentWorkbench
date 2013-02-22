package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatMaxPmeasurment
* @author ontology bean generator
* @version 2013/02/22, 16:57:41
*/
public class CompStatMaxPmeasurment implements Concept {

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

}
