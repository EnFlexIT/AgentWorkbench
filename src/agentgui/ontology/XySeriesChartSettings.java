package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XySeriesChartSettings
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
*/
public class XySeriesChartSettings extends ChartSettingsGeneral{ 

   /**
* Protege name: allowDuplicateXValues
   */
   private boolean allowDuplicateXValues;
   public void setAllowDuplicateXValues(boolean value) { 
    this.allowDuplicateXValues=value;
   }
   public boolean getAllowDuplicateXValues() {
     return this.allowDuplicateXValues;
   }

   /**
* Protege name: autoSort
   */
   private boolean autoSort;
   public void setAutoSort(boolean value) { 
    this.autoSort=value;
   }
   public boolean getAutoSort() {
     return this.autoSort;
   }

}
