package mas.environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jade.core.AID;

/**
 * Umgebungsobjekt, das einen Agenten repräsentiert
 * @author Nils
 *
 */
public class Old_AgentObject extends Old_BasicObject{
	private String agentClass = null;
	private AID agentAID = null;
	
	public Old_AgentObject(){
		// "Leeres" Objekt, Initialisierung über loadFromXML()
	};
	
	public Old_AgentObject(String id, Element svg, String agentClass){
		super(id, svg);
		this.agentClass = agentClass;
	}
	
	public Old_AgentObject(Element svg, String agentClass){
		super(svg);
		this.agentClass = agentClass;
	}
	
	/**
	 * Liefert die Klasse des Agenten
	 * @return
	 */
	public String getAgentClass(){
		return this.agentClass;
	}
	
	/**
	 * Liefert die AID des Agenten
	 * @return
	 */
	public AID getAID(){
		return this.agentAID;
	}

	@Override
	public Element saveAsXML(Document doc) {
		Element xml = doc.createElement("agent");
		this.saveBasics(xml);
		xml.setAttribute("class", this.getAgentClass());
//		if(this.agentAID!=null){
//			xml.setAttribute("aid", this.getAID().toString());
//		}
		return xml;
	}

	@Override
	public void loadFromXML(Element elem) {
		this.loadBasics(elem);
		this.agentClass = elem.getAttribute("class");
//		this.agentAID = null;		
	}	
}
