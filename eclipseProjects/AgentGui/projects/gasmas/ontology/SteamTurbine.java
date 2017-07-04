package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SteamTurbine
* @author ontology bean generator
* @version 2013/03/10, 21:16:57
*/
public class SteamTurbine extends CompStatDrive{ 

   /**
* Protege name: powerMin
   */
   private ValueType powerMin;
   public void setPowerMin(ValueType value) { 
    this.powerMin=value;
   }
   public ValueType getPowerMin() {
     return this.powerMin;
   }

   /**
* Protege name: explicit
   */
   private boolean explicit;
   public void setExplicit(boolean value) { 
    this.explicit=value;
   }
   public boolean getExplicit() {
     return this.explicit;
   }

   /**
* Protege name: powerMax
   */
   private ValueType powerMax;
   public void setPowerMax(ValueType value) { 
    this.powerMax=value;
   }
   public ValueType getPowerMax() {
     return this.powerMax;
   }

}