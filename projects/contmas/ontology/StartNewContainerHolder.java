package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: StartNewContainerHolder
* @author ontology bean generator
* @version 2010/04/18, 11:36:17
*/
public class StartNewContainerHolder implements AgentAction {

   /**
* Protege name: to_be_added
   */
   private ContainerHolder to_be_added;
   public void setTo_be_added(ContainerHolder value) { 
    this.to_be_added=value;
   }
   public ContainerHolder getTo_be_added() {
     return this.to_be_added;
   }

   /**
* Protege name: name
   */
   private String name;
   public void setName(String value) { 
    this.name=value;
   }
   public String getName() {
     return this.name;
   }

   /**
* Protege name: populate
   */
   private boolean populate;
   public void setPopulate(boolean value) { 
    this.populate=value;
   }
   public boolean getPopulate() {
     return this.populate;
   }

   /**
* Protege name: randomize
   */
   private boolean randomize;
   public void setRandomize(boolean value) { 
    this.randomize=value;
   }
   public boolean getRandomize() {
     return this.randomize;
   }

}
