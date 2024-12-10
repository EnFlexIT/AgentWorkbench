package de.enflexit.awb.simulation.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PlatformTime
* @author ontology bean generator
* @version 2024/02/9, 15:58:57
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
