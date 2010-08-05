package distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientRegister
* @author ontology bean generator
* @version 2010/08/3, 22:33:26
*/
public class ClientRegister implements AgentAction {

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
* Protege name: clientPerformance
   */
   private PlatformPerformance clientPerformance;
   public void setClientPerformance(PlatformPerformance value) { 
    this.clientPerformance=value;
   }
   public PlatformPerformance getClientPerformance() {
     return this.clientPerformance;
   }

}
