package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
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
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.DataTransformationVisitor;
import de.enflexit.df.core.processing.transformation.ui.DataTransformationConfigurationPanel;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeDataSource;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import tech.tablesaw.api.Table;

import java.awt.Point;
import java.awt.Window;
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
	
	private static final String ICON_TABLE_LIGHT_MODE = "table_black.png";
	private static final String ICON_TABLE_DARK_MODE = "table_grey.png";
	private static final String ICON_TRANSFORMATION_LIGHT_MODE = "hierarchy_black.png";
	private static final String ICON_TRANSFORMATION_DARK_MODE = "hierarchy_grey.png";
	private static final String ICON_VERIFY = "MBcheckGreen.png";
	private static final String ICON_DELETE = "Delete.png";
	
	private JToolBar jToolBarMain;
	private JButton jButtonAddDataSource;
	private JButton jButtonAddDataTransformation;
	private JButton jButtonRemove;
	private JButton jButtonVerify;
	
	private DataController dataController;
	private TransformationGraphController graphController;
	
	private DataTransformationConfigurationPanel transformationPanel;
	
	private GraphZoomScrollPane graphZoomScrollPane;
	private VisualizationViewer<DataTableNode, DataTransformationEdge> visualizationViewer;
	private JSplitPane jSplitPaneMainView;
	
	private Object selectedGraphElement;
	
	private DataTransformationVisitor transformationVisitor;
	
	/**
	 * Instantiates a new j panel transformation graph editor.
	 * @param graphController the graph controller
	 */
	public TransformationGraphEditorPanel(DataController dataController) {
		this.dataController = dataController;
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
			jButtonVerify.setToolTipText("Verify the transformation graph");
			jButtonVerify.addActionListener(this);
		}
		return jButtonVerify;
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
		
		// --- If necessary, perform the transformation to generate the output data.
		if (tableNode instanceof DataTableNodeTransformationResult transformationNode && tableNode.getDataTable()==null) {
			this.getTransformationVisitor().performTransformation(transformationNode.getDataTransformation());
		}
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
			graphController = new TransformationGraphController();
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
			visualizationViewer.getRenderContext().setVertexLabelTransformer(node -> getLabelRendererHTMLString(node.getLabelText()));
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
		final double startX = 0.10;
	    final double startY = 0.10;
	    final double step = 0.20;

	    Dimension viewerSize = this.getVisualizationViewer().getSize();
	    
	    for (double relativeX = startX; relativeX<1.0; relativeX+=step) {
	    	Point2D pointInView = new Point2D.Double(viewerSize.getWidth() * relativeX, viewerSize.getHeight() * startY);
	    	
	    	DataTableNode existingNode = this.getVisualizationViewer().getPickSupport().getVertex(this.getGraphController().getGraphLayout(), pointInView.getX(), pointInView.getY());
	    	
	    	if (existingNode==null) {
	    		return this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(pointInView);
	    	}
	    }
	    
	    return null;
	    
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAddDataSource()) {
			this.showDataSourceSelectionDialog();
		} else if (ae.getSource()==this.getjButtonAddDataTransformation()) {
			this.transformationPanel = null;
			this.showEdgeDetails(null);
		} else if (ae.getSource()==this.getJButtonRemove()) {
			this.removeSelectedElement();
		} else if (ae.getSource()==this.getJButtonVerify()) {
			JOptionPane.showMessageDialog(this, "Not implemented yet");
		}
	}
	
	/**
	 * Shows the data source selection dialog.
	 */
	private void showDataSourceSelectionDialog() {
		Window owner = OwnerDetection.getOwnerWindowForComponent(this.getJButtonAddDataSource());
		DataSourceSelectionDialog selectionDialog = new DataSourceSelectionDialog(owner, this, this.dataController);
		
		Point buttonLocation = this.getJButtonAddDataSource().getLocationOnScreen();
		Double dialogPositionX = buttonLocation.getX() + this.getJButtonAddDataSource().getWidth();
		Double dialogPositionY = buttonLocation.getY();
		
		selectionDialog.setLocation(dialogPositionX.intValue(), dialogPositionY.intValue());
		selectionDialog.setVisible(true);
		selectionDialog.requestFocus();
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
	 */
	private void removeDataSourceNode(DataTableNodeDataSource dataSourceTableNode) {
		int outEdges = this.getGraphController().getTransformationGraph().getOutEdges(dataSourceTableNode).size();
		if (outEdges>0) {
			JOptionPane.showMessageDialog(this, "The selected node is an input for at least one subsequent transformation!\nIf you want to remove it, remove or reconfigure the transformation first.", "Currently not possible", JOptionPane.WARNING_MESSAGE);
		} else {
			this.getGraphController().removeDataTableNode(dataSourceTableNode);
			this.selectedGraphElement = null;
			this.getJSplitPaneMainView().setDividerLocation(1.0);
		}
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
	}
	
	/**
	 * Removes the graph node for the provided data source from the graph.
	 * @param dataSourceDTNO the data source DTNO
	 */
	public void removeDataSourceGraphNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		//TODO check if the source is involved in an active transformation
		this.getGraphController().removeDataTableNode(dataSourceDTNO);
	}

	/**
	 * Adds a new data transformation to the graph.
	 * @param newTransformation the new transformation
	 */
	public void addNewDataTransformation(AbstractDataTransformation newTransformation) {
		DataTableNodeTransformationResult outputNode = new DataTableNodeTransformationResult();
		newTransformation.setOutputNode(outputNode);
		outputNode.setDataTransformation(newTransformation);
		outputNode.setPosition(this.determineOutputNodePosition(newTransformation));
		
		this.getGraphController().addDataTableNode(outputNode, outputNode.getPosition());
		
		for (DataTableNode inputNode : newTransformation.getInputNodes()) {
			DataTransformationEdge transformationEdge = new DataTransformationEdge();
			transformationEdge.setDataTransformation(newTransformation);
			
			this.getGraphController().getTransformationGraph().addEdge(transformationEdge, inputNode, outputNode);
		}
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
		
		if (graphElement==null) {
			// --- Clear selection, hide details panel --------------
			this.selectedGraphElement = null;
			this.hideDetailsPanel();
		} else if (graphElement instanceof DataTableNode node) {
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
		Table tableToShow = node.getDataTable();
		if (tableToShow!=null) {
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setRightComponent(TransformationGraphEditorPanel.this.getTableDetailsViewPanel(node));
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(0.67);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No table to visualize!");
			TransformationGraphEditorPanel.this.getJSplitPaneMainView().setDividerLocation(1.0);
		}
		
		TransformationGraphEditorPanel.this.getJButtonRemove().setEnabled(true);
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
		graphMouse.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
		return graphMouse;
	}
	
	/**
	 * Creates a HTML version of the label text for centered display with line breaks.
	 * @param labelString the label string
	 * @return the label renderer HTML string
	 */
	private String getLabelRendererHTMLString(String labelString) {
		return "<html><div style='width:130px; text-align:center;'>"
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
		} else if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TRANSFORMATION_CANCELED)) {
			this.hideDetailsPanel();
		}
	}
	
	public DataTransformationVisitor getTransformationVisitor() {
		if (transformationVisitor==null) {
			transformationVisitor = new DataTransformationVisitor(this.getDataController());
		}
		return transformationVisitor;
	}
	
}
