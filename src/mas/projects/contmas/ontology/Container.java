package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Container
* @author ontology bean generator
* @version 2009/10/5, 23:56:08
*/
public class Container implements Concept {

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
* Protege name: is_coolable
   */
   private boolean is_coolable;
   public void setIs_coolable(boolean value) { 
    this.is_coolable=value;
   }
   public boolean getIs_coolable() {
     return this.is_coolable;
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
* Protege name: carries_dangerous_goods
   */
   private boolean carries_dangerous_goods;
   public void setCarries_dangerous_goods(boolean value) { 
    this.carries_dangerous_goods=value;
   }
   public boolean getCarries_dangerous_goods() {
     return this.carries_dangerous_goods;
   }

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
   * LSCU 107737 9
* Protege name: id
   */
   private String id;
   public void setId(String value) { 
    this.id=value;
   }
   public String getId() {
     return this.id;
   }

}
