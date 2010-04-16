package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: phy:Position
* @author ontology bean generator
* @version 2010/04/16, 17:21:34
*/
public class Phy_Position implements Concept {

   /**
* Protege name: phy:z_dimension
   */
   private float phy_z_dimension;
   public void setPhy_z_dimension(float value) { 
    this.phy_z_dimension=value;
   }
   public float getPhy_z_dimension() {
     return this.phy_z_dimension;
   }

   /**
* Protege name: phy:y_dimension
   */
   private float phy_y_dimension;
   public void setPhy_y_dimension(float value) { 
    this.phy_y_dimension=value;
   }
   public float getPhy_y_dimension() {
     return this.phy_y_dimension;
   }

   /**
* Protege name: phy:x_dimension
   */
   private float phy_x_dimension;
   public void setPhy_x_dimension(float value) { 
    this.phy_x_dimension=value;
   }
   public float getPhy_x_dimension() {
     return this.phy_x_dimension;
   }

}
