package mas.environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Oberklasse für alle Umgebungsobjekte
 * @author Nils
 *
 */
public abstract class BasicObject{
	private int width;
	private int height;
	private int posX;
	private int posY;
	
	private Playground playground;
	private String id;
	
	/**
	 * 
	 */
	public BasicObject(){
		
	}
	
	public BasicObject(Element svg){
		this(svg.getAttributeNS(null, "id"), svg);
	}
	
	public BasicObject(String id, Element svg){
	
		this.setPhysics(svg);
		this.playground = null;
		this.id = id;		
	}
	
	/**
	 * Setzt Größe und Position des Objektes abhängig vom übergebenen SVG-Element
	 * @param svg
	 */
	private void setPhysics(Element svg){
		if(svg.getTagName().equals("svg")){		// SVG-Root als Playground
			this.width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			this.height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			this.posX = 0;
			this.posY = 0;
		}else if(svg.getTagName().equals("rect")){
			this.width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			this.height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			this.posX = (int) Float.parseFloat(svg.getAttributeNS(null, "x"));
			this.posY = (int) Float.parseFloat(svg.getAttributeNS(null, "y"));
		}else if(svg.getTagName().equals("circle")){
			int r = (int) Float.parseFloat(svg.getAttributeNS(null, "r"));
			int x = (int) Float.parseFloat(svg.getAttributeNS(null, "cx"));
			int y = (int) Float.parseFloat(svg.getAttributeNS(null, "cy"));
			
			this.width = 2*r;
			this.height = 2*r;
			this.posX = x-r;
			this.posY = y-r;			
		}else if(svg.getTagName().equals("ellipse")){
			int rx = (int) Float.parseFloat(svg.getAttributeNS(null, "rx"));
			int ry = (int) Float.parseFloat(svg.getAttributeNS(null, "ry"));
			int x = (int) Float.parseFloat(svg.getAttributeNS(null, "cx"));
			int y = (int) Float.parseFloat(svg.getAttributeNS(null, "cy"));
			
			this.width = 2*rx;
			this.height = 2*ry;
			this.posX = x-rx;
			this.posY = y-ry;
		}
	}

	public int getWidth() {
		return width;
	}

//	public void setWidth(int width) {
//		this.width = width;
//	}

	public int getHeight() {
		return height;
	}

//	public void setHeight(int height) {
//		this.height = height;
//	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

//	public Element getSvgRepresentation() {
//		return svgRepresentation;
//	}

	public void setSvgRepresentation(Element svgRepresentation) {
//		this.svgRepresentation = svgRepresentation;
	}

	public Playground getPlayground() {
		return playground;
	}

	public void setPlayground(Playground playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public abstract Element saveAsXML(Document doc);
	
	public abstract void loadFromXML(Element elem);
	
	protected void saveBasics(Element xml){
		xml.setAttribute("id", this.getId());
//		xml.setAttribute("svgId", this.svgRepresentation.getAttributeNS(null, "id"));
		xml.setAttribute("x", ""+this.getPosX());
		xml.setAttribute("y", ""+this.getPosY());
		xml.setAttribute("width", ""+this.getWidth());
		xml.setAttribute("height", ""+this.getHeight());
	}
	
	protected void loadBasics(Element xml){
		this.id = xml.getAttribute("id");
		this.posX = Integer.parseInt(xml.getAttribute("x"));
		this.posY = Integer.parseInt(xml.getAttribute("y"));
		this.width = Integer.parseInt(xml.getAttribute("width"));
		this.height = Integer.parseInt(xml.getAttribute("height"));
	}
}
