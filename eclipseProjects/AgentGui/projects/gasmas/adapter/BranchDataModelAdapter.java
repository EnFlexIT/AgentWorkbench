package gasmas.adapter;

import gasmas.ontology.GasGridOntology;
import jade.content.onto.Ontology;

import java.util.Vector;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;
import agentgui.ontology.TimeSeriesChart;
import agentgui.ontology.XyChart;

public class BranchDataModelAdapter extends NetworkComponentAdapter4Ontology {

	private Vector<Class<? extends Ontology>> ontologyBaseClasses = null;
	private String[] ontologyClassReferences = null;
	
	public BranchDataModelAdapter(GraphEnvironmentController graphController) {
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
			this.ontologyClassReferences = new String[2];
			this.ontologyClassReferences[0] = TimeSeriesChart.class.getName();
			this.ontologyClassReferences[1] = XyChart.class.getName();
		}
		return this.ontologyClassReferences;
	}

}
