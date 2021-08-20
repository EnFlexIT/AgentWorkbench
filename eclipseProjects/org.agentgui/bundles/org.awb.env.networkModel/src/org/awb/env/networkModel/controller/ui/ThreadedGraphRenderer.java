package org.awb.env.networkModel.controller.ui;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;

/**
 * The Class ThreadedGraphRenderer basically extends the JUNG {@link BasicRenderer}, but organizes 
 * the paintings in dedicated thread to accelerate the graph rendering.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ThreadedGraphRenderer<V, E> extends BasicRenderer<V, E> {

	private boolean debugPrintRenderTime = false;
	private long renderStartTime;
	private long renderEndTime;

	private boolean isUseRegularJungRendering = false;
	private boolean isUseMultipleThreads = false;
	
	private int noOfThreads = 4;
	private List<GraphRenderThread> graphRenderThreadList;
	private Object renderThreadTrigger;
	private GraphRenderJob graphRenderJob;
	
	private RenderContext<V, E> renderContext;
	private Layout<V, E> layout;
	
	
	/**
	 * Instantiates a new threaded graph renderer.
	 */
	public ThreadedGraphRenderer() {
		this.initializeRenderThreads();
	}
	/**
	 * Sets the current render context.
	 * @param renderContext the render context
	 */
	private void setRenderContext(RenderContext<V, E> renderContext) {
		this.renderContext = renderContext;
	}
	/**
	 * Returns the current render context.
	 * @return the render context
	 */
	private RenderContext<V, E> getRenderContext() {
		return renderContext;
	}

	/**
	 * Sets the current layout.
	 * @param layout the layout
	 */
	private void setLayout(Layout<V, E> layout) {
		this.layout = layout;
	}
	/**
	 * Return the current layout.
	 * @return the layout
	 */
	private Layout<V, E> getLayout() {
		return layout;
	}

	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.renderers.BasicRenderer#render(edu.uci.ics.jung.visualization.RenderContext, edu.uci.ics.jung.algorithms.layout.Layout)
	 */
	@Override
	public void render(RenderContext<V, E> renderContext, Layout<V, E> layout) {
		
		// --------------------------------------------------------------------
		// --- Possibility to use the regular JUNG rendering ------------------
		// --------------------------------------------------------------------
		if (this.isUseRegularJungRendering==true) {
			this.startPerformanceMeasurement();
			super.render(renderContext, layout);
			this.stopPerformanceMeasurement();
			this.printRenderTime();
			return;
		}
		// --------------------------------------------------------------------
		
		
		// --------------------------------------------------------------------
		// --- Prepare for the threaded rendering -----------------------------
		// --------------------------------------------------------------------
		this.startPerformanceMeasurement();
		this.setRenderContext(renderContext);
		this.setLayout(layout);
		
		// --- Get the arrays of the vertices and edges -----------------------
		Object[] edgeArray = layout.getGraph().getEdges().toArray();
		Object[] vertexArray = layout.getGraph().getVertices().toArray();

		if (this.isUseMultipleThreads==true) {
			// --- Start the working Threads ----------------------------------
			this.startGraphRenderJob(GraphRenderJob.RenderEdges);
			this.startGraphRenderJob(GraphRenderJob.RenderVertices);
		} else {
			// --- Do rendering by calling rendering methods directly ---------
			this.renderEdges(edgeArray);
			this.renderVertices(vertexArray);
		}
		
		this.stopPerformanceMeasurement();
		this.printRenderTime();
	}
	
	/**
	 * Renders the edges, specified in the given array.
	 *
	 * @param edgeArray the edge array
	 * @param renderContext the render context
	 * @param layout the layout
	 */
	@SuppressWarnings("unchecked")
	private void renderEdges(Object[] edgeArray) {
		for (int i = 0; i < edgeArray.length; i++) {
			this.renderEdge((E) edgeArray[i]);
		}
	}
	/**
	 * Renders the specified edge.
	 * @param e the edge to render
	 */
	private void renderEdge(E e) {
		try {
			this.renderEdge(this.getRenderContext(), this.getLayout(), e);
			this.renderEdgeLabel(this.getRenderContext(), this.getLayout(), e);
		} catch (ConcurrentModificationException cme) {
        	renderContext.getScreenDevice().repaint();
        }
	}
	
	/**
	 * Render the vertices, specified in the given array.
	 *
	 * @param vertexArray the vertex array
	 * @param renderContext the render context
	 * @param layout the layout
	 */
	@SuppressWarnings("unchecked")
	private void renderVertices(Object[] vertexArray) {
		for (int i = 0; i < vertexArray.length; i++) {
			this.renderVertex((V) vertexArray[i]);
		}
	}
	/**
	 * Render the specified vertex.
	 * @param v the vertex to render
	 */
	private void renderVertex(V v) {
		try {
			this.renderVertex(this.getRenderContext(), this.getLayout(), v);
			this.renderVertexLabel(this.getRenderContext(), this.getLayout(), v);
		} catch (ConcurrentModificationException cme) {
			 renderContext.getScreenDevice().repaint();
		}
	}
	
	
	// --------------------------------------------------------------------------------------------
	// --- From here, handling of the render working threads ---------------------------- Start ---
	// --------------------------------------------------------------------------------------------	
	/**
	 * Initialize the render threads.
	 */
	private void initializeRenderThreads() {

		if (this.isUseMultipleThreads==false) return;
		
		// --- Start the rendering threads ----------------
		for (int i = 0; i < this.noOfThreads; i++) {
			GraphRenderThread grt = new GraphRenderThread(this, i);
			this.getGraphRenderThreadList().add(grt);
			grt.start();
		}
	}

	/**
	 * Return the list of running position determiner threads.
	 * @return the running position determiner threads
	 */
	private List<GraphRenderThread> getGraphRenderThreadList() {
		if (graphRenderThreadList==null) {
			graphRenderThreadList = new ArrayList<>();
		}
		return graphRenderThreadList;
	}
	
	/**
	 * Returns the render thread trigger.
	 * @return the render thread trigger
	 */
	private Object getRenderThreadTrigger() {
		if (renderThreadTrigger==null) {
			renderThreadTrigger = new Object();
		}
		return renderThreadTrigger;
	}
	
	/**
	 * Starts the specified {@link GraphRenderJob} and will wait until the end of its execution.
	 * @param job the job to be done
	 */
	private void startGraphRenderJob(GraphRenderJob job) {
		this.graphRenderJob = job;
		// --- Re-Start all waiting working threads -------
		synchronized (this.getRenderThreadTrigger()) {
			this.getRenderThreadTrigger().notifyAll();
		}
		// --- Wait for the end of the job ----------------
		// TODO
	}
	/**
	 * Returns the current {@link GraphRenderJob} to be done by the working threads.
	 * @return the graph render job
	 */
	private GraphRenderJob getGraphRenderJob() {
		return graphRenderJob;
	}
	
	/**
	 * The enumeration GraphRenderJob.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private enum GraphRenderJob {
		RenderEdges,
		RenderVertices
	}

	/**
	 * The Class GraphRenderThread.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class GraphRenderThread extends Thread {
		
		private ThreadedGraphRenderer<V, E> graphRenderer;
		private boolean terminate;

		
		/**
		 * Instantiates a new graph render thread.
		 *
		 * @param graphRenderer the graph renderer
		 * @param threadIndex the thread index
		 */
		public GraphRenderThread(ThreadedGraphRenderer<V, E> graphRenderer, int threadIndex) {
			this.graphRenderer = graphRenderer;
			this.setName("GraphRenderThread-" + (threadIndex+1));
		}
		/**
		 * Will cause the termination of the current working thread.
		 */
		private void terminate() {
			this.terminate = true;
		}
		/**
		 * Returns the render thread trigger.
		 * @return the render thread trigger
		 */
		private Object getRenderThreadTrigger() {
			return this.graphRenderer.getRenderThreadTrigger();
		}
		/**
		 * Returns the current {@link GraphRenderJob}.
		 * @return the graph render job
		 */
		private GraphRenderJob getGraphRenderJob() {
			return this.graphRenderer.getGraphRenderJob();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
		
			try {

				while (this.terminate==false) {
					
					// --- Wait for job trigger -------------------------------
					synchronized (this.getRenderThreadTrigger()) {
						this.getRenderThreadTrigger().wait();
					}
					
					// --- Check which job is to be done ----------------------
					if (this.getGraphRenderJob()!=null) {
						switch (this.getGraphRenderJob()) {
						case RenderEdges:
							// --- TODO: get the instances to work on --------- 
							//ThreadedGraphRenderer.this.renderEdges(x);
							break;
						case RenderVertices:
							// --- TODO: get the instances to work on ---------
							//ThreadedGraphRenderer.this.renderVertices(x);
							break;
						}
					}
				}

			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
			}
			
		}
	}
	// --------------------------------------------------------------------------------------------
	// --- From here, handling of the render working threads ---------------------------- End -----
	// --------------------------------------------------------------------------------------------	
	

	// --------------------------------------------------------------------------------------------
	// --- From here, methods for performance measurements and its printing -----------------------
	// --------------------------------------------------------------------------------------------
	private void startPerformanceMeasurement() {
		this.renderStartTime = System.nanoTime();
	}
	private void stopPerformanceMeasurement() {
		this.renderEndTime = System.nanoTime();
	}
	private long getRenderTimeNanos() {
		return this.renderEndTime - this.renderStartTime;
	}
	private long getRenderTimeMillis() {
		return this.getRenderTimeNanos() / 1000000;
	}
	private double getRenderTimeSeconds() {
		return this.getRenderTimeMillis() /1000.0;
	}
	
	private void printRenderTime() {
		if (this.debugPrintRenderTime==true) {
			if (this.renderStartTime==0) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Render measurement was not started yet!");
			} else if (this.renderEndTime==0) {
				System.out.println("[" + this.getClass().getSimpleName() + "] Render measurement was not stopped yet!");
			} else {
				System.out.println("[" + this.getClass().getSimpleName() + "] Render Time: " + this.getRenderTimeMillis() + " ms, " + this.getRenderTimeSeconds() + " s");
			}
		}
	}
	
}
