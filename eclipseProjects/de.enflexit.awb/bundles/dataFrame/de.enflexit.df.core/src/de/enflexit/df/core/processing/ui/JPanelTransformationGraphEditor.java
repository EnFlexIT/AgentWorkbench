package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

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
import de.enflexit.df.core.processing.transformation.ui.JDialogDataTransformationConfiguration;
import de.enflexit.df.core.processing.transformation.ui.JPanelDataTransformationConfiguration;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
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
public class JPanelTransformationGraphEditor extends JPanel implements ActionListener, GraphEventListener<DataTableNode, DataTransformationEdge> {
	
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
	
	private DataTransformationTableViewPanel tablePanel;
	private JPanelDataTransformationConfiguration transformationPanel;
	
	private GraphZoomScrollPane graphZoomScrollPane;
	private VisualizationViewer<DataTableNode, DataTransformationEdge> visualizationViewer;
	private JSplitPane jSplitPaneMainView;
	
	/**
	 * Instantiates a new j panel transformation graph editor.
	 * @param graphController the graph controller
	 */
	public JPanelTransformationGraphEditor(DataController dataController) {
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
		
		// --- Hide the right component intitially --------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(1.0);
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
	private GraphZoomScrollPane getJPanelGraph() {
		if (graphZoomScrollPane == null) {
			graphZoomScrollPane = new GraphZoomScrollPane(this.getVisualizationViewer());
		}
		return graphZoomScrollPane;
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
	 * Gets the visualization viewer.
	 * @return the visualization viewer
	 */
	private VisualizationViewer<DataTableNode, DataTransformationEdge> getVisualizationViewer() {
		if (visualizationViewer==null) {
			
			visualizationViewer = new VisualizationViewer<DataTableNode, DataTransformationEdge>(this.getGraphController().getGraphLayout());
			
			visualizationViewer.setBackground(AwbThemeColor.Canvas_Background.getColor());
			
			// --- Set a rounded rectangle as node shape. Dimensions are specified in the class constants above.
			double xPos = -(NODE_RECTANGLE_WIDTH/2);	// JUNG coordinates refer to the center
			double yPos = -(NODE_RECTANGLE_HEIGHT/2);
			visualizationViewer.getRenderContext().setVertexShapeTransformer(node -> new RoundRectangle2D.Double(xPos, yPos, NODE_RECTANGLE_WIDTH, NODE_RECTANGLE_HEIGHT, NODE_RECTANGLE_ARC, NODE_RECTANGLE_ARC));
			visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape<DataTableNode, DataTransformationEdge>(this.getGraphController().getTransformationGraph()).new Line());
			visualizationViewer.getRenderContext().setVertexLabelTransformer(node -> node.getLabelText());
			visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
			
			// --- Configure mouse interactions -----------
			visualizationViewer.setPickSupport(new ShapePickSupport<DataTableNode, DataTransformationEdge>(visualizationViewer));
			visualizationViewer.setGraphMouse(new DefaultModalGraphMouse<DataTableNode, DataTransformationEdge>());
			visualizationViewer.addMouseListener(new TransformationGraphMouseAdapter());
			
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
			this.showDataTransformationConfigurationDialog();
		} else if (ae.getSource()==this.getJButtonRemove()) {
			JOptionPane.showMessageDialog(this, "Not implemented yet");
		} else if (ae.getSource()==this.getJButtonVerify()) {
			JOptionPane.showMessageDialog(this, "Not implemented yet");
		}
	}
	
	/**
	 * Shows the data source selection dialog.
	 */
	private void showDataSourceSelectionDialog() {
		Window owner = OwnerDetection.getOwnerWindowForComponent(this.getJButtonAddDataSource());
		JDialogDataSourceSelection selectionDialog = new JDialogDataSourceSelection(owner, this, this.dataController);
		
		Point buttonLocation = this.getJButtonAddDataSource().getLocationOnScreen();
		Double dialogPositionX = buttonLocation.getX() + this.getJButtonAddDataSource().getWidth();
		Double dialogPositionY = buttonLocation.getY();
		
		selectionDialog.setLocation(dialogPositionX.intValue(), dialogPositionY.intValue());
		selectionDialog.setVisible(true);
		selectionDialog.requestFocus();
	}
	
	/**
	 * Shows the data transformation configuration dialog.
	 */
	private void showDataTransformationConfigurationDialog() {
		Window owner = OwnerDetection.getOwnerWindowForComponent(this.getJButtonAddDataSource());
		JDialogDataTransformationConfiguration transformationDialog = new JDialogDataTransformationConfiguration(owner, this);
		
		Point buttonLocation = this.getjButtonAddDataTransformation().getLocationOnScreen();
		Double dialogPositionX = buttonLocation.getX() + this.getjButtonAddDataTransformation().getWidth();
		Double dialogPositionY = buttonLocation.getY();
		
		transformationDialog.setLocation(dialogPositionX.intValue(), dialogPositionY.intValue());
		transformationDialog.setVisible(true);
		transformationDialog.requestFocus();
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
	 * The Class TransformationGraphMouseAdapter.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class TransformationGraphMouseAdapter extends MouseAdapter {
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			// --- Just a shorthand for shorter calls.
			VisualizationViewer<DataTableNode, DataTransformationEdge> visViewer = JPanelTransformationGraphEditor.this.getVisualizationViewer();
			
			DataTableNode nodeClicked = visViewer.getPickSupport().getVertex(visViewer.getGraphLayout(), e.getX(), e.getY());
			
			// --- Clicked on a node, show the corresponding table ------------
			if (nodeClicked!=null) {
				Table tableToShow = nodeClicked.getDataTable();
				if (tableToShow!=null) {
					JPanelTransformationGraphEditor.this.getTablePanel().setDataTable(nodeClicked);
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setRightComponent(JPanelTransformationGraphEditor.this.getTablePanel());
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(0.67);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] No table to visualize!");
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(1.0);
				}
				return;
			}
			
			// --- Clicked on an edge, show the corresponding transformation --
			DataTransformationEdge edgeClicked = visViewer.getPickSupport().getEdge(visViewer.getGraphLayout(), e.getX(), e.getY());
			if (edgeClicked!=null) {
				AbstractDataTransformation transformationToShow = edgeClicked.getDataTransformation();
				if (transformationToShow!=null) {
					JPanelTransformationGraphEditor.this.getTransformationPanel().setDataTransformation(transformationToShow);
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setRightComponent(JPanelTransformationGraphEditor.this.getTransformationPanel());
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(0.67);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] No transformation defined!");
					JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(1.0);
				}
				return;
			}
			
			// --- Clicked on an empty area, hide the panel 
			JPanelTransformationGraphEditor.this.getJSplitPaneMainView().setDividerLocation(1.0);
		}
	}
	
	private JSplitPane getJSplitPaneMainView() {
		if (jSplitPaneMainView == null) {
			jSplitPaneMainView = new JSplitPane();
			jSplitPaneMainView.setLeftComponent(this.getJPanelGraph());
			jSplitPaneMainView.setRightComponent(new JPanel());
		}
		return jSplitPaneMainView;
	}
	
	
	private DataTransformationTableViewPanel getTablePanel() {
		if (tablePanel==null) {
			tablePanel = new DataTransformationTableViewPanel();
		}
		return tablePanel;
	}
	
	private JPanelDataTransformationConfiguration getTransformationPanel() {
		if (transformationPanel==null) {
			transformationPanel = new JPanelDataTransformationConfiguration(this);
		}
		return transformationPanel;
	}
	
	/**
	 * Finds the corresponding table node for the provided table..
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
}
