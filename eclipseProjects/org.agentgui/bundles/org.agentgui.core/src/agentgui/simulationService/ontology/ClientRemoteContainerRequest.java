package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: ClientRemoteContainerRequest
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
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
