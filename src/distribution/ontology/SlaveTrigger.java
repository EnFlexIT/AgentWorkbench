package distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SlaveTrigger
* @author ontology bean generator
* @version 2010/06/30, 19:28:08
*/
public class SlaveTrigger implements AgentAction {

   /**
* Protege name: triggerTime
   */
   private PlatformTime triggerTime;
   public void setTriggerTime(PlatformTime value) { 
    this.triggerTime=value;
   }
   public PlatformTime getTriggerTime() {
     return this.triggerTime;
   }

}
