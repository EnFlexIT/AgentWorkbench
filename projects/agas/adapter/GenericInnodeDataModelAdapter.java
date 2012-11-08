package agas.adapter;

import gasmas.ontology.GasGridOntology;
import gasmas.ontology.Innode;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;
import agentgui.ontology.TimeSeriesChart;

/**
 * The Class ExitAdapterVisualisation.
 */
public class GenericInnodeDataModelAdapter<T extends Innode> extends NetworkComponentAdapter4Ontology {

	private Class<T> clazz;
	
	public GenericInnodeDataModelAdapter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Vector<String> getOntologyBaseClasses() {
		Vector<String> onotolgyRefs = new Vector<String>();
		onotolgyRefs.add(GasGridOntology.class.getName());
		onotolgyRefs.add(AgentGUI_BaseOntology.class.getName());
		return onotolgyRefs;
	}

	@Override
	public String[] getOntologyClassReferences() {
		String[] onotolgyClassRefs  = new String[2];
		onotolgyClassRefs[0] = clazz.getName();
		onotolgyClassRefs[1] = TimeSeriesChart.class.getName();
		return onotolgyClassRefs;
	}

}
