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
package agentgui.core.jade;

import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.BaseService;

import java.util.Vector;

import agentgui.core.application.Project;
import agentgui.core.gui.components.JListClassSearcher;


/**
 * This class controls the search process for agent-, ontology- and service-classes.<br>
 * The search process will be started right after the execution of Agent.GUI or if a <br>
 * new project is focused within the application (new/open).<br>
 * <br>
 * The found classes will be stored and used in several user views like 'Configuration' - 'Agents'<br> 
 * or for the selection of an ontology.<br>
 * <br>
 * Within Agent.GUI and for the visualization of the search result, the {@link JListClassSearcher} is used.<br> 
 * 
 * @see Agent
 * @see Ontology
 * @see BaseService
 * @see JListClassSearcher
 * @see ClassFinder
 * @see ClassSearcherSingle
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassSearcher {

	public static final int RESTART_AGENT_SEARCH = 1; 
	public static final int RESTART_ONTOLOGY_SEARCH = 2;
	public static final int RESTART_BASESERVICE_SEARCH = 3;

	public static final Class<Agent> CLASSES_AGENTS = jade.core.Agent.class;
	public static final Class<Ontology> CLASSES_ONTOLOGIES = jade.content.onto.Ontology.class;
	public static final Class<BaseService> CLASSES_BASESERVICE = jade.core.BaseService.class;
	
	private Project currProject = null;
	
	public ClassSearcherSingle csAgents =  new ClassSearcherSingle(CLASSES_AGENTS);
	public ClassSearcherSingle csOntologies = new ClassSearcherSingle(CLASSES_ONTOLOGIES);
	public ClassSearcherSingle csBaseService = new ClassSearcherSingle(CLASSES_BASESERVICE);

	
	/**
	 * Instantiates a new class searcher.
	 */
	public ClassSearcher() {
		this.reStartSearch(null, null);
	}
	
	/**
	 * Instantiates a new class searcher.
	 *
	 * @param project the project
	 */
	public ClassSearcher(Project project) {
		this.reStartSearch(project, null);
	}
	
	/**
	 * Re start search.
	 *
	 * @param project the project
	 * @param searchFor the search for
	 */
	public void reStartSearch(Project project, Integer searchFor) {
		
		this.currProject = project;
		
		if (searchFor==null) {
			// ------------------------------------------------
			// --- Restart all searches -----------------------
			// ------------------------------------------------
			
			// --- Start search for Agent ---------------------
			csAgents.stopSearch();
			csAgents.setProject(this.currProject);
			csAgents.startSearch();
			
			// --- Start search for Ontology ------------------
			csOntologies.stopSearch();
			csOntologies.setProject(this.currProject);
			csOntologies.startSearch();
			
			// --- Start search for Agent BaseService ---------
			csBaseService.stopSearch();
			csBaseService.setProject(this.currProject);
			csBaseService.startSearch();

		} else {
			// ------------------------------------------------
			// --- Start selected searches --------------------
			// ------------------------------------------------
			int search = searchFor;
			switch (search) {
			case RESTART_AGENT_SEARCH:
				// --- Start search for Agent ---------------------
				csAgents.stopSearch();
				csAgents.setProject(this.currProject);
				csAgents.startSearch();				
				break;
				
			case RESTART_ONTOLOGY_SEARCH:
				// --- Start search for Ontology ------------------
				csOntologies.stopSearch();
				csOntologies.setProject(this.currProject);
				csOntologies.startSearch();
				break;
				
			case RESTART_BASESERVICE_SEARCH:
				// --- Start search for BaseService ---------------
				csBaseService.stopSearch();
				csBaseService.setProject(this.currProject);
				csBaseService.startSearch();
				break;
			}
		}
		
	}
	
	/**
	 * Gets the agent classe.
	 *
	 * @param filtered the filtered
	 * @return the agent classe
	 */
	public Vector<Class<?>> getAgentClasse(boolean filtered) {
		return csAgents.getClassesFound(filtered);
	}
	
	/**
	 * Gets the ontologie classe.
	 *
	 * @param filtered the filtered
	 * @return the ontologie classe
	 */
	public Vector<Class<?>> getOntologieClasse(boolean filtered) {
		return csOntologies.getClassesFound(filtered);
	}
	
	/**
	 * Gets the base service classe.
	 *
	 * @param filtered the filtered
	 * @return the base service classe
	 */
	public Vector<Class<?>> getBaseServiceClasse(boolean filtered) {
		return csBaseService.getClassesFound(filtered);
	}
	
}
