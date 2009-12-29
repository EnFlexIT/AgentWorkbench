package mas.environment;

import org.w3c.dom.Element;

import jade.core.AID;

/**
 * Object representing an agent
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
	
	public String getAgentClass(){
		return this.agentClass;
	}
	
	public AID getAID(){
		return this.agentAID;
	}
}
