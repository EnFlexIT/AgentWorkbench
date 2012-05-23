package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Pipe
* @author ontology bean generator
* @version 2012/05/21, 12:49:00
*/
public class Pipe extends GridComponent{ 

   /**
* Protege name: length
   */
   private float length;
   public void setLength(float value) { 
    this.length=value;
   }
   public float getLength() {
     return this.length;
   }

   /**
   * The ResistanceParameters of this pipe.
* Protege name: resistance
   */
   private ResistanceParameters resistance;
   public void setResistance(ResistanceParameters value) { 
    this.resistance=value;
   }
   public ResistanceParameters getResistance() {
     return this.resistance;
   }

}
