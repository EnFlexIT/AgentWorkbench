package agentgui.graphEnvironment.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;

import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphNode;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

/**
 * Handling mouse interaction with graph visualizations in a BasicGraphGUI.
 * @author Nils
 *
 */
public class GraphEnvironmentMousePlugin extends PickingGraphMousePlugin<GraphNode, GraphEdge> {
	/**
	 * The parent BasicGraphGUI
	 */
	private BasicGraphGUI myGUI = null;
	
	/**
	 * Constructor
	 * @param parentGUI The parent BasicGraphGUI
	 */
	public GraphEnvironmentMousePlugin(BasicGraphGUI parentGUI) {
		super();
//		this.locked = true;
		this.myGUI = parentGUI;
	}

	@Override
	public void mouseClicked(MouseEvent e){
		
		Object pickedObject = null;
		
		Point point = e.getPoint();
		GraphElementAccessor<GraphNode, GraphEdge>ps = myGUI.getVisView().getPickSupport();
		
		// Get the graph node / PropagationPoint at the clicked coordinates
		GraphNode pickedPP = ps.getVertex(myGUI.getVisView().getGraphLayout(), point.getX(), point.getY());
		
		if(pickedPP != null){		// A node / PropagationPoint was clicked
//			System.out.println("PropagationPoint PP"+pickedPP.getIndex());
			pickedObject = pickedPP;
		}else{			// No node / PropagationPoint was clicked
			
			// Get the graph edge / GridComponent at the clicked coordinates
			GraphEdge pickedGC = ps.getEdge(myGUI.getVisView().getGraphLayout(), point.getX(), point.getY());
			
			if(pickedGC != null){	// An edge / GridComponent was clicked
//				System.out.println("GridComponent "+pickedGC.getAgentID()+" of type "+pickedGC.getType());
				pickedObject = pickedGC;
			}
		}
		
		
		
		myGUI.handleObjectSelection(pickedObject);
	}
}
