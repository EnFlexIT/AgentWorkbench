package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesChartSettings
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
*/
public class TimeSeriesChartSettings extends ChartSettingsGeneral{ 

   /**
* Protege name: timeFormat
   */
   private String timeFormat;
   public void setTimeFormat(String value) { 
    this.timeFormat=value;
   }
   public String getTimeFormat() {
     return this.timeFormat;
   }

}
