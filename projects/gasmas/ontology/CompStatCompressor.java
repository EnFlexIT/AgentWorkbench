package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatCompressor
* @author ontology bean generator
* @version 2013/02/20, 13:32:45
*/
public class CompStatCompressor extends GridComponent{ 

   /**
* Protege name: drive
   */
   private String drive;
   public void setDrive(String value) { 
    this.drive=value;
   }
   public String getDrive() {
     return this.drive;
   }

   /**
* Protege name: speedMin
   */
   private ValueType speedMin;
   public void setSpeedMin(ValueType value) { 
    this.speedMin=value;
   }
   public ValueType getSpeedMin() {
     return this.speedMin;
   }

   /**
* Protege name: speedMax
   */
   private ValueType speedMax;
   public void setSpeedMax(ValueType value) { 
    this.speedMax=value;
   }
   public ValueType getSpeedMax() {
     return this.speedMax;
   }

}
