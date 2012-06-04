package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: DirectionSettingNotification
* @author ontology bean generator
* @version 2012/06/4, 15:20:38
*/
public class DirectionSettingNotification implements AgentAction {

   /**
* Protege name: directionNote
   */
   private Notification directionNote;
   public void setDirectionNote(Notification value) { 
    this.directionNote=value;
   }
   public Notification getDirectionNote() {
     return this.directionNote;
   }

}
