package mas.environment;

import mas.display.SvgTypes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.Language;

/**
 * Oberklasse für alle Umgebungsobjekte
 * @author Nils
 *
 */
public abstract class Old_BasicObject{
	private int width;
	private int height;
	private int posX;
	private int posY;
	
	/**
	 * Übergeordneter Playground
	 */
	private Old_Playground parentPlayground;
	/**
	 * Eindeutige ID des Objektes
	 */
	private String id;
	/**
	 * SVG-Elementtyp des Objektes
	 */
	private SvgTypes svgType = null;
	
	/**
	 * Erzeugt ein "leeres" BasicObject
	 */
	public Old_BasicObject(){
		
	}
	
	/**
	 * Erzeugt ein BasicObject, das auf dem übergebenen SVG-Element basiert 
	 * @param svg
	 */
	public Old_BasicObject(Element svg){
		this(svg.getAttributeNS(null, "id"), svg);
	}
	
	
	public Old_BasicObject(String id, Element svg){
				
		this.setSvgType(svg.getTagName());		
		switch(this.getSvgType()){
		case SVG:
			this.width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			this.height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			this.posX = 0;
			this.posY = 0;
		break;
			
		case RECT:
			this.width = (int) Float.parseFloat(svg.getAttributeNS(null, "width"));
			this.height = (int) Float.parseFloat(svg.getAttributeNS(null, "height"));
			this.posX = (int) Float.parseFloat(svg.getAttributeNS(null, "x"));
			this.posY = (int) Float.parseFloat(svg.getAttributeNS(null, "y"));
		break;
		
		case CIRCLE:
			int r = (int) Float.parseFloat(svg.getAttributeNS(null, "r"));
			int cx = (int) Float.parseFloat(svg.getAttributeNS(null, "cx"));
			int cy = (int) Float.parseFloat(svg.getAttributeNS(null, "cy"));
			
			this.width = 2*r;
			this.height = 2*r;
			this.posX = cx-r;
			this.posY = cy-r;
		break;
		
		case ELLIPSE:
			int rx = (int) Float.parseFloat(svg.getAttributeNS(null, "rx"));
			int ry = (int) Float.parseFloat(svg.getAttributeNS(null, "ry"));
			int ex = (int) Float.parseFloat(svg.getAttributeNS(null, "cx"));
			int ey = (int) Float.parseFloat(svg.getAttributeNS(null, "cy"));
			
			this.width = 2*rx;
			this.height = 2*ry;
			this.posX = ex-rx;
			this.posY = ey-ry;
		break;
		}
		this.parentPlayground = null;
		this.id = id;		
	}

	/**
	 * 
	 * @return
	 */
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

	public Old_Playground getParentPlayground() {
		return parentPlayground;
	}

	public void setPlayground(Old_Playground playground) {
		this.parentPlayground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Speichert das Objekt als XML-Element
	 * @param doc Übergeordnetes XML-Dokument
	 * @return Das erzeugte XML-Element  
	 */
	public abstract Element saveAsXML(Document doc);
	
	/**
	 * Läd die Objekteigenschaften aus dem übergebenen XML-Element
	 * @param element XML-Element, in dem die Objekteigenschaften gespeichert sind
	 */
	public abstract void loadFromXML(Element element);
	
	/**
	 * Speichert die gemeinsamen Eigenschaften aller Unterklassen 
	 * @param element XML-Element, dass die Daten aufnimmt
	 */
	protected void saveBasics(Element element){
		element.setAttribute("id", this.getId());
		element.setAttribute("svgType", this.getSvgType().toString());
		element.setAttribute("x", ""+this.getPosX());
		element.setAttribute("y", ""+this.getPosY());
		element.setAttribute("width", ""+this.getWidth());
		element.setAttribute("height", ""+this.getHeight());
	}
	
	/**
	 * Läd die gemeinsamen Eigenschaften aller Unterklassen
	 * @param element XML-Element, in dem die Daten stehen  
	 */
	protected void loadBasics(Element element){
		this.id = element.getAttribute("id");
		this.setSvgType(element.getAttribute("svgType"));
		this.posX = Integer.parseInt(element.getAttribute("x"));
		this.posY = Integer.parseInt(element.getAttribute("y"));
		this.width = Integer.parseInt(element.getAttribute("width"));
		this.height = Integer.parseInt(element.getAttribute("height"));
	}
	
	public SvgTypes getSvgType(){
		return this.svgType;
	}
	
	
	public void setSvgType(String tagName){
		if(tagName.equals("svg")){
			this.svgType = SvgTypes.SVG;
		}else if(tagName.equals("rect")){
			this.svgType = SvgTypes.RECT;
		}else if(tagName.equals("circle")){
			this.svgType = SvgTypes.CIRCLE;
		}else if(tagName.equals("ellipse")){
			this.svgType = SvgTypes.ELLIPSE;
		}else{
			System.err.println(Language.translate("SVG-Elementtyp")+" "+tagName+" "+Language.translate("nicht unterstützt"));
		}
		
	}
}
