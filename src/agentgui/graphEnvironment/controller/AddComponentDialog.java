/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.graphEnvironment.controller;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphElement;
import agentgui.graphEnvironment.networkModel.GraphNode;
import agentgui.graphEnvironment.networkModel.NetworkComponent;
import agentgui.graphEnvironment.networkModel.NetworkModel;
import agentgui.graphEnvironment.prototypes.GraphElementPrototype;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.WindowConstants;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * Dialog for adding a new network component to the model.
 * All the component types are shown as a list and a preview of the graph prototype 
 * is shown on selecting the component type.
 * Adds the selected component to the graph by merging the common selected nodes.
 * @author Satyadeep
 *
 */
public class AddComponentDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	/**
	 * The graph element prototype of the selected component type.
	 */
	private GraphElementPrototype graphElement = null;  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JList componentTypesList = null;

	/**
	 * The GraphEnvironmentControllerGUI that started this dialog
	 */
	private GraphEnvironmentControllerGUI parent = null;
	private JScrollPane jScrollPane = null;
	/**
	 * Graph visualization component
	 */
	private VisualizationViewer<GraphNode, GraphEdge> visView = null;
	private JButton btnOK = null;
	private JPanel jBottomPanel = null;
	private JButton btnCancel = null;
	private JPanel jViewerPanel = null;
	private JLabel jLabel = null;
	/**
	 * Gets the parent object and initializes
	 * @param owner
	 */
	public AddComponentDialog(GraphEnvironmentControllerGUI parent) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {				
		this.setSize(390, 343);
		this.setTitle(Language.translate("Select Network Component to Add",Language.EN));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 0.0;
			gridBagConstraints4.weighty = 0.6;
			gridBagConstraints4.insets = new Insets(0, 105, 0, 105);
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.CENTER;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 1;			
			gridBagConstraints11.insets = new Insets(6, 5, 5, 5);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 0.4;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPane(), gridBagConstraints2);
			jContentPane.add(getJBottomPanel(), gridBagConstraints3);
			jContentPane.add(getJLabel(), gridBagConstraints11);
			jContentPane.add(getJViewerPanel(), gridBagConstraints4);			
		}
		return jContentPane;
	}

	/**
	 * This method initializes componentTypesList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getComponentTypesList() {
		if (componentTypesList == null) {
			componentTypesList = new JList(getListData());
			componentTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			componentTypesList
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							if(!e.getValueIsAdjusting()){
								String selected = (String) componentTypesList.getSelectedValue();
								//Gets the class name of the GraphPrototype of the selected component type from the environment controller
								String graphPrototype = parent.getController().getComponentTypeSettings().get(selected).getGraphPrototype();
								//System.out.println("selected prototype: "+graphPrototype);
								showPrototypePreview(graphPrototype);
							}
						}
					});
		}
		return componentTypesList;
	}

	/**
	 * This method takes the GraphPrototype class name as string and creates a graph 
	 * of the prototype and shows a preview in the visualizationViewer	
	 * @param graphPrototype
	 */
	private void showPrototypePreview(String graphPrototype){
		
		graphElement = null;
		try {
			Class theClass;
			theClass = Class.forName(graphPrototype);
			graphElement = (GraphElementPrototype)theClass.newInstance();
		}
		catch ( ClassNotFoundException ex ){
		      System.err.println( ex + " GraphElementPrototype class must be in class path.");
		}
	    catch( InstantiationException ex ){
	      System.err.println( ex + " GraphElementPrototype class must be concrete.");
	    }
	    catch( IllegalAccessException ex ){
	      System.err.println( ex + " GraphElementPrototype class must have a no-arg constructor.");
	    }
	    
	    if(graphElement!=null){	   
	    	//Generate and use the next unique network component ID
	    	String nextID = parent.getController().getGridModel().nextNetworkComponentID();
	    	graphElement.setId(nextID);	    	
	    	graphElement.setType(componentTypesList.getSelectedValue().toString());
			
	    	//Create an empty graph and add the graphElement to it
	    	Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode,GraphEdge>();	    	
	    	graphElement.addToGraph(graph);
	    	graphRepaint(graph);
	    }
	
	}
	
	
	/**
	 * Initializes label
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLabel(){
		if(jLabel == null) {
			jLabel = new JLabel(Language.translate("Select a vertex to merge",Language.EN));
		}
		return jLabel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(20, 200));
			jScrollPane.setViewportView(getComponentTypesList());
		}
		return jScrollPane;
	}
	
	/**
	 * Gets the list of componentTypeSettings from the controller 
	 * and returns it as an array
	 * @return Object[] - array of component types
	 */
	private Object[] getListData(){
		Vector<String> list = new Vector<String>();
		HashMap<String, ComponentTypeSettings> etsHash = parent.getController().getComponentTypeSettings();
		if(etsHash != null){
			Iterator<String> etsIter = etsHash.keySet().iterator();
			while(etsIter.hasNext()){
				String etName = etsIter.next();
				if(!etName.equals("node")){	// The node is not created manually					
					list.add(etName);					
				}
			}
		}
		return list.toArray();
	}
	
	/**
	 * Initializes the VisualizationViewer
	 * @return The VisualizationViewer
	 */
	private VisualizationViewer<GraphNode, GraphEdge> getVisView(){
		// create VisualizationViewer if it is not there
		if(visView==null){			
				// Define graph layout
			    Graph<GraphNode,GraphEdge> graph = new SparseGraph<GraphNode,GraphEdge>();
				Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
				layout.setSize(new Dimension(120,120));
				// Create a new VisualizationViewer instance
				visView = new VisualizationViewer<GraphNode, GraphEdge>(layout);
				
				visView.setLayout(new GridBagLayout());
				
				// Configure node labels
				visView.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
					
					@Override
					public String transform(GraphNode arg0) {
						return arg0.getId();
					}
				});
				
				// Configure edge labels
				visView.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
		
					@Override
					public String transform(GraphEdge arg0) {
						return arg0.getComponentType()+" "+arg0.getId();
					}
				});
				// Use straight lines as edges
				visView.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());
				
				//Mouse plugin to be placed here
				PluggableGraphMouse pgm = new PluggableGraphMouse();
				pgm.add(new PickingGraphMousePlugin<GraphNode,GraphEdge>());
				visView.setGraphMouse(pgm);
				
				visView.setBackground(Color.WHITE);

				visView.setPreferredSize(new Dimension(200, 200));
		}
		return visView;
	}
	
	/**
	 * Repaints/Refreshes the visualisation viewer, with the given graph
	 * @param graph - The new graph to be painted
	 */
	public void graphRepaint(Graph<GraphNode, GraphEdge> graph)
	{
		Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		layout.setSize(new Dimension(120,120));
		visView.setGraphLayout(layout);
		visView.repaint();
		jContentPane.repaint();
	}
	
	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText("OK");
			btnOK.addActionListener(this);
		}
		return btnOK;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		//OK button
		if(ae.getSource().equals(getBtnOK())){					
			//check whether only one vertex is picked or not
			String selected = (String) componentTypesList.getSelectedValue();
			Set<GraphNode> nodeSet = getVisView().getPickedVertexState().getPicked();
			if(nodeSet.size() == 1){
				GraphNode pickedVertex = nodeSet.iterator().next();
				//System.out.println("pos: "+getVisView().getGraphLayout().transform(pickedVertex));

				addElement(selected,pickedVertex);
				
				//this.setVisible(false);
				this.dispose();
			}
			else{
				JOptionPane.showMessageDialog(this,Language.translate("Select one vertex",Language.EN),Language.translate("Warning",Language.EN),JOptionPane.WARNING_MESSAGE);
			}		
		}
		//Cancel button
		else if(ae.getSource().equals(getBtnCancel()))
		{
			//this.setVisible(false);
			this.dispose();
		}
	}

