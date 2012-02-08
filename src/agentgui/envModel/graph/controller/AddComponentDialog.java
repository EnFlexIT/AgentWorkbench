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
package agentgui.envModel.graph.controller;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.Star3GraphElement;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import javax.swing.JTextField;

/**
 * Dialog for adding a new network component to the model.<br>
 * List of component types is displayed and on selecting a component type, the preview of the graph prototype is shown .<br>
 * Adds the selected component to the graph by merging the common selected nodes.
 * 
 * @see GraphEnvironmentControllerGUI
 * @see GraphEnvironmentController
 * @see GraphElementPrototype
 * 
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 * 
 */
public class AddComponentDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = -7481141098749690137L;

    /** The graph element prototype of the selected component type. */
    private GraphElementPrototype graphElement = null; // @jve:decl-index=0:

	private JPanel jContentPane = null;
    private JPanel jPanelBottom = null;
    private JPanel jPanelViewer = null;  //  @jve:decl-index=0:visual-constraint="601,164"
    private JScrollPane jScrollPane = null;  //  @jve:decl-index=0:visual-constraint="521,58"
    private JList componentTypesList = null;
    private JLabel jLabelInstructionMerge = null;
    private JLabel jLabelInstructionSelect = null;
    
    private JButton jButtonOK = null;
    private JButton jButtonCancel = null;

    private GraphEnvironmentController graphController = null;
    private BasicGraphGui basicGraphGui = null;
    
    private VisualizationViewer<GraphNode, GraphEdge> visualizationViewer = null;

    

    /**
     * Gets the parent object and initializes.
     * @param controller the GraphEnvironmentController
     */
    public AddComponentDialog(GraphEnvironmentController controller) {
		super(Application.MainWindow);
		this.graphController = controller;
		this.basicGraphGui = ((GraphEnvironmentControllerGUI) this.graphController.getEnvironmentPanel()).getGraphGUI();
		initialize();
    }

    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
		this.setSize(400, 542);
		this.setContentPane(getJContentPane());
		this.setModal(true);
		this.setTitle(Language.translate("Select Network Component to Add", Language.EN));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
	 * This method initializes jContentPane	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
			gridBagConstraints0.fill = GridBagConstraints.BOTH;
			gridBagConstraints0.gridx = 0;
			gridBagConstraints0.gridy = 0;
			gridBagConstraints0.insets = new Insets(15, 15, 0, 15);
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(5, 15, 0, 15);
			
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new Insets(15, 15, 0, 15);
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 3;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.insets = new Insets(5, 15, 0, 15);
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.CENTER;
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 4;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.insets = new Insets(15, 15, 15, 15);

			jLabelInstructionSelect = new JLabel();
			jLabelInstructionSelect.setText("Select a network component");
			jLabelInstructionSelect.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelInstructionSelect.setText(Language.translate(jLabelInstructionSelect.getText(), Language.EN));

			jLabelInstructionMerge = new JLabel();
		    jLabelInstructionMerge.setText("Select a vertex to merge");
		    jLabelInstructionMerge.setFont(new Font("Dialog", Font.BOLD, 12));
		    jLabelInstructionMerge.setText(Language.translate(jLabelInstructionMerge.getText(), Language.EN));
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelInstructionSelect, gridBagConstraints0);
			jContentPane.add(getJScrollPane(), gridBagConstraints1);
			jContentPane.add(jLabelInstructionMerge, gridBagConstraints2);
			jContentPane.add(getJViewerPanel(), gridBagConstraints3);
			jContentPane.add(getJBottomPanel(), gridBagConstraints4);
			
		}
		return jContentPane;
	}
	
    /**
     * This method initializes componentTypesList
     * @return javax.swing.JList
     */
    private JList getComponentTypesList() {
		if (componentTypesList == null) {
		    componentTypesList = new JList(getListData());
		    componentTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    componentTypesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			    if (!e.getValueIsAdjusting()) {
				String selected = (String) componentTypesList.getSelectedValue();
				// Gets the class name of the GraphPrototype of the
				// selected component type from the environment
				// controller
				String graphPrototype = graphController.getComponentTypeSettings().get(selected).getGraphPrototype();
				// System.out.println("selected prototype: "+graphPrototype);
				showPrototypePreview(graphPrototype);
			    }
			}
		    });
		}
		return componentTypesList;
    }

    /**
     * This method takes the GraphPrototype class name as string and creates a graph of the prototype and shows a preview in the visualizationViewer
     * @param graphPrototype
     */
    private void showPrototypePreview(String graphPrototype) {
	
		graphElement = null;
		try {
		    Class<?> theClass;
		    theClass = Class.forName(graphPrototype);
		    graphElement = (GraphElementPrototype) theClass.newInstance();
		} catch (ClassNotFoundException ex) {
		    System.err.println(ex + " GraphElementPrototype class must be in class path.");
		} catch (InstantiationException ex) {
		    System.err.println(ex + " GraphElementPrototype class must be concrete.");
		} catch (IllegalAccessException ex) {
		    System.err.println(ex + " GraphElementPrototype class must have a no-arg constructor.");
		}
	
		if (graphElement != null) {
		    // Generate and use the next unique network component ID
		    String nextID = this.graphController.getNetworkModel().nextNetworkComponentID();
		    graphElement.setId(nextID);
		    graphElement.setType(componentTypesList.getSelectedValue().toString());
	
		    // Create an empty graph and add the graphElement to it
		    Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode, GraphEdge>();
		    graphElement.addToGraph(graph);
		    graphRepaint(graph);
		}

    }

    /**
     * This method initializes jScrollPane
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
     * Gets the list of componentTypeSettings from the controller and returns it as an array
     * @return Object[] - array of component types
     */
    private Object[] getListData() {
		Vector<String> list = new Vector<String>();
		HashMap<String, ComponentTypeSettings> etsHash = this.graphController.getComponentTypeSettings();
		if (etsHash != null) {
		    Iterator<String> etsIter = etsHash.keySet().iterator();
		    while (etsIter.hasNext()) {
		    	String etName = etsIter.next();
				if (!etName.equals("node")) { // The node is not created manually
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
    private VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		// create VisualizationViewer if it is not there
		if (visualizationViewer == null) {

			Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode, GraphEdge>();
			
			visualizationViewer = basicGraphGui.getNewVisualizationViewer(graph);

			PluggableGraphMouse pgm = new PluggableGraphMouse();
		    pgm.add(new PickingGraphMousePlugin<GraphNode, GraphEdge>());
		    visualizationViewer.setGraphMouse(pgm);

			MutableTransformer mutableLayout = visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
			mutableLayout.translate(120, 10);

		    Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		    layout.setSize(new Dimension(120, 120));
			
		    visualizationViewer.setGraphLayout(layout);
		    visualizationViewer.setLayout(new GridBagLayout());
			visualizationViewer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		    visualizationViewer.setPreferredSize(new Dimension(200, 200));
			
			
//			// Define graph layout
//		    Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
//		    layout.setSize(new Dimension(120, 120));
//		    // Create a new VisualizationViewer instance
//		    visualizationViewer = new VisualizationViewer<GraphNode, GraphEdge>(layout);
//	
//		    visualizationViewer.setLayout(new GridBagLayout());
//	
//		    // Translate the graph to the right to bring it to the center
//		    MutableTransformer mutableLayout = visualizationViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
//		    mutableLayout.translate(120, 10);
//	
//		    // Configure edge labels, show the icon
//		    visualizationViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
//	
//			@Override
//			public String transform(GraphEdge edge) {
//			    // Get the path of the Image from the component type
//			    // settings
//			    String edgeImage = graphController.getComponentTypeSettings().get(edge.getComponentType()).getEdgeImage();
//			    if (edgeImage != null) {
//				URL url = getClass().getResource(edgeImage);
//				if (url != null) {
//				    // If the image path is valid
//				    return "<html><img src=" + url + " height=16 width=16 >";
//				} else
//				    return "";
//			    } else
//				return "";
//			}
//		    });
//	
//		    // Configure vertex colors
//		    visualizationViewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
//				public Paint transform(GraphNode arg0) {
//				    if (visualizationViewer.getPickedVertexState().isPicked(arg0)) {
//						// Highlight color when picked
//						return GeneralGraphSettings4MAS.DEFAULT_VERTEX_PICKED_COLOR;
//				    } else {
//						// Get the color from the component type settings
////						String colorString = graphController.getComponentTypeSettings().get("node").getColor();
////						if (colorString != null) {
////						    Color color = new Color(Integer.parseInt(colorString));
////						    return color;
////						} else {
//						    return GeneralGraphSettings4MAS.DEFAULT_VERTEX_COLOR;
////						}
//				    }
//				}
//		    });
//	
//		    // Configure edge colors
//		    visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<GraphEdge, Paint>() {
//				public Paint transform(GraphEdge arg0) {
//				    if (visualizationViewer.getPickedEdgeState().isPicked(arg0)) {
//						// Highlight color when picked
//						return GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
//		
//				    } else {
//						// Get the color from the component type settings
//						String colorString = graphController.getComponentTypeSettings().get(arg0.getComponentType()).getColor();
//						if (colorString != null) {
//						    Color color = new Color(Integer.parseInt(colorString));
//						    return color;
//						} else {
//						    return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
//						}
//				    }
//				}
//		    });
//	
//		    // Use straight lines as edges
//		    visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());
//	
//		    // Set edge width
//		    visualizationViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
//	
//			@Override
//			public Stroke transform(GraphEdge arg0) {
//			    return new BasicStroke(2);
//			}
//	
//		    });
//	
//		    // Mouse plugin to be placed here
//		    PluggableGraphMouse pgm = new PluggableGraphMouse();
//		    pgm.add(new PickingGraphMousePlugin<GraphNode, GraphEdge>());
//		    visualizationViewer.setGraphMouse(pgm);
//		    visualizationViewer.setBackground(Color.WHITE);
//		    visualizationViewer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		    visualizationViewer.setPreferredSize(new Dimension(200, 200));
	
		}
		return visualizationViewer;
    }
    
    /**
     * Repaints/Refreshes the visualisation viewer, with the given graph
     * @param graph The new graph to be painted
     */
    private void graphRepaint(Graph<GraphNode, GraphEdge> graph) {
		Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		layout.setSize(new Dimension(120, 120));
	
		visualizationViewer.setGraphLayout(layout);
		visualizationViewer.repaint();
		jContentPane.repaint();
    }

    /**
     * This method initializes btnOK
     * @return javax.swing.JButton
     */
    private JButton getBtnOK() {
		if (jButtonOK == null) {
		    jButtonOK = new JButton();
		    jButtonOK.setText("OK");
		    jButtonOK.setPreferredSize(new Dimension(80, 26));
		    jButtonOK.setForeground(new Color(0, 153, 0));
		    jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
		    jButtonOK.addActionListener(this);
		}
		return jButtonOK;
    }

    /**
     * Add a new network component of selectedType to the main graph joined at the pickedVertex. Updates the environment network model accordingly.
     * 
     * @param selectedType - The Network component type selected
     * @param pickedVertex - The vertex selected in the prototype preview which is to be used as common point for merging
     */
    private void addGraphPrototype(String selectedType, GraphNode pickedVertex) {
	
		// Environment network model
		NetworkModel networkModel = this.graphController.getNetworkModel();
		// The Node picked in the parent graph
		GraphNode parentPickedVertex = ((GraphEnvironmentControllerGUI) this.graphController.getEnvironmentPanel()).getGraphGUI().getPickedVertex();
	
		HashMap<String, ComponentTypeSettings> componentTypeSettings = this.graphController.getComponentTypeSettings();
	
		// Adding to an empty graph - starting from scratch
		if (networkModel.getGraph().getVertexCount() == 0) {
		    // Creating an initial dummy vertex on the parent graph so that the
		    // same merge function can be used
		    parentPickedVertex = new GraphNode(GraphNode.GRAPH_NODE_PREFIX + "0", new Point(30, 30));
		    networkModel.getGraph().addVertex(parentPickedVertex);
		}
	
		// Adding the prototype element to the main graph
		HashSet<GraphElement> graphElements = merge(networkModel.getGraph(), getVisualizationViewer().getGraphLayout().getGraph(), parentPickedVertex, pickedVertex);
	
		// create new NetworkComponent
		NetworkComponent newComponent = networkModel.addNetworkComponent(graphElement.getId(), selectedType, componentTypeSettings.get(selectedType).getGraphPrototype(), graphElement.isDirected(), graphElements, componentTypeSettings.get(selectedType).getAgentClass());
		this.graphController.refreshNetworkModel();
		// Adding the new agent to the agent start list of the environment
		this.graphController.add2Agents2Start(newComponent);

    }

    /**
     * Merge the two graphs into one graph as if v1 and v2 are the same. The resultant graph is g1 Modifying the network model here, may move this function to GraphEnvironmentController instead
     * 
     * @param g1 First Graph
     * @param g2 Second Graph
     * @param v1 a vertex in g1
     * @param v2 a vertex in g2
     */
    private HashSet<GraphElement> merge(Graph<GraphNode, GraphEdge> g1, Graph<GraphNode, GraphEdge> g2, GraphNode v1, GraphNode v2) {
		// A mapping from vertices in g2 to the newly created corresponding
		// vertices in g1
		HashMap<GraphNode, GraphNode> map = new HashMap<GraphNode, GraphNode>();
	
		// Create a HashSet containing the nodes and edges belonging to the new
		// network component and return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
	
		// Copy the vertices from g2 to g1
		// Adding the common vertex to the elements set
		elements.add(v1);
		for (GraphNode v : g2.getVertices()) {
		    if (v != v2) {
			GraphNode newNode = new GraphNode();
			// Generate the unique ID to be assigned to the new node
			String nextID = graphController.getNetworkModel().nextNodeID();
			newNode.setId(nextID);
	
			// Set position of node v1+(v-v2)
			int x = (int) (v1.getPosition().getX() + (getVisualizationViewer().getGraphLayout().transform(v).getX() - getVisualizationViewer().getGraphLayout().transform(v2).getX()));
			int y = (int) (v1.getPosition().getY() + (getVisualizationViewer().getGraphLayout().transform(v).getY() - getVisualizationViewer().getGraphLayout().transform(v2).getY()));
			newNode.setPosition(new Point(x, y));
			g1.addVertex(newNode);
			// inserting a mapping from g2 to g1 of corresponding nodes
			map.put(v, newNode);
			elements.add(newNode);
		    }
		}
	
		// copy the edges from g2 to g1
		EdgeType direction = (graphElement.isDirected()) ? EdgeType.DIRECTED : EdgeType.UNDIRECTED;
	
		for (GraphEdge e : g2.getEdges()) {
		    GraphNode front = g2.getEndpoints(e).getFirst();
		    GraphNode back = g2.getEndpoints(e).getSecond();
		    GraphEdge newEdge = new GraphEdge(e.getId(), graphElement.getType());
		    if (front == v2) {
			g1.addEdge(newEdge, v1, map.get(back), direction);
		    } else if (back == v2) {
			g1.addEdge(newEdge, map.get(front), v1, direction);
		    } else {
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
		if (jPanelBottom == null) {
		    GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		    gridBagConstraints5.insets = new Insets(5, 25, 5, 0);
		    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		    gridBagConstraints4.insets = new Insets(5, 0, 5, 25);
		    jPanelBottom = new JPanel();
		    jPanelBottom.setLayout(new GridBagLayout());
		    jPanelBottom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		    jPanelBottom.add(getBtnOK(), gridBagConstraints4);
		    jPanelBottom.add(getBtnCancel(), gridBagConstraints5);
		}
		return jPanelBottom;
    }

    /**
     * This method initializes btnCancel
     * 
     * @return javax.swing.JButton
     */
    private JButton getBtnCancel() {
		if (jButtonCancel == null) {
		    jButtonCancel = new JButton();
		    jButtonCancel.setText("Cancel");
		    jButtonCancel.setPreferredSize(new Dimension(80, 26));
		    jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
		    jButtonCancel.setForeground(new Color(153, 0, 0));
		    jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
    }

    /**
     * This method initializes jViewerPanel
     * @return javax.swing.JPanel
     */
    private JPanel getJViewerPanel() {
		if (jPanelViewer == null) {
		    GridBagConstraints gridBagConstraints = new GridBagConstraints();
		    gridBagConstraints.fill = GridBagConstraints.BOTH;
		    gridBagConstraints.weighty = 1.0;
		    gridBagConstraints.weightx = 1.0;
		    jPanelViewer = new JPanel();
		    jPanelViewer.setLayout(new GridBagLayout());
		    jPanelViewer.setPreferredSize(new Dimension(100, 250));
		    //jPanelViewer.setSize(new Dimension(100, 200));
		    jPanelViewer.add(getVisualizationViewer(), gridBagConstraints);
		}
		return jPanelViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(getBtnOK())) {
		    // --- OK button ---
		    // check whether only one vertex is picked or not
		    String selected = (String) componentTypesList.getSelectedValue();
		    Set<GraphNode> nodeSet = getVisualizationViewer().getPickedVertexState().getPicked();
		    if (nodeSet.size() == 1) {
				// Picked one vertex
				GraphNode pickedVertex = nodeSet.iterator().next();
				// Check for Star prototype
				if (graphElement instanceof Star3GraphElement) {
				    // If the picked vertex is the center of the star, cannot add
				    Graph<GraphNode, GraphEdge> graph = getVisualizationViewer().getGraphLayout().getGraph();
				    // All the edges in the graph or incident on the pickedVertex => It is a center
				    if (graph.getEdgeCount() == graph.getIncidentEdges(pickedVertex).size()) {
				    	JOptionPane.showMessageDialog(this, Language.translate("Select a vertex other than the center of the star", Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
				    	return;
				    }
				} else if (graphElement instanceof DistributionNode) {
					// --- If the current selection of the main graph is also a DistributionNode => disallow ---
					GraphNode nodeSelected = ((GraphEnvironmentControllerGUI) this.graphController.getEnvironmentPanel()).getGraphGUI().getPickedVertex();
					HashSet<NetworkComponent> components = this.graphController.getNetworkModel().getNetworkComponents(nodeSelected);
					NetworkComponent containsDistributionNode = this.graphController.getNetworkModel().componentListContainsDistributionNode(components);
					if (containsDistributionNode!=null) {
						String msg = "The selection in the main graph already contains a component of\n";
						msg += "the type 'DistributionNode'. This is only allowed once at one node! ";
						JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
				}
				
				addGraphPrototype(selected, pickedVertex);
		
				// this.setVisible(false);
				this.dispose();
	
		    } else {
		    	JOptionPane.showMessageDialog(this, Language.translate("Select one vertex", Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    }
	
		} else if (ae.getSource().equals(getBtnCancel())) {
		    // --- Cancel button ---
		    // this.setVisible(false);
		    this.dispose();
		}
    }


} // @jve:decl-index=0:visual-constraint="18,-13"
