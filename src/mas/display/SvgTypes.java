package mas.display;

import org.w3c.dom.Element;

/**
 * Definiert die SVG-Elemente, die zur Darstellung von Umgebungsobjekten benutzt werden können
 * @author Nils
 *
 */
public enum SvgTypes {
	SVG, RECT, CIRCLE, ELLIPSE;
	
	public static SvgTypes getType(Element element){
		SvgTypes type = null;
		
		if(element.getTagName().equals("svg")){
			type = SVG;
		}else if(element.getTagName().equals("rect")){
			type = RECT;
		}else if(element.getTagName().equals("circle")){
			type = CIRCLE;
		}else if(element.getTagName().equals("ellipse")){
			type = ELLIPSE;
		}
		
		return type;
	}
}
