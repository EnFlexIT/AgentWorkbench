package mas.display.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * Represents a 2D envoronment or subenvironment, can contain all kinds of EnvironmentObjects
* Protege name: PlaygroundObject
* @author ontology bean generator
* @version 2010/05/23, 13:16:47
*/
public class PlaygroundObject extends AbstractObject{ 

//////////////////////////// User code
/** 	 * Setting the child object's parent property to null to avoid problems with recursion when serializing 	 * @param root The playground containing the child objects 	 */    public void unsetParent(){ 	   java.util.Iterator<AbstractObject> objects = getAllChildObjects(); 	   while(objects.hasNext()){ 		   AbstractObject object = objects.next(); 		   object.setParent(null); 		   if(mas.environment.ObjectTypes.getType(object) == mas.environment.ObjectTypes.PLAYGROUND){ 			   ((PlaygroundObject)object).unsetParent(); 		   } 	   }    }        /** 	 * Setting the child object's parent property 	 * @param root The playground containing the child objects 	 */    public void setParent(){ 	   java.util.Iterator<AbstractObject> objects = getAllChildObjects(); 	   while(objects.hasNext()){ 		   AbstractObject object = objects.next(); 		   object.setParent(this); 		   if(mas.environment.ObjectTypes.getType(object) == mas.environment.ObjectTypes.PLAYGROUND){ 			   ((PlaygroundObject)object).setParent(); 		   } 		    	   }    }  /** 	 * Checks if an environment object is inside a playgrounds area 	 * @param object The environment object 	 * @return True if the object is inside the playgrounds area, false otherwise 	 */    public boolean contains(AbstractObject object){ 	   java.awt.geom.Area pgArea = new java.awt.geom.Area( 			   new java.awt.geom.Rectangle2D.Float( 					   getPosition().getX(),  					   getPosition().getY(),  					   getSize().getWidth(),  					   getSize().getHeight() 			   ) 		); 	   java.awt.geom.Rectangle2D.Float objectRect = new java.awt.geom.Rectangle2D.Float( 				object.getPosition().getX(),  				object.getPosition().getY(),  				object.getSize().getWidth(),  				object.getSize().getHeight() 		); 		 		return pgArea.contains(objectRect);	       }
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
