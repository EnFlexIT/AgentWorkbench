package mas.display;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Defining SVG representation for different agent types
 * @author nils
 *
 */
public class AgentSVGDefinitions {
	// SVG namespace definition
	public static String svgNs = SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	
	/**
	 * Creating an SVG element to represent an agent  
	 * @param type	Type name, defining which SVG element represents the agent 
	 * @param xPos 	Horizontal position
	 * @param yPos	Vertical position 
	 * @param doc	SVG Document reference
	 * @return 		SVG element representing the agent
	 */
	public static Element getSVG(String type, String xPos, String yPos, Document doc){
		Element agentSVG = null;
		if(type.equals("A")){
			agentSVG = doc.createElementNS(svgNs, "rect");
			agentSVG.setAttributeNS(null, "width", "20");
			agentSVG.setAttributeNS(null, "height", "20");
			agentSVG.setAttributeNS(null, "x", xPos);
			agentSVG.setAttributeNS(null, "y", yPos);
			agentSVG.setAttributeNS(null, "style", "fill:red");
		}
		if(type.equals("B")){
			agentSVG = doc.createElementNS(svgNs, "rect");
			agentSVG.setAttributeNS(null, "width", "50");
			agentSVG.setAttributeNS(null, "height", "5");
			agentSVG.setAttributeNS(null, "x", ""+xPos);
			agentSVG.setAttributeNS(null, "y", ""+yPos);
			agentSVG.setAttributeNS(null, "style", "fill:blue");			
		}
		if(type.equals("C")){
			agentSVG = doc.createElementNS(svgNs, "circle");
			agentSVG.setAttributeNS(null, "r", "15");
			agentSVG.setAttributeNS(null, "cx", ""+xPos);
			agentSVG.setAttributeNS(null, "cy", ""+yPos);
			agentSVG.setAttributeNS(null, "style", "fill:green");
		}
		return agentSVG;
	}
}
