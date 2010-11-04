package agentgui.core.agents;

import jade.core.Agent;

public class AgentClassElement {
	Class<? extends Agent> agentClass = null;
	public AgentClassElement(Class<? extends Agent> agentClass){
		this.agentClass=agentClass;
	}
	@Override
	public String toString(){
		return agentClass.getName();
	}
	public Class<? extends Agent> getElementClass(){
		return agentClass;
	}
}
