package agentgui.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: XyValuePair
* @author ontology bean generator
* @version 2013/09/12, 17:14:05
*/
public class XyValuePair extends ValuePair{ 

   /**
* Protege name: yValue
   */
   private Simple_Float yValue;
   public void setYValue(Simple_Float value) { 
    this.yValue=value;
   }
   public Simple_Float getYValue() {
     return this.yValue;
   }

   /**
* Protege name: xValue
   */
   private Simple_Float xValue;
   public void setXValue(Simple_Float value) { 
    this.xValue=value;
   }
   public Simple_Float getXValue() {
     return this.xValue;
   }

}
