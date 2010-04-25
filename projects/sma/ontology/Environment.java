package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A project's environment definition
* Protege name: Environment
* @author ontology bean generator
* @version 2010/04/25, 13:46:18
*/
public class Environment implements Concept {

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
* Protege name: scale
   */
   private Scale scale;
   public void setScale(Scale value) { 
    this.scale=value;
   }
   public Scale getScale() {
     return this.scale;
   }

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

}
