package agentgui.graphEnvironment.controller;

import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Language;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

/**
 * This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * @author Nils
 *
 */
public class GridModel2 {
	/**
	 * The JUNG graph.
	 */
	private Graph<PropagationPoint, GridComponent> graph;
	/**
	 * HashMap providing access to the grid components based on the component's agentID
	 */
	private HashMap<String, GridComponent> components;
	/**
	 * Internal counter, used for generating sequential CheckPoint indexes 
	 */
	private int ppCounter = 0;
	/**
	 * Default constructor
	 */
	public GridModel2(){
		this.graph = new SparseGraph<PropagationPoint, GridComponent>();
		this.components = new HashMap<String, GridComponent>();
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid, without any connection to other components.
	 * @param component The simple component to add
	 */
	public void addSimpleComponent(GridComponent component){
		PropagationPoint pp1 = new PropagationPoint(ppCounter++);
		PropagationPoint pp2 = new PropagationPoint(ppCounter++);
		graph.addVertex(pp1);
		graph.addVertex(pp2);
		graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
		components.put(component.getAgentID(), component);
		System.out.println("Adding "+component.getType()+" "+component.getAgentID());
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed before the component specified by the given ID.
	 * @param component The simple component to add
	 * @param successorID The ID of the successor component
	 */
	public void addSimpleComponentBefore(GridComponent component, String successorID){
		
		PropagationPoint pp2 = graph.getSource(components.get(successorID));
		if(pp2 != null){
			PropagationPoint pp1 = new PropagationPoint(ppCounter++);
			graph.addVertex(pp1);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.getAgentID(), component);
			System.out.println("Adding "+component.getType()+" "+component.getAgentID()+
					" before "+components.get(successorID).getType()+" "+successorID);
		}else{
			System.err.println(Language.translate("Nachfolger-Knoten")+" "+successorID+" "+Language.translate("nicht gefunden!"));
		}
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed after the component specified by the given ID.
	 * @param component The simple component to add
	 * @param predecessorID The ID of the predecessor component
	 */
	public void addSimpleComponentAfter(GridComponent component, String predecessorID){
		PropagationPoint pp1 = graph.getDest(components.get(predecessorID));
		if(pp1 != null){
			PropagationPoint pp2 = new PropagationPoint(ppCounter++);
			graph.addVertex(pp2);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.getAgentID(), component);
			System.out.println("Adding "+component.getType()+" "+component.getAgentID()+
					" after "+components.get(predecessorID).getType()+" "+predecessorID);
		}else{
			System.err.println(Language.translate("Vorgänger-Knoten")+" "+predecessorID+" "+Language.translate("nicht gefunden!"));
		}
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed between the two components specified by the given IDs.
	 * @param component The simple component to add
	 * @param predecessorID The ID of the predecessor component
	 * @param successorID The ID of the successor component
	 */
	public void addSimpleComponentBetween(GridComponent component, String predecessorID, String successorID){
		PropagationPoint pp1 = graph.getDest(components.get(predecessorID));
		PropagationPoint pp2 = graph.getSource(components.get(successorID));
		if(pp1 != null && pp2 != null){
//			if(cp1 == cp2){		!!! Fallunterscheidung vermutlich überflüssig !!!
//				// Predecessor and successor are direct neighbors. Additional CheckPoit required. 
//				GridComponent successor = components.get(successorID);
//				graph.removeEdge(successor);
//				CheckPoint cp3 = new CheckPoint(cpCounter++);
//				graph.addVertex(cp3);
//				graph.addEdge(component, cp1, cp3, EdgeType.DIRECTED);
//				graph.addEdge(successor, cp3, cp2, EdgeType.DIRECTED);
//			}else{
				// Predecessor and successor are not neighbors. The component can be added without additional CheckPoints.
				graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
				System.out.println("Adding "+component.getType()+" "+component.getAgentID()+
						" between "+components.get(predecessorID).getType()+" "+predecessorID+
						" and "+components.get(successorID).getType()+" "+successorID);
//			}
		}else{
			if(pp1 == null){
				System.err.println(Language.translate("Vorgänger-Knoten")+" "+predecessorID+" "+Language.translate("nicht gefunden!"));
			}
			if(pp2 == null){
				System.err.println(Language.translate("Nachfolger-Knoten")+" "+successorID+" "+Language.translate("nicht gefunden!"));
			}
		}
	}
	public void fixDirections(){
		Iterator<GridComponent> components = graph.getEdges().iterator();
		while(components.hasNext()){
			GridComponent component = components.next();
			if(!component.getType().equals("compressor")){
				PropagationPoint pp1 = graph.getSource(component);
				PropagationPoint pp2 = graph.getDest(component);
				graph.removeEdge(component);
				graph.addEdge(component, pp1, pp2, EdgeType.UNDIRECTED);
			}
		}
	}
	/**
	 * Returns the GridComponent with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return The GridComponent
	 */
	public GridComponent getComponent(String id){
		return components.get(id);
	}
	/**
	 * Returns a list of all GridComponents
	 * @return The list
	 */
	public Collection<GridComponent> getComponents() {
		return graph.getEdges();
	}
	/**
	 * This method shows a JUNG visualization of the graph representing the grid.
	 */
	public void showJungVisualization(){
		Layout<PropagationPoint, GridComponent> layout = new FRLayout<PropagationPoint, GridComponent>(graph);
		layout.setSize(new Dimension(700, 600));
		BasicVisualizationServer<PropagationPoint, GridComponent> visServ = new BasicVisualizationServer<PropagationPoint, GridComponent>(layout);
		visServ.setPreferredSize(new Dimension(750, 650));
		
		// Node labels
		visServ.getRenderContext().setVertexLabelTransformer(new Transformer<PropagationPoint, String>() {
			
			@Override
			public String transform(PropagationPoint arg0) {
				return "PP"+arg0.getIndex();
			}
		});
		
		// Edge labels
		visServ.getRenderContext().setEdgeLabelTransformer(new Transformer<GridComponent, String>() {

			@Override
			public String transform(GridComponent arg0) {
				return arg0.getType()+" "+arg0.getAgentID();
			}
		});
		
		JFrame frame = new JFrame("Debug Graph View");
		frame.getContentPane().add(visServ);
		frame.pack();
		frame.setVisible(true);
		
		
	}
}
