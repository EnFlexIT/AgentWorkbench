package de.enflexit.awb.simulation.ontology;


import jade.content.*;

/**
* Protege name: Version
* @author ontology bean generator
* @version 2024/02/9, 15:58:57
*/
public class Version implements Concept {

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
