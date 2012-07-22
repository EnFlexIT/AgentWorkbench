package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AgentGuiVersion
* @author ontology bean generator
* @version 2012/07/23, 00:15:35
*/
public class AgentGuiVersion implements Concept {

   /**
* Protege name: buildNo
   */
   private int buildNo;
   public void setBuildNo(int value) { 
    this.buildNo=value;
   }
   public int getBuildNo() {
     return this.buildNo;
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

}
