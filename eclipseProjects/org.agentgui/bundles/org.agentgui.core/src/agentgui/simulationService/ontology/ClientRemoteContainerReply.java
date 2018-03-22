package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: ClientRemoteContainerReply
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class ClientRemoteContainerReply implements AgentAction {

   /**
* Protege name: remotePID
   */
   private String remotePID;
   public void setRemotePID(String value) { 
    this.remotePID=value;
   }
   public String getRemotePID() {
     return this.remotePID;
   }

   /**
* Protege name: remoteAddress
   */
   private PlatformAddress remoteAddress;
   public void setRemoteAddress(PlatformAddress value) { 
    this.remoteAddress=value;
   }
   public PlatformAddress getRemoteAddress() {
     return this.remoteAddress;
   }

   /**
* Protege name: remoteBenchmarkResult
   */
   private BenchmarkResult remoteBenchmarkResult;
   public void setRemoteBenchmarkResult(BenchmarkResult value) { 
    this.remoteBenchmarkResult=value;
   }
   public BenchmarkResult getRemoteBenchmarkResult() {
     return this.remoteBenchmarkResult;
   }

   /**
* Protege name: remotePerformance
   */
   private PlatformPerformance remotePerformance;
   public void setRemotePerformance(PlatformPerformance value) { 
    this.remotePerformance=value;
   }
   public PlatformPerformance getRemotePerformance() {
     return this.remotePerformance;
   }

   /**
* Protege name: remoteAgentGuiVersion
   */
   private AgentGuiVersion remoteAgentGuiVersion;
   public void setRemoteAgentGuiVersion(AgentGuiVersion value) { 
    this.remoteAgentGuiVersion=value;
   }
   public AgentGuiVersion getRemoteAgentGuiVersion() {
     return this.remoteAgentGuiVersion;
   }

   /**
* Protege name: remoteOS
   */
   private OSInfo remoteOS;
   public void setRemoteOS(OSInfo value) { 
    this.remoteOS=value;
   }
   public OSInfo getRemoteOS() {
     return this.remoteOS;
   }

   /**
* Protege name: remoteContainerName
   */
   private String remoteContainerName;
   public void setRemoteContainerName(String value) { 
    this.remoteContainerName=value;
   }
   public String getRemoteContainerName() {
     return this.remoteContainerName;
   }

}
