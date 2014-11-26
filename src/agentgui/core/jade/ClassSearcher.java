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

import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.project.Project;


/**
 * This class controls the search process for agent-, ontology- and service-classes.<br>
 * The search process will be started right after the execution of Agent.GUI or if a <br>
 * new project is focused within the application (new/open).<br>
 * <br>
 * The found classes will be stored and used in several user views like 'Configuration' - 'Agents'<br> 
 * or for the selection of an ontology.<br>
 * <br>
 * Within Agent.GUI and for the visualisation of the search result, the {@link JListClassSearcher} is used.<br> 
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

	public static enum ClassSearcherProcess {
		ALL_SEARCH_PROCESSES,
		AGENT_SEARCH,
		ONTOLOGY_SEARCH,
		BASESERVICE_SEARCH
	}
	
	public static final Class<Agent> CLASSES_AGENTS = jade.core.Agent.class;
	public static final Class<Ontology> CLASSES_ONTOLOGIES = jade.content.onto.Ontology.class;
	public static final Class<BaseService> CLASSES_BASESERVICE = jade.core.BaseService.class;
	
	private Project currProject = null;
	
	private ClassSearcherSingle csAgents =  new ClassSearcherSingle(CLASSES_AGENTS);
	private ClassSearcherSingle csOntologies = new ClassSearcherSingle(CLASSES_ONTOLOGIES);
	private ClassSearcherSingle csBaseService = new ClassSearcherSingle(CLASSES_BASESERVICE);

	
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
	 * Re start the specified search process.
	 *
	 * @param project the project
	 * @param searchProcess the search process
	 */
	public void reStartSearch(Project project, ClassSearcherProcess searchProcess) {
		
		this.currProject = project;
		if (searchProcess==null)  {
			searchProcess = ClassSearcherProcess.ALL_SEARCH_PROCESSES;
		}
		
		// --- ReStart selected searches ------------------
		switch (searchProcess) {
		case ALL_SEARCH_PROCESSES:
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
			break;
			
		case AGENT_SEARCH:
			// --- Start search for Agent ---------------------
			csAgents.stopSearch();
			csAgents.setProject(this.currProject);
			csAgents.startSearch();				
			break;
			
		case ONTOLOGY_SEARCH:
			// --- Start search for Ontology ------------------
			csOntologies.stopSearch();
			csOntologies.setProject(this.currProject);
			csOntologies.startSearch();
			break;
			
		case BASESERVICE_SEARCH:
			// --- Start search for BaseService ---------------
			csBaseService.stopSearch();
			csBaseService.setProject(this.currProject);
			csBaseService.startSearch();
			break;
			
		}
		
	}
	
	/**
	 * Stops the specified search processes (one or all).
	 * @param searchProcess the search of
	 */
	public void stopSearch(ClassSearcherProcess searchProcess) {
	
		if (searchProcess==null)  {
			searchProcess = ClassSearcherProcess.ALL_SEARCH_PROCESSES;
		}
		
		switch (searchProcess) {
		case ALL_SEARCH_PROCESSES:
			csAgents.stopSearch();
			csOntologies.stopSearch();
			csBaseService.stopSearch();
			break;
		
		case AGENT_SEARCH:
			// --- Start search for Agent ---------------------
			csAgents.stopSearch();
			break;
			
		case ONTOLOGY_SEARCH:
			// --- Start search for Ontology ------------------
			csOntologies.stopSearch();
			break;
			
		case BASESERVICE_SEARCH:
			// --- Start search for BaseService ---------------
			csBaseService.stopSearch();
			break;
			
		}
	}
	
	/**
	 * Returns the ClassSearcherSingle for Agents.
	 * @return the csAgents
	 */
	public ClassSearcherSingle getClassSearcher4Agents() {
		return csAgents;
	}
	/**
	 * Returns the ClassSearcherSingle for Ontologies.
	 * @return the csOntologies
	 */
	public ClassSearcherSingle getClassSearcher4Ontologies() {
		return csOntologies;
	}
	/**
	 * Returns the ClassSearcherSingle for BaseService's.
	 * @return the csBaseService
	 */
	public ClassSearcherSingle getClassSearcher4Services() {
		return csBaseService;
	}

	
	/**
	 * Returns the agent classes.
	 * @param filtered the filtered
	 * @return the agent classes
	 */
	public Vector<Class<?>> getAgentClasse(boolean filtered) {
		return csAgents.getClassesFound(filtered);
	}
	/**
	 * Returns  the ontology classes.
	 * @param filtered the filtered
	 * @return the ontology classes
	 */
	public Vector<Class<?>> getOntologieClasse(boolean filtered) {
		return csOntologies.getClassesFound(filtered);
	}
	/**
	 * Returns  the base service classes.
	 * @param filtered the filtered
	 * @return the base service classes
	 */
	public Vector<Class<?>> getBaseServiceClasse(boolean filtered) {
		return csBaseService.getClassesFound(filtered);
	}
	
}
