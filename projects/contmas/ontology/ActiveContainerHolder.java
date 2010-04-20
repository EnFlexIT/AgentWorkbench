package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ActiveContainerHolder
* @author ontology bean generator
* @version 2010/04/20, 14:23:52
*/
public class ActiveContainerHolder extends ContainerHolder{ 

   /**
* Protege name: capable_of
   */
   private List capable_of = new ArrayList();
   public void addCapable_of(Domain elem) { 
     List oldList = this.capable_of;
     capable_of.add(elem);
   }
   public boolean removeCapable_of(Domain elem) {
     List oldList = this.capable_of;
     boolean result = capable_of.remove(elem);
     return result;
   }
   public void clearAllCapable_of() {
     List oldList = this.capable_of;
     capable_of.clear();
   }
   public Iterator getAllCapable_of() {return capable_of.iterator(); }
   public List getCapable_of() {return capable_of; }
   public void setCapable_of(List l) {capable_of = l; }

}
