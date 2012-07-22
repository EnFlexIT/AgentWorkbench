package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: OSInfo
* @author ontology bean generator
* @version 2012/07/23, 00:15:35
*/
public class OSInfo implements Concept {

   /**
* Protege name: os_version
   */
   private String os_version;
   public void setOs_version(String value) { 
    this.os_version=value;
   }
   public String getOs_version() {
     return this.os_version;
   }

   /**
* Protege name: os_arch
   */
   private String os_arch;
   public void setOs_arch(String value) { 
    this.os_arch=value;
   }
   public String getOs_arch() {
     return this.os_arch;
   }

   /**
* Protege name: os_name
   */
   private String os_name;
   public void setOs_name(String value) { 
    this.os_name=value;
   }
   public String getOs_name() {
     return this.os_name;
   }

}
