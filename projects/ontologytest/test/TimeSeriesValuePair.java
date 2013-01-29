package ontologytest.test;



/**
* One single data item in a time series.
* Protege name: TimeSeriesValuePair
* @author OntologyBeanGenerator v4.1
* @version 2013/01/21, 11:05:48
*/
public interface TimeSeriesValuePair extends ValuePair {

   /**
   * Protege name: timestamp
   */
   public void setTimestamp(Simple_Long value);
   public Simple_Long getTimestamp();

   /**
   * Protege name: value
   */
   public void setValue(Simple_Float value);
   public Simple_Float getValue();

}
