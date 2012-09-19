package gasmas.adapter;

import gasmas.ontology.Connection;
import gasmas.ontology.GasGridOntology;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;

public abstract class GenericConnectionDataModelAdapter<T extends Connection> extends NetworkComponentAdapter4Ontology {

	private Class<T> clazz;
	
	public GenericConnectionDataModelAdapter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public final Vector<String> getOntologyBaseClasses() {
		Vector<String> onotolgyRefs = new Vector<String>();
		onotolgyRefs.add(GasGridOntology.class.getName());
		onotolgyRefs.add(AgentGUI_BaseOntology.class.getName());
		return onotolgyRefs;
	}

	@Override
	public final String[] getOntologyClassReferences() {
		String[] onotolgyClassRefs  = new String[1];
		onotolgyClassRefs[0] = clazz.getName();
		return onotolgyClassRefs;
	}
	
}
