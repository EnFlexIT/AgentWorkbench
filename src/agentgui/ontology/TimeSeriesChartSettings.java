package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesChartSettings
* @author ontology bean generator
* @version 2013/10/1, 11:28:40
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
