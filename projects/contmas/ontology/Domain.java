package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Domain
* @author ontology bean generator
* @version 2010/03/27, 20:53:54
*/
public class Domain implements Concept {

   /**
* Protege name: lies_in
   */
   private Domain lies_in;
   public void setLies_in(Domain value) { 
    this.lies_in=value;
   }
   public Domain getLies_in() {
     return this.lies_in;
   }

   /**
* Protege name: has_subdomains
   */
   private List has_subdomains = new ArrayList();
   public void addHas_subdomains(Domain elem) { 
     List oldList = this.has_subdomains;
     has_subdomains.add(elem);
   }
   public boolean removeHas_subdomains(Domain elem) {
     List oldList = this.has_subdomains;
     boolean result = has_subdomains.remove(elem);
     return result;
   }
   public void clearAllHas_subdomains() {
     List oldList = this.has_subdomains;
     has_subdomains.clear();
   }
   public Iterator getAllHas_subdomains() {return has_subdomains.iterator(); }
   public List getHas_subdomains() {return has_subdomains; }
   public void setHas_subdomains(List l) {has_subdomains = l; }

   /**
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
