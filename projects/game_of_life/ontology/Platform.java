package game_of_life.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: platform
* @author ontology bean generator
* @version 2010/07/15, 11:53:49
*/
public class Platform implements Predicate {

   /**
* Protege name: PlatformInfo
   */
   private PlatformInfo platformInfo;
   public void setPlatformInfo(PlatformInfo value) { 
    this.platformInfo=value;
   }
   public PlatformInfo getPlatformInfo() {
     return this.platformInfo;
   }

}
