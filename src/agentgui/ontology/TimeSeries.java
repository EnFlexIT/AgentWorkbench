package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeries
* @author ontology bean generator
* @version 2012/04/1, 18:52:07
*/
public class TimeSeries extends Chart{ 

   /**
* Protege name: valueAxisDataTable
   */
   private NumericDataTable valueAxisDataTable;
   public void setValueAxisDataTable(NumericDataTable value) { 
    this.valueAxisDataTable=value;
   }
   public NumericDataTable getValueAxisDataTable() {
     return this.valueAxisDataTable;
   }

   /**
* Protege name: valueAxisLineWidth
   */
   private List valueAxisLineWidth = new ArrayList();
   public void addValueAxisLineWidth(Float elem) { 
     List oldList = this.valueAxisLineWidth;
     valueAxisLineWidth.add(elem);
   }
   public boolean removeValueAxisLineWidth(Float elem) {
     List oldList = this.valueAxisLineWidth;
     boolean result = valueAxisLineWidth.remove(elem);
     return result;
   }
   public void clearAllValueAxisLineWidth() {
     List oldList = this.valueAxisLineWidth;
     valueAxisLineWidth.clear();
   }
   public Iterator getAllValueAxisLineWidth() {return valueAxisLineWidth.iterator(); }
   public List getValueAxisLineWidth() {return valueAxisLineWidth; }
   public void setValueAxisLineWidth(List l) {valueAxisLineWidth = l; }

   /**
* Protege name: valueAxisUnits
   */
   private List valueAxisUnits = new ArrayList();
   public void addValueAxisUnits(String elem) { 
     List oldList = this.valueAxisUnits;
     valueAxisUnits.add(elem);
   }
   public boolean removeValueAxisUnits(String elem) {
     List oldList = this.valueAxisUnits;
     boolean result = valueAxisUnits.remove(elem);
     return result;
   }
   public void clearAllValueAxisUnits() {
     List oldList = this.valueAxisUnits;
     valueAxisUnits.clear();
   }
   public Iterator getAllValueAxisUnits() {return valueAxisUnits.iterator(); }
   public List getValueAxisUnits() {return valueAxisUnits; }
   public void setValueAxisUnits(List l) {valueAxisUnits = l; }

   /**
* Protege name: timeAxis
   */
   private List timeAxis = new ArrayList();
   public void addTimeAxis(Float elem) { 
     List oldList = this.timeAxis;
     timeAxis.add(elem);
   }
   public boolean removeTimeAxis(Float elem) {
     List oldList = this.timeAxis;
     boolean result = timeAxis.remove(elem);
     return result;
   }
   public void clearAllTimeAxis() {
     List oldList = this.timeAxis;
     timeAxis.clear();
   }
   public Iterator getAllTimeAxis() {return timeAxis.iterator(); }
   public List getTimeAxis() {return timeAxis; }
   public void setTimeAxis(List l) {timeAxis = l; }

   /**
* Protege name: timeAxisLabel
   */
   private String timeAxisLabel;
   public void setTimeAxisLabel(String value) { 
    this.timeAxisLabel=value;
   }
   public String getTimeAxisLabel() {
     return this.timeAxisLabel;
   }

   /**
* Protege name: valueAxisColors
   */
   private List valueAxisColors = new ArrayList();
   public void addValueAxisColors(String elem) { 
     List oldList = this.valueAxisColors;
     valueAxisColors.add(elem);
   }
   public boolean removeValueAxisColors(String elem) {
     List oldList = this.valueAxisColors;
     boolean result = valueAxisColors.remove(elem);
     return result;
   }
   public void clearAllValueAxisColors() {
     List oldList = this.valueAxisColors;
     valueAxisColors.clear();
   }
   public Iterator getAllValueAxisColors() {return valueAxisColors.iterator(); }
   public List getValueAxisColors() {return valueAxisColors; }
   public void setValueAxisColors(List l) {valueAxisColors = l; }

   /**
* Protege name: valueAxisLabel
   */
   private String valueAxisLabel;
   public void setValueAxisLabel(String value) { 
    this.valueAxisLabel=value;
   }
   public String getValueAxisLabel() {
     return this.valueAxisLabel;
   }

   /**
* Protege name: chartTitle
   */
   private String chartTitle;
   public void setChartTitle(String value) { 
    this.chartTitle=value;
   }
   public String getChartTitle() {
     return this.chartTitle;
   }

   /**
* Protege name: timeFormat
   */
   private List timeFormat = new ArrayList();
   public void addTimeFormat(String elem) { 
     List oldList = this.timeFormat;
     timeFormat.add(elem);
   }
   public boolean removeTimeFormat(String elem) {
     List oldList = this.timeFormat;
     boolean result = timeFormat.remove(elem);
     return result;
   }
   public void clearAllTimeFormat() {
     List oldList = this.timeFormat;
     timeFormat.clear();
   }
   public Iterator getAllTimeFormat() {return timeFormat.iterator(); }
   public List getTimeFormat() {return timeFormat; }
   public void setTimeFormat(List l) {timeFormat = l; }

   /**
* Protege name: valueAxisDescriptions
   */
   private List valueAxisDescriptions = new ArrayList();
   public void addValueAxisDescriptions(String elem) { 
     List oldList = this.valueAxisDescriptions;
     valueAxisDescriptions.add(elem);
   }
   public boolean removeValueAxisDescriptions(String elem) {
     List oldList = this.valueAxisDescriptions;
     boolean result = valueAxisDescriptions.remove(elem);
     return result;
   }
   public void clearAllValueAxisDescriptions() {
     List oldList = this.valueAxisDescriptions;
     valueAxisDescriptions.clear();
   }
   public Iterator getAllValueAxisDescriptions() {return valueAxisDescriptions.iterator(); }
   public List getValueAxisDescriptions() {return valueAxisDescriptions; }
   public void setValueAxisDescriptions(List l) {valueAxisDescriptions = l; }

}
