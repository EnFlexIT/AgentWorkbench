package distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRemoteContainerRequest
* @author ontology bean generator
* @version 2010/08/5, 16:22:27
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
