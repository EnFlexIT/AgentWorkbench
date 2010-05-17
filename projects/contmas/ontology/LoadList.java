package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: LoadList
* @author ontology bean generator
* @version 2010/05/17, 22:08:19
*/
public class LoadList implements Concept {

   /**
* Protege name: consists_of
   */
   private List consists_of = new ArrayList();
   public void addConsists_of(TransportOrderChain elem) { 
     List oldList = this.consists_of;
     consists_of.add(elem);
   }
   public boolean removeConsists_of(TransportOrderChain elem) {
     List oldList = this.consists_of;
     boolean result = consists_of.remove(elem);
     return result;
   }
   public void clearAllConsists_of() {
     List oldList = this.consists_of;
     consists_of.clear();
   }
   public Iterator getAllConsists_of() {return consists_of.iterator(); }
   public List getConsists_of() {return consists_of; }
   public void setConsists_of(List l) {consists_of = l; }

   /**
* Protege name: next_step
   */
   private LoadList next_step;
   public void setNext_step(LoadList value) { 
    this.next_step=value;
   }
   public LoadList getNext_step() {
     return this.next_step;
   }

}
