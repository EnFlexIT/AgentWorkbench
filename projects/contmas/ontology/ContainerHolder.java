package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ContainerHolder
* @author ontology bean generator
* @version 2010/02/28, 13:48:47
*/
public class ContainerHolder implements Concept {

   /**
   * the states of all containers, affected by this ContainerHolder
* Protege name: container_states
   */
   private List container_states = new ArrayList();
   public void addContainer_states(TOCHasState elem) { 
     List oldList = this.container_states;
     container_states.add(elem);
   }
   public boolean removeContainer_states(TOCHasState elem) {
     List oldList = this.container_states;
     boolean result = container_states.remove(elem);
     return result;
   }
   public void clearAllContainer_states() {
     List oldList = this.container_states;
     container_states.clear();
   }
   public Iterator getAllContainer_states() {return container_states.iterator(); }
   public List getContainer_states() {return container_states; }
   public void setContainer_states(List l) {container_states = l; }

   /**
   * the domain, in which the containerholder is located
* Protege name: lives_in
   */
   private Domain lives_in;
   public void setLives_in(Domain value) { 
    this.lives_in=value;
   }
   public Domain getLives_in() {
     return this.lives_in;
   }

   /**
   * the storage area provided by the holder
* Protege name: contains
   */
   private BayMap contains;
   public void setContains(BayMap value) { 
    this.contains=value;
   }
   public BayMap getContains() {
     return this.contains;
   }

   /**
   * agents, with which this containerholder is able to interoperate
* Protege name: contractors
   */
   private List contractors = new ArrayList();
   public void addContractors(AID elem) { 
     List oldList = this.contractors;
     contractors.add(elem);
   }
   public boolean removeContractors(AID elem) {
     List oldList = this.contractors;
     boolean result = contractors.remove(elem);
     return result;
   }
   public void clearAllContractors() {
     List oldList = this.contractors;
     contractors.clear();
   }
   public Iterator getAllContractors() {return contractors.iterator(); }
   public List getContractors() {return contractors; }
   public void setContractors(List l) {contractors = l; }

}
