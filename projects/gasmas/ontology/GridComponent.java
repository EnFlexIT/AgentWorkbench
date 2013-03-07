package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: GridComponent
* @author ontology bean generator
* @version 2013/03/7, 00:03:01
*/
public class GridComponent implements Concept {

//////////////////////////// User code
public boolean isEmpty() {
	if (this.iD==null | this.iD.equals("")) {
		return true;
	}
	return false;
}
   /**
* Protege name: ID
   */
   private String iD;
   public void setID(String value) { 
    this.iD=value;
   }
   public String getID() {
     return this.iD;
   }

   /**
* Protege name: alias
   */
   private String alias;
   public void setAlias(String value) { 
    this.alias=value;
   }
   public String getAlias() {
     return this.alias;
   }

}
