package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: phy:EnvironmentUpdate
* @author ontology bean generator
* @version 2010/05/17, 22:08:19
*/
public class Phy_EnvironmentUpdate implements AgentAction {

   /**
* Protege name: phy:environment
   */
   private Phy_Environment phy_environment;
   public void setPhy_environment(Phy_Environment value) { 
    this.phy_environment=value;
   }
   public Phy_Environment getPhy_environment() {
     return this.phy_environment;
   }

}
