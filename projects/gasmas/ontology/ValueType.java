package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ValueType
* @author ontology bean generator
* @version 2013/03/10, 21:16:57
*/
public class ValueType implements Concept {

//////////////////////////// User code
@Override
public String toString() {
	String display = ((Float)this.getValue()).toString();
	if (this.getUnit()==null || this.getUnit().equals("")) {
		display += " []";
	} else {
		display += " [" + this.getUnit() + "]";
	}
	return display;
}
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
