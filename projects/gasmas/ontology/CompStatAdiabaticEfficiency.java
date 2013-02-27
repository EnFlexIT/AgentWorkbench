package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatAdiabaticEfficiency
* @author ontology bean generator
* @version 2013/02/26, 16:41:10
*/
public class CompStatAdiabaticEfficiency implements Concept {

   /**
* Protege name: measurements
   */
   private List measurements = new ArrayList();
   public void addMeasurements(CompStatTcMeasurement elem) { 
     List oldList = this.measurements;
     measurements.add(elem);
   }
   public boolean removeMeasurements(CompStatTcMeasurement elem) {
     List oldList = this.measurements;
     boolean result = measurements.remove(elem);
     return result;
   }
   public void clearAllMeasurements() {
     List oldList = this.measurements;
     measurements.clear();
   }
   public Iterator getAllMeasurements() {return measurements.iterator(); }
   public List getMeasurements() {return measurements; }
   public void setMeasurements(List l) {measurements = l; }

   /**
* Protege name: adiabaticEfficiency
   */
   private String adiabaticEfficiency;
   public void setAdiabaticEfficiency(String value) { 
    this.adiabaticEfficiency=value;
   }
   public String getAdiabaticEfficiency() {
     return this.adiabaticEfficiency;
   }

}
