package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: LookForContainer
* @author ontology bean generator
* @version 2009/10/13, 22:18:34
*/
public class LookForContainer implements AgentAction {

   /**
   * container to be looked for
* Protege name: searched_after
   */
   private Container searched_after;
   public void setSearched_after(Container value) { 
    this.searched_after=value;
   }
   public Container getSearched_after() {
     return this.searched_after;
   }

}
