package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * A project's environment definition
* Protege name: phy:Environment
* @author ontology bean generator
* @version 2010/05/3, 17:06:09
*/
public class Phy_Environment implements Concept {

   /**
   * All objects existing in this environment
* Protege name: phy:objects
   */
   private List phy_objects = new ArrayList();
   public void addPhy_objects(Phy_AbstractObject elem) { 
     List oldList = this.phy_objects;
     phy_objects.add(elem);
   }
   public boolean removePhy_objects(Phy_AbstractObject elem) {
     List oldList = this.phy_objects;
     boolean result = phy_objects.remove(elem);
     return result;
   }
   public void clearAllPhy_objects() {
     List oldList = this.phy_objects;
     phy_objects.clear();
   }
   public Iterator getAllPhy_objects() {return phy_objects.iterator(); }
   public List getPhy_objects() {return phy_objects; }
   public void setPhy_objects(List l) {phy_objects = l; }

   /**
* Protege name: phy:scale
   */
   private Phy_Scale phy_scale;
   public void setPhy_scale(Phy_Scale value) { 
    this.phy_scale=value;
   }
   public Phy_Scale getPhy_scale() {
     return this.phy_scale;
   }

   /**
   * The SVG document object
* Protege name: phy:svgDoc
   */
   private Object phy_svgDoc;
   public void setPhy_svgDoc(Object value) { 
    this.phy_svgDoc=value;
   }
   public Object getPhy_svgDoc() {
     return this.phy_svgDoc;
   }

   /**
   * Root of the project's environment representation, containing all it's EnvironmentObjects
* Protege name: phy:rootPlayground
   */
   private Phy_PlaygroundObject phy_rootPlayground;
   public void setPhy_rootPlayground(Phy_PlaygroundObject value) { 
    this.phy_rootPlayground=value;
   }
   public Phy_PlaygroundObject getPhy_rootPlayground() {
     return this.phy_rootPlayground;
   }

   /**
   * The project's name
* Protege name: phy:projectName
   */
   private String phy_projectName;
   public void setPhy_projectName(String value) { 
    this.phy_projectName=value;
   }
   public String getPhy_projectName() {
     return this.phy_projectName;
   }

}
