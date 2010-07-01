package distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SlaveRegister
* @author ontology bean generator
* @version 2010/06/30, 19:28:08
*/
public class SlaveRegister implements AgentAction {

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

}
