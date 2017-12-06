package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PlatformTime
* @author ontology bean generator
* @version 2017/12/6, 09:15:27
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
