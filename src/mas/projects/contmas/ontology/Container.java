package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Container
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class Container implements Concept {

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
* Protege name: is_coolable
   */
   private boolean is_coolable;
   public void setIs_coolable(boolean value) { 
    this.is_coolable=value;
   }
   public boolean getIs_coolable() {
     return this.is_coolable;
   }

}
