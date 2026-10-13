package de.enflexit.df.core.processing.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * {@link MouseAdapter} implementation for the transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphMouseAdapter extends MouseAdapter {

	private TransformationGraphEditorPanel graphEditorPanel;
	
	private DataTableNode draggedNode;
	private Point2D dragOffset;

	private Point pressPoint;
	private boolean dragged;
	
	private Point panPoint;
	private boolean panCandidate;

	public TransformationGraphMouseAdapter(TransformationGraphEditorPanel graphEditorPanel) {
		this.graphEditorPanel = graphEditorPanel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		
		if (this.dragged==true) {
			return;
		}
		
		// --- Clicked on a node, show the corresponding table ------------
		DataTableNode nodeClicked = this.getVisViewer().getPickSupport().getVertex(this.getVisViewer().getGraphLayout(), me.getX(), me.getY());
		if (nodeClicked!=null) {
			this.graphEditorPanel.setSelectedGraphElement(nodeClicked);
		} else {
			DataTransformationEdge edgeClicked = this.getVisViewer().getPickSupport().getEdge(this.getVisViewer().getGraphLayout(), me.getX(), me.getY());
			if (edgeClicked!=null) {
				this.graphEditorPanel.setSelectedGraphElement(edgeClicked);
			} else {
				this.graphEditorPanel.setSelectedGraphElement(null);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		
		if (SwingUtilities.isLeftMouseButton(me)==false) {
			return;
		}
		this.draggedNode = this.getVisViewer().getPickSupport().getVertex(this.getVisViewer().getGraphLayout(), me.getX(), me.getY());
		this.pressPoint = me.getPoint();
		this.dragged = false;
		
		this.panCandidate = false;
		this.panPoint = null;
		
		if (this.draggedNode!=null) {
			// --- Clicked on a node -> move that node --------------
			Point2D mouseLayout = this.getVisViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(pressPoint);
			Layout<DataTableNode, DataTransformationEdge> layout = this.graphEditorPanel.getVisualizationViewer().getGraphLayout();
			Point2D nodePosition = layout.apply(this.draggedNode);
			this.dragOffset = new Point2D.Double(mouseLayout.getX()-nodePosition.getX(), mouseLayout.getY()-nodePosition.getY());
		} else {
			// --- CLicked outside a node -> move the graph view ----
			this.panCandidate = true;
			this.panPoint = me.getPoint();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {

		if (this.pressPoint.distance(me.getPoint()) >= 3) {
			this.dragged = true;
		}

		Layout<DataTableNode, DataTransformationEdge> layout = this.getVisViewer().getGraphLayout();
		
		if (this.draggedNode!=null) {

			// --- Move a node ----------------------------
			Point2D mouseLayout = this.getVisViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(me.getPoint());
			Point2D newPosition = new Point2D.Double(mouseLayout.getX() - this.dragOffset.getX(), mouseLayout.getY() - this.dragOffset.getY());
			
			layout.setLocation(this.draggedNode, newPosition);
			this.draggedNode.setPosition(new Point2D.Double(newPosition.getX(), newPosition.getY()));
		
		} else if (panCandidate && panPoint != null) {
			// --- Move the graph view --------------------
			int dx = me.getX() - this.panPoint.x;
			int dy = me.getY() - this.panPoint.y;

			this.getVisViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).translate(dx, dy);

			this.panPoint = me.getPoint();
		}

		this.getVisViewer().repaint();
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		this.draggedNode = null;
		this.dragOffset = null;
		this.pressPoint = null;

		this.panPoint = null;
		this.panCandidate = false;
	}
	
	/**
	 * Gets the visualization viewer.
	 * @return the visualization viewer
	 */
	private VisualizationViewer<DataTableNode, DataTransformationEdge> getVisViewer(){
		return this.graphEditorPanel.getVisualizationViewer();
	};
	
}
