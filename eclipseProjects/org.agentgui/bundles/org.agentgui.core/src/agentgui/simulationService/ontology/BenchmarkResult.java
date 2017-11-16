package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: BenchmarkResult
* @author ontology bean generator
* @version 2017/11/16, 10:27:58
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
