package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ProvideBlockAdress
* @author ontology bean generator
* @version 2010/05/13, 14:50:09
*/
public class ProvideBlockAdress implements AgentAction {

   /**
* Protege name: suiting_address
   */
   private BlockAddress suiting_address;
   public void setSuiting_address(BlockAddress value) { 
    this.suiting_address=value;
   }
   public BlockAddress getSuiting_address() {
     return this.suiting_address;
   }

}
