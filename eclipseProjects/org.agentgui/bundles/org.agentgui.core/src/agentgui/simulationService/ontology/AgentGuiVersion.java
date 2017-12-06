package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AgentGuiVersion
* @author ontology bean generator
* @version 2017/12/6, 09:15:27
*/
public class AgentGuiVersion implements Concept {

   /**
* Protege name: minorRevision
   */
   private int minorRevision;
   public void setMinorRevision(int value) { 
    this.minorRevision=value;
   }
   public int getMinorRevision() {
     return this.minorRevision;
   }

   /**
* Protege name: microRevision
   */
   private int microRevision;
   public void setMicroRevision(int value) { 
    this.microRevision=value;
   }
   public int getMicroRevision() {
     return this.microRevision;
   }

   /**
* Protege name: qualifier
   */
   private String qualifier;
   public void setQualifier(String value) { 
    this.qualifier=value;
   }
   public String getQualifier() {
     return this.qualifier;
   }

   /**
* Protege name: majorRevision
   */
   private int majorRevision;
   public void setMajorRevision(int value) { 
    this.majorRevision=value;
   }
   public int getMajorRevision() {
     return this.majorRevision;
   }

}
