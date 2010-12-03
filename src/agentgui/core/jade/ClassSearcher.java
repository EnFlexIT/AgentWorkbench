package agentgui.core.jade;

import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.BaseService;

import java.util.Vector;

import agentgui.core.application.Project;


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

	
	public ClassSearcher() {
		this.reStartSearch(null, null);
	}
	public ClassSearcher(Project project) {
		this.reStartSearch(project, null);
	}
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
	
	public Vector<Class<?>> getAgentClasse(boolean filtered) {
		return csAgents.getClassesFound(filtered);
	}
	public Vector<Class<?>> getOntologieClasse(boolean filtered) {
		return csOntologies.getClassesFound(filtered);
	}
	public Vector<Class<?>> getBaseServiceClasse(boolean filtered) {
		return csBaseService.getClassesFound(filtered);
	}
	
}
