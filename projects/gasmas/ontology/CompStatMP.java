package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatMP
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class CompStatMP implements Concept {

   /**
* Protege name: measurementsType
   */
   private List measurementsType = new ArrayList();
   public void addMeasurementsType(CompStatMaxPmeasurment elem) { 
     List oldList = this.measurementsType;
     measurementsType.add(elem);
   }
   public boolean removeMeasurementsType(CompStatMaxPmeasurment elem) {
     List oldList = this.measurementsType;
     boolean result = measurementsType.remove(elem);
     return result;
   }
   public void clearAllMeasurementsType() {
     List oldList = this.measurementsType;
     measurementsType.clear();
   }
   public Iterator getAllMeasurementsType() {return measurementsType.iterator(); }
   public List getMeasurementsType() {return measurementsType; }
   public void setMeasurementsType(List l) {measurementsType = l; }

   /**
* Protege name: ambientTemperature
   */
   private List ambientTemperature = new ArrayList();
   public void addAmbientTemperature(CompStatMaxPtoAmbientTemperature elem) { 
     List oldList = this.ambientTemperature;
     ambientTemperature.add(elem);
   }
   public boolean removeAmbientTemperature(CompStatMaxPtoAmbientTemperature elem) {
     List oldList = this.ambientTemperature;
     boolean result = ambientTemperature.remove(elem);
     return result;
   }
   public void clearAllAmbientTemperature() {
     List oldList = this.ambientTemperature;
     ambientTemperature.clear();
   }
   public Iterator getAllAmbientTemperature() {return ambientTemperature.iterator(); }
   public List getAmbientTemperature() {return ambientTemperature; }
   public void setAmbientTemperature(List l) {ambientTemperature = l; }

}
