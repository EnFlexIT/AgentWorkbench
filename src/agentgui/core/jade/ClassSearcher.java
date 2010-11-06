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
			csAgents = new ClassSearcherSingle(jade.core.Agent.class, currProject);
			
			// --- Start search for Ontology ------------------
			csOntologies = new ClassSearcherSingle(jade.content.onto.Ontology.class, currProject);
			
			// --- Start search for Agent BaseService ---------
			csBaseService = new ClassSearcherSingle(jade.core.BaseService.class, currProject);

		} else {
			// ------------------------------------------------
			// --- Nur ausgewählte Suchen starten -------------
			// ------------------------------------------------
			int search = searchFor;
			switch (search) {
			case RESTART_AGENT_SEARCH:
				// --- Start search for Agent ---------------------
				csAgents = new ClassSearcherSingle(jade.core.Agent.class, currProject);
				break;
			case RESTART_ONTOLOGY_SEARCH:
				// --- Start search for Ontology ------------------
				csOntologies = new ClassSearcherSingle(jade.content.onto.Ontology.class, currProject);
				break;
			case RESTART_BASESERVICE_SEARCH:
				// --- Start search for BaseService ---------------
				csBaseService = new ClassSearcherSingle(jade.core.BaseService.class, currProject);
				break;
			}
		}
		
	}
	
	public Vector<Class<?>> getAgentClasse() {
		return csAgents.getClassesFound();
	}
	public Vector<Class<?>> getOntologieClasse() {
		return csOntologies.getClassesFound();
	}
	public Vector<Class<?>> getBaseServiceClasse() {
		return csBaseService.getClassesFound();
	}
	
}
