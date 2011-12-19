package gasmas.clustering;

import jade.core.behaviours.SimpleBehaviour;

import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

public class EdgeBetweenessBehaviour extends SimpleBehaviour
{
	private EnvironmentModel environmentModel;

	public EdgeBetweenessBehaviour( EnvironmentModel environmentModel )
	{
		this.environmentModel = environmentModel;
	}

	@Override
	public void action()
	{
		EdgeBetweennessClusterer<GraphNode, GraphEdge> edgeBetweennessClusterer = new EdgeBetweennessClusterer<GraphNode, GraphEdge>(
				5 );
		edgeBetweennessClusterer
				.transform( (Graph<GraphNode, GraphEdge>) ( (NetworkModel) environmentModel
						.getDisplayEnvironment() ).getGraph() );
		List<GraphEdge> edges = edgeBetweennessClusterer.getEdgesRemoved();
		for ( GraphEdge edge : edges )
		{
			System.out.println( edge.getId() );
		}
	}

	@Override
	public boolean done()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
