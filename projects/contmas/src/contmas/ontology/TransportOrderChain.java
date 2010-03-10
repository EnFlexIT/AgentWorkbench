package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TransportOrderChain
* @author ontology bean generator
* @version 2010/03/9, 21:12:43
*/
public class TransportOrderChain implements Concept {

   /**
* Protege name: terminates_at
   */
   private String terminates_at;
   public void setTerminates_at(String value) { 
    this.terminates_at=value;
   }
   public String getTerminates_at() {
     return this.terminates_at;
   }

   /**
* Protege name: is_linked_by
   */
   private List is_linked_by = new ArrayList();
   public void addIs_linked_by(TransportOrder elem) { 
     List oldList = this.is_linked_by;
     is_linked_by.add(elem);
   }
   public boolean removeIs_linked_by(TransportOrder elem) {
     List oldList = this.is_linked_by;
     boolean result = is_linked_by.remove(elem);
     return result;
   }
   public void clearAllIs_linked_by() {
     List oldList = this.is_linked_by;
     is_linked_by.clear();
   }
   public Iterator getAllIs_linked_by() {return is_linked_by.iterator(); }
   public List getIs_linked_by() {return is_linked_by; }
   public void setIs_linked_by(List l) {is_linked_by = l; }

   /**
* Protege name: transports
   */
   private Container transports;
   public void setTransports(Container value) { 
    this.transports=value;
   }
   public Container getTransports() {
     return this.transports;
   }

}
