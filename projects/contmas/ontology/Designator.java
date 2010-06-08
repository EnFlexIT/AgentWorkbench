package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Designator
* @author ontology bean generator
* @version 2010/06/8, 22:03:06
*/
public class Designator implements Concept {

   /**
* Protege name: at_address
   */
   private BlockAddress at_address;
   public void setAt_address(BlockAddress value) { 
    this.at_address=value;
   }
   public BlockAddress getAt_address() {
     return this.at_address;
   }

   /**
* Protege name: abstract_designation
   */
   private Domain abstract_designation;
   public void setAbstract_designation(Domain value) { 
    this.abstract_designation=value;
   }
   public Domain getAbstract_designation() {
     return this.abstract_designation;
   }

   /**
* Protege name: concrete_designation
   */
   private AID concrete_designation;
   public void setConcrete_designation(AID value) { 
    this.concrete_designation=value;
   }
   public AID getConcrete_designation() {
     return this.concrete_designation;
   }

   /**
* Protege name: type
   */
   private String type;
   public void setType(String value) { 
    this.type=value;
   }
   public String getType() {
     return this.type;
   }

}
