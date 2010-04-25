package sma.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents a 2D envoronment or subenvironment, can contain all kinds of EnvironmentObjects
* Protege name: PlaygroundObject
* @author ontology bean generator
* @version 2010/04/25, 13:46:18
*/
public class PlaygroundObject extends AbstractObject{ 

   /**
   * List of EnvironmentObjects inside this PlaygroundObject
* Protege name: childObjects
   */
   private List childObjects = new ArrayList();
   public void addChildObjects(AbstractObject elem) { 
     List oldList = this.childObjects;
     childObjects.add(elem);
   }
   public boolean removeChildObjects(AbstractObject elem) {
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
