package game_of_life.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: platformInfo
* @author ontology bean generator
* @version 2010/07/13, 12:36:14
*/
public class PlatformInfo implements Concept {

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
* Protege name: currentState
   */
   private int currentState;
   public void setCurrentState(int value) { 
    this.currentState=value;
   }
   public int getCurrentState() {
     return this.currentState;
   }

   /**
* Protege name: freeRAM
   */
   private float freeRAM;
   public void setFreeRAM(float value) { 
    this.freeRAM=value;
   }
   public float getFreeRAM() {
     return this.freeRAM;
   }

   /**
* Protege name: useRAM
   */
   private float useRAM;
   public void setUseRAM(float value) { 
    this.useRAM=value;
   }
   public float getUseRAM() {
     return this.useRAM;
   }

   /**
* Protege name: totalRAM
   */
   private float totalRAM;
   public void setTotalRAM(float value) { 
    this.totalRAM=value;
   }
   public float getTotalRAM() {
     return this.totalRAM;
   }

   /**
* Protege name: typOfMessage
   */
   private String typOfMessage;
   public void setTypOfMessage(String value) { 
    this.typOfMessage=value;
   }
   public String getTypOfMessage() {
     return this.typOfMessage;
   }

   /**
* Protege name: whatToDo
   */
   private String whatToDo;
   public void setWhatToDo(String value) { 
    this.whatToDo=value;
   }
   public String getWhatToDo() {
     return this.whatToDo;
   }

   /**
* Protege name: portNumber
   */
   private int portNumber;
   public void setPortNumber(int value) { 
    this.portNumber=value;
   }
   public int getPortNumber() {
     return this.portNumber;
   }

   /**
* Protege name: platformName
   */
   private String platformName;
   public void setPlatformName(String value) { 
    this.platformName=value;
   }
   public String getPlatformName() {
     return this.platformName;
   }

   /**
* Protege name: nextState
   */
   private int nextState;
   public void setNextState(int value) { 
    this.nextState=value;
   }
   public int getNextState() {
     return this.nextState;
   }

   /**
* Protege name: platformUrl
   */
   private String platformUrl;
   public void setPlatformUrl(String value) { 
    this.platformUrl=value;
   }
   public String getPlatformUrl() {
     return this.platformUrl;
   }

}
