package mas.environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ObstacleObject extends BasicObject {
	
	public ObstacleObject(){
		// "Leeres" Objekt, Initialisierung über loadFromXML() 
	};
	
	public ObstacleObject(Element svg){
		super(svg);
	}
	
	public ObstacleObject(String id, Element svg){
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
