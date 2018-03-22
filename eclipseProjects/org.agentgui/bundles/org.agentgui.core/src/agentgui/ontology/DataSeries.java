package agentgui.ontology;


import jade.content.*;

/**
   * This class represents one data series, containing x and y values
* Protege name: DataSeries
* @author ontology bean generator
* @version 2013/10/8, 13:06:41
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
