package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A set of parameters describing the physical properties of a PropagationPoint.
* Protege name: StructuralParameters
* @author ontology bean generator
* @version 2012/01/4, 17:18:05
*/
public class StructuralParameters implements Concept {

   /**
   * The geographical y coordinate of the propagation point.
* Protege name: diameter
   */
   private float diameter;
   public void setDiameter(float value) { 
    this.diameter=value;
   }
   public float getDiameter() {
     return this.diameter;
   }

   /**
* Protege name: atParentLengthPos
   */
   private float atParentLengthPos;
   public void setAtParentLengthPos(float value) { 
    this.atParentLengthPos=value;
   }
   public float getAtParentLengthPos() {
     return this.atParentLengthPos;
   }

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
