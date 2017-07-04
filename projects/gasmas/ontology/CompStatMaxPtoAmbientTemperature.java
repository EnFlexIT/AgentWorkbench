package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatMaxPtoAmbientTemperature
* @author ontology bean generator
* @version 2013/03/10, 21:16:56
*/
public class CompStatMaxPtoAmbientTemperature implements Concept {

   /**
* Protege name: measurementsMP
   */
   private List measurementsMP = new ArrayList();
   public void addMeasurementsMP(CompStatMaxPmeasurment elem) { 
     List oldList = this.measurementsMP;
     measurementsMP.add(elem);
   }
   public boolean removeMeasurementsMP(CompStatMaxPmeasurment elem) {
     List oldList = this.measurementsMP;
     boolean result = measurementsMP.remove(elem);
     return result;
   }
   public void clearAllMeasurementsMP() {
     List oldList = this.measurementsMP;
     measurementsMP.clear();
   }
   public Iterator getAllMeasurementsMP() {return measurementsMP.iterator(); }
   public List getMeasurementsMP() {return measurementsMP; }
   public void setMeasurementsMP(List l) {measurementsMP = l; }

   /**
* Protege name: temperatureMP
   */
   private ValueType temperatureMP;
   public void setTemperatureMP(ValueType value) { 
    this.temperatureMP=value;
   }
   public ValueType getTemperatureMP() {
     return this.temperatureMP;
   }

}
