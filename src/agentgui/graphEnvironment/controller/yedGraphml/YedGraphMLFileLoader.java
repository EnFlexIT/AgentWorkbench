package agentgui.graphEnvironment.controller.yedGraphml;

import java.io.File;
import java.util.HashMap;

import agentgui.core.application.Project;
import agentgui.graphEnvironment.controller.GraphEdge;
import agentgui.graphEnvironment.controller.GraphFileLoader;
import agentgui.graphEnvironment.controller.GraphNode;

import edu.uci.ics.jung.graph.Graph;

public class YedGraphMLFileLoader implements GraphFileLoader {
	
	private String fileExtension = ".graphml";
	
	private String typeString = "yEd GraphML Datei";
	
	private HashMap<String, String> ontoClasses = null;
	
	public YedGraphMLFileLoader(Project project){
		ontoClasses = project.getOntoClassHash();
	}

	@Override
	public Graph<GraphNode, GraphEdge> loadGraphFromFile(File graphFile) {
		YedGraphMLParser parser = new YedGraphMLParser(ontoClasses);
		return parser.getGraph(graphFile);
	}

	@Override
	public String getGraphFileExtension() {
		return fileExtension;
	}

	@Override
	public String getTypeString() {
		return typeString;
	}

}
