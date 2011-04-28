package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A set of parameters describing properties of a gas.
* Protege name: FluidParameters
* @author ontology bean generator
* @version 2011/04/28, 16:09:11
*/
public class FluidParameters implements Concept {

   /**
* Protege name: dynamicViscosity
   */
   private float dynamicViscosity;
   public void setDynamicViscosity(float value) { 
    this.dynamicViscosity=value;
   }
   public float getDynamicViscosity() {
     return this.dynamicViscosity;
   }

   /**
* Protege name: thermalConductivity
   */
   private float thermalConductivity;
   public void setThermalConductivity(float value) { 
    this.thermalConductivity=value;
   }
   public float getThermalConductivity() {
     return this.thermalConductivity;
   }

   /**
* Protege name: density
   */
   private float density;
   public void setDensity(float value) { 
    this.density=value;
   }
   public float getDensity() {
     return this.density;
   }

   /**
* Protege name: kinematicViscosity
   */
   private float kinematicViscosity;
   public void setKinematicViscosity(float value) { 
    this.kinematicViscosity=value;
   }
   public float getKinematicViscosity() {
     return this.kinematicViscosity;
   }

}
