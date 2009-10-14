package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Address of a free slot or of a container occupying a slot in a bay map
* Protege name: BlockAddress
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class BlockAddress implements Concept {

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
* Protege name: locates
   */
   private Container locates;
   public void setLocates(Container value) { 
    this.locates=value;
   }
   public Container getLocates() {
     return this.locates;
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

}
