package sma.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A project's environment definition
* Protege name: Environment
* @author ontology bean generator
* @version 2010/05/12, 16:14:24
*/
public class Environment implements Concept {

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
* Protege name: svgDoc
   */
   private String svgDoc;
   public void setSvgDoc(String value) { 
    this.svgDoc=value;
   }
   public String getSvgDoc() {
     return this.svgDoc;
   }

}
