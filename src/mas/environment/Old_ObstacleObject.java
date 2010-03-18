package mas.environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Old_ObstacleObject extends Old_BasicObject {
	
	public Old_ObstacleObject(){
		// "Leeres" Objekt, Initialisierung über loadFromXML() 
	};
	
	public Old_ObstacleObject(Element svg){
		super(svg);
	}
	
	public Old_ObstacleObject(String id, Element svg){
		super(id, svg);
	}

	@Override
	public Element saveAsXML(Document doc) {
		Element xml = doc.createElement("obstacle");
		this.saveBasics(xml);
		return xml;
	}

	@Override
	public void loadFromXML(Element elem) {
		this.loadBasics(elem);		
	}
}