/**
 * Add a new network component of selectedType to the main graph joined at the pickedVertex.
 * Updates the environment network model accordingly.  
 * @param selectedType - The Network component type selected
 * @param pickedVertex - The vertex selected in the prototype preview which is to be used as common point for merging
 */
	private void addElement(String selectedType, GraphNode pickedVertex) {
		//Environment network model
		NetworkModel gridModel = parent.getController().getGridModel();		
		//The Node picked in the parent graph
		GraphNode parentPickedVertex = parent.getPickedVertex();
		
		HashMap<String, ComponentTypeSettings> componentTypeSettings = parent.getController().getComponentTypeSettings();
		
		// Create a NetworkComponent representing the element
		NetworkComponent newComponent = new NetworkComponent();
		newComponent.setId(graphElement.getId()); 
		newComponent.setType(selectedType);
		newComponent.setPrototypeClassName(componentTypeSettings.get(selectedType).getGraphPrototype());
		newComponent.setAgentClassName(componentTypeSettings.get(selectedType).getAgentClass());
		newComponent.setDirected(graphElement.isDirected());
		
		HashSet<GraphElement> graphElements = null;
		
		//Adding to an empty graph - starting from scratch 
		if(gridModel.getGraph().getVertexCount()==0){
			//Creating an initial dummy vertex on the parent graph so that the same merge function can be used
			GraphNode firstNode = new GraphNode();
			firstNode.setId("PP0"); //TODO ID may not be hard coded
			firstNode.setPosition(new Point(30,30));
			gridModel.getGraph().addVertex(firstNode);
			parentPickedVertex = firstNode;
		}
		
		// Adding the prototype element to the main graph
		graphElements = merge(gridModel.getGraph(), getVisView().getGraphLayout().getGraph(), parentPickedVertex, pickedVertex);
		// Add the graph element IDs of the newly added network component
		if(graphElements != null){
			Iterator<GraphElement> geIter = graphElements.iterator();
			while(geIter.hasNext()){			
				String id = geIter.next().getId();
				newComponent.getGraphElementIDs().add(id);
			}
		}	
		//Add the newly created component to the network model
		gridModel.addNetworkComponent(newComponent);
		//Replace the network model graph with the new one
		gridModel.setGraph(gridModel.getGraph());
		
		parent.getController().refreshNetworkModel();
	}
		
	/**
	 * Merge the two graphs into one graph as if v1 and v2 are the same.
	 * The resultant graph is g1
	 * Modifying the network model here, may move this function to GraphEnvironmentController instead
	 * @param g1 First Graph
	 * @param g2 Second Graph
	 * @param v1 a vertex in g1
	 * @param v2 a vertex in g2
	 */
	public HashSet<GraphElement> merge(Graph<GraphNode, GraphEdge> g1, Graph<GraphNode, GraphEdge> g2, GraphNode v1, GraphNode v2){
		//A mapping from vertices in g2 to the newly created corresponding vertices in g1
		HashMap<GraphNode, GraphNode> map = new HashMap<GraphNode, GraphNode>();
		
		// Create a HashSet containing the nodes and edges belonging to the new network component and return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();		
		
		//Copy the vertices from g2 to g1
		Iterator<GraphNode> vertices= g2.getVertices().iterator();	
		//Adding the common vertex to the elements set
		elements.add(v1);
		while(vertices.hasNext()){
			GraphNode v = vertices.next();
			if(v != v2){
				GraphNode newNode = new GraphNode();
				//Generate the unique ID to be assigned to the new node
				String nextID = parent.getController().getGridModel().nextNodeID();
				newNode.setId(nextID); 
				
				//Set position of node  v1+(v-v2)
				int x = (int) (v1.getPosition().getX() + (getVisView().getGraphLayout().transform(v).getX() - getVisView().getGraphLayout().transform(v2).getX())) ;
				int y = (int) (v1.getPosition().getY() + (getVisView().getGraphLayout().transform(v).getY() - getVisView().getGraphLayout().transform(v2).getY())) ;				
				newNode.setPosition(new Point(x, y));
				g1.addVertex(newNode);		
				//inserting a mapping from g2 to g1 of corresponding nodes
				map.put(v, newNode);
				elements.add(newNode);
			}
		}
		
		//copy the edges from g2 to g1
		Iterator<GraphEdge> edges = g2.getEdges().iterator();
		EdgeType direction = null;
		//Check whether the graph prototype is directed
		if(graphElement.isDirected())
			direction = EdgeType.DIRECTED;
		else
			direction = EdgeType.UNDIRECTED;
		
		while(edges.hasNext()){
			GraphEdge e = edges.next();
			GraphNode front = g2.getEndpoints(e).getFirst();
			GraphNode back = g2.getEndpoints(e).getSecond();
			GraphEdge newEdge = new GraphEdge(e.getId(), graphElement.getType());
			if(front == v2){
				g1.addEdge(newEdge, v1, map.get(back), direction);
			}
			else if(back == v2){
				g1.addEdge(newEdge, map.get(front), v1, direction);	
			}
			else{
				g1.addEdge(newEdge, map.get(front), map.get(back), direction);
			}
			elements.add(newEdge);
		}
		return elements;
	}
	
	/**
	 * This method initializes jBottomPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJBottomPanel() {
		if (jBottomPanel == null) {
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.anchor = GridBagConstraints.CENTER;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints1.ipadx = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints.gridy = 0;
			jBottomPanel = new JPanel();
			jBottomPanel.setLayout(new GridBagLayout());
			jBottomPanel.add(getBtnOK(), gridBagConstraints1);
			jBottomPanel.add(getBtnCancel(), gridBagConstraints);
		}
		return jBottomPanel;
	}


	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	/**
	 * This method initializes jViewerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJViewerPanel() {
		if (jViewerPanel == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(0);
			jViewerPanel = new JPanel();
			jViewerPanel.setLayout(borderLayout);
			jViewerPanel.add(getVisView(), BorderLayout.CENTER);
		}
		return jViewerPanel;
	}
}  //  @jve:decl-index=0:visual-constraint="18,-13"
