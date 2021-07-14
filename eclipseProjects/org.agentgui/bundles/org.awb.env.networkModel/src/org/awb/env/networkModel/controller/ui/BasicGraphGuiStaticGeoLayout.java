package org.awb.env.networkModel.controller.ui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import edu.uci.ics.jung.graph.Graph;

/**
 * The Class BasicGraphGuiStaticGeoLayout provides the geographical layout 
 * for the visualization of AWB graph and network models.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BasicGraphGuiStaticGeoLayout extends BasicGraphGuiStaticLayout {

	private TransformerForGraphNodeGeoPosition coordinateSystemNodeGeoPositionTransformer;
	
	/**
	 * Instantiates a static layout.
	 *
	 * @param graphController the graph controller
	 * @param graph the graph
	 */
	public BasicGraphGuiStaticGeoLayout(GraphEnvironmentController graphController, Graph<GraphNode, GraphEdge> graph) {
		super(graphController, graph);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#getCoordinateSystemPositionTransformer()
	 */
	@Override
	public TransformerForGraphNodeGeoPosition getCoordinateSystemPositionTransformer() {
		if (coordinateSystemNodeGeoPositionTransformer==null) {
			coordinateSystemNodeGeoPositionTransformer = new TransformerForGraphNodeGeoPosition(this.graphController);
		}
		return coordinateSystemNodeGeoPositionTransformer;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#setBasicGraphGuiVisViewer(org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer)
	 */
	@Override
	public void setBasicGraphGuiVisViewer(BasicGraphGuiVisViewer<?, ?> basicGraphGuiVisViewer) {
		super.setBasicGraphGuiVisViewer(basicGraphGuiVisViewer);
		basicGraphGuiVisViewer.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
//				System.out.println("[" + BasicGraphGuiStaticGeoLayout.this.getClass().getSimpleName() + "] VisViewer state changes " + ce.toString());
			}
		});
	}
}
