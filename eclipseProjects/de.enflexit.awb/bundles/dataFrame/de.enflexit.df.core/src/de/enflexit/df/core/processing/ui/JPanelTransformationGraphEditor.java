package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * The Class JPanelTransformationGraphEditor.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelTransformationGraphEditor extends JPanel implements ActionListener, GraphEventListener<DataTableNode, DataTransformationEdge> {
	
	private static final long serialVersionUID = 3869719655631231629L;
	
	private static final double NODE_RECTANGLE_WIDTH = 140;
	private static final double NODE_RECTANGLE_HEIGHT = 50;
	private static final double NODE_RECTANGLE_ARC = 15;
	
	private JToolBar jToolBarMain;
	private JButton jButtonAdd;
	private JButton jButtonRemove;
	private JButton jButtonVerify;
	
	private TransformationGraphController graphController;
	private AbstractLayout<DataTableNode, DataTransformationEdge> graphLayout;
	
	private GraphZoomScrollPane graphZoomScrollPane;
	private VisualizationViewer<DataTableNode, DataTransformationEdge> visualizationViewer;
	
	/**
	 * Instantiates a new j panel transformation graph editor.
	 * @param graphController the graph controller
	 */
	public JPanelTransformationGraphEditor(TransformationGraphController graphController) {
		this.graphController = graphController;
		this.initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJToolBarMain(), BorderLayout.WEST);
		add(getJPanelGraph(), BorderLayout.CENTER);
	}

	private JToolBar getJToolBarMain() {
		if (jToolBarMain == null) {
			jToolBarMain = new JToolBar();
			jToolBarMain.setOrientation(SwingConstants.VERTICAL);
			jToolBarMain.add(getJButtonAdd());
			jToolBarMain.add(getJButtonRemove());
			jToolBarMain.add(getJButtonVerify());
		}
		return jToolBarMain;
	}
	private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
			jButtonAdd = new JButton("Add");
		}
		return jButtonAdd;
	}
	private JButton getJButtonRemove() {
		if (jButtonRemove == null) {
			jButtonRemove = new JButton("Remove");
		}
		return jButtonRemove;
	}
	private JButton getJButtonVerify() {
		if (jButtonVerify == null) {
			jButtonVerify = new JButton("Verify");
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
	private TransformationGraphController getGraphController() {
		return graphController;
	}
	/**
	 * Gets the visualization viewer.
	 * @return the visualization viewer
	 */
	private VisualizationViewer<DataTableNode, DataTransformationEdge> getVisualizationViewer() {
		if (visualizationViewer==null) {
			visualizationViewer = new VisualizationViewer<DataTableNode, DataTransformationEdge>(this.getGraphLayout());
			
			visualizationViewer.setBackground(AwbThemeColor.Canvas_Background.getColor());
			
			// --- Set a rounded rectangle as node shape. Dimensions are specified in the class constants above.
			double xPos = -(NODE_RECTANGLE_WIDTH/2);	// JUNG coordinates refer to the center
			double yPos = -(NODE_RECTANGLE_HEIGHT/2);
			visualizationViewer.getRenderContext().setVertexShapeTransformer(node -> new RoundRectangle2D.Double(xPos, yPos, NODE_RECTANGLE_WIDTH, NODE_RECTANGLE_HEIGHT, NODE_RECTANGLE_ARC, NODE_RECTANGLE_ARC));
			visualizationViewer.getRenderContext().setVertexLabelTransformer(node -> node.getDataSourceDTNO() != null ? node.getDataSourceDTNO().getCaption() : "Not defined");
			visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
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

	    final double minimumDistance = 30.0;
	    
	    Dimension viewerSize = this.getVisualizationViewer().getSize();
	    
	    for (double relativeX = startX; relativeX<1.0; relativeX+=step) {
	    	Point2D pointInView = new Point2D.Double(viewerSize.getWidth() * relativeX, viewerSize.getHeight() * startY);
	    	
	    	DataTableNode existingNode = this.getVisualizationViewer().getPickSupport().getVertex(this.getGraphLayout(), pointInView.getX(), pointInView.getY());
	    	
	    	if (existingNode==null) {
	    		return this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(pointInView);
	    	}
	    }
	    
	    return null;
	    
	}
	
	
	/**
	 * Gets the graph layout.
	 * @return the graph layout
	 */
	private AbstractLayout<DataTableNode, DataTransformationEdge> getGraphLayout() {
		if (graphLayout==null) {
			ObservableGraph<DataTableNode, DataTransformationEdge> graph = (ObservableGraph<DataTableNode, DataTransformationEdge>) this.getGraphController().getTransformationGraph();
			graph.addGraphEventListener(this);
			graphLayout = new CircleLayout<DataTableNode, DataTransformationEdge>(this.getGraphController().getTransformationGraph());
		}
		return graphLayout;
	}
	
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.graph.event.GraphEventListener#handleGraphEvent(edu.uci.ics.jung.graph.event.GraphEvent)
	 */
	@Override
	public void handleGraphEvent(GraphEvent<DataTableNode, DataTransformationEdge> arg0) {
		this.getVisualizationViewer().repaint();
	}

}
