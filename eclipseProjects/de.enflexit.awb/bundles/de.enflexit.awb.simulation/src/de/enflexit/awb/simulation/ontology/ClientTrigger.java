package de.enflexit.awb.simulation.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClientTrigger
* @author ontology bean generator
* @version 2024/02/9, 15:58:57
*/
public class ClientTrigger implements AgentAction {

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

}
