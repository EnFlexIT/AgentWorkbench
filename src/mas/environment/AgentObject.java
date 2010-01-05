package mas.environment;

import org.w3c.dom.Element;

import jade.core.AID;

/**
 * Umgebungsobjekt, das einen Agenten repräsentiert
 * @author Nils
 *
 */
public class AgentObject extends BasicObject {
	private String agentClass = null;
	private AID agentAID = null;
	public AgentObject(String id, Element svg, String agentClass){
		super(id, svg);
		this.agentClass = agentClass;
	}
	
	public AgentObject(Element svg, String agentClass){
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
}
