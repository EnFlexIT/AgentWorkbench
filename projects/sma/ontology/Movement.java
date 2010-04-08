package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Movement
* @author ontology bean generator
* @version 2010/04/7, 20:19:12
*/
public class Movement implements AgentAction {

   /**
* Protege name: Steps
   */
   private List steps = new ArrayList();
   public void addSteps(Position elem) { 
     List oldList = this.steps;
     steps.add(elem);
   }
   public boolean removeSteps(Position elem) {
     List oldList = this.steps;
     boolean result = steps.remove(elem);
     return result;
   }
   public void clearAllSteps() {
     List oldList = this.steps;
     steps.clear();
   }
   public Iterator getAllSteps() {return steps.iterator(); }
   public List getSteps() {return steps; }
   public void setSteps(List l) {steps = l; }

}
