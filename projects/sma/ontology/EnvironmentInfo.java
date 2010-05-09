package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EnvironmentInfo
* @author ontology bean generator
* @version 2010/05/9, 17:23:54
*/
public class EnvironmentInfo implements AgentAction {

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
