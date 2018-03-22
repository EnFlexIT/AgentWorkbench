package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: SlaveRegister
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class SlaveRegister implements AgentAction {

   /**
* Protege name: slaveVersion
   */
   private AgentGuiVersion slaveVersion;
   public void setSlaveVersion(AgentGuiVersion value) { 
    this.slaveVersion=value;
   }
   public AgentGuiVersion getSlaveVersion() {
     return this.slaveVersion;
   }

   /**
* Protege name: slaveTime
   */
   private PlatformTime slaveTime;
   public void setSlaveTime(PlatformTime value) { 
    this.slaveTime=value;
   }
   public PlatformTime getSlaveTime() {
     return this.slaveTime;
   }

   /**
* Protege name: slaveOS
   */
   private OSInfo slaveOS;
   public void setSlaveOS(OSInfo value) { 
    this.slaveOS=value;
   }
   public OSInfo getSlaveOS() {
     return this.slaveOS;
   }

   /**
* Protege name: slavePerformance
   */
   private PlatformPerformance slavePerformance;
   public void setSlavePerformance(PlatformPerformance value) { 
    this.slavePerformance=value;
   }
   public PlatformPerformance getSlavePerformance() {
     return this.slavePerformance;
   }

   /**
* Protege name: slaveAddress
   */
   private PlatformAddress slaveAddress;
   public void setSlaveAddress(PlatformAddress value) { 
    this.slaveAddress=value;
   }
   public PlatformAddress getSlaveAddress() {
     return this.slaveAddress;
   }

}
