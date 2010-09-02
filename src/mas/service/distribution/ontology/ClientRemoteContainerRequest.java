package mas.service.distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRemoteContainerRequest
* @author ontology bean generator
* @version 2010/09/2, 16:06:42
*/
public class ClientRemoteContainerRequest implements AgentAction {

   /**
* Protege name: RemoteConfig
   */
   private RemoteContainerConfig remoteConfig;
   public void setRemoteConfig(RemoteContainerConfig value) { 
    this.remoteConfig=value;
   }
   public RemoteContainerConfig getRemoteConfig() {
     return this.remoteConfig;
   }

}
