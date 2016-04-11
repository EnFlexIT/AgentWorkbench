package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRegister
* @author ontology bean generator
* @version 2016/04/11, 16:51:02
*/
public class ClientRegister implements AgentAction {

   /**
* Protege name: clientPerformance
   */
   private PlatformPerformance clientPerformance;
   public void setClientPerformance(PlatformPerformance value) { 
    this.clientPerformance=value;
   }
   public PlatformPerformance getClientPerformance() {
     return this.clientPerformance;
   }

   /**
* Protege name: clientTime
   */
   private PlatformTime clientTime;
   public void setClientTime(PlatformTime value) { 
    this.clientTime=value;
   }
   public PlatformTime getClientTime() {
     return this.clientTime;
   }

   /**
* Protege name: clientVersion
   */
   private AgentGuiVersion clientVersion;
   public void setClientVersion(AgentGuiVersion value) { 
    this.clientVersion=value;
   }
   public AgentGuiVersion getClientVersion() {
     return this.clientVersion;
   }

   /**
* Protege name: clientAddress
   */
   private PlatformAddress clientAddress;
   public void setClientAddress(PlatformAddress value) { 
    this.clientAddress=value;
   }
   public PlatformAddress getClientAddress() {
     return this.clientAddress;
   }

   /**
* Protege name: clientOS
   */
   private OSInfo clientOS;
   public void setClientOS(OSInfo value) { 
    this.clientOS=value;
   }
   public OSInfo getClientOS() {
     return this.clientOS;
   }

}
