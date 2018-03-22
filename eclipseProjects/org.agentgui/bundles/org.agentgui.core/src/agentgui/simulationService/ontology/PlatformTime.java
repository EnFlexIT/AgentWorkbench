package agentgui.simulationService.ontology;


import jade.content.*;

/**
* Protege name: PlatformTime
* @author ontology bean generator
* @version 2018/02/10, 18:49:04
*/
public class PlatformTime implements Concept {

   /**
* Protege name: TimeStampAsString
   */
   private String timeStampAsString;
   public void setTimeStampAsString(String value) { 
    this.timeStampAsString=value;
   }
   public String getTimeStampAsString() {
     return this.timeStampAsString;
   }

}
