package mas.environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TransformRemover {
	/**
	 * The elements with these ids will not be touched
	 */
//	static String[] skipIds = {};

	static String[] skipIds = {"layer4", "layer3", "layer22", "layer10", "layer24", "layer11", "layer23", "layer32", "layer18", "layer14", "layer13", "layer5", "layer21", "layer20", "layer1", "layer31", "layer15", "layer28", "g17583", "path14290", "path14270"};
	
	/**
	 * Removing the transform attribute from a node
	 * @param node The node
	 * @param xDiff The x coordinate difference from the parent elements transform=translate attributes
	 * @param yDiff The y coordinate difference from the parent elements transform=translate attributes
	 */
	public static void removeTransform(Node node, float xDiff, float yDiff){
		System.out.println("RemoveTransform called");
		String regex = "-?\\d+\\.?\\d*,-?\\d+\\.?\\d*";
		String transform = ((Element) node).getAttributeNS(null, "transform");
		System.out.println(((Element)node).getAttributeNS(null, "id")+": "+transform);
		if(transform != null && transform.length()>0 && transform.contains("translate")){
			Matcher m = Pattern.compile(regex).matcher(transform);
			if(m.find()){
				String posChange = m.group();
				String x = posChange.substring(0, posChange.indexOf(','));
				String y = posChange.substring(posChange.indexOf(',')+1);
				xDiff += Float.parseFloat(x);
				yDiff += Float.parseFloat(y);
				
				if(! dontMove(((Element)node).getAttributeNS(null, "id"))){
					((Element) node).setAttributeNS(null, "transform", "");
					System.out.println("Removed");
					System.out.println();
				}else{
					System.out.println("Skipped");
					System.out.println();
				}
				
			}			
		}
		if(node instanceof Element){
			changePos((Element)node, xDiff, yDiff);
		}
		if(node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			for(int i=0; i<children.getLength(); i++){
				if(children.item(i) instanceof Element){
					removeTransform(children.item(i), xDiff, yDiff);
				}
			}
		}
	}
	
	/**
	 * Adding the changes from the removed transform=translate attributes to the elements coordinates 
	 * @param element The element
	 * @param xDiff The x coordinate difference from the parent elements transform=translate attributes
	 * @param yDiff The y coordinate difference from the parent elements transform=translate attributes
	 */
	private static void changePos(Element element, float xDiff, float yDiff){
		String type = element.getTagName();
		if(type.equals("rect") || type.equals("image")){
			String xPos = element.getAttributeNS(null, "x");
			String yPos = element.getAttributeNS(null, "y");
			if(xPos != null && yPos != null && xPos.length()>0 && yPos.length()>0){
				Float x = Float.parseFloat(xPos);
				Float y = Float.parseFloat(yPos);
				x += xDiff;
				y += yDiff;
				element.setAttributeNS(null, "x", ""+x);
				element.setAttributeNS(null, "y", ""+y);
			}
			
		}else if (type.equals("circle") || type.equals("ellipse")){
			String xPos = element.getAttributeNS(null, "cx");
			String yPos = element.getAttributeNS(null, "cy");
			if(xPos != null && yPos != null && xPos.length()>0 && yPos.length()>0){
				Float x = Float.parseFloat(xPos);
				Float y = Float.parseFloat(yPos);
				x += xDiff;
				y += yDiff;
				element.setAttributeNS(null, "cx", ""+x);
				element.setAttributeNS(null, "cy", ""+y);
			}
			
		}
	}
	
	/**
	 * Checking if an elements id is in the skip list
	 * @param id The elements id
	 * @return True if id is part of the skip list, false otherwise
	 */
	private static boolean dontMove(String id){
		boolean result = false;
		for(int i=0; i<skipIds.length; i++){
			if(id.equals(skipIds[i]))
				result=true;
		}
		return result;
	}
}
