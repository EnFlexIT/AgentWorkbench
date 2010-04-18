package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * 
* Protege name: phy:AbstractObject
* @author ontology bean generator
* @version 2010/04/18, 11:36:16
*/
public class Phy_AbstractObject implements Concept {

   /**
   * This EnvironmentObject's center position
* Protege name: phy:position
   */
   private Phy_Position phy_position;
   public void setPhy_position(Phy_Position value) { 
    this.phy_position=value;
   }
   public Phy_Position getPhy_position() {
     return this.phy_position;
   }

   /**
   * Unique id to identify this object
* Protege name: phy:id
   */
   private String phy_id;
   public void setPhy_id(String value) { 
    this.phy_id=value;
   }
   public String getPhy_id() {
     return this.phy_id;
   }

   /**
   * This EnvironmentObject's size
* Protege name: phy:size
   */
   private Phy_Size phy_size;
   public void setPhy_size(Phy_Size value) { 
    this.phy_size=value;
   }
   public Phy_Size getPhy_size() {
     return this.phy_size;
   }

   /**
   * The playground containing this EnvironmentObject
* Protege name: phy:parent
   */
   private Phy_PlaygroundObject phy_parent;
   public void setPhy_parent(Phy_PlaygroundObject value) { 
    this.phy_parent=value;
   }
   public Phy_PlaygroundObject getPhy_parent() {
     return this.phy_parent;
   }

}
