package agentgui.gasgridEnvironment.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This is the abstract superclass for any kind of grid component.
* Protege name: GridComponent
* @author ontology bean generator
* @version 2010/12/30, 20:54:11
*/
public class GridComponent implements Concept {

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
