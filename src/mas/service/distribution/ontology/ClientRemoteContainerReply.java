package mas.service.distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRemoteContainerReply
* @author ontology bean generator
* @version 2010/09/14, 14:55:17
*/
public class ClientRemoteContainerReply implements AgentAction {

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
