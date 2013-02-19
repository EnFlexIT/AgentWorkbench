package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ElectricMotor
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class ElectricMotor extends CompStatDrive{ 

   /**
* Protege name: emPowerFunCoeff
   */
   private Calc9Parameter emPowerFunCoeff;
   public void setEmPowerFunCoeff(Calc9Parameter value) { 
    this.emPowerFunCoeff=value;
   }
   public Calc9Parameter getEmPowerFunCoeff() {
     return this.emPowerFunCoeff;
   }

   /**
* Protege name: emSpecificEnergyConsumptionMeasurements
   */
   private CompStatSECmeasurment emSpecificEnergyConsumptionMeasurements;
   public void setEmSpecificEnergyConsumptionMeasurements(CompStatSECmeasurment value) { 
    this.emSpecificEnergyConsumptionMeasurements=value;
   }
   public CompStatSECmeasurment getEmSpecificEnergyConsumptionMeasurements() {
     return this.emSpecificEnergyConsumptionMeasurements;
   }

   /**
* Protege name: emMaximalPowerMeasurements
   */
   private CompStatMP emMaximalPowerMeasurements;
   public void setEmMaximalPowerMeasurements(CompStatMP value) { 
    this.emMaximalPowerMeasurements=value;
   }
   public CompStatMP getEmMaximalPowerMeasurements() {
     return this.emMaximalPowerMeasurements;
   }

}
