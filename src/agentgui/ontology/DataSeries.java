package agentgui.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents one data series, containing x and y values
* Protege name: DataSeries
* @author ontology bean generator
* @version 2013/10/1, 11:28:40
*/
public class DataSeries implements Concept {

   /**
* Protege name: unit
   */
   private String unit;
   public void setUnit(String value) { 
    this.unit=value;
   }
   public String getUnit() {
     return this.unit;
   }

   /**
   * The data series label
* Protege name: label
   */
   private String label;
   public void setLabel(String value) { 
    this.label=value;
   }
   public String getLabel() {
     return this.label;
   }

}
