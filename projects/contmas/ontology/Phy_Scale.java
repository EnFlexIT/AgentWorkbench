package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Defining the scale used for displaying the environment
* Protege name: phy:Scale
* @author ontology bean generator
* @version 2010/06/9, 14:40:33
*/
public class Phy_Scale implements Concept {

   /**
   * The number of pixel standing for the number of real world untis specified in value
* Protege name: phy:pixel
   */
   private float phy_pixel;
   public void setPhy_pixel(float value) { 
    this.phy_pixel=value;
   }
   public float getPhy_pixel() {
     return this.phy_pixel;
   }

   /**
   * Name of the used real world unit. Just for display, no automatic conversion at the moment
* Protege name: phy:unit
   */
   private String phy_unit;
   public void setPhy_unit(String value) { 
    this.phy_unit=value;
   }
   public String getPhy_unit() {
     return this.phy_unit;
   }

   /**
   * The number of real world untis represented by the number of pixels specified in pixels
* Protege name: phy:value
   */
   private float phy_value;
   public void setPhy_value(float value) { 
    this.phy_value=value;
   }
   public float getPhy_value() {
     return this.phy_value;
   }

}
