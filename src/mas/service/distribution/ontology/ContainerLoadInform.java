package mas.service.distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ContainerLoadInform
* @author ontology bean generator
* @version 2010/09/2, 16:06:42
*/
public class ContainerLoadInform implements AgentAction {

   /**
* Protege name: containerName
   */
   private String containerName;
   public void setContainerName(String value) { 
    this.containerName=value;
   }
   public String getContainerName() {
     return this.containerName;
   }

   /**
* Protege name: currentLoad
   */
   private PlatformLoad currentLoad;
   public void setCurrentLoad(PlatformLoad value) { 
    this.currentLoad=value;
   }
   public PlatformLoad getCurrentLoad() {
     return this.currentLoad;
   }

}
