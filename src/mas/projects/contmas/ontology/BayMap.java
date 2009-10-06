package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: BayMap
* @author ontology bean generator
* @version 2009/10/5, 23:56:07
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
   * BlockAdresses and therefore containers this bay map contains
* Protege name: is_filled_with
   */
   private List is_filled_with = new ArrayList();
   public void addIs_filled_with(BlockAddress elem) { 
     List oldList = this.is_filled_with;
     is_filled_with.add(elem);
   }
   public boolean removeIs_filled_with(BlockAddress elem) {
     List oldList = this.is_filled_with;
     boolean result = is_filled_with.remove(elem);
     return result;
   }
   public void clearAllIs_filled_with() {
     List oldList = this.is_filled_with;
     is_filled_with.clear();
   }
   public Iterator getAllIs_filled_with() {return is_filled_with.iterator(); }
   public List getIs_filled_with() {return is_filled_with; }
   public void setIs_filled_with(List l) {is_filled_with = l; }

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
