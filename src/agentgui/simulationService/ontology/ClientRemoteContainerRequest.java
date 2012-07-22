package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRemoteContainerRequest
* @author ontology bean generator
* @version 2012/07/23, 00:15:35
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
