package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: ClientTrigger
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class ClientTrigger implements AgentAction {

   /**
* Protege name: clientBenchmarkValue
   */
   private BenchmarkResult clientBenchmarkValue;
   public void setClientBenchmarkValue(BenchmarkResult value) { 
    this.clientBenchmarkValue=value;
   }
   public BenchmarkResult getClientBenchmarkValue() {
     return this.clientBenchmarkValue;
   }

   /**
* Protege name: clientLoad
   */
   private PlatformLoad clientLoad;
   public void setClientLoad(PlatformLoad value) { 
    this.clientLoad=value;
   }
   public PlatformLoad getClientLoad() {
     return this.clientLoad;
   }

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
