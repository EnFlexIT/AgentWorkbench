package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Entry
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class Entry extends Exit{ 

   /**
* Protege name: heatCapacityCoefficient
   */
   private HeatCapacityCoefficient heatCapacityCoefficient;
   public void setHeatCapacityCoefficient(HeatCapacityCoefficient value) { 
    this.heatCapacityCoefficient=value;
   }
   public HeatCapacityCoefficient getHeatCapacityCoefficient() {
     return this.heatCapacityCoefficient;
   }

   /**
* Protege name: calorificValue
   */
   private ValueType calorificValue;
   public void setCalorificValue(ValueType value) { 
    this.calorificValue=value;
   }
   public ValueType getCalorificValue() {
     return this.calorificValue;
   }

   /**
* Protege name: pseudocricalPressure
   */
   private ValueType pseudocricalPressure;
   public void setPseudocricalPressure(ValueType value) { 
    this.pseudocricalPressure=value;
   }
   public ValueType getPseudocricalPressure() {
     return this.pseudocricalPressure;
   }

   /**
* Protege name: normDensity
   */
   private ValueType normDensity;
   public void setNormDensity(ValueType value) { 
    this.normDensity=value;
   }
   public ValueType getNormDensity() {
     return this.normDensity;
   }

   /**
* Protege name: molarMass
   */
   private float molarMass;
   public void setMolarMass(float value) { 
    this.molarMass=value;
   }
   public float getMolarMass() {
     return this.molarMass;
   }

   /**
* Protege name: pseudocriticalTemperature
   */
   private ValueType pseudocriticalTemperature;
   public void setPseudocriticalTemperature(ValueType value) { 
    this.pseudocriticalTemperature=value;
   }
   public ValueType getPseudocriticalTemperature() {
     return this.pseudocriticalTemperature;
   }

   /**
* Protege name: gasTemperature
   */
   private ValueType gasTemperature;
   public void setGasTemperature(ValueType value) { 
    this.gasTemperature=value;
   }
   public ValueType getGasTemperature() {
     return this.gasTemperature;
   }

}
