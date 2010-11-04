package mas.service.distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PlatformPerformance
* @author ontology bean generator
* @version 2010/09/2, 16:06:42
*/
public class PlatformPerformance implements Concept {

   /**
* Protege name: cpu_vendor
   */
   private String cpu_vendor;
   public void setCpu_vendor(String value) { 
    this.cpu_vendor=value;
   }
   public String getCpu_vendor() {
     return this.cpu_vendor;
   }

   /**
* Protege name: cpu_speedMhz
   */
   private int cpu_speedMhz;
   public void setCpu_speedMhz(int value) { 
    this.cpu_speedMhz=value;
   }
   public int getCpu_speedMhz() {
     return this.cpu_speedMhz;
   }

   /**
* Protege name: memory_totalMB
   */
   private int memory_totalMB;
   public void setMemory_totalMB(int value) { 
    this.memory_totalMB=value;
   }
   public int getMemory_totalMB() {
     return this.memory_totalMB;
   }

   /**
* Protege name: cpu_model
   */
   private String cpu_model;
   public void setCpu_model(String value) { 
    this.cpu_model=value;
   }
   public String getCpu_model() {
     return this.cpu_model;
   }

   /**
* Protege name: cpu_numberOf
   */
   private int cpu_numberOf;
   public void setCpu_numberOf(int value) { 
    this.cpu_numberOf=value;
   }
   public int getCpu_numberOf() {
     return this.cpu_numberOf;
   }

}
