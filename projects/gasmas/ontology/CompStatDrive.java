package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatDrive
* @author ontology bean generator
* @version 2013/03/7, 00:03:01
*/
public class CompStatDrive extends GridComponent{ 

   /**
* Protege name: energyRateFunCoeff
   */
   private Calc3Parameter energyRateFunCoeff;
   public void setEnergyRateFunCoeff(Calc3Parameter value) { 
    this.energyRateFunCoeff=value;
   }
   public Calc3Parameter getEnergyRateFunCoeff() {
     return this.energyRateFunCoeff;
   }

}
