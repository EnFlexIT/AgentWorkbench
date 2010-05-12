package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Defining the scale used for displaying the environment
* Protege name: Scale
* @author ontology bean generator
* @version 2010/05/12, 16:14:34
*/
public class Scale implements Concept {

   /**
   * Name of the used real world unit. Just for display, no automatic conversion at the moment
* Protege name: unit
   */
   private String unit;
   public void setUnit(String value) { 
    this.unit=value;
   }
   public String getUnit() {
     return this.unit;
   }

   /**
   * The number of pixel standing for the number of real world untis specified in value
* Protege name: pixel
   */
   private float pixel;
   public void setPixel(float value) { 
    this.pixel=value;
   }
   public float getPixel() {
     return this.pixel;
   }

   /**
   * The number of real world untis represented by the number of pixels specified in pixels
* Protege name: value
   */
   private float value;
   public void setValue(float value) { 
    this.value=value;
   }
   public float getValue() {
     return this.value;
   }

}
