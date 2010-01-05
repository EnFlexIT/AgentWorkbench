package mas.environment;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import org.w3c.dom.Element;

/**
 * Oberklasse für alle Arten von Umgebungsobjekten
 * @author Nils
 *
 */
public class BasicObject {
	protected Area area;
	
	/**
	 * SVG-Element, das dieses Objekt darstellt
	 */
	protected Element svgRepresentation;
	/**
	 * Playground, der dieses Objekt enthält
	 */
	protected Playground playground = null;
	
	private String id;
	
	public BasicObject(){
		
	}
	
	public BasicObject(String id, Element svg){
		this.id = id;
		// Benötigte Attribute je nach SVG Elementtyp unterschiedlich 
		if(svg.getTagName().equals("rect")){
			int width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			int height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			int x = (int) Float.parseFloat(svg.getAttributeNS(null, "x"));
			int y = (int) Float.parseFloat(svg.getAttributeNS(null, "y"));
			this.area = new Area(new Rectangle(x, y, width, height));
			this.svgRepresentation = svg;
		}else if(svg.getTagName().equals("circle")){
			float r = Float.parseFloat(svg.getAttributeNS(null, "r"));
			float width = r*2;
			float height = r*2;
			float x = Float.parseFloat(svg.getAttributeNS(null, "cx"))-r;
			float y = Float.parseFloat(svg.getAttributeNS(null, "cy"))-r;
			this.area = new Area(new Ellipse2D.Float(x, y, width, height));
			this.svgRepresentation = svg;
		}else if(svg.getTagName().equals("ellipse")){
			float rx = Float.parseFloat(svg.getAttributeNS(null, "rx"));
			float ry = Float.parseFloat(svg.getAttributeNS(null, "ry"));
			float width = rx*2;
			float height = ry*2;
			float x = Float.parseFloat(svg.getAttributeNS(null, "cx"))-rx;
			float y = Float.parseFloat(svg.getAttributeNS(null, "cy"))-ry;
			this.area = new Area(new Ellipse2D.Float(x, y, width, height));
			this.svgRepresentation = svg;
		}else if(svg.getTagName().equals("path")){
//			float rx = Float.parseFloat(svg.getAttributeNS(null, "sodipodi:rx"));
//			float ry = Float.parseFloat(svg.getAttributeNS(null, "sodipodi:ry"));
//			float width = rx*2;
//			float height = ry*2;
//			float x = Float.parseFloat(svg.getAttributeNS(null, "sodipodi:cx"))-rx;
//			float y = Float.parseFloat(svg.getAttributeNS(null, "sodipodi:cy"))-ry;
//			this.area = new Area(new Ellipse2D.Float(x, y, width, height));
//			this.svgRepresentation = svg;
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
