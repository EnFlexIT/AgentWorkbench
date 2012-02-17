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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.components.ComponentTypeListElement;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelAdapter;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.Star3GraphElement;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

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

	private JPanel jPanelCenter = null;
	private JPanel jPanelSouth = null;

	private JPanel jContentPane = null;
    private JPanel jPanelBottom = null;
    private JPanel jPanelViewer = null;
    private JScrollPane jScrollPane = null;
    private JList componentTypesList = null;
    private JLabel jLabelInstructionMerge = null;
    private JLabel jLabelInstructionSelect = null;
    
    private JButton jButtonOK = null;
    private JButton jButtonCancel = null;

    private GraphEnvironmentController graphController = null;
    private BasicGraphGui basicGraphGui = null;
    
    private VisualizationViewer<GraphNode, GraphEdge> visViewer = null;

    /** The graph element prototype of the selected component type. */
    private GraphElementPrototype graphElement = null; // @jve:decl-index=0:
	private ComponentTypeListElement currCtsListElement;
	private DomainSettings currDomainSetings;
	

	/**
     * Gets the parent object and initializes.
     * @param controller the GraphEnvironmentController
     */
    public AddComponentDialog(GraphEnvironmentController controller) {
		super(Application.MainWindow);
		this.graphController = controller;
		this.basicGraphGui = ((GraphEnvironmentControllerGUI)controller.getEnvironmentPanel()).getGraphGUI();
		initialize();
    }

    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
		this.setSize(400, 560);
		this.setModal(true);
		this.setTitle("Select a Network Component to Add");
		this.setTitle(Language.translate(this.getTitle(), Language.EN));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.registerEscapeKeyStroke();
    }
    
    /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    /**
	 * This method initializes jContentPane	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelCenter(), BorderLayout.CENTER);
			jContentPane.add(getJPanelSouth(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jPanelSouth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(15, 15, 0, 15);
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.ipadx = 0;
			gridBagConstraints6.insets = new Insets(15, 15, 15, 15);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.CENTER;
			gridBagConstraints3.insets = new Insets(5, 15, 0, 15);
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 0.0;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			
			jLabelInstructionMerge = new JLabel();
		    jLabelInstructionMerge.setText("Select a vertex to merge");
		    jLabelInstructionMerge.setFont(new Font("Dialog", Font.BOLD, 12));
		    jLabelInstructionMerge.setText(Language.translate(jLabelInstructionMerge.getText(), Language.EN));
		
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new GridBagLayout());
			jPanelSouth.add(getJViewerPanel(), gridBagConstraints3);
			jPanelSouth.add(getJBottomPanel(), gridBagConstraints6);
			jPanelSouth.add(jLabelInstructionMerge, gridBagConstraints2);
		}
		return jPanelSouth;
	}

	/**
	 * This method initializes jPanelCenter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCenter() {
		if (jPanelCenter == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(5, 15, 0, 15);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.insets = new Insets(15, 15, 0, 15);
			
			jLabelInstructionSelect = new JLabel();
			jLabelInstructionSelect.setText("Select a network component");
			jLabelInstructionSelect.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelInstructionSelect.setText(Language.translate(jLabelInstructionSelect.getText(), Language.EN));

			jPanelCenter = new JPanel();
			jPanelCenter.setLayout(new GridBagLayout());
			jPanelCenter.add(jLabelInstructionSelect, gridBagConstraints7);
			jPanelCenter.add(getJScrollPane(), gridBagConstraints1);
		}
		return jPanelCenter;
	}
	
    /**
     * This method initializes componentTypesList
     * @return javax.swing.JList
     */
    private JList getComponentTypesList() {
		if (componentTypesList == null) {
		    componentTypesList = new JList(this.getListData());
		    componentTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    componentTypesList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				    if (!e.getValueIsAdjusting()) {
						ComponentTypeListElement ctsSelected = (ComponentTypeListElement) componentTypesList.getSelectedValue();
						String graphPrototype = ctsSelected.getComponentTypeSettings().getGraphPrototype();
						if (graphPrototype!=null) {
							setPrototypePreview(ctsSelected);	
						} else {
							String msg = Language.translate("The definition of the component contains no graph prototyp definition.", Language.EN);
							String titel = Language.translate("Missing definition of the Graph prototype for '" + ctsSelected.getComponentName() + "'!", Language.EN);
							JOptionPane.showMessageDialog(getJContentPane(), msg, titel, JOptionPane.WARNING_MESSAGE);
						}
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
    private void setPrototypePreview(ComponentTypeListElement ctsListElement) {
	
    	// --- Get the configuration for the selection -------------- 
    	this.currCtsListElement = ctsListElement;
    	String domain = this.currCtsListElement.getDomain();

    	if (domain==null) {
    		this.currDomainSetings = this.graphController.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
    	} else {
    		this.currDomainSetings = this.graphController.getDomainSettings().get(domain);	
    	}
    	
    	// --- Create the graph of the NetworkComponent -------------
		this.graphElement = null;
		try {
		    Class<?> theClass = Class.forName(this.currCtsListElement.getComponentTypeSettings().getGraphPrototype());
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
		    String nextID = this.graphController.getNetworkModelAdapter().nextNetworkComponentID();
		    graphElement.setId(nextID);
		    graphElement.setType(this.currCtsListElement.getComponentName());
	
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
     * Gets the list of componentTypeSettings from the controller and 
     * returns it as a Vector<ComponentTypeListElement>
     * @see ComponentTypeListElement
     * @return Object[] - array of component types
     */
    private Vector<ComponentTypeListElement> getListData() {
		
    	// --- Create the Vector ----------------
    	Vector<ComponentTypeListElement> list = new Vector<ComponentTypeListElement>();
    	HashMap<String, ComponentTypeSettings> ctsHash = this.graphController.getComponentTypeSettings();
		if (ctsHash != null) {
			Iterator<String> ctsIt = ctsHash.keySet().iterator();
		    while (ctsIt.hasNext()) {
		    	String componentName = ctsIt.next(); 
		    	ComponentTypeSettings cts = ctsHash.get(componentName); 
				list.add(new ComponentTypeListElement(componentName, cts));
		    }
		} 
		// --- Sort the Vector ------------------
		Collections.sort(list, new Comparator<ComponentTypeListElement>() {
			@Override
			public int compare(ComponentTypeListElement cts1, ComponentTypeListElement cts2) {
				if (cts1.getDomain().equals(cts2.getDomain())) {
					return cts1.getComponentName().compareTo(cts2.getComponentName());
				} else {
					return cts1.getDomain().compareTo(cts2.getDomain());
				}
			}
		});
		return list;
    }

    /**
     * Initializes the VisualizationViewer
     * @return The VisualizationViewer
     */
    private VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		// create VisualizationViewer if it is not there
		if (visViewer == null) {

			// --- Define the graph -------------
			Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode, GraphEdge>();
			// --- Define the GraphMouse --------
			PluggableGraphMouse pgm = new PluggableGraphMouse();
		    pgm.add(new PickingGraphMousePlugin<GraphNode, GraphEdge>());
			// --- Define the Layout ------------
		    Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		    layout.setSize(new Dimension(120, 120));
		   
		    // --- Create VisualizationViewer ---
		    visViewer = new VisualizationViewer<GraphNode, GraphEdge>(layout);

		    visViewer.setLayout(new GridBagLayout());
		    visViewer.setGraphMouse(pgm);
		    visViewer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			visViewer.setBackground(Color.WHITE);
			visViewer.setPreferredSize(new Dimension(200, 200));
		    
		    // --- Define a 
			MutableTransformer mutableLayout = visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
			mutableLayout.translate(120, 10);
	
			// --- Configure the vertex shape and size ------------------------
			visViewer.getRenderContext().setVertexShapeTransformer(new VertexShapeSizeAspect<GraphNode, GraphEdge>());
			

			// --- Set tool tip for nodes -------------------------------------
			visViewer.setVertexToolTipTransformer(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode edge) {
					return edge.getId();
				}
			});

			// --- Configure vertex colors ------------------------------------
			visViewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
				@Override
				public Paint transform(GraphNode node) {

					String colorString = null;
					String colorStringDefault = currDomainSetings.getVertexColor();
					Color defaultColor = new Color(Integer.parseInt(colorStringDefault));

					// --- Get color from component type settings -----
					try {
						// --- Get the vertex size from the component type settings -
						if (isCurrentComponentDistributionNode()) {
							// --- Distribution node ----------------
							if (visViewer.getPickedVertexState().isPicked(node) == true) {
								colorString = currDomainSetings.getVertexColorPicked();
							} else {
								colorString = currCtsListElement.getComponentTypeSettings().getColor();	
							}
						} else {
							if (visViewer.getPickedVertexState().isPicked(node) == true) {
								colorString = currDomainSetings.getVertexColorPicked();
							} else {
								colorString = currDomainSetings.getVertexColor();
							}
						}
						if (colorString != null) {
							return new Color(Integer.parseInt(colorString));
						}
						return defaultColor;

					} catch (NullPointerException ex) {
						ex.printStackTrace();
						return defaultColor;
					}
				}
			}); // end transformer
					
			// --- Configure to show node labels ------------------------------
			visViewer.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
					@Override
					public String transform(GraphNode node) {
						if (isCurrentComponentDistributionNode()) {
							if (currCtsListElement.getComponentTypeSettings().isShowLabel()) {
								return node.getId();
							}
						} else {
							if (currDomainSetings.isShowLabel()) {
								return node.getId();
							}
						}
						return null;
					}
				} // end transformer
			);

			// --- Configure edge colors --------------------------------------
			visViewer.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<GraphEdge, Paint>() {
				@Override
				public Paint transform(GraphEdge edge) {
					if (visViewer.getPickedEdgeState().isPicked(edge)) {
						return GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
					}

					try {
						String colorString = currCtsListElement.getComponentTypeSettings().getColor();
						if (colorString != null) {
							return new Color(Integer.parseInt(colorString));
						}

					} catch (NullPointerException ex) {
						ex.printStackTrace();
					}
					return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
				}
			});
			// --- Configure Edge Image Labels --------------------------------
			visViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge edge) {
					// Get the path of the Image from the component type settings
					String textDisplay = "";
					try {
						String edgeImage = currCtsListElement.getComponentTypeSettings().getEdgeImage();
						boolean showLabel = currCtsListElement.getComponentTypeSettings().isShowLabel();

						if (showLabel) {
							textDisplay = edge.getId();
						}

						if (edgeImage != null) {
							URL url = getClass().getResource(edgeImage);
							if (url != null) {
								if (showLabel) {
									textDisplay = "<html><center>" + textDisplay + "<br><img src='" + url + "'></center></html>";
								} else {
									textDisplay = "<html><center><img src='" + url + "'></center></html>";
								}
							}
						}
						return textDisplay;

					} catch (NullPointerException ex) {
						ex.printStackTrace();
						return edge.getId();
					}
				}
			});
			// --- Configure edge label position ------------------------------
			visViewer.getRenderContext().setLabelOffset(0);
			visViewer.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<GraphNode, GraphEdge>(.5, .5));

			// --- Use straight lines as edges --------------------------------
			visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());

			// --- Set edge width ---------------------------------------------
			visViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
				@Override
				public Stroke transform(GraphEdge edge) {
					float edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					try {
						edgeWidth = currCtsListElement.getComponentTypeSettings().getEdgeWidth();
						if (edgeWidth == 0) {
							edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
						}

					} catch (Exception e) {
						e.printStackTrace();
						edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					}
					return new BasicStroke(edgeWidth);
				}
			});
	
		}
		return visViewer;
    }
    
    
    /**
     * Checks if the current selection is a DistributionNode.
     * @return true, if it is a DistributionNode
     */
    private boolean isCurrentComponentDistributionNode() {
    	if (currCtsListElement==null) {
    		return false;
    	} else {
    		return currCtsListElement.getComponentTypeSettings().getGraphPrototype().equals(DistributionNode.class.getName());	
    	}
    }
    
    /**
     * Repaints/Refreshes the visualisation viewer, with the given graph
     * @param graph The new graph to be painted
     */
    private void graphRepaint(Graph<GraphNode, GraphEdge> graph) {
		
		// --- Define default layout ------------
    	Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		layout.setSize(new Dimension(100, 100));

		if (graph.getEdgeCount()==1) {
			
			GraphEdge edge = graph.getEdges().iterator().next();
			EdgeType edgeType = graph.getEdgeType(edge);
			if (edgeType==EdgeType.DIRECTED) {
			
				GraphNode nodeSource = graph.getSource(edge);
				GraphNode nodeDestin = graph.getDest(edge);
				nodeSource.setPosition(new Point2D.Double(0, 50));
				nodeDestin.setPosition(new Point2D.Double(100, 50));
				
				layout = new StaticLayout<GraphNode, GraphEdge>(graph);
				layout.setInitializer(new Transformer<GraphNode, Point2D>() {
					public Point2D transform(GraphNode node) {
						return node.getPosition(); // The position is specified in the GraphNode instance
					}
				});
				
			}
		}

		// --- Set the new layout ---------------
		visViewer.setGraphLayout(layout);
		visViewer.repaint();
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
     * @param selectedComponentTypeListElement - The Network component type selected
     * @param pickedVertex - The vertex selected in the prototype preview which is to be used as common point for merging
     */
    private void addGraphPrototype(ComponentTypeListElement selectedComponentTypeListElement, GraphNode pickedVertex) {
	
    	String comp2Add = selectedComponentTypeListElement.getComponentName();
    	ComponentTypeSettings cts = selectedComponentTypeListElement.getComponentTypeSettings();
    	
    	// --- Get information about the current network model ----------------
		NetworkModelAdapter networkModel = this.graphController.getNetworkModelAdapter();
		GraphNode parentPickedVertex = this.basicGraphGui.getPickedSingleNode();
	
		// --- Adding to an empty graph - starting from scratch ---------------
		if (networkModel.getGraph().getVertexCount() == 0) {
		    // Creating an initial dummy vertex on the parent graph so that the
		    // same merge function can be used
		    parentPickedVertex = new GraphNode(GraphNode.GRAPH_NODE_PREFIX + "0", new Point(30, 30));
		    networkModel.getGraph().addVertex(parentPickedVertex);
		}
	
		// --- Adding the prototype element to the main graph -----------------
		HashSet<GraphElement> graphElements = merge(networkModel.getGraph(), getVisualizationViewer().getGraphLayout().getGraph(), parentPickedVertex, pickedVertex);
	
		// --- register the new NetworkComponent in the NetworkModel ----------
		NetworkComponent newComponent = new NetworkComponent(graphElement.getId(), comp2Add, cts.getGraphPrototype(), cts.getAgentClass(), graphElements, graphElement.isDirected());
		networkModel.addNetworkComponent(newComponent);
		
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

    	// A mapping from vertices in g2 to the newly created corresponding vertices in g1
		HashMap<GraphNode, GraphNode> map = new HashMap<GraphNode, GraphNode>();
	
		// Create a HashSet containing the nodes and edges belonging to the new
		// network component and return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		// Adding the common vertex to the elements set
		elements.add(v1);

		// Copy the vertices from g2 to g1
		for (GraphNode v : g2.getVertices()) {
		    if (v != v2) {
				GraphNode newNode = new GraphNode();
				// Generate the unique ID to be assigned to the new node
				String nextID = graphController.getNetworkModelAdapter().nextNodeID();
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
		    jPanelViewer.setPreferredSize(new Dimension(20, 150));
		    jPanelViewer.add(getVisualizationViewer(), gridBagConstraints);
		}
		return jPanelViewer;
    }

    /**
	 * Controls the shape, size, and aspect ratio for each vertex.
	 * 
	 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private final class VertexShapeSizeAspect<V, E> extends AbstractVertexShapeTransformer<GraphNode> implements Transformer<GraphNode, Shape> {

		/**
		 * Instantiates a new vertex shape size aspect.
		 */
		public VertexShapeSizeAspect() {

			this.setSizeTransformer(new Transformer<GraphNode, Integer>() {

				@Override
				public Integer transform(GraphNode node) {

					Integer size = graphController.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexSize();
					Integer sizeSettings = null;
					try {
						if (isCurrentComponentDistributionNode()) {
							// --- DistributionNode: get size from ComponentTypeSettings - Start --
							sizeSettings = (int) currCtsListElement.getComponentTypeSettings().getEdgeWidth();
						} else {
							// --- Normal node or ClusterNode ---------------------------- Start --
							sizeSettings = currDomainSetings.getVertexSize();
						}
						if (sizeSettings != null) {
							size = sizeSettings;
						}

					} catch (NullPointerException ex) {
						System.err.println("Invalid vertex size");
						ex.printStackTrace();
					}
					return size;
				}
			});

		}// end constructor

		/*
		 * (non-Javadoc)
		 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
		 */
		@Override
		public Shape transform(GraphNode node) {
			return factory.getEllipse(node); // DEFAULT;
		}
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
		    ComponentTypeListElement selected = (ComponentTypeListElement) componentTypesList.getSelectedValue();
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
					GraphNode nodeSelected = this.basicGraphGui.getPickedSingleNode();
					HashSet<NetworkComponent> components = this.graphController.getNetworkModelAdapter().getNetworkComponents(nodeSelected);
					NetworkComponent containsDistributionNode = this.graphController.getNetworkModelAdapter().containsDistributionNode(components);
					if (containsDistributionNode!=null) {
						String msg = "The selection in the main graph already contains a component of\n";
						msg += "the type 'DistributionNode'. This is only allowed once at one node! ";
						JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				this.addGraphPrototype(selected, pickedVertex);
				this.dispose();
	
		    } else {
		    	JOptionPane.showMessageDialog(this, Language.translate("Select one vertex", Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    }
	
		} else if (ae.getSource().equals(getBtnCancel())) {
		    // --- Cancel button ---
		    this.dispose();
		}
    }


} // @jve:decl-index=0:visual-constraint="18,-13"
