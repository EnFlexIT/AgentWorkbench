package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatCompressor
* @author ontology bean generator
* @version 2013/03/10, 21:16:57
*/
public class CompStatCompressor extends GridComponent{ 

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
* Protege name: drive
   */
   private String drive;
   public void setDrive(String value) { 
    this.drive=value;
   }
   public String getDrive() {
     return this.drive;
   }

}
