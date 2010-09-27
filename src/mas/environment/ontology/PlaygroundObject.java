package mas.environment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents an area inside a physical 2D (sub)environment that can contain all kinds of physical 2D objects, i.e. instances of subclasses of Physical2DObject.
* Protege name: PlaygroundObject
* @author ontology bean generator
* @version 2010/09/21, 16:26:57
*/
public class PlaygroundObject extends Physical2DObject{ 

   /**
   * The physical 2D objects existing in this (sub)environment
* Protege name: childObjects
   */
   private List childObjects = new ArrayList();
   public void addChildObjects(Physical2DObject elem) { 
     List oldList = this.childObjects;
     childObjects.add(elem);
   }
   public boolean removeChildObjects(Physical2DObject elem) {
     List oldList = this.childObjects;
     boolean result = childObjects.remove(elem);
     return result;
   }
   public void clearAllChildObjects() {
     List oldList = this.childObjects;
     childObjects.clear();
   }
   public Iterator getAllChildObjects() {return childObjects.iterator(); }
   public List getChildObjects() {return childObjects; }
   public void setChildObjects(List l) {childObjects = l; }

}
