package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Movement
* @author ontology bean generator
* @version 2010/03/4, 21:33:23
*/
public class Movement implements Concept {

   /**
   * Position change
* Protege name: speed
   */
   private Speed speed;
   public void setSpeed(Speed value) { 
    this.speed=value;
   }
   public Speed getSpeed() {
     return this.speed;
   }

   /**
   * Start position
* Protege name: startPos
   */
   private Position startPos;
   public void setStartPos(Position value) { 
    this.startPos=value;
   }
   public Position getStartPos() {
     return this.startPos;
   }

}
