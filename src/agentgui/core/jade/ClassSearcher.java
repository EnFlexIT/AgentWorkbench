package agentgui.core.jade;

import java.util.Vector;

import agentgui.core.application.Project;


public class ClassSearcher {

	public static final int RESTART_AGENT_SEARCH = 1; 
	public static final int RESTART_ONTOLOGY_SEARCH = 2;
	public static final int RESTART_BASESERVICE_SEARCH = 3;

	private Project currProject = null;
	
	private ClassSearcherSingle csAgents = null;
	private ClassSearcherSingle csOntologies = null;
	private ClassSearcherSingle csBaseService = null;

	
	public ClassSearcher() {
		this.reStartSearch(null, null);
	}
	public ClassSearcher(Project project) {
		this.currProject = project;
		this.reStartSearch(this.currProject, null);
	}
	public void reStartSearch(Project project, Integer searchFor) {
		
		this.currProject = project;
		
		if (searchFor==null) {
			// ------------------------------------------------
			// --- Alle Suchen erneut starten -----------------
			// ------------------------------------------------
			
			// --- Start search for Agent ---------------------
			if (csAgents!=null) {
				csAgents.stopSearch();
			}
			csAgents = new ClassSearcherSingle(jade.core.Agent.class, currProject);
			
			// --- Start search for Ontology ------------------
			if (csOntologies!=null) {
				csOntologies.stopSearch();
			}
			csOntologies = new ClassSearcherSingle(jade.content.onto.Ontology.class, currProject);
			
			// --- Start search for Agent BaseService ---------
			if (csBaseService!=null) {
				csBaseService.stopSearch();
			}
			csBaseService = new ClassSearcherSingle(jade.core.BaseService.class, currProject);

		} else {
			// ------------------------------------------------
			// --- Nur ausgewählte Suchen starten -------------
			// ------------------------------------------------
			int search = searchFor;
			switch (search) {
			case RESTART_AGENT_SEARCH:
				// --- Start search for Agent ---------------------
				if (csAgents!=null) {
					csAgents.stopSearch();
				}
				csAgents = new ClassSearcherSingle(jade.core.Agent.class, currProject);
				
				break;
			case RESTART_ONTOLOGY_SEARCH:
				// --- Start search for Ontology ------------------
				if (csOntologies!=null) {
					csOntologies.stopSearch();
				}
				csOntologies = new ClassSearcherSingle(jade.content.onto.Ontology.class, currProject);
				break;
			case RESTART_BASESERVICE_SEARCH:
				// --- Start search for BaseService ---------------
				if (csBaseService!=null) {
					csBaseService.stopSearch();
				}
				csBaseService = new ClassSearcherSingle(jade.core.BaseService.class, currProject);
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
