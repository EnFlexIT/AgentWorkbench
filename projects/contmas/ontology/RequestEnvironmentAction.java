package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestEnvironmentAction
* @author ontology bean generator
* @version 2010/06/9, 14:40:33
*/
public class RequestEnvironmentAction implements AgentAction {

   /**
* Protege name: action
   */
   private String action;
   public void setAction(String value) { 
    this.action=value;
   }
   public String getAction() {
     return this.action;
   }

   /**
* Protege name: affected_agents
   */
   private List affected_agents = new ArrayList();
   public void addAffected_agents(AID elem) { 
     List oldList = this.affected_agents;
     affected_agents.add(elem);
   }
   public boolean removeAffected_agents(AID elem) {
     List oldList = this.affected_agents;
     boolean result = affected_agents.remove(elem);
     return result;
   }
   public void clearAllAffected_agents() {
     List oldList = this.affected_agents;
     affected_agents.clear();
   }
   public Iterator getAllAffected_agents() {return affected_agents.iterator(); }
   public List getAffected_agents() {return affected_agents; }
   public void setAffected_agents(List l) {affected_agents = l; }

}
