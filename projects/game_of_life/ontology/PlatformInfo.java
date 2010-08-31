package game_of_life.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: platformInfo
* @author ontology bean generator
* @version 2010/08/31, 16:02:20
*/
public class PlatformInfo implements Concept {

   /**
* Protege name: thresholdExceeded
   */
   private boolean thresholdExceeded;
   public void setThresholdExceeded(boolean value) { 
    this.thresholdExceeded=value;
   }
   public boolean getThresholdExceeded() {
     return this.thresholdExceeded;
   }

   /**
* Protege name: localContainerURL
   */
   private String localContainerURL;
   public void setLocalContainerURL(String value) { 
    this.localContainerURL=value;
   }
   public String getLocalContainerURL() {
     return this.localContainerURL;
   }

   /**
* Protege name: currentFreeMemory
   */
   private float currentFreeMemory;
   public void setCurrentFreeMemory(float value) { 
    this.currentFreeMemory=value;
   }
   public float getCurrentFreeMemory() {
     return this.currentFreeMemory;
   }

   /**
* Protege name: numberOfAgents
   */
   private int numberOfAgents;
   public void setNumberOfAgents(int value) { 
    this.numberOfAgents=value;
   }
   public int getNumberOfAgents() {
     return this.numberOfAgents;
   }

   /**
* Protege name: useMemory
   */
   private float useMemory;
   public void setUseMemory(float value) { 
    this.useMemory=value;
   }
   public float getUseMemory() {
     return this.useMemory;
   }

   /**
* Protege name: totalMemory
   */
   private float totalMemory;
   public void setTotalMemory(float value) { 
    this.totalMemory=value;
   }
   public float getTotalMemory() {
     return this.totalMemory;
   }

   /**
* Protege name: currentCpuIdleTime
   */
   private float currentCpuIdleTime;
   public void setCurrentCpuIdleTime(float value) { 
    this.currentCpuIdleTime=value;
   }
   public float getCurrentCpuIdleTime() {
     return this.currentCpuIdleTime;
   }

   /**
* Protege name: remoteContainerURL
   */
   private String remoteContainerURL;
   public void setRemoteContainerURL(String value) { 
    this.remoteContainerURL=value;
   }
   public String getRemoteContainerURL() {
     return this.remoteContainerURL;
   }

   /**
* Protege name: remoteContainerName
   */
   private String remoteContainerName;
   public void setRemoteContainerName(String value) { 
    this.remoteContainerName=value;
   }
   public String getRemoteContainerName() {
     return this.remoteContainerName;
   }

}
