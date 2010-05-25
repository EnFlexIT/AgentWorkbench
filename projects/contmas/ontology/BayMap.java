package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: BayMap
* @author ontology bean generator
* @version 2010/05/25, 12:32:32
*/
public class BayMap implements Concept {

   /**
* Protege name: y_dimension
   */
   private int y_dimension;
   public void setY_dimension(int value) { 
    this.y_dimension=value;
   }
   public int getY_dimension() {
     return this.y_dimension;
   }

   /**
* Protege name: x_dimension
   */
   private int x_dimension;
   public void setX_dimension(int value) { 
    this.x_dimension=value;
   }
   public int getX_dimension() {
     return this.x_dimension;
   }

   /**
* Protege name: z_dimension
   */
   private int z_dimension;
   public void setZ_dimension(int value) { 
    this.z_dimension=value;
   }
   public int getZ_dimension() {
     return this.z_dimension;
   }

}
