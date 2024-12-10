package de.enflexit.awb.simulation.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: BenchmarkResult
* @author ontology bean generator
* @version 2024/02/9, 15:58:57
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
