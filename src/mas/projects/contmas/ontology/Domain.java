package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * the domain, in which the containerholder is located
* Protege name: Domain
* @author ontology bean generator
* @version 2009/08/25, 14:09:13
*/
public class Domain implements Concept {

   /**
* Protege name: has_subdomains
   */
   private List has_subdomains = new ArrayList();
   public void addHas_subdomains(Subdomain elem) { 
     List oldList = this.has_subdomains;
     has_subdomains.add(elem);
   }
   public boolean removeHas_subdomains(Subdomain elem) {
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

}
