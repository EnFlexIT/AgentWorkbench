package ontologytest.test;



/**
* This class represents one data series, containing x and y values
* Protege name: DataSeries
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface DataSeries extends jade.content.Concept {

   /**
   * Protege name: unit
   */
   public void setUnit(String value);
   public String getUnit();

   /**
   * The data series label
   * Protege name: label
   */
   public void setLabel(String value);
   public String getLabel();

}
