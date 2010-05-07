package contmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents a 2D envoronment or subenvironment, can contain all kinds of EnvironmentObjects
* Protege name: phy:PlaygroundObject
* @author ontology bean generator
* @version 2010/05/7, 17:07:53
*/
public class Phy_PlaygroundObject extends Phy_AbstractObject{ 

   /**
   * List of EnvironmentObjects inside this PlaygroundObject
* Protege name: phy:childObjects
   */
   private List phy_childObjects = new ArrayList();
   public void addPhy_childObjects(Phy_AbstractObject elem) { 
     List oldList = this.phy_childObjects;
     phy_childObjects.add(elem);
   }
   public boolean removePhy_childObjects(Phy_AbstractObject elem) {
     List oldList = this.phy_childObjects;
     boolean result = phy_childObjects.remove(elem);
     return result;
   }
   public void clearAllPhy_childObjects() {
     List oldList = this.phy_childObjects;
     phy_childObjects.clear();
   }
   public Iterator getAllPhy_childObjects() {return phy_childObjects.iterator(); }
   public List getPhy_childObjects() {return phy_childObjects; }
   public void setPhy_childObjects(List l) {phy_childObjects = l; }

}
