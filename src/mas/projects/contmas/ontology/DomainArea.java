package mas.projects.contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: DomainArea
* @author ontology bean generator
* @version 2009/10/5, 23:56:08
*/
public class DomainArea implements Concept {

   /**
   * end of available domainarea
* Protege name: to
   */
   private String to;
   public void setTo(String value) { 
    this.to=value;
   }
   public String getTo() {
     return this.to;
   }

   /**
* Protege name: part_of
   */
   private DomainArea part_of;
   public void setPart_of(DomainArea value) { 
    this.part_of=value;
   }
   public DomainArea getPart_of() {
     return this.part_of;
   }

   /**
   * start of available domainarea
* Protege name: from
   */
   private String from;
   public void setFrom(String value) { 
    this.from=value;
   }
   public String getFrom() {
     return this.from;
   }

}
