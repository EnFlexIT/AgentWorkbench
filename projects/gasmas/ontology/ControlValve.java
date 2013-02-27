package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ControlValve
* @author ontology bean generator
* @version 2013/02/26, 16:41:10
*/
public class ControlValve extends Valve{ 

   /**
* Protege name: gasPreheheaterExisting
   */
   private boolean gasPreheheaterExisting;
   public void setGasPreheheaterExisting(boolean value) { 
    this.gasPreheheaterExisting=value;
   }
   public boolean getGasPreheheaterExisting() {
     return this.gasPreheheaterExisting;
   }

   /**
* Protege name: dragFactorIn
   */
   private float dragFactorIn;
   public void setDragFactorIn(float value) { 
    this.dragFactorIn=value;
   }
   public float getDragFactorIn() {
     return this.dragFactorIn;
   }

   /**
* Protege name: pressureSet
   */
   private ValueType pressureSet;
   public void setPressureSet(ValueType value) { 
    this.pressureSet=value;
   }
   public ValueType getPressureSet() {
     return this.pressureSet;
   }

   /**
* Protege name: diameterIn
   */
   private ValueType diameterIn;
   public void setDiameterIn(ValueType value) { 
    this.diameterIn=value;
   }
   public ValueType getDiameterIn() {
     return this.diameterIn;
   }

   /**
* Protege name: pressureOutMax
   */
   private ValueType pressureOutMax;
   public void setPressureOutMax(ValueType value) { 
    this.pressureOutMax=value;
   }
   public ValueType getPressureOutMax() {
     return this.pressureOutMax;
   }

   /**
* Protege name: increasedOutputTemperature
   */
   private ValueType increasedOutputTemperature;
   public void setIncreasedOutputTemperature(ValueType value) { 
    this.increasedOutputTemperature=value;
   }
   public ValueType getIncreasedOutputTemperature() {
     return this.increasedOutputTemperature;
   }

   /**
* Protege name: internalBypassRequired
   */
   private boolean internalBypassRequired;
   public void setInternalBypassRequired(boolean value) { 
    this.internalBypassRequired=value;
   }
   public boolean getInternalBypassRequired() {
     return this.internalBypassRequired;
   }

   /**
* Protege name: dragFactorOut
   */
   private float dragFactorOut;
   public void setDragFactorOut(float value) { 
    this.dragFactorOut=value;
   }
   public float getDragFactorOut() {
     return this.dragFactorOut;
   }

   /**
* Protege name: diameterOut
   */
   private ValueType diameterOut;
   public void setDiameterOut(ValueType value) { 
    this.diameterOut=value;
   }
   public ValueType getDiameterOut() {
     return this.diameterOut;
   }

   /**
* Protege name: pressureDifferentialMin
   */
   private ValueType pressureDifferentialMin;
   public void setPressureDifferentialMin(ValueType value) { 
    this.pressureDifferentialMin=value;
   }
   public ValueType getPressureDifferentialMin() {
     return this.pressureDifferentialMin;
   }

   /**
* Protege name: pressureInMin
   */
   private ValueType pressureInMin;
   public void setPressureInMin(ValueType value) { 
    this.pressureInMin=value;
   }
   public ValueType getPressureInMin() {
     return this.pressureInMin;
   }

}
