package mas.projects.contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * can manipulate containers, can move them between domains, e.g. crane
* Protege name: ActiveContainerHolder
* @author ontology bean generator
* @version 2009/08/25, 14:09:13
*/
public class ActiveContainerHolder extends ContainerHolder{ 

   /**
* Protege name: tonnage_capacity
   */
   private float tonnage_capacity;
   public void setTonnage_capacity(float value) { 
    this.tonnage_capacity=value;
   }
   public float getTonnage_capacity() {
     return this.tonnage_capacity;
   }

   /**
   * domains, in which containers can be manipulated
* Protege name: capable_of
   */
   private List capable_of = new ArrayList();
   public void addCapable_of(Object elem) { 
     List oldList = this.capable_of;
     capable_of.add(elem);
   }
   public boolean removeCapable_of(Object elem) {
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

   /**
   * x-over-y
capability of device to move x container above y
* Protege name: stowage_capability
   */
   private String stowage_capability;
   public void setStowage_capability(String value) { 
    this.stowage_capability=value;
   }
   public String getStowage_capability() {
     return this.stowage_capability;
   }

}
