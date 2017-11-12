package agentgui.simulationService.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: PlatformAddress
* @author ontology bean generator
* @version 2017/11/12, 21:16:28
*/
public class PlatformAddress implements Concept {

   /**
* Protege name: ip
   */
   private String ip;
   public void setIp(String value) { 
    this.ip=value;
   }
   public String getIp() {
     return this.ip;
   }

   /**
* Protege name: port
   */
   private int port;
   public void setPort(int value) { 
    this.port=value;
   }
   public int getPort() {
     return this.port;
   }

   /**
* Protege name: http4mtp
   */
   private String http4mtp;
   public void setHttp4mtp(String value) { 
    this.http4mtp=value;
   }
   public String getHttp4mtp() {
     return this.http4mtp;
   }

   /**
* Protege name: url
   */
   private String url;
   public void setUrl(String value) { 
    this.url=value;
   }
   public String getUrl() {
     return this.url;
   }

}
