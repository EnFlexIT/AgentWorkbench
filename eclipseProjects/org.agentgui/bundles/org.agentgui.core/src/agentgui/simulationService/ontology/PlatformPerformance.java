package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: PlatformPerformance
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class PlatformPerformance implements Concept {

   /**
* Protege name: cpu_numberOfLogicalCores
   */
   private int cpu_numberOfLogicalCores;
   public void setCpu_numberOfLogicalCores(int value) { 
    this.cpu_numberOfLogicalCores=value;
   }
   public int getCpu_numberOfLogicalCores() {
     return this.cpu_numberOfLogicalCores;
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
* Protege name: cpu_numberOfPhysicalCores
   */
   private int cpu_numberOfPhysicalCores;
   public void setCpu_numberOfPhysicalCores(int value) { 
    this.cpu_numberOfPhysicalCores=value;
   }
   public int getCpu_numberOfPhysicalCores() {
     return this.cpu_numberOfPhysicalCores;
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
* Protege name: cpu_processorName
   */
   private String cpu_processorName;
   public void setCpu_processorName(String value) { 
    this.cpu_processorName=value;
   }
   public String getCpu_processorName() {
     return this.cpu_processorName;
   }

}
