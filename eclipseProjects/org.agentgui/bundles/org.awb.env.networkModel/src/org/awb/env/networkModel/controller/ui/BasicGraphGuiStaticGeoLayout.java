package org.awb.env.networkModel.controller.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

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
	
	private Boolean lastIsDoMapPreRendering;
	private Double lastOverallScale;
	private Point2D lastJungCenterPosition;
	private double moveDistanceToRefreshPositionsAt = 2.5;
	
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
	
	/**
	 * Returns the center visualization coordinate as UTM coordinate.
	 * @return the center visualization coordinate
	 */
	private Point2D getJungCenterPosition() {
		
		Rectangle visRect = this.getBasicGraphGuiVisViewer().getVisibleRect();
		Point2D visCenter = new Point2D.Double(visRect.getCenterX(), visRect.getCenterY());

		AffineTransform trans = this.getBasicGraphGuiVisViewer().getOverallAffineTransform();
		Point2D jungCenter  = null;
		try {
			jungCenter = trans.inverseTransform(visCenter, null);
			
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return jungCenter;
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

		// ----------------------------------------------------------
		// --- Check if map pre-rendering has changed --------------- 
		boolean isChangedDoMapPreRendering = false;
		boolean currIsDoMapPreRendering = this.getBasicGraphGuiVisViewer().isDoMapPreRendering();
		if (this.lastIsDoMapPreRendering==null || currIsDoMapPreRendering!=this.lastIsDoMapPreRendering) {
			isChangedDoMapPreRendering = true;
		}
		// --- => Fast exit ? ---------------------------------------
		if (currIsDoMapPreRendering==false && isChangedDoMapPreRendering==false) return;
		
		
		// ----------------------------------------------------------
		// --- Check if scale has changed ---------------------------
		boolean isChangedScale = false;
		Double currScale = this.getBasicGraphGuiVisViewer().getOverallScale(); 
		if (this.lastOverallScale==null || currScale.equals(this.lastOverallScale)==false) {
			isChangedScale = true;
		}

		// ----------------------------------------------------------
		// --- Check if visual position has changed -----------------
		boolean isChangedPos = false;
		Point2D jungCenterPosition  = this.getJungCenterPosition();
		if (this.lastJungCenterPosition==null) {
			isChangedPos = true;
		} else {
			// --- Calculate distance moved -------------------------
			double distancMoved = Point2D.distance(jungCenterPosition.getX(), jungCenterPosition.getY(), this.lastJungCenterPosition.getX(), this.lastJungCenterPosition.getY());
			if (distancMoved>=this.moveDistanceToRefreshPositionsAt) {
				isChangedPos = true;
			}
		}
		
		// ----------------------------------------------------------
		// --- Check if a refreshment is necessary ------------------
		if (isChangedDoMapPreRendering==true || isChangedScale==true || isChangedPos==true) {
			// --- Remind current scale as last scale ---------------
			this.lastIsDoMapPreRendering = currIsDoMapPreRendering;
			this.lastOverallScale = currScale;
			this.lastJungCenterPosition = jungCenterPosition;
			
			// --- Reset positions of nodes -------------------------  
			this.setInitializer(this.getCoordinateSystemPositionTransformer());
			this.getBasicGraphGuiVisViewer().paintComponentRenderGraph();
		}
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
	 * Starts the timer for graph node position refreshing.
	 */
	private void resetTimerForGraphNodeRefreshing() {
		if (this.debugRefreshmentActions) System.out.println("Starting refreshment timer ...");
		this.getResetTimerForGraphNodeRefreshing().start();
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
						// --- Some debug output ----------------------------------------
						if (debugRefreshmentActions) {
							System.out.println("ChangeListener informed about changes in vis.-viewer ...");
						}
						// --- Restart GraphNode position calculation -------------------
						BasicGraphGuiStaticGeoLayout.this.resetTimerForGraphNodeRefreshing();
						// --- Update center coordinate in the current MapRenderer ------
						BasicGraphGuiStaticGeoLayout.this.getBasicGraphGuiVisViewer().updateMapRendererCenterGeoLocation();
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
