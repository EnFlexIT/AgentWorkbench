package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatTcMeasurement
* @author ontology bean generator
* @version 2013/02/22, 16:57:41
*/
public class CompStatTcMeasurement implements Concept {

   /**
* Protege name: adiabaticHead
   */
   private ValueType adiabaticHead;
   public void setAdiabaticHead(ValueType value) { 
    this.adiabaticHead=value;
   }
   public ValueType getAdiabaticHead() {
     return this.adiabaticHead;
   }

   /**
* Protege name: speed
   */
   private ValueType speed;
   public void setSpeed(ValueType value) { 
    this.speed=value;
   }
   public ValueType getSpeed() {
     return this.speed;
   }

   /**
* Protege name: volumetricFlowrate
   */
   private ValueType volumetricFlowrate;
   public void setVolumetricFlowrate(ValueType value) { 
    this.volumetricFlowrate=value;
   }
   public ValueType getVolumetricFlowrate() {
     return this.volumetricFlowrate;
   }

}
