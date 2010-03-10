package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Container
* @author ontology bean generator
* @version 2010/03/9, 21:12:43
*/
public class Container implements Concept {

   /**
* Protege name: height
   */
   private float height;
   public void setHeight(float value) { 
    this.height=value;
   }
   public float getHeight() {
     return this.height;
   }

   /**
* Protege name: bic_code
   */
   private String bic_code;
   public void setBic_code(String value) { 
    this.bic_code=value;
   }
   public String getBic_code() {
     return this.bic_code;
   }

   /**
* Protege name: width
   */
   private float width;
   public void setWidth(float value) { 
    this.width=value;
   }
   public float getWidth() {
     return this.width;
   }

   /**
* Protege name: weight
   */
   private float weight;
   public void setWeight(float value) { 
    this.weight=value;
   }
   public float getWeight() {
     return this.weight;
   }

   /**
* Protege name: length
   */
   private float length;
   public void setLength(float value) { 
    this.length=value;
   }
   public float getLength() {
     return this.length;
   }

}
