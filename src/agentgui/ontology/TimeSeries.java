package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeries
* @author ontology bean generator
* @version 2012/02/25, 19:42:36
*/
public class TimeSeries extends Chart{ 

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
* Protege name: yAxisDescriptions
   */
   private List yAxisDescriptions = new ArrayList();
   public void addYAxisDescriptions(String elem) { 
     List oldList = this.yAxisDescriptions;
     yAxisDescriptions.add(elem);
   }
   public boolean removeYAxisDescriptions(String elem) {
     List oldList = this.yAxisDescriptions;
     boolean result = yAxisDescriptions.remove(elem);
     return result;
   }
   public void clearAllYAxisDescriptions() {
     List oldList = this.yAxisDescriptions;
     yAxisDescriptions.clear();
   }
   public Iterator getAllYAxisDescriptions() {return yAxisDescriptions.iterator(); }
   public List getYAxisDescriptions() {return yAxisDescriptions; }
   public void setYAxisDescriptions(List l) {yAxisDescriptions = l; }

   /**
* Protege name: yAxisDataTable
   */
   private NumericDataTable yAxisDataTable;
   public void setYAxisDataTable(NumericDataTable value) { 
    this.yAxisDataTable=value;
   }
   public NumericDataTable getYAxisDataTable() {
     return this.yAxisDataTable;
   }

   /**
* Protege name: yAxisColors
   */
   private List yAxisColors = new ArrayList();
   public void addYAxisColors(String elem) { 
     List oldList = this.yAxisColors;
     yAxisColors.add(elem);
   }
   public boolean removeYAxisColors(String elem) {
     List oldList = this.yAxisColors;
     boolean result = yAxisColors.remove(elem);
     return result;
   }
   public void clearAllYAxisColors() {
     List oldList = this.yAxisColors;
     yAxisColors.clear();
   }
   public Iterator getAllYAxisColors() {return yAxisColors.iterator(); }
   public List getYAxisColors() {return yAxisColors; }
   public void setYAxisColors(List l) {yAxisColors = l; }

   /**
* Protege name: yAxisLineWidth
   */
   private List yAxisLineWidth = new ArrayList();
   public void addYAxisLineWidth(Float elem) { 
     List oldList = this.yAxisLineWidth;
     yAxisLineWidth.add(elem);
   }
   public boolean removeYAxisLineWidth(Float elem) {
     List oldList = this.yAxisLineWidth;
     boolean result = yAxisLineWidth.remove(elem);
     return result;
   }
   public void clearAllYAxisLineWidth() {
     List oldList = this.yAxisLineWidth;
     yAxisLineWidth.clear();
   }
   public Iterator getAllYAxisLineWidth() {return yAxisLineWidth.iterator(); }
   public List getYAxisLineWidth() {return yAxisLineWidth; }
   public void setYAxisLineWidth(List l) {yAxisLineWidth = l; }

   /**
* Protege name: yAxisUnits
   */
   private List yAxisUnits = new ArrayList();
   public void addYAxisUnits(String elem) { 
     List oldList = this.yAxisUnits;
     yAxisUnits.add(elem);
   }
   public boolean removeYAxisUnits(String elem) {
     List oldList = this.yAxisUnits;
     boolean result = yAxisUnits.remove(elem);
     return result;
   }
   public void clearAllYAxisUnits() {
     List oldList = this.yAxisUnits;
     yAxisUnits.clear();
   }
   public Iterator getAllYAxisUnits() {return yAxisUnits.iterator(); }
   public List getYAxisUnits() {return yAxisUnits; }
   public void setYAxisUnits(List l) {yAxisUnits = l; }

   /**
* Protege name: timeAxis
   */
   private List timeAxis = new ArrayList();
   public void addTimeAxis(String elem) { 
     List oldList = this.timeAxis;
     timeAxis.add(elem);
   }
   public boolean removeTimeAxis(String elem) {
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

}
