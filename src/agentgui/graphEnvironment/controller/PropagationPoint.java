package agentgui.graphEnvironment.controller;

import jade.content.Concept;

public class PropagationPoint {
	private int index;
	
	private Concept ontoObject = null;

	public PropagationPoint(int index) {
		super();
		this.index = index;
		//this.ontoObject = new gasmas.ontology.PropagationPoint();
	}

	public int getIndex() {
		return index;
	}
	
	public Concept getOntoObject(){
		return ontoObject;
	}
}
