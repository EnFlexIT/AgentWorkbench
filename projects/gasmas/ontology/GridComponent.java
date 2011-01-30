package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This is the abstract superclass for any kind of grid component.
* Protege name: GridComponent
* @author ontology bean generator
* @version 2011/01/27, 22:34:19
*/
public class GridComponent implements Concept {

   /**
* Protege name: position1
   */
   private PropagationPoint position1;
   public void setPosition1(PropagationPoint value) { 
    this.position1=value;
   }
   public PropagationPoint getPosition1() {
     return this.position1;
   }

   /**
   * The GridComponent's uinique ID
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
