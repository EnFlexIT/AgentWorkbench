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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.graphEnvironment.networkModel.GraphEdge;
import agentgui.graphEnvironment.networkModel.GraphElement;
import agentgui.graphEnvironment.networkModel.GraphNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * This class implements a GUI component for displaying visualizations for JUNG graphs. <br>
 * This class also has a toolbar component which provides various features for editing, importing and 
 * interacting with the graph.
 * 
 * @see GraphEnvironmentControllerGUI
 * @see GraphEnvironmentMousePlugin
 * 
 * @author Nils
 * @author Satyadeep
 */
public class BasicGraphGUI extends JPanel implements ActionListener {
	/**
	 * Event argument for notifying observers that Clear Graph button is clicked
	 */
	public static final String EVENT_NETWORKMODEL_CLEAR = "clear_graph"; // @jve:decl-index=0:
	/**
	 * Event argument for notifying observers that Add new component button is clicked
	 */
	public static final String EVENT_ADD_COMPONENT_CLICKED = "add_component_clicked"; // @jve:decl-index=0:
	/**
	 * Event argument for notifying observers that remove selected component button is clicked
	 */
	public static final String EVENT_REMOVE_COMPONENT_CLICKED = "remove_component_clicked"; // @jve:decl-index=0:
	/**
	 * Event argument for notifying observers that merge two selected nodes button is clicked
	 */
	public static final String EVENT_MERGE_NODES_CLICKED = "merge_nodes_clicked"; // @jve:decl-index=0:
	/**
	 * Event argument for notifying observers that split selected node button is clicked
	 */
	public static final String EVENT_SPLIT_NODE_CLICKED = "split_node_clicked"; // @jve:decl-index=0:
	/**
	 * Event argument for notifying observers that import graph from file button is clicked
	 */
	public static final String EVENT_IMPORT_GRAPH_CLICKED = "import_graph_clicked"; // @jve:decl-index=0:
	public static final String EVENT_OBJECT_LEFT_CLICK = "object_left_click"; // @jve:decl-index=0:
	public static final String EVENT_OBJECT_RIGHT_CLICK = "object_right_click"; // @jve:decl-index=0:
	public static final String EVENT_OBJECT_SHIFT_LEFT_CLICK = "object_shift_left_click"; // @jve:decl-index=0:
	public static final String EVENT_OBJECT_SHIFT_RIGHT_CLICK = "object_shift_right_click"; // @jve:decl-index=0:
	public static final String EVENT_OBJECT_DOUBLE_CLICK = "object_double_click"; // @jve:decl-index=0:
	
	private static final long serialVersionUID = 1L;
	/**
	 * Panel containing control buttons.
	 */
	private JPanel pnlControlls = null;
	/**
	 * Graph visualization component
	 */
	private VisualizationViewer<GraphNode, GraphEdge> visView = null;
	/**
	 * The pluggable graph mouse which can be added to the visualization viewer.
	 * Used here for customized Picking mode
	 */
	private PluggableGraphMouse pgm = null; // @jve:decl-index=0:
	/**
	 * The DefaultModalGraphMouse which can be added to the visualization
	 * viewer. Used here for the transforming mode
	 */
	private DefaultModalGraphMouse<GraphNode,GraphEdge> dgm = null; // @jve:decl-index=0:
	/**
	 * JUNG object handling zooming
	 */
	private ScalingControl scalingControl = null;
	/**
	 * The GUI's main component, either the graph visualization, or an empty
	 * JPanel if no graph is loaded
	 */
	private Component rightComponent = null;
	/**
	 * As the superclass relationship is occupied by the JPanel, notifications
	 * have to be handled by this object
	 */
	private MyObservable myObservable;
	private JToolBar jJToolBar = null;
	private JButton jButtonClearGraph = null;
	private JButton jButtonZoomIn = null;
	private JButton jButtonZoomOut = null;
	private JButton jButtonZoomReset = null;
	private JButton jButtonAddComponent = null;
	private JToggleButton jToggleMouseTransforming = null;

	private final String pathImage = Application.RunInfo.PathImageIntern(); // @jve:decl-index=0:
	private JToggleButton jToggleMousePicking = null;
	private JButton jButtonRemoveComponent = null;
	private JButton jButtonMergeNodes = null;
	private JButton jButtonSplitNode = null;
	private JButton jButtonImportGraph = null;

