package mas.service.distribution.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RemoteContainerConfig
* @author ontology bean generator
* @version 2010/09/7, 14:14:03
*/
public class RemoteContainerConfig implements Concept {

   /**
* Protege name: jadeContainerName
   */
   private String jadeContainerName;
   public void setJadeContainerName(String value) { 
    this.jadeContainerName=value;
   }
   public String getJadeContainerName() {
     return this.jadeContainerName;
   }

   /**
* Protege name: jadeServices
   */
   private String jadeServices;
   public void setJadeServices(String value) { 
    this.jadeServices=value;
   }
   public String getJadeServices() {
     return this.jadeServices;
   }

   /**
* Protege name: jvmMemAllocMaximum
   */
   private String jvmMemAllocMaximum;
   public void setJvmMemAllocMaximum(String value) { 
    this.jvmMemAllocMaximum=value;
   }
   public String getJvmMemAllocMaximum() {
     return this.jvmMemAllocMaximum;
   }

   /**
* Protege name: jadeShowGUI
   */
   private boolean jadeShowGUI;
   public void setJadeShowGUI(boolean value) { 
    this.jadeShowGUI=value;
   }
   public boolean getJadeShowGUI() {
     return this.jadeShowGUI;
   }

   /**
* Protege name: jadePort
   */
   private String jadePort;
   public void setJadePort(String value) { 
    this.jadePort=value;
   }
   public String getJadePort() {
     return this.jadePort;
   }

   /**
* Protege name: jadeIsRemoteContainer
   */
   private boolean jadeIsRemoteContainer;
   public void setJadeIsRemoteContainer(boolean value) { 
    this.jadeIsRemoteContainer=value;
   }
   public boolean getJadeIsRemoteContainer() {
     return this.jadeIsRemoteContainer;
   }

   /**
* Protege name: jvmMemAllocInitial
   */
   private String jvmMemAllocInitial;
   public void setJvmMemAllocInitial(String value) { 
    this.jvmMemAllocInitial=value;
   }
   public String getJvmMemAllocInitial() {
     return this.jvmMemAllocInitial;
   }

   /**
* Protege name: jadeHost
   */
   private String jadeHost;
   public void setJadeHost(String value) { 
    this.jadeHost=value;
   }
   public String getJadeHost() {
     return this.jadeHost;
   }

}
