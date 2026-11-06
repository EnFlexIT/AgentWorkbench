package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.ProcessingDataSourceIntegration;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.DataTransformationProcessor;
import de.enflexit.df.core.processing.transformation.ui.DataTransformationConfigurationPanel;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeDataSource;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformationGraph.PersistentTransformationGraph;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import tech.tablesaw.api.Table;

import javax.swing.JSplitPane;

/**
 * The Class JPanelTransformationGraphEditor.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphEditorPanel extends JPanel implements ActionListener, GraphEventListener<DataTableNode, DataTransformationEdge>, PropertyChangeListener {
	
	private static final long serialVersionUID = 3869719655631231629L;
	
	private static final double NODE_RECTANGLE_WIDTH = 140;
	private static final double NODE_RECTANGLE_HEIGHT = 50;
	private static final double NODE_RECTANGLE_ARC = 15;
	
	private static final Color COLOR_NODE_NORMAL = new Color(14, 87, 114); 
	private static final Color COLOR_NODE_PICKED = new Color(112, 161, 192);
	
	private static final String HTML_COLOR_LABEL_NORMAL = "White";
	private static final String HTML_COLOR_LABEL_PICKED = "Black";
	
	private static final String ICON_TABLE_LIGHT_MODE = "table_black.png";
	private static final String ICON_TABLE_DARK_MODE = "table_grey.png";
	private static final String ICON_TRANSFORMATION_LIGHT_MODE = "hierarchy_black.png";
	private static final String ICON_TRANSFORMATION_DARK_MODE = "hierarchy_grey.png";
	private static final String ICON_VERIFY = "MBcheckGreen.png";
	private static final String ICON_DELETE = "Delete.png";
	private static final String ICON_SAVE = "MBsave.png";
	private static final String ICON_REVERT_LIGHT_MODE = "revert_black.png";
	private static final String ICON_REVERT_DARK_MODE = "revert_grey.png";
	
	private JToolBar jToolBarMain;
	private JButton jButtonAddDataSource;
	private JButton jButtonAddDataTransformation;
	private JButton jButtonRemove;
	private JButton jButtonVerify;
	private JButton jButtonApplyChanges;
	private JButton jButtonRevertChanges;
	
	private DataController dataController;
	private TransformationGraphController graphController;
	
	private DataTransformationConfigurationPanel transformationPanel;
	
	private GraphZoomScrollPane graphZoomScrollPane;
	private VisualizationViewer<DataTableNode, DataTransformationEdge> visualizationViewer;
	private JSplitPane jSplitPaneMainView;
	
	private Object selectedGraphElement;
	
	private DataTransformationProcessor transformationVisitor;
	
	private ProcessingDataSourceIntegration dataSourceIntegration;
	
	private Graph<DataTableNode, DataTransformationEdge> transformationGraph;
	
	private boolean graphChanged;
	
	/**
	 * Instantiates a new j panel transformation graph editor.
	 * @param graphController the graph controller
	 */
	public TransformationGraphEditorPanel(DataController dataController) {
		this.dataController = dataController;
		this.initialize();
	}
	
	public TransformationGraphEditorPanel(DataController dataController, Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		this.dataController = dataController;
		this.transformationGraph = transformationGraph;
		this.initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getJToolBarMain(), BorderLayout.NORTH);
		this.add(getJSplitPaneMainView(), BorderLayout.CENTER);
		
		// --- Hide the right component initially ---------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(1.0);
			}
		});
	}

	private JToolBar getJToolBarMain() {
		if (jToolBarMain == null) {
			jToolBarMain = new JToolBar();
			jToolBarMain.add(getJButtonAddDataSource());
			jToolBarMain.add(this.getjButtonAddDataTransformation());
			jToolBarMain.add(getJButtonRemove());
			jToolBarMain.add(getJButtonVerify());
			jToolBarMain.addSeparator();
			jToolBarMain.add(getJButtonApplyChanges());
			jToolBarMain.add(getJButtonRevertChanges());
		}
		return jToolBarMain;
	}
	private JButton getJButtonAddDataSource() {
		if (jButtonAddDataSource == null) {
			jButtonAddDataSource = new JButton(BundleHelper.getThemedIcon(ICON_TABLE_LIGHT_MODE, ICON_TABLE_DARK_MODE));
			jButtonAddDataSource.setToolTipText("Select data sources");
			jButtonAddDataSource.addActionListener(this);
		}
		return jButtonAddDataSource;
	}
	private JButton getjButtonAddDataTransformation() {
		if (jButtonAddDataTransformation==null) {
			jButtonAddDataTransformation = new JButton(BundleHelper.getThemedIcon(ICON_TRANSFORMATION_LIGHT_MODE, ICON_TRANSFORMATION_DARK_MODE));
			jButtonAddDataTransformation.setToolTipText("Add a data transformation");
			jButtonAddDataTransformation.addActionListener(this);
			jButtonAddDataTransformation.setEnabled(false);
		}
		return jButtonAddDataTransformation;
	}
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton(BundleHelper.getImageIcon(ICON_DELETE));
			jButtonRemove.setToolTipText("Remove the selected item");
			jButtonRemove.addActionListener(this);
			jButtonRemove.setEnabled(false);
		}
		return jButtonRemove;
	}
	private JButton getJButtonVerify() {
		if (jButtonVerify == null) {
			jButtonVerify = new JButton(BundleHelper.getImageIcon(ICON_VERIFY));
			jButtonVerify.setToolTipText("Verify the transformation graph - not implemented yet!");
			jButtonVerify.addActionListener(this);
			jButtonVerify.setEnabled(false);
		}
		return jButtonVerify;
	}
	private JButton getJButtonApplyChanges() {
		if (jButtonApplyChanges == null) {
			jButtonApplyChanges = new JButton(BundleHelper.getImageIcon(ICON_SAVE));
			jButtonApplyChanges.setToolTipText("Save the changes, save or update the transformation result data source.");
			jButtonApplyChanges.setEnabled(false);
			jButtonApplyChanges.addActionListener(this);
		}
		return jButtonApplyChanges;
	}

	private JButton getJButtonRevertChanges() {
		if (jButtonRevertChanges == null) {
			jButtonRevertChanges = new JButton(BundleHelper.getThemedIcon(ICON_REVERT_LIGHT_MODE, ICON_REVERT_DARK_MODE));
			jButtonRevertChanges.setToolTipText("Revert all changes, reset the transformation graph to the stored version.");
			jButtonRevertChanges.setEnabled(false);
			jButtonRevertChanges.addActionListener(this);
		}
		return jButtonRevertChanges;
	}

	private JSplitPane getJSplitPaneMainView() {
		if (jSplitPaneMainView == null) {
			jSplitPaneMainView = new JSplitPane();
			jSplitPaneMainView.setLeftComponent(this.getJPanelGraph());
			jSplitPaneMainView.setRightComponent(new JPanel());
		}
		return jSplitPaneMainView;
	}
	private GraphZoomScrollPane getJPanelGraph() {
		if (graphZoomScrollPane == null) {
			graphZoomScrollPane = new GraphZoomScrollPane(this.getVisualizationViewer());
		}
		return graphZoomScrollPane;
	}
	
	/**
	 * Creates a table details view panel for the provided table node.
	 * @param tableNode the table node
	 * @return the table view panel
	 */
	private DataTransformaitonTableDetailsViewPanel getTableDetailsViewPanel(DataTableNode tableNode) {
		return new DataTransformaitonTableDetailsViewPanel(tableNode);
	}
	
	private DataTransformationConfigurationPanel getTransformationPanel() {
		if (transformationPanel==null) {
			transformationPanel = new DataTransformationConfigurationPanel(this);
			transformationPanel.addChangeListener(this);
		}
		return transformationPanel;
	}
	
	/**
	 * Gets the graph controller.
	 * @return the graph controller
	 */
	public TransformationGraphController getGraphController() {
		if (graphController==null) {
			graphController = new TransformationGraphController(this.getDataController());
			if (this.transformationGraph!=null) {
				graphController.setTransformationGraph(transformationGraph);
			}
		}
		return graphController;
	}
	/**
	 * Gets the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Gets the visualization viewer.
	 * @return the visualization viewer
	 */
	VisualizationViewer<DataTableNode, DataTransformationEdge> getVisualizationViewer() {
		if (visualizationViewer==null) {
			
			visualizationViewer = new VisualizationViewer<DataTableNode, DataTransformationEdge>(this.getGraphController().getGraphLayout());
			visualizationViewer.setBackground(AwbThemeColor.Canvas_Background.getColor());
			
			// --- Customize the renderer (round rectangles as nodes, straight lines as edges, node labels in the middle of the rectangle)
			double xPos = -(NODE_RECTANGLE_WIDTH/2);	// JUNG coordinates refer to the center
			double yPos = -(NODE_RECTANGLE_HEIGHT/2);
			visualizationViewer.getRenderContext().setVertexShapeTransformer(node -> new RoundRectangle2D.Double(xPos, yPos, NODE_RECTANGLE_WIDTH, NODE_RECTANGLE_HEIGHT, NODE_RECTANGLE_ARC, NODE_RECTANGLE_ARC));
			visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape<DataTableNode, DataTransformationEdge>(this.getGraphController().getTransformationGraph()).new Line());
			
			PickedState<DataTableNode> pickedState = this.getVisualizationViewer().getPickedVertexState();
			visualizationViewer.getRenderContext().setVertexStrokeTransformer(node -> pickedState.isPicked(node) ? new BasicStroke(3.0f) : new BasicStroke(1.0f));
			visualizationViewer.getRenderContext().setVertexFillPaintTransformer(node -> pickedState.isPicked(node) ? COLOR_NODE_PICKED : COLOR_NODE_NORMAL);
//			visualizationViewer.getRenderContext().setVertexLabelRenderer(new CustomVertexLabelRenderer(pickedState));
			
			visualizationViewer.getRenderContext().setVertexLabelTransformer(node -> getLabelRendererHTMLString(node.getLabelText(), pickedState.isPicked(node)));
			visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
			
			// --- Configure mouse interactions -----------
			visualizationViewer.setPickSupport(new ShapePickSupport<DataTableNode, DataTransformationEdge>(visualizationViewer));
			visualizationViewer.setGraphMouse(this.getZoomOnlyGraphMouse());
			TransformationGraphMouseAdapter mouseAdapter = new TransformationGraphMouseAdapter(this);
			visualizationViewer.addMouseListener(mouseAdapter);
			visualizationViewer.addMouseMotionListener(mouseAdapter);

			// --- Register as graph event listener -------
			ObservableGraph<DataTableNode, DataTransformationEdge> graph = (ObservableGraph<DataTableNode, DataTransformationEdge>) this.getGraphController().getTransformationGraph();
			graph.addGraphEventListener(this);
			
		}
		return visualizationViewer;
	}
	
	/**
	 * Gets the next free node position.
	 * @return the next free node position
	 */
	protected Point2D getNextFreeNodePosition() {
		return this.getNextFreeNodePosition(null);
	}
	
	/**
	 * Gets the next free node position, starting from the provided position.
	 * @param startingFrom the starting position.
	 * @return the next free node position
	 */
	protected Point2D getNextFreeNodePosition(Point2D startingFrom) {
		double startX, startY;
		Dimension viewerSize = this.getVisualizationViewer().getSize();
		
		if (startingFrom!=null) {
			startX = startingFrom.getX() / viewerSize.getWidth();
			startY = startingFrom.getY() / viewerSize.getHeight();
		} else {
			startX = 0.1;
			startY = 0.1;
		}
		
		final double step = 0.20;
		
		for (double relativeX = startX; relativeX<1.0; relativeX+=step) {
			Point2D pointInView = new Point2D.Double(viewerSize.getWidth() * relativeX, viewerSize.getHeight() * startY);
			
			DataTableNode existingNode = this.getVisualizationViewer().getPickSupport().getVertex(this.getGraphController().getGraphLayout(), pointInView.getX(), pointInView.getY());
			
			if (existingNode==null) {
				return this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(pointInView);
			}
		}
		
		return null;
		
	}
	
	/**
	 * Gets the data source integration.
	 * @return the data source integration
	 */
	private ProcessingDataSourceIntegration getDataSourceIntegration() {
		if (dataSourceIntegration==null) {
			dataSourceIntegration = (ProcessingDataSourceIntegration) this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource().getDataSourceIntegration();
		}
		return dataSourceIntegration;
	}

	/**
	 * Shows the data source selection dialog.
	 */
	private void showDataSourceSelectionPanel() {
		DataSourceSelectionPanel dataSelectionPanel = new DataSourceSelectionPanel(this.getDataController(), this);
		this.getJSplitPaneMainView().setRightComponent(dataSelectionPanel);
		this.getJSplitPaneMainView().setDividerLocation(0.8);
	}
	
	/**
	 * Removes the currently selected element from the transformation graph.
	 */
	private void removeSelectedElement() {
		if (this.selectedGraphElement!=null) {
			if (this.selectedGraphElement instanceof DataTableNodeDataSource) {
				this.removeDataSourceNode((DataTableNodeDataSource) this.selectedGraphElement);
			} else if (this.selectedGraphElement instanceof DataTableNodeTransformationResult) {
				this.removeDataTransformation((DataTableNodeTransformationResult) this.selectedGraphElement);
			} else if (this.selectedGraphElement instanceof DataTransformationEdge) {
				this.removeDataTransformation((DataTransformationEdge) this.selectedGraphElement);
			}
		} 
	}
	
	/**
	 * Tries to remove the provided data source table node.
	 * @param dataSourceTableNode the data source table node
	 * @return true, if the node could be removed
	 */
	private boolean removeDataSourceNode(DataTableNodeDataSource dataSourceTableNode) {
		boolean couldBeRemoved = this.getGraphController().removeDataTableNode(dataSourceTableNode);
		if (couldBeRemoved==true) {
			this.selectedGraphElement = null;
			this.getJSplitPaneMainView().setDividerLocation(1.0);
		} else {
			JOptionPane.showMessageDialog(this, "The selected node is an input for at least one subsequent transformation!\nIf you want to remove it, remove or reconfigure the transformation first.", "Currently not possible", JOptionPane.WARNING_MESSAGE);
		}
		return couldBeRemoved;
	}
	
	/**
	 * Tries to remove the data transformation represented by the currently selected edge.
	 * @param transformationEdge the transformation edge
	 */
	private void removeDataTransformation(DataTransformationEdge transformationEdge) {
		AbstractDataTransformation dataTransformation = transformationEdge.getDataTransformation();
		this.removeDataTransformation(dataTransformation.getOutputNode());
	}
	
	/**
	 * Tries to remove the data transformation represented by the currently result node.
	 * @param resultNode the result node
	 */
	private void removeDataTransformation(DataTableNodeTransformationResult resultNode) {
		int outEdges = this.getGraphController().getTransformationGraph().getOutEdges(resultNode).size();
		if (outEdges>0) {
			JOptionPane.showMessageDialog(this, "The result of the selected transformation is an input for at least one subsequent transformation!\nIf you want to remove it, remove or reconfigure the subsequent transformation(s) first.", "Currently not possible", JOptionPane.WARNING_MESSAGE);
		} else {
			this.getGraphController().removeDataTransformation(resultNode);
			this.selectedGraphElement = null;
			this.getJSplitPaneMainView().setDividerLocation(1.0);
		}
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.graph.event.GraphEventListener#handleGraphEvent(edu.uci.ics.jung.graph.event.GraphEvent)
	 */
	@Override
	public void handleGraphEvent(GraphEvent<DataTableNode, DataTransformationEdge> arg0) {
		this.getVisualizationViewer().repaint();
		this.updateButtonsState();
	}
	
	/**
	 * Update buttons state.
	 */
	private void updateButtonsState() {
		this.getjButtonAddDataTransformation().setEnabled(this.getGraphController().getTransformationGraph().getVertexCount()>0);
	}
	
	/**
	 * Adds a new graph node for the provided data source at the specified position.
	 * @param dataSourceDTNO the data source DTNO
	 */
	public void addDataSourceGraphNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		this.addDataSourceGraphNode(dataSourceDTNO, this.getNextFreeNodePosition());
	}
	
	/**
	 * Adds a new graph node for the provided data source. The position will be determined automatically.
	 * @param dataSourceDTNO the data source DTNO
	 * @param position the position
	 */
	public void addDataSourceGraphNode(AbstractDataSourceDTNO<?> dataSourceDTNO, Point2D position) {
		this.getGraphController().addDataTableNode(dataSourceDTNO, position);
		this.setGraphChanged(true);
	}
	
	/**
	 * Removes the graph node for the provided data source from the graph.
	 * @param dataSourceDTNO the data source DTNO
	 * @return true, if the node could be removed
	 */
	public boolean removeDataSourceGraphNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		boolean couldBeRemoved = this.getGraphController().removeDataTableNode(dataSourceDTNO)==true;
		if (couldBeRemoved) {
			this.setGraphChanged(true);
		}
		return couldBeRemoved;
	}

	/**
	 * Adds a new data transformation to the graph.
	 * @param newTransformation the new transformation
	 */
	public void addNewDataTransformation(AbstractDataTransformation newTransformation) {
		
		DataTableNodeTransformationResult outputNode = new DataTableNodeTransformationResult();
		newTransformation.setOutputNode(outputNode);
		outputNode.setDataTransformation(newTransformation);
		// --- Determine a node position in relation to the input nodes -------
		Point2D intendedNodePosition = this.determineOutputNodePosition(newTransformation);
		// --- If already occupied, find a free spot --------------------------
		Point2D finalNodePosition = this.getNextFreeNodePosition(intendedNodePosition);
		outputNode.setPosition(finalNodePosition);
		
		this.getGraphController().addDataTableNode(outputNode);
		
		for (DataTableNode inputNode : newTransformation.getInputNodes()) {
			DataTransformationEdge transformationEdge = new DataTransformationEdge();
			transformationEdge.setDataTransformation(newTransformation);
			
			this.getGraphController().getTransformationGraph().addEdge(transformationEdge, inputNode, outputNode);
		}
		this.setGraphChanged(true);
	}
	
	/**
	 * Determines the position for the output node of a data transformation in relation to the input nodes.
	 * @param dataTransformation the data transformation
	 * @return the position
	 */
	private Point2D determineOutputNodePosition(AbstractDataTransformation dataTransformation) {
		
		// --- Distance from the calculated reference coordinate ----
		double defaultDistanceX = 0;
		double defaultDistanceY = 150;
		
		double meanX = 0;
		double maxY = 0;

		// --- Calculate reference coordinate: Mean of X values, max of Y values
		for (DataTableNode inputNode : dataTransformation.getInputNodes()) {
			meanX += this.getGraphController().getGraphLayout().getX(inputNode);
			
			double nodeY = this.getGraphController().getGraphLayout().getY(inputNode);
			maxY = (maxY<nodeY ? nodeY : maxY);
		}
		meanX /= dataTransformation.getInputNodes().size();
		
		return new Point2D.Double(meanX+defaultDistanceX, maxY+defaultDistanceY);
	}

	/**
	 * Finds the corresponding table node for the provided table.
	 * @param dataTable the data table
	 * @return the data table node, null if not found
	 */
	public DataTableNode findCorrespondingTableNode(Table dataTable) {
		for (DataTableNode tableNode : this.getGraphController().getTransformationGraph().getVertices()) {
			if (tableNode.getDataTable() == dataTable) {
				return tableNode;
			}
		}
		return null;
	}
	
	/**
	 * Find corresponding table node for the provided data source UUID..
	 * @param dataSourceUUID the data source UUID
	 * @return the data table node
	 */
	public DataTableNode findCorrespondingTableNode(String dataSourceUUID) {
		for (DataTableNode tableNode : this.getGraphController().getTransformationGraph().getVertices()) {
			if (tableNode.getDataSourceUUID().equals(dataSourceUUID)) {
				return tableNode;
			}
		}
		return null;
	}

	/**
	 * Sets the selected graph element.
	 * @param graphElement the new selected graph element
	 */
	public void setSelectedGraphElement(Object graphElement) {
		
		this.getVisualizationViewer().getPickedVertexState().clear();
		
		if (graphElement==null) {
			// --- Clear selection, hide details panel --------------
			this.selectedGraphElement = null;
			this.hideDetailsPanel();
		} else if (graphElement instanceof DataTableNode node) {
			
			// --- Mark as picked in the JUNG visualization
			this.getVisualizationViewer().getPickedVertexState().pick(node, true);
			
			// --- Show the details for the selected node -----------
			this.selectedGraphElement = node;
			this.showNodeDetails(node);
			
		} else if (graphElement instanceof DataTransformationEdge edge) {
			// --- Show the details for the selected edge -----------
			this.selectedGraphElement = edge;
			this.showEdgeDetails(edge);
			
		} else {
			// --- Unexpected type ----------------------------------
			throw new IllegalArgumentException("Unsupported graph element type: " + graphElement.getClass().getName());
		}
	}
	
	/**
	 * Gets the selected graph element.
	 * @return the selected graph element
	 */
	public Object getSelectedGraphElement() {
		return this.selectedGraphElement;
	}
	
	/**
	 * Hides the details view.
	 */
	private void hideDetailsPanel() {
		this.getJSplitPaneMainView().setDividerLocation(1.0);
		this.getJButtonRemove().setEnabled(false);
	}
	
	/**
	 * Shows the details view for the provided node.
	 * @param node the node
	 */
	private void showNodeDetails(DataTableNode node) {
		if (this.getTableFroNode(node)!=null) {
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setRightComponent(TransformationGraphEditorPanel.this.getTableDetailsViewPanel(node));
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(0.67);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No table to visualize!");
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(1.0);
		}
		
		TransformationGraphEditorPanel.this.getJButtonRemove().setEnabled(true);
	}
	
	private Table getTableFroNode(DataTableNode tableNode) {
		Table table = tableNode.getDataTable();
		if (table==null && tableNode instanceof DataTableNodeTransformationResult transformationNode) {
			this.getTransformationProcessor().performTransformation(transformationNode.getDataTransformation());
			table = tableNode.getDataTable();
		}
		return table;
		
	}
	
	/**
	 * Shows the details view for the provided edge.
	 * @param edge the edge
	 */
	private void showEdgeDetails(DataTransformationEdge edge) {
		if (edge!=null) {
			AbstractDataTransformation transformationToShow = edge.getDataTransformation();
			if (transformationToShow!=null) {
				TransformationGraphEditorPanel.this.getTransformationPanel().setDataTransformation(transformationToShow);
				TransformationGraphEditorPanel.this.getJButtonRemove().setEnabled(true);
			}
		}
		TransformationGraphEditorPanel.this.getJSplitPaneMainView().setRightComponent(TransformationGraphEditorPanel.this.getTransformationPanel());
		TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(0.6);
	}
	
	/**
	 * Gets a {@link PluggableGraphMouse} that only uses the zoom plug-in ----- 
	 * @return the zoom only graph mouse
	 */
	private PluggableGraphMouse getZoomOnlyGraphMouse() {
		PluggableGraphMouse graphMouse = new PluggableGraphMouse();
		graphMouse.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 0.9f, 1.1f));
		return graphMouse;
	}
	
	/**
	 * Creates a HTML version of the label text for centered display with line breaks.
	 * @param labelString the label string
	 * @return the label renderer HTML string
	 */
	private String getLabelRendererHTMLString(String labelString, boolean isPicked) {
		String colorString = isPicked==true ? HTML_COLOR_LABEL_PICKED : HTML_COLOR_LABEL_NORMAL;
		return "<html><div style='width:130px; text-align:center; font-weight: bold; color: " + colorString + "'>"
		        + escapeHtml(labelString)
		        + "</div></html>";
	}
	
	/**
	 * Escapes special characters for HTML.
	 * @param text the original text
	 * @return the modified text
	 */
	private static String escapeHtml(String text) {
	    if (text == null) {
	        return "";
	    }

	    return text
	        .replace("&", "&amp;")
	        .replace("<", "&lt;")
	        .replace(">", "&gt;")
	        .replace("\"", "&quot;")
	        .replace("'", "&#39;");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAddDataSource()) {
			this.showDataSourceSelectionPanel();
		} else if (ae.getSource()==this.getjButtonAddDataTransformation()) {
			this.transformationPanel = null;
			this.showEdgeDetails(null);
		} else if (ae.getSource()==this.getJButtonRemove()) {
			this.removeSelectedElement();
		} else if (ae.getSource()==this.getJButtonVerify()) {
			JOptionPane.showMessageDialog(this, "Not implemented yet");
		} else if (ae.getSource()==this.getJButtonApplyChanges()) {
			PersistentTransformationGraph persistentGraph = PersistentTransformationGraph.fromJungGraph(this.getGraphController().getTransformationGraph());
			this.getDataSourceIntegration().setTransformationGraph(persistentGraph);
			this.setGraphChanged(false);
		} else if (ae.getSource()==this.getJButtonRevertChanges()) {
			//TODO implement graph reset
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TRANSFORMATION_CREATED)) {
			// --- Add the new transformation to the graph ----------
			AbstractDataTransformation transformation = (AbstractDataTransformation) pce.getNewValue();
			this.addNewDataTransformation(transformation);
			this.setSelectedGraphElement(transformation.getOutputNode());
			
		} else if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TRANSFORMATION_MODIFIED)) {
			// --- Select the output node to show the result --------
			AbstractDataTransformation transformation = (AbstractDataTransformation) pce.getNewValue();
			this.getGraphController().transformationModified(transformation);
			this.setSelectedGraphElement(transformation.getOutputNode());
			this.setGraphChanged(true);
			
		} else if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TRANSFORMATION_CANCELED)) {
			this.hideDetailsPanel();
		}
	}
	
	/**
	 * Gets the transformation processor.
	 * @return the transformation processor
	 */
	public DataTransformationProcessor getTransformationProcessor() {
		if (transformationVisitor==null) {
			transformationVisitor = new DataTransformationProcessor(this.getGraphController());
		}
		return transformationVisitor;
	}

	/**
	 * Checks if the graph has pending changes.
	 * @return true, if is graph changed
	 */
	public boolean isGraphChanged() {
		return graphChanged;
	}
	/**
	 * Sets the graph changed status of the graph, adjusts button states accordingly.
	 * @param graphChanged the new graph changed
	 */
	protected void setGraphChanged(boolean graphChanged) {
		if (graphChanged!=this.isGraphChanged()) {
			this.graphChanged = graphChanged;
			this.getJButtonApplyChanges().setEnabled(graphChanged);
//			this.getJButtonRevertChanges().setEnabled(graphChanged);
		}
	}
	
	/**
	 * Sets the transformation graph.
	 *
	 * @param transformationGraph the transformation graph
	 */
	public void setTransformationGraph(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		this.getGraphController().setTransformationGraph(transformationGraph);
		this.getVisualizationViewer().setGraphLayout(this.getGraphController().getGraphLayout());
		this.getVisualizationViewer().repaint();
	}
	
}
