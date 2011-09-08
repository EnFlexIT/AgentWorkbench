package agentgui.envModel.p2Dsvg.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents a (sub)environment that is part of the environment model. It is a container for other environment objects, i.e. instances of subclasses of Physical2DObject. As it is a subclass of Physical2DObject itself, a PlaygroundObject may contain other PlaygroundObjects.
* Protege name: PlaygroundObject
* @author ontology bean generator
* @version 2010/11/17, 20:10:06
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
