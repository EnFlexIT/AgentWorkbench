package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ValueType
* @author ontology bean generator
* @version 2012/10/24, 18:22:30
*/
public class ValueType implements Concept {

   /**
* Protege name: Unit
   */
   private String unit;
   public void setUnit(String value) { 
    this.unit=value;
   }
   public String getUnit() {
     return this.unit;
   }

   /**
* Protege name: Value
   */
   private float value;
   public void setValue(float value) { 
    this.value=value;
   }
   public float getValue() {
     return this.value;
   }

}
