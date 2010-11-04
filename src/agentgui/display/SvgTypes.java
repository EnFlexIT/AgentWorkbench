package mas.display;

import org.w3c.dom.Element;

/**
 * The types of SVG elements supported 
 * @author Nils
 *
 */
public enum SvgTypes {
	SVG, RECT, CIRCLE, ELLIPSE, IMAGE;
	/**
	 * Returns the type constant for a given SVG element
	 * @param element The SVG element
	 * @return The type constant, or null if the type is not supported
	 */
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
		}else if(element.getTagName().equals("image")){
			type = IMAGE;
		}
		
		return type;
	}
}
