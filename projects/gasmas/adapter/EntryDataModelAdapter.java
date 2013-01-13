package gasmas.adapter;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;
import agentgui.ontology.TimeSeriesChart;
import gasmas.ontology.Entry;
import gasmas.ontology.GasGridOntology;

public class EntryDataModelAdapter extends NetworkComponentAdapter4Ontology {

	private Vector<String> ontologyBaseClasses = null;
	private String[] ontologyClassReferences = null;
	
	@Override
	public Vector<String> getOntologyBaseClasses() {
		if (this.ontologyBaseClasses==null) {
			this.ontologyBaseClasses = new Vector<String>();
			this.ontologyBaseClasses.add(GasGridOntology.class.getName());
			this.ontologyBaseClasses.add(AgentGUI_BaseOntology.class.getName());
		}
		return this.ontologyBaseClasses;
	}

	@Override
	public String[] getOntologyClassReferences() {
		if (ontologyClassReferences==null) {
			this.ontologyClassReferences = new String[2];
			this.ontologyClassReferences[0] = Entry.class.getName();
			this.ontologyClassReferences[1] = TimeSeriesChart.class.getName();
		}
		return this.ontologyClassReferences;
	}
}