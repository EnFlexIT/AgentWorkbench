package agentgui.ontology;

/**
* Protege name: XyValuePair
* @author ontology bean generator
* @version 2013/10/8, 13:06:41
*/
public class XyValuePair extends ValuePair{ 

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

}
