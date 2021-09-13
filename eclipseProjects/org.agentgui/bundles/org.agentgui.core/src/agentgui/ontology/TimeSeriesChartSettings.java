package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimeSeriesChartSettings
* @author ontology bean generator
* @version 2019/09/27, 17:26:05
*/
@SuppressWarnings("unused")
public class TimeSeriesChartSettings extends ChartSettingsGeneral{ 

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
