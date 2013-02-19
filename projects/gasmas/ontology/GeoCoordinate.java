package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A set of parameters describing the physical properties of a PropagationPoint.
* Protege name: GeoCoordinate
* @author ontology bean generator
* @version 2013/02/19, 16:35:18
*/
public class GeoCoordinate implements Concept {

   /**
   * The geographical x coordinate of the propagation point.
* Protege name: geoX
   */
   private float geoX;
   public void setGeoX(float value) { 
    this.geoX=value;
   }
   public float getGeoX() {
     return this.geoX;
   }

   /**
* Protege name: geoW
   */
   private float geoW;
   public void setGeoW(float value) { 
    this.geoW=value;
   }
   public float getGeoW() {
     return this.geoW;
   }

   /**
   * The geographical z coordinate of the propagation point.
* Protege name: geoZ
   */
   private float geoZ;
   public void setGeoZ(float value) { 
    this.geoZ=value;
   }
   public float getGeoZ() {
     return this.geoZ;
   }

   /**
   * The geographical y coordinate of the propagation point.
* Protege name: geoY
   */
   private float geoY;
   public void setGeoY(float value) { 
    this.geoY=value;
   }
   public float getGeoY() {
     return this.geoY;
   }

}
