package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: BenchmarkResult
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class BenchmarkResult implements Concept {

   /**
* Protege name: benchmarkValue
   */
   private float benchmarkValue;
   public void setBenchmarkValue(float value) { 
    this.benchmarkValue=value;
   }
   public float getBenchmarkValue() {
     return this.benchmarkValue;
   }

}
