package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Movement
* @author ontology bean generator
* @version 2010/05/3, 22:21:54
*/
public class Movement implements Concept {

   /**
* Protege name: be_there_at
   */
   private String be_there_at;
   public void setBe_there_at(String value) { 
    this.be_there_at=value;
   }
   public String getBe_there_at() {
     return this.be_there_at;
   }

   /**
* Protege name: move_to
   */
   private Phy_Position move_to;
   public void setMove_to(Phy_Position value) { 
    this.move_to=value;
   }
   public Phy_Position getMove_to() {
     return this.move_to;
   }

}
