package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: GasDrivenMotor
* @author ontology bean generator
* @version 2013/03/7, 00:03:01
*/
public class GasDrivenMotor extends CompStatDrive{ 

   /**
* Protege name: gmSpecificEnergyConsumptionMeasurements
   */
   private List gmSpecificEnergyConsumptionMeasurements = new ArrayList();
   public void addGmSpecificEnergyConsumptionMeasurements(CompStatSECmeasurment elem) { 
     List oldList = this.gmSpecificEnergyConsumptionMeasurements;
     gmSpecificEnergyConsumptionMeasurements.add(elem);
   }
   public boolean removeGmSpecificEnergyConsumptionMeasurements(CompStatSECmeasurment elem) {
     List oldList = this.gmSpecificEnergyConsumptionMeasurements;
     boolean result = gmSpecificEnergyConsumptionMeasurements.remove(elem);
     return result;
   }
   public void clearAllGmSpecificEnergyConsumptionMeasurements() {
     List oldList = this.gmSpecificEnergyConsumptionMeasurements;
     gmSpecificEnergyConsumptionMeasurements.clear();
   }
   public Iterator getAllGmSpecificEnergyConsumptionMeasurements() {return gmSpecificEnergyConsumptionMeasurements.iterator(); }
   public List getGmSpecificEnergyConsumptionMeasurements() {return gmSpecificEnergyConsumptionMeasurements; }
   public void setGmSpecificEnergyConsumptionMeasurements(List l) {gmSpecificEnergyConsumptionMeasurements = l; }

   /**
* Protege name: gmPowerFunCoeff
   */
   private Calc3Parameter gmPowerFunCoeff;
   public void setGmPowerFunCoeff(Calc3Parameter value) { 
    this.gmPowerFunCoeff=value;
   }
   public Calc3Parameter getGmPowerFunCoeff() {
     return this.gmPowerFunCoeff;
   }

   /**
* Protege name: gmMaximalPowerMeasurements
   */
   private List gmMaximalPowerMeasurements = new ArrayList();
   public void addGmMaximalPowerMeasurements(CompStatMaxPmeasurment elem) { 
     List oldList = this.gmMaximalPowerMeasurements;
     gmMaximalPowerMeasurements.add(elem);
   }
   public boolean removeGmMaximalPowerMeasurements(CompStatMaxPmeasurment elem) {
     List oldList = this.gmMaximalPowerMeasurements;
     boolean result = gmMaximalPowerMeasurements.remove(elem);
     return result;
   }
   public void clearAllGmMaximalPowerMeasurements() {
     List oldList = this.gmMaximalPowerMeasurements;
     gmMaximalPowerMeasurements.clear();
   }
   public Iterator getAllGmMaximalPowerMeasurements() {return gmMaximalPowerMeasurements.iterator(); }
   public List getGmMaximalPowerMeasurements() {return gmMaximalPowerMeasurements; }
   public void setGmMaximalPowerMeasurements(List l) {gmMaximalPowerMeasurements = l; }

}
