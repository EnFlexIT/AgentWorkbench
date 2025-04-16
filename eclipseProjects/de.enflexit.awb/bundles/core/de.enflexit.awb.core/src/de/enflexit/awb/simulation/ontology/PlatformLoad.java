package de.enflexit.awb.simulation.ontology;


import jade.content.*;

/**
* Protege name: PlatformLoad
* @author ontology bean generator
* @version 2024/02/9, 15:58:57
*/
public class PlatformLoad implements Concept {

   /**
* Protege name: loadMemoryJVM
   */
   private float loadMemoryJVM;
   public void setLoadMemoryJVM(float value) { 
    this.loadMemoryJVM=value;
   }
   public float getLoadMemoryJVM() {
     return this.loadMemoryJVM;
   }

   /**
* Protege name: loadMemorySystem
   */
   private float loadMemorySystem;
   public void setLoadMemorySystem(float value) { 
    this.loadMemorySystem=value;
   }
   public float getLoadMemorySystem() {
     return this.loadMemorySystem;
   }

   /**
* Protege name: available
   */
   private boolean available;
   public void setAvailable(boolean value) { 
    this.available=value;
   }
   public boolean getAvailable() {
     return this.available;
   }

   /**
* Protege name: loadNoThreads
   */
   private int loadNoThreads;
   public void setLoadNoThreads(int value) { 
    this.loadNoThreads=value;
   }
   public int getLoadNoThreads() {
     return this.loadNoThreads;
   }

   /**
* Protege name: loadExceeded
   */
   private int loadExceeded;
   public void setLoadExceeded(int value) { 
    this.loadExceeded=value;
   }
   public int getLoadExceeded() {
     return this.loadExceeded;
   }

   /**
* Protege name: loadCPU
   */
   private float loadCPU;
   public void setLoadCPU(float value) { 
    this.loadCPU=value;
   }
   public float getLoadCPU() {
     return this.loadCPU;
   }

}
