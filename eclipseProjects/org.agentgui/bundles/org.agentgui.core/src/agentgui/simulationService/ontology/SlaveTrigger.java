package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: SlaveTrigger
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
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

   /**
* Protege name: slaveBenchmarkValue
   */
   private BenchmarkResult slaveBenchmarkValue;
   public void setSlaveBenchmarkValue(BenchmarkResult value) { 
    this.slaveBenchmarkValue=value;
   }
   public BenchmarkResult getSlaveBenchmarkValue() {
     return this.slaveBenchmarkValue;
   }

   /**
* Protege name: slaveLoad
   */
   private PlatformLoad slaveLoad;
   public void setSlaveLoad(PlatformLoad value) { 
    this.slaveLoad=value;
   }
   public PlatformLoad getSlaveLoad() {
     return this.slaveLoad;
   }

}
