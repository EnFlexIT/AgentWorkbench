package agentgui.physical2Denvironment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * An object's size.
* Protege name: Size
* @author ontology bean generator
* @version 2010/11/4, 22:28:26
*/
public class Size implements Concept {

   /**
   * The object's height
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
   * The object's width
* Protege name: width
   */
   private float width;
   public void setWidth(float value) { 
    this.width=value;
   }
   public float getWidth() {
     return this.width;
   }

}
