package gasmas.adapter;

import jade.content.onto.Ontology;

import java.util.Vector;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;
import gasmas.ontology.GasGridOntology;
import gasmas.ontology.ShortPipe;

public class ShortPipeDataModelAdapter extends NetworkComponentAdapter4Ontology {

	private Vector<Class<? extends Ontology>> ontologyBaseClasses = null;
	private String[] ontologyClassReferences = null;
	
	
	public ShortPipeDataModelAdapter(GraphEnvironmentController graphController) {
		super(graphController);
	}

	@Override
	public Vector<Class<? extends Ontology>> getOntologyBaseClasses() {
		if (this.ontologyBaseClasses==null) {
			this.ontologyBaseClasses = new Vector<Class<? extends Ontology>>();
			this.ontologyBaseClasses.add(GasGridOntology.class);
			this.ontologyBaseClasses.add(AgentGUI_BaseOntology.class);
		}
		return this.ontologyBaseClasses;
	}

	@Override
	public String[] getOntologyClassReferences() {
		if (ontologyClassReferences==null) {
			this.ontologyClassReferences = new String[1];
			this.ontologyClassReferences[0] = ShortPipe.class.getName();
		}
		return this.ontologyClassReferences;
	}
	
}