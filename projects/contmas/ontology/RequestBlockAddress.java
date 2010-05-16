package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RequestBlockAddress
* @author ontology bean generator
* @version 2010/05/16, 12:52:00
*/
public class RequestBlockAddress implements AgentAction {

   /**
* Protege name: subjected_toc
   */
   private TransportOrderChain subjected_toc;
   public void setSubjected_toc(TransportOrderChain value) { 
    this.subjected_toc=value;
   }
   public TransportOrderChain getSubjected_toc() {
     return this.subjected_toc;
   }

   /**
* Protege name: provides
   */
   private BayMap provides;
   public void setProvides(BayMap value) { 
    this.provides=value;
   }
   public BayMap getProvides() {
     return this.provides;
   }

   /**
* Protege name: provides_population
   */
   private List provides_population = new ArrayList();
   public void addProvides_population(TOCHasState elem) { 
     List oldList = this.provides_population;
     provides_population.add(elem);
   }
   public boolean removeProvides_population(TOCHasState elem) {
     List oldList = this.provides_population;
     boolean result = provides_population.remove(elem);
     return result;
   }
   public void clearAllProvides_population() {
     List oldList = this.provides_population;
     provides_population.clear();
   }
   public Iterator getAllProvides_population() {return provides_population.iterator(); }
   public List getProvides_population() {return provides_population; }
   public void setProvides_population(List l) {provides_population = l; }

}
