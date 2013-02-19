package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PistonCompressor
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class PistonCompressor extends CompStatCompressor{ 

   /**
* Protege name: operatingVolume
   */
   private ValueType operatingVolume;
   public void setOperatingVolume(ValueType value) { 
    this.operatingVolume=value;
   }
   public ValueType getOperatingVolume() {
     return this.operatingVolume;
   }

   /**
* Protege name: maximalTorque
   */
   private ValueType maximalTorque;
   public void setMaximalTorque(ValueType value) { 
    this.maximalTorque=value;
   }
   public ValueType getMaximalTorque() {
     return this.maximalTorque;
   }

   /**
* Protege name: maximalCompressionRatio
   */
   private float maximalCompressionRatio;
   public void setMaximalCompressionRatio(float value) { 
    this.maximalCompressionRatio=value;
   }
   public float getMaximalCompressionRatio() {
     return this.maximalCompressionRatio;
   }

   /**
* Protege name: adiabaticEfficiencyPiston
   */
   private float adiabaticEfficiencyPiston;
   public void setAdiabaticEfficiencyPiston(float value) { 
    this.adiabaticEfficiencyPiston=value;
   }
   public float getAdiabaticEfficiencyPiston() {
     return this.adiabaticEfficiencyPiston;
   }

   /**
* Protege name: additionalReductionVolFlow
   */
   private float additionalReductionVolFlow;
   public void setAdditionalReductionVolFlow(float value) { 
    this.additionalReductionVolFlow=value;
   }
   public float getAdditionalReductionVolFlow() {
     return this.additionalReductionVolFlow;
   }

}
