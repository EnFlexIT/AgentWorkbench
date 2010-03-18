package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A project's environment definition
* Protege name: Environment
* @author ontology bean generator
* @version 2010/03/16, 20:55:13
*/
public class Environment implements Concept {

   /**
   * The SVG document object
* Protege name: svgDoc
   */
   private Object svgDoc;
   public void setSvgDoc(Object value) { 
    this.svgDoc=value;
   }
   public Object getSvgDoc() {
     return this.svgDoc;
   }

   /**
   * Root of the project's environment representation, containing all it's EnvironmentObjects
* Protege name: rootPlayground
   */
   private PlaygroundObject rootPlayground;
   public void setRootPlayground(PlaygroundObject value) { 
    this.rootPlayground=value;
   }
   public PlaygroundObject getRootPlayground() {
     return this.rootPlayground;
   }

   /**
   * The project's name
* Protege name: projectName
   */
   private String projectName;
   public void setProjectName(String value) { 
    this.projectName=value;
   }
   public String getProjectName() {
     return this.projectName;
   }

   /**
   * All objects existing in this environment
* Protege name: objects
   */
   private List objects = new ArrayList();
   public void addObjects(AbstractObject elem) { 
     List oldList = this.objects;
     objects.add(elem);
   }
   public boolean removeObjects(AbstractObject elem) {
     List oldList = this.objects;
     boolean result = objects.remove(elem);
     return result;
   }
   public void clearAllObjects() {
     List oldList = this.objects;
     objects.clear();
   }
   public Iterator getAllObjects() {return objects.iterator(); }
   public List getObjects() {return objects; }
   public void setObjects(List l) {objects = l; }

}
