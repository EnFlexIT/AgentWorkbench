package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EnvironmentUpdate
* @author ontology bean generator
* @version 2010/04/7, 20:19:12
*/
public class EnvironmentUpdate implements AgentAction {

   /**
* Protege name: environment
   */
   private Environment environment;
   public void setEnvironment(Environment value) { 
    this.environment=value;
   }
   public Environment getEnvironment() {
     return this.environment;
   }

}