	/**
	 * This is the default constructor
	 */
	public BasicGraphGUI() {
		super();
		myObservable = new MyObservable();
		scalingControl = new CrossoverScalingControl();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getPnlControlls(), BorderLayout.WEST);
		// Initializing JUNG mouse modes
		initMouseModes();
	}

	/**
	 * This method initializes the two mouse modes - Transforming mode and picking mode
	 * by using PluggableGraphMouse and DefaultModalGraphMouse respectively
	 */
	private void initMouseModes() {
		pgm = new PluggableGraphMouse();
		pgm.add(new GraphEnvironmentMousePlugin(this));

		dgm = new DefaultModalGraphMouse<GraphNode, GraphEdge>();
		dgm.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
	}

	/**
	 * This method initializes pnlControlls
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlControlls() {
		if (pnlControlls == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 0.0;
			gridBagConstraints13.gridheight = 1;
			gridBagConstraints13.anchor = GridBagConstraints.NORTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			pnlControlls = new JPanel();
			pnlControlls.setPreferredSize(new Dimension(50, 400));
			pnlControlls.setLayout(new GridBagLayout());
			pnlControlls.add(getJJToolBar(), gridBagConstraints13);
		}
		return pnlControlls;
	}

	/**
	 * Gets the VisualizationViewer
	 * 
	 * @return The VisualizationViewer
	 */
	VisualizationViewer<GraphNode, GraphEdge> getVisView() {
		return visView;
	}

	/**
	 * This method assigns a graph to a new VisualizationViewer and adds it to
	 * the GUI
	 * This is used for creating graph for the first time
	 * @param graph
	 *            The graph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> graph) {

		if (graph != null) { // A graph was passed
			// Define graph layout
			Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(
					graph);
			layout.setSize(new Dimension(400, 400));
			layout.setInitializer(new Transformer<GraphNode, Point2D>() {

				@Override
				public Point2D transform(GraphNode node) {
					// The position is specified in the GraphNode instance
					return node.getPosition();
				}
			});

			// Create a new VisualizationViewer instance
			visView = new VisualizationViewer<GraphNode, GraphEdge>(layout);
			// Configure node labels
			visView.getRenderContext().setVertexLabelTransformer(
					new Transformer<GraphNode, String>() {

						@Override
						public String transform(GraphNode arg0) {
							return arg0.getId();
						}
					});

			// Configure edge labels
			visView.getRenderContext().setEdgeLabelTransformer(
					new Transformer<GraphEdge, String>() {

						@Override
						public String transform(GraphEdge arg0) {
							return arg0.getId();
						}
					});
			// Use straight lines as edges
			visView.getRenderContext().setEdgeShapeTransformer(
					new EdgeShape.Line<GraphNode, GraphEdge>());

			// Configure mouse interaction
			visView.setGraphMouse(pgm);

			visView.setBackground(Color.WHITE);

			rightComponent = new GraphZoomScrollPane(visView);			

		} else { // No graph passed
			// Use a JPanel as dummy component
			rightComponent = new JPanel();
		}

		this.add(rightComponent, BorderLayout.CENTER);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Button Zoom in
		if (e.getSource() == getJButtonZoomIn() && visView != null) {
			scalingControl.scale(visView, 1.1f, visView.getCenter());
			// Button Zoom out
		} else if (e.getSource() == getJButtonZoomOut() && visView != null) {
			scalingControl.scale(visView, 1 / 1.1f, visView.getCenter());
			// Button Reset zoom
		} else if (e.getSource() == getJButtonZoomReset() && visView != null) {
			visView.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).setToIdentity();
			visView.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.VIEW).setToIdentity();
		}
		// Button Clear graph
		else if (e.getSource() == getJButtonClearGraph() && visView != null) {
			int n = JOptionPane.showConfirmDialog(this, Language.translate(
					"Are you sure to clear the graph?", Language.EN), Language
					.translate("Confirmation", Language.EN),
					JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				myObservable.setChanged();
				myObservable.notifyObservers(new Notification(
						EVENT_NETWORKMODEL_CLEAR, null));
			}
		}

		// Button Add component clicked
		else if (e.getSource() == getJButtonAddComponent() && visView != null) {
			myObservable.setChanged();
			myObservable.notifyObservers(new Notification(
					EVENT_ADD_COMPONENT_CLICKED, null));
		}
		// Button Remove component clicked
		else if (e.getSource() == getJButtonRemoveComponent()
				&& visView != null) {
			myObservable.setChanged();
			myObservable.notifyObservers(new Notification(
					EVENT_REMOVE_COMPONENT_CLICKED, null));
		}
		// Button Merge Nodes clicked
		else if (e.getSource() == getJButtonMergeNodes() && visView != null) {
			myObservable.setChanged();
			myObservable.notifyObservers(new Notification(
					EVENT_MERGE_NODES_CLICKED, null));
		}
		// Button Split node clicked
		else if (e.getSource() == getJButtonSplitNode() && visView != null) {
			myObservable.setChanged();
			myObservable.notifyObservers(new Notification(
					EVENT_SPLIT_NODE_CLICKED, null));
		}
		// Button Transforming Mouse mode clicked
		else if (e.getSource() == getJToggleMouseTransforming()
				&& visView != null) {
			// Transforming mode
			// Setting DefaultModalGraphMouse
			visView.setGraphMouse(dgm);
		}
		// Button Picking Mouse mode clicked
		else if (e.getSource() == getJToggleMousePicking() && visView != null) {
			
			// Picking mode
			// Setting custom pluggable mouse graph
			visView.setGraphMouse(pgm);
		}
		// Button Import graph from file clicked
		else if (e.getSource() == getJButtonImportGraph() && visView != null) {
			myObservable.setChanged();
			myObservable.notifyObservers(new Notification(
					EVENT_IMPORT_GRAPH_CLICKED, null));
		}
	}

	/**
	 * Repaints the visualisation viewer, with the given graph 
	 * @param graph  -The new graph to be painted with.
	 */
	public void repaintGraph(Graph<GraphNode, GraphEdge> graph) {
		visView.getGraphLayout().setGraph(graph);
		visView.repaint();
	}

	/**
	 * This method notifies the observers about a graph object selection
	 * 
	 * @param pickedObject
	 *            The selected object
	 */
	void handleObjectSelection(Object pickedObject) {
		myObservable.setChanged();
		myObservable.notifyObservers(new Notification(EVENT_OBJECT_LEFT_CLICK,
				pickedObject));
	}

	/**
	 * Notifies the observers that this object is right clicked
	 * 
	 * @param pickedObject
	 *            the selected object
	 */
	void handleObjectRightClick(Object pickedObject) {
		myObservable.setChanged();
		myObservable.notifyObservers(new Notification(EVENT_OBJECT_RIGHT_CLICK,
				pickedObject));
	}

	/**
	 * Invoked on Shift + Left click over a node or and edge
	 * 
	 * @param pickedObject
	 *            the selected object
	 */
	public void handleObjectShiftLeftClick(Object pickedObject) {
		myObservable.setChanged();
		myObservable.notifyObservers(new Notification(
				EVENT_OBJECT_SHIFT_LEFT_CLICK, pickedObject));
	}

	/**
	 * Invoked on Shift + Right click over a node or and edge
	 * 
	 * @param pickedObject
	 *            the selected object
	 */
	public void handleObjectShiftRightClick(Object pickedObject) {
		myObservable.setChanged();
		myObservable.notifyObservers(new Notification(
				EVENT_OBJECT_SHIFT_RIGHT_CLICK, pickedObject));
	}

	/**
	 * Inovked when a graph node or edge is double clicked (left or right)
	 * @param pickedObject
	 */
	public void handleObjectDoubleClick(Object pickedObject) {
		myObservable.setChanged();
		myObservable.notifyObservers(new Notification(EVENT_OBJECT_DOUBLE_CLICK,
				pickedObject));
	}
	/**
	 * Clears the picked nodes and edges
	 */
	void clearPickedObjects() {
		visView.getPickedVertexState().clear();
		visView.getPickedEdgeState().clear();
	}

	/**
	 * Sets a node or edge as picked
	 * 
	 * @param object
	 *            The GraphNode or GraphEdge to pick
	 */
	void setPickedObject(GraphElement object) {
		if (object instanceof GraphEdge) {
			visView.getPickedEdgeState().pick((GraphEdge) object, true);
		} else if (object instanceof GraphNode) {
			visView.getPickedVertexState().pick((GraphNode) object, true);
		}
	}

	/**
	 * Marks a group of objects as picked
	 * 
	 * @param objects
	 *            The objects
	 */
	void setPickedObjects(Vector<GraphElement> objects) {
		Iterator<GraphElement> objIter = objects.iterator();
		while (objIter.hasNext()) {
			setPickedObject(objIter.next());
		}
	}

	/**
	 * Add an observer to the GUI's Observable
	 * 
	 * @param observer
	 */
	public void addObserver(Observer observer) {
		myObservable.addObserver(observer);
	}

	/**
	 * @return the myObservable
	 */
	public Observable getMyObservable() {
		return myObservable;
	}

	/**
	 * Helper class for making Observable usable in this context
	 * 
	 * @author Nils
	 */
	private class MyObservable extends Observable {
		/**
		 * Calls the superclass' protected setChanged method
		 */
		public void setChanged() {
			super.setChanged();
		}
	}

	/**
	 * A Notification class used for combining arguments into single one for
	 * notifying observers. Contains the event and the argument to be passed.
	 * 
	 * @author Satyadeep
	 * 
	 */
	public class Notification {
		/**
		 * The argument object
		 */
		
		private Object arg;
		/**
		 * The event description
		 */
		private String event;
		
		/**
		 * Constructor for creating notification object
		 * @param event
		 * @param arg
		 */
		public Notification(String event, Object arg) {
			this.event = event;
			this.arg = arg;
		}
		
		/**
		 * 
		 * @return The argument object
		 */
		public Object getArg() {
			return arg;
		}

		/**
		 * 
		 * @return The event String
		 */
		public String getEvent() {
			return event;
		}
	}

	/**
	 * This method initializes jJToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJJToolBar() {
		if (jJToolBar == null) {
			jJToolBar = new JToolBar();
			jJToolBar.setOrientation(JToolBar.VERTICAL);
			jJToolBar.setFloatable(false);
			jJToolBar.setPreferredSize(new Dimension(40, 380));

			jJToolBar.add(getJButtonZoomIn());
			jJToolBar.add(getJButtonZoomOut());

			jJToolBar.add(getJButtonZoomReset());
			jJToolBar.addSeparator();
			jJToolBar.add(getJToggleMouseTransforming());
			jJToolBar.add(getJToggleMousePicking());
			ButtonGroup bg = new ButtonGroup();
			bg.add(jToggleMousePicking);
			bg.add(jToggleMouseTransforming);
			jJToolBar.addSeparator();
			jJToolBar.add(getJButtonAddComponent());
			jJToolBar.add(getJButtonRemoveComponent());
			jJToolBar.add(getJButtonMergeNodes());
			jJToolBar.add(getJButtonSplitNode());
			jJToolBar.addSeparator();
			jJToolBar.add(getJButtonClearGraph());
			jJToolBar.add(getJButtonImportGraph());

		}
		return jJToolBar;
	}

	/**
	 * This method initializes jButtonClearGraph
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonClearGraph() {
		if (jButtonClearGraph == null) {
			jButtonClearGraph = new JButton();
			jButtonClearGraph.setPreferredSize(new Dimension(26, 26));
			jButtonClearGraph.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "Remove.png")));
			jButtonClearGraph.setToolTipText(Language.translate("Clear graph",
					Language.EN));
			jButtonClearGraph.addActionListener(this);

		}
		return jButtonClearGraph;
	}

	/**
	 * This method initializes jButtonZoomIn
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomIn() {
		if (jButtonZoomIn == null) {
			jButtonZoomIn = new JButton();
			jButtonZoomIn.setPreferredSize(new Dimension(26, 26));
			jButtonZoomIn.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "ZoomIn.png")));
			jButtonZoomIn.setToolTipText(Language.translate("Vergrößern"));
			jButtonZoomIn.addActionListener(this);

		}
		return jButtonZoomIn;
	}

	/**
	 * This method initializes jButtonZoomOut
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomOut() {
		if (jButtonZoomOut == null) {
			jButtonZoomOut = new JButton();
			jButtonZoomOut.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "ZoomOut.png")));
			jButtonZoomOut.setPreferredSize(new Dimension(26, 26));
			jButtonZoomOut.setToolTipText(Language.translate("Verkleinern"));
			jButtonZoomOut.addActionListener(this);

		}
		return jButtonZoomOut;
	}

	/**
	 * This method initializes jButtonZoomReset
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomReset() {
		if (jButtonZoomReset == null) {
			jButtonZoomReset = new JButton();
			jButtonZoomReset.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "Refresh.png")));
			jButtonZoomReset.setPreferredSize(new Dimension(26, 26));
			jButtonZoomReset.setToolTipText(Language.translate("Zurücksetzen"));
			jButtonZoomReset.addActionListener(this);

		}
		return jButtonZoomReset;
	}

	/**
	 * This method initializes jButtonAddComponent
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAddComponent() {
		if (jButtonAddComponent == null) {
			jButtonAddComponent = new JButton();
			jButtonAddComponent.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "ListPlus.png")));
			jButtonAddComponent.setPreferredSize(new Dimension(26, 26));
			jButtonAddComponent.setToolTipText(Language.translate(
					"Add new component", Language.EN));
			jButtonAddComponent.addActionListener(this);
		}
		return jButtonAddComponent;
	}

	/**
	 * This method initializes jToggleMouseTransforming
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private JToggleButton getJToggleMouseTransforming() {
		if (jToggleMouseTransforming == null) {
			jToggleMouseTransforming = new JToggleButton();
			jToggleMouseTransforming.setIcon(new ImageIcon(getClass()
					.getResource(pathImage + "move.png")));
			jToggleMouseTransforming.setPreferredSize(new Dimension(26, 26));
			jToggleMouseTransforming.setToolTipText(Language.translate(
					"Switch to Transforming mode", Language.EN));
			jToggleMouseTransforming.setSize(new Dimension(36, 36));
			jToggleMouseTransforming.addActionListener(this);

		}
		return jToggleMouseTransforming;
	}

	/**
	 * This method initializes jToggleMousePicking
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private JToggleButton getJToggleMousePicking() {
		if (jToggleMousePicking == null) {
			jToggleMousePicking = new JToggleButton();
			jToggleMousePicking.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "edit.png")));
			jToggleMousePicking.setPreferredSize(new Dimension(26, 26));
			jToggleMousePicking.setToolTipText(Language.translate(
					"Switch to Picking mode", Language.EN));
			jToggleMousePicking.setSize(new Dimension(36, 36));
			jToggleMousePicking.addActionListener(this);
			jToggleMousePicking.setSelected(true);
		}
		return jToggleMousePicking;
	}

	/**
	 * This method initializes jButtonRemoveComponent
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRemoveComponent() {
		if (jButtonRemoveComponent == null) {
			jButtonRemoveComponent = new JButton();
			jButtonRemoveComponent.setIcon(new ImageIcon(getClass()
					.getResource(pathImage + "ListMinus.png")));
			jButtonRemoveComponent.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveComponent.setToolTipText(Language.translate(
					"Remove selected component", Language.EN));
			jButtonRemoveComponent.addActionListener(this);
		}
		return jButtonRemoveComponent;
	}

	/**
	 * This method initializes jButtonMergeNodes
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonMergeNodes() {
		if (jButtonMergeNodes == null) {
			jButtonMergeNodes = new JButton();
			jButtonMergeNodes.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "Merge.png")));
			jButtonMergeNodes.setPreferredSize(new Dimension(26, 26));
			jButtonMergeNodes.setToolTipText(Language.translate(
					"Merge two nodes", Language.EN));
			jButtonMergeNodes.addActionListener(this);

		}
		return jButtonMergeNodes;
	}

	/**
	 * This method initializes jButtonSplitNode
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSplitNode() {
		if (jButtonSplitNode == null) {
			jButtonSplitNode = new JButton();
			jButtonSplitNode.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "split.png")));
			jButtonSplitNode.setPreferredSize(new Dimension(26, 26));
			jButtonSplitNode.setToolTipText(Language.translate(
					"Split the node into two nodes", Language.EN));
			jButtonSplitNode.addActionListener(this);

		}
		return jButtonSplitNode;
	}

	/**
	 * This method initializes jButtonImportGraph
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonImportGraph() {
		if (jButtonImportGraph == null) {
			jButtonImportGraph = new JButton();
			jButtonImportGraph.setIcon(new ImageIcon(getClass().getResource(
					pathImage + "import.png")));
			jButtonImportGraph.setPreferredSize(new Dimension(26, 26));
			jButtonImportGraph.setToolTipText(Language.translate(
					"Import Graph from file", Language.EN));
			jButtonImportGraph.addActionListener(this);

		}
		return jButtonImportGraph;
	}



}
