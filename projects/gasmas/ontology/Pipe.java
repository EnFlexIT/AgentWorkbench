package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Pipe
* @author ontology bean generator
* @version 2012/06/4, 15:20:38
*/
public class Pipe extends GridComponent{ 

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

}
