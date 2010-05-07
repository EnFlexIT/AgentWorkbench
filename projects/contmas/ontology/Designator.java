package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Designator
* @author ontology bean generator
* @version 2010/05/7, 17:07:53
*/
public class Designator implements Concept {

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
