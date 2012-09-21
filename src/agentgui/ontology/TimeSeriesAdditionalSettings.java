package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesAdditionalSettings
* @author ontology bean generator
* @version 2012/09/21, 16:08:41
*/
public class TimeSeriesAdditionalSettings extends ChartSettingSpecial{ 

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

}
