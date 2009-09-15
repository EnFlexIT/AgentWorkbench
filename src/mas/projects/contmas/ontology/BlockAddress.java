package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Address of a free slot or of a container occupying a slot in a bay map
* Protege name: BlockAddress
* @author ontology bean generator
* @version 2009/09/15, 02:37:50
*/
public class BlockAddress implements Concept {

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
* Protege name: locates
   */
   private List locates = new ArrayList();
   public void addLocates(Container elem) { 
     List oldList = this.locates;
     locates.add(elem);
   }
   public boolean removeLocates(Container elem) {
     List oldList = this.locates;
     boolean result = locates.remove(elem);
     return result;
   }
   public void clearAllLocates() {
     List oldList = this.locates;
     locates.clear();
   }
   public Iterator getAllLocates() {return locates.iterator(); }
   public List getLocates() {return locates; }
   public void setLocates(List l) {locates = l; }

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

}
