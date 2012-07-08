package gasmas.adapter;

import gasmas.ontology.Compressor;
import gasmas.ontology.GasGridOntology;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;

public class CompressorDataModelAdapter extends NetworkComponentAdapter4Ontology{

	@Override
	public Vector<String> getOntologyBaseClasses() {
		Vector<String> onotolgyRefs = new Vector<String>();
		onotolgyRefs.add(GasGridOntology.class.getName());
		onotolgyRefs.add(AgentGUI_BaseOntology.class.getName());
		return onotolgyRefs;
	}

	@Override
	public String[] getOntologyClassReferences() {
		String[] onotolgyClassRefs  = new String[1];
		onotolgyClassRefs[0] = Compressor.class.getName();
		return onotolgyClassRefs;
	}
	
}
