package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: phy:Movement
* @author ontology bean generator
* @version 2010/05/16, 12:52:00
*/
public class Phy_Movement implements AgentAction {

   /**
* Protege name: phy:Steps
   */
   private List phy_Steps = new ArrayList();
   public void addPhy_Steps(Phy_Position elem) { 
     List oldList = this.phy_Steps;
     phy_Steps.add(elem);
   }
   public boolean removePhy_Steps(Phy_Position elem) {
     List oldList = this.phy_Steps;
     boolean result = phy_Steps.remove(elem);
     return result;
   }
   public void clearAllPhy_Steps() {
     List oldList = this.phy_Steps;
     phy_Steps.clear();
   }
   public Iterator getAllPhy_Steps() {return phy_Steps.iterator(); }
   public List getPhy_Steps() {return phy_Steps; }
   public void setPhy_Steps(List l) {phy_Steps = l; }

}
