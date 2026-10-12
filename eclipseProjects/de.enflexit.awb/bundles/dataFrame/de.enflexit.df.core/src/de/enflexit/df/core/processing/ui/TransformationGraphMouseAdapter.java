package de.enflexit.df.core.processing.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * {@link MouseAdapter} implementation for the transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphMouseAdapter extends MouseAdapter {

	private TransformationGraphEditorPanel graphEditorPanel;

	public TransformationGraphMouseAdapter(TransformationGraphEditorPanel graphEditorPanel) {
		this.graphEditorPanel = graphEditorPanel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me) {
		// --- Just a shorthand for shorter calls.
		VisualizationViewer<DataTableNode, DataTransformationEdge> visViewer = this.graphEditorPanel.getVisualizationViewer();
		
		// --- Clicked on a node, show the corresponding table ------------
		DataTableNode nodeClicked = visViewer.getPickSupport().getVertex(visViewer.getGraphLayout(), me.getX(), me.getY());
		if (nodeClicked!=null) {
			this.graphEditorPanel.setSelectedGraphElement(nodeClicked);
		} else {
			DataTransformationEdge edgeClicked = visViewer.getPickSupport().getEdge(visViewer.getGraphLayout(), me.getX(), me.getY());
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
		// TODO Auto-generated method stub
		super.mousePressed(me);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO Auto-generated method stub
		super.mouseReleased(me);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub
		super.mouseDragged(me);
	}
	
}
