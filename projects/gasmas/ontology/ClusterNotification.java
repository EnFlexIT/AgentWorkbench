package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ClusterNotification
* @author ontology bean generator
* @version 2012/06/4, 15:20:38
*/
public class ClusterNotification implements AgentAction {

   /**
* Protege name: clusterNote
   */
   private Notification clusterNote;
   public void setClusterNote(Notification value) { 
    this.clusterNote=value;
   }
   public Notification getClusterNote() {
     return this.clusterNote;
   }

}
