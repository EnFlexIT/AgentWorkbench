package mas.display;

import org.w3c.dom.Element;

/**
 * Helper class for storing agent animation information
 * @author nils
 */
public class AnimAgent{
	
	String id;
	Element elem;	// Element visualizing the agent
	int xPos;		// The agents x coordinate
	int yPos;		// The agents y coordinate
	int width;
	int height;		
	
	public AnimAgent(String id, Element elem, int xPos, int yPos, int width, int height) {
		this.id=id;
		this.elem = elem;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width=width;
		this.height=height;			
	}		
	
	public String getId(){
		return id;
	}
	
	public Element getElem() {
		return elem;
	}		
	public int getXPos() {
		return xPos;
	}
	public void setXPos(int pos) {
		xPos = pos;
	}
	public int getYPos() {
		return yPos;
	}
	public void setYPos(int pos) {
		yPos = pos;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}
