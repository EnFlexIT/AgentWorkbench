package org.awb.env.networkModel.controller.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
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

	private boolean debugRefreshmentActions = false;
	
	private TransformerForGraphNodeGeoPosition coordinateSystemNodeGeoPositionTransformer;
	
	private int resetTimerDelay = 50;
	private Timer resetTimerForGraphNodeRefreshing;

	private ChangeListener localChangeListener;
	private boolean pauseRefreshGraphNode;
	
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
		this.addLocalChangeListener();
	}


	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#refreshGraphNodePosition()
	 */
	@Override
	public void refreshGraphNodePosition() {
		this.pauseRefreshGraphNode = false;
		if (this.debugRefreshmentActions) {
			System.out.println("Refreshing GraphNode positions ...");
		}
		this.setInitializer(this.getCoordinateSystemPositionTransformer());
		this.getBasicGraphGuiVisViewer().paintComponentRenderGraph();
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#setRefreshGraphNodePositionPaused(boolean)
	 */
	@Override
	public void setRefreshGraphNodePositionPaused(boolean isPaused) {
		this.pauseRefreshGraphNode = isPaused;
		if (this.pauseRefreshGraphNode==false) {
			this.resetTimerForGraphNodeRefreshing();
		}
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#isRefreshGraphNodePositionPaused()
	 */
	@Override
	public boolean isRefreshGraphNodePositionPaused() {
		return pauseRefreshGraphNode;
	}
	
	
	/**
	 * Returns the reset timer for graph node refreshing.
	 * @return the reset timer for graph node refreshing
	 */
	private Timer getResetTimerForGraphNodeRefreshing() {
		if (resetTimerForGraphNodeRefreshing==null) {
			resetTimerForGraphNodeRefreshing = new Timer(this.resetTimerDelay, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (pauseRefreshGraphNode==false) {
						BasicGraphGuiStaticGeoLayout.this.refreshGraphNodePosition();
					}
				}
			});
			resetTimerForGraphNodeRefreshing.setRepeats(false);
		}
		return resetTimerForGraphNodeRefreshing;
	}
	/**
	 * Reset timer for graph node refreshing.
	 */
	private void resetTimerForGraphNodeRefreshing() {
		if (this.getResetTimerForGraphNodeRefreshing().isRunning()==false) {
			if (this.debugRefreshmentActions) System.out.println("Starting refreshment timer ...");
			this.getResetTimerForGraphNodeRefreshing().start();
		} else {
			if (this.debugRefreshmentActions) System.out.println("Restarting refreshment timer ...");
			this.getResetTimerForGraphNodeRefreshing().start();
		}
	}
	
	
	
	/**
	 * Returns the local ChangeListener for changes in the visualization viewer.
	 * @return the local change listener
	 */
	private ChangeListener getLocalChangeListener() {
		if (localChangeListener==null) {
			localChangeListener = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (pauseRefreshGraphNode==false) {
						if (BasicGraphGuiStaticGeoLayout.this.debugRefreshmentActions) {
							System.out.println("ChangeListener informed about changes in vis.-viewer ...");
						}
						BasicGraphGuiStaticGeoLayout.this.resetTimerForGraphNodeRefreshing();
					}
				}
			};
		}
		return localChangeListener;
	}
	/**
	 * Adds the local change listener.
	 */
	private void addLocalChangeListener() {
		this.getBasicGraphGuiVisViewer().addChangeListener(this.getLocalChangeListener());
	}
	/**
	 * Removes the local change listener.
	 */
	private void removeLocalChangeListener() {
		this.getBasicGraphGuiVisViewer().removeChangeListener(this.getLocalChangeListener());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiStaticLayout#dispose()
	 */
	@Override
	public void dispose() {
		this.removeLocalChangeListener();
	}
	
}
