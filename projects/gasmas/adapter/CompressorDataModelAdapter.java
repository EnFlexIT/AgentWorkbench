/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.adapter;

import gasmas.ontology.CompStat;
import gasmas.ontology.Compressor;
import gasmas.ontology.GasGridOntology;

import jade.content.onto.Ontology;

import java.util.Vector;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;

public class CompressorDataModelAdapter extends NetworkComponentAdapter4Ontology {

	private Vector<Class<? extends Ontology>> ontologyBaseClasses = null;
	private String[] ontologyClassReferences = null;

	
	public CompressorDataModelAdapter(GraphEnvironmentController environmentController) {
		super(environmentController);
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
			this.ontologyClassReferences[0] = Compressor.class.getName();
			this.ontologyClassReferences[1] = CompStat.class.getName();
		}
		return this.ontologyClassReferences;
	}

}
