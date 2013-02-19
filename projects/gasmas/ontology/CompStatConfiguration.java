package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatConfiguration
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class CompStatConfiguration implements Concept {

   /**
* Protege name: stages
   */
   private List stages = new ArrayList();
   public void addStages(CompStatStage elem) { 
     List oldList = this.stages;
     stages.add(elem);
   }
   public boolean removeStages(CompStatStage elem) {
     List oldList = this.stages;
     boolean result = stages.remove(elem);
     return result;
   }
   public void clearAllStages() {
     List oldList = this.stages;
     stages.clear();
   }
   public Iterator getAllStages() {return stages.iterator(); }
   public List getStages() {return stages; }
   public void setStages(List l) {stages = l; }

   /**
* Protege name: confID
   */
   private String confID;
   public void setConfID(String value) { 
    this.confID=value;
   }
   public String getConfID() {
     return this.confID;
   }

   /**
* Protege name: nrOfSerialStages
   */
   private int nrOfSerialStages;
   public void setNrOfSerialStages(int value) { 
    this.nrOfSerialStages=value;
   }
   public int getNrOfSerialStages() {
     return this.nrOfSerialStages;
   }

}
