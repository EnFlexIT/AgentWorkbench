package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ElectricMotor
* @author ontology bean generator
* @version 2013/02/20, 13:32:45
*/
public class ElectricMotor extends CompStatDrive{ 

   /**
* Protege name: emMaximalPowerMeasurements
   */
   private CompStatMaxP emMaximalPowerMeasurements;
   public void setEmMaximalPowerMeasurements(CompStatMaxP value) { 
    this.emMaximalPowerMeasurements=value;
   }
   public CompStatMaxP getEmMaximalPowerMeasurements() {
     return this.emMaximalPowerMeasurements;
   }

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
   private List emSpecificEnergyConsumptionMeasurements = new ArrayList();
   public void addEmSpecificEnergyConsumptionMeasurements(CompStatSECmeasurment elem) { 
     List oldList = this.emSpecificEnergyConsumptionMeasurements;
     emSpecificEnergyConsumptionMeasurements.add(elem);
   }
   public boolean removeEmSpecificEnergyConsumptionMeasurements(CompStatSECmeasurment elem) {
     List oldList = this.emSpecificEnergyConsumptionMeasurements;
     boolean result = emSpecificEnergyConsumptionMeasurements.remove(elem);
     return result;
   }
   public void clearAllEmSpecificEnergyConsumptionMeasurements() {
     List oldList = this.emSpecificEnergyConsumptionMeasurements;
     emSpecificEnergyConsumptionMeasurements.clear();
   }
   public Iterator getAllEmSpecificEnergyConsumptionMeasurements() {return emSpecificEnergyConsumptionMeasurements.iterator(); }
   public List getEmSpecificEnergyConsumptionMeasurements() {return emSpecificEnergyConsumptionMeasurements; }
   public void setEmSpecificEnergyConsumptionMeasurements(List l) {emSpecificEnergyConsumptionMeasurements = l; }

}
