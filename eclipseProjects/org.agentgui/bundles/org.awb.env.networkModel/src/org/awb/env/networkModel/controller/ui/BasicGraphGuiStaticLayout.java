package org.awb.env.networkModel.controller.ui;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class BasicGraphGuiStaticLayout provides the default layout for the visualization
 * of AWB graph and network models.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BasicGraphGuiStaticLayout extends StaticLayout<GraphNode, GraphEdge> {

	protected GraphEnvironmentController graphController;
	
	private BasicGraphGuiVisViewer<?, ?> basicGraphGuiVisViewer;
	private TransformerForGraphNodePosition coordinateSystemNodePositionTransformer;
	
	private List<PositionDeterminerThread> positionDeterminerThreadList;
	
	
	/**
	 * Instantiates a static layout.
	 *
	 * @param graphController the graph controller
	 * @param graph the graph
	 */
	public BasicGraphGuiStaticLayout(GraphEnvironmentController graphController, Graph<GraphNode, GraphEdge> graph) {
		super(graph);
		this.graphController = graphController;
		this.doInitialConfigurations();
	}
	/**
	 * Does the initial configurations.
	 */
	private void doInitialConfigurations() {

		// --- Set the initializer / the coordinate transformer for the layout ----------
		this.setInitializer(this.getCoordinateSystemPositionTransformer());

		// --- Set the size of the layout -----------------------------------------------
		Rectangle2D graphDimension = GraphGlobals.getGraphSpreadDimension(this.getGraph());
		this.setSize(new Dimension((int) (graphDimension.getWidth() + 2 * BasicGraphGui.graphMargin), (int) (graphDimension.getHeight() + 2 * BasicGraphGui.graphMargin)));
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.algorithms.layout.AbstractLayout#setInitializer(org.apache.commons.collections15.Transformer)
	 */
	@Override
	public void setInitializer(Transformer<GraphNode, Point2D> initializer) {

		// --- Call super method first (will not fill the locations HashMap)-------------
		super.setInitializer(initializer);
		
		// --- For bigger Networks => use multiple threads to get positions -------------
		int chunkSize = 200000; // TODO Open issue for large graphs with more than 1000 nodes
		if (this.getGraph()!=null && this.getGraph().getVertexCount() > chunkSize) {
			// --- Start PositionDeterminerThreads --------------------------------------
			Object[] vertities = this.getGraph().getVertices().toArray();
			int noOfThreads = (int) Math.ceil((double) vertities.length / (double) chunkSize);
			for (int i = 0; i < noOfThreads; i++) {
				PositionDeterminerThread pdt = new PositionDeterminerThread(vertities, i, chunkSize);
				this.getRunningPositionDeterminerThreads().add(pdt);
				pdt.start();
			}
			// --- Wait for threads to be terminated ------------------------------------
			synchronized (this.getRunningPositionDeterminerThreads()) {
				try {
					this.getRunningPositionDeterminerThreads().wait(10000);
				} catch (InterruptedException iEx) {
					iEx.printStackTrace();
				}
			}
		}
	}
	/**
	 * Return the list of running position determiner threads.
	 * @return the running position determiner threads
	 */
	private List<PositionDeterminerThread> getRunningPositionDeterminerThreads() {
		if (positionDeterminerThreadList==null) {
			positionDeterminerThreadList = new ArrayList<>();
		}
		return positionDeterminerThreadList;
	}
	
	
	/**
	 * Returns the position transformer that considers the directions of the defined coordinate system.
	 * @return the TransformerForGraphNodePosition
	 * 
	 * @see LayoutSettings
	 */
	public TransformerForGraphNodePosition getCoordinateSystemPositionTransformer() {
		if (coordinateSystemNodePositionTransformer==null) {
			coordinateSystemNodePositionTransformer = new TransformerForGraphNodePosition(this.graphController);
		}
		return coordinateSystemNodePositionTransformer;
	}
	
	/**
	 * Sets the current instance of the {@link BasicGraphGuiVisViewer} to this static layout.
	 * @param basicGraphGuiVisViewer the basic graph gui vis viewer
	 */
	public void setBasicGraphGuiVisViewer(BasicGraphGuiVisViewer<?, ?> basicGraphGuiVisViewer) {
		this.basicGraphGuiVisViewer = basicGraphGuiVisViewer;
		this.getCoordinateSystemPositionTransformer().setBasicGraphGuiVisViewer(basicGraphGuiVisViewer);
	}
	/**
	 * Returns the current {@link BasicGraphGuiVisViewer} that is used to visualize the current Graph.
	 * @return the current BasicGraphGuiVisViewer
	 */
	public BasicGraphGuiVisViewer<?, ?> getBasicGraphGuiVisViewer() {
		return basicGraphGuiVisViewer;
	}

	
	/**
	 * Will immediately refresh the graph node positions in the layout.
	 */
	public void refreshGraphNodePosition() {
		// --- In the default layout case, nothing is to do here ----
	}
	/**
	 * Sets the refresh graph node position paused.
	 * @param isPaused the new refresh graph node position paused
	 */
	public void setRefreshGraphNodePositionPaused(boolean isPaused) {
		// --- In the default layout case, nothing is to do here ----
	}
	/**
	 * Checks if the graph node position refreshment is paused.
	 * @return true, if is graph node position refreshment is paused
	 */
	public boolean isRefreshGraphNodePositionPaused() {
		return false;
	}
	
	/**
	 * The dispose method will be called if the layout in the visualization viewer will be exchanged.
	 */
	public void dispose() {
		// --- In the default layout case, nothing is to do here ----
	}
	
	
	/**
	 * The Class PositionDeterminerThread.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class PositionDeterminerThread extends Thread {
		
		private Object[] vertexArray;
		private int threadNumber;
		private int chunkSize;
		
		/**
		 * Instantiates a new position determiner thread.
		 *
		 * @param vertexArray the vertex array
		 * @param threadNumber the thread number
		 * @param chunkSize the chunk size
		 */
		public PositionDeterminerThread(Object[] vertexArray, int threadNumber, int chunkSize) {
			this.vertexArray = vertexArray;
			this.threadNumber = threadNumber;
			this.chunkSize = chunkSize;
			this.setName(this.getClass().getSimpleName() + " " + (threadNumber+1));
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
			int start = this.threadNumber * this.chunkSize;
			int end = Math.min(start + chunkSize, vertexArray.length);
			
			for (int i = start; i<end; i++) {
				GraphNode graphNode = (GraphNode) vertexArray[i];
				locations.put(graphNode, getCoordinateSystemPositionTransformer().transform(graphNode));
			}
			
			// --- Remove this thread from the list of threads ----------------
			synchronized (getRunningPositionDeterminerThreads()) {
				getRunningPositionDeterminerThreads().remove(this);
				if (getRunningPositionDeterminerThreads().size()==0) {
					getRunningPositionDeterminerThreads().notify();
				}
			}
		}
	} // --- end sub class --- 
	
}
