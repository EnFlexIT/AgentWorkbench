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
package agas.adapter;

import gasmas.ontology.Exit;
import gasmas.ontology.GasGridOntology;

import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4Ontology;
import agentgui.ontology.AgentGUI_BaseOntology;
import agentgui.ontology.TimeSeriesChart;

/**
 * The Class ExitAdapterVisualisation.
 */
public class ExitDataModelAdapter extends NetworkComponentAdapter4Ontology {

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
		onotolgyClassRefs[0] = Exit.class.getName();
		onotolgyClassRefs[1] = TimeSeriesChart.class.getName();
		return onotolgyClassRefs;
	}

}