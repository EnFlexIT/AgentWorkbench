package mas.environment;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;

import org.w3c.dom.Element;

/**
 * Abstract parent class for all kinds of objects in an environmennt
 * @author Nils
 *
 */
public class BasicObject {
	protected Area area;
	
	/**
	 * SVG element representing this BasicObject
	 */
	protected Element svgRepresentation;
	/**
	 * Playground instance containing this BasicObject
	 */
	protected Playground playground = null;
	
	private String id;
	
	public BasicObject(){
		
	}
	
	public BasicObject(String id, Element svg){
		this.id = id;
		if(svg.getTagName().equals("rect")){
			int width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			int height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			int x = (int) Float.parseFloat(svg.getAttributeNS(null, "x"));
			int y = (int) Float.parseFloat(svg.getAttributeNS(null, "y"));
			this.area = new Area(new Rectangle(width, height));
			this.svgRepresentation = svg;
		}
	}
	
	public BasicObject(Element svg){
		this.id=svg.getAttributeNS(null, "id");
		String strWidth = svg.getAttributeNS(null, "width");
		String strHeight = svg.getAttributeNS(null, "height");
		
		int width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
		int height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
		
		this.id = svg.getAttributeNS(null, "id");
		this.area = new Area(new Rectangle(width, height));
		this.svgRepresentation = svg;
	}
	
	public int getWidth(){
		return this.area.getBounds().width;
	}
	public int getHeight(){
		return this.area.getBounds().height;
	}
	public int getX(){
		return this.area.getBounds().x;
	}
	public int getY(){
		return this.area.getBounds().y;
	}
	public Element getSvg(){
		return this.svgRepresentation;
	}
	public Area getArea(){
		return this.area;
	}
	void setPlayground(Playground pg){
		this.playground = pg;
	}
	public String getId(){
		return this.id;
	}
	void setId(String id){
		this.id = id;
	}
}
