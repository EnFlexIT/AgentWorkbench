package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Compressor
* @author ontology bean generator
* @version 2012/08/14, 13:58:40
*/
public class Compressor extends Connection{ 

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
* Protege name: fuelGasVertex
   */
   private String fuelGasVertex;
   public void setFuelGasVertex(String value) { 
    this.fuelGasVertex=value;
   }
   public String getFuelGasVertex() {
     return this.fuelGasVertex;
   }

   /**
* Protege name: cooledOutputTemperature
   */
   private ValueType cooledOutputTemperature;
   public void setCooledOutputTemperature(ValueType value) { 
    this.cooledOutputTemperature=value;
   }
   public ValueType getCooledOutputTemperature() {
     return this.cooledOutputTemperature;
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

   /**
* Protege name: gasCoolerExisting
   */
   private boolean gasCoolerExisting;
   public void setGasCoolerExisting(boolean value) { 
    this.gasCoolerExisting=value;
   }
   public boolean getGasCoolerExisting() {
     return this.gasCoolerExisting;
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
* Protege name: pressureLossOut
   */
   private ValueType pressureLossOut;
   public void setPressureLossOut(ValueType value) { 
    this.pressureLossOut=value;
   }
   public ValueType getPressureLossOut() {
     return this.pressureLossOut;
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
* Protege name: pressureInMin
   */
   private ValueType pressureInMin;
   public void setPressureInMin(ValueType value) { 
    this.pressureInMin=value;
   }
   public ValueType getPressureInMin() {
     return this.pressureInMin;
   }

   /**
* Protege name: pressureLossIn
   */
   private ValueType pressureLossIn;
   public void setPressureLossIn(ValueType value) { 
    this.pressureLossIn=value;
   }
   public ValueType getPressureLossIn() {
     return this.pressureLossIn;
   }

}
