package org.awb.env.networkModel.controller.ui;

import java.util.ConcurrentModificationException;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * The Class OptimizingGraphRenderer basically extends the JUNG {@link BasicRenderer}, 
 * but organizes the rendering of a graph differently .
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OptimizingGraphRenderer<V, E> extends BasicRenderer<V, E> {

	private boolean debugPrintRenderTime = true;
	private long renderStartTime;
	private long renderEndTime;

	private boolean isUseRegularJungRendering = false;
	
	private RenderContext<V, E> renderContext;
	private Layout<V, E> layout;
	private Object[] edgeArray;
	private Object[] vertexArray;
	
	private boolean doLabelRendering = true;
	
	/**
	 * Instantiates a new threaded graph renderer.
	 */
	public OptimizingGraphRenderer() {
	}
	
	// --------------------------------------------------------------------------------------------
	// --- From here, the actual render methods ----------------------------------------- Start ---
	// --------------------------------------------------------------------------------------------	
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
		
		
		// --- Start performance measurement ----------------------------------
		this.startPerformanceMeasurement();
		
		// --- Set local variables for the rendering --------------------------
		this.setRenderContext(renderContext);
		this.setLayout(layout);
		this.setEdgeArray(layout.getGraph().getEdges().toArray());
		this.setVertexArray(layout.getGraph().getVertices().toArray());

		// --- Decide to do the label rendering -------------------------------
		double scale = this.getOverallScale();
		this.setDoLabelRendering(scale>=0.5);
		
		// --- Do the rendering -----------------------------------------------
		this.renderEdges(this.getEdgeArray());
		this.renderVertices(this.getVertexArray());
		
		// --- Stop performance measurement -----------------------------------
		this.stopPerformanceMeasurement();
		this.printRenderTime();
	}
	
	/**
	 * Renders the edges, specified in the given array.
	 * @param edgeArray the edge array
	 */
	private void renderEdges(Object[] edgeArray) {
		try {
			for (int i = 0; i < edgeArray.length; i++) {
				this.renderEdgeObject(edgeArray[i]);
			}
		} catch (ConcurrentModificationException cme) {
        	renderContext.getScreenDevice().repaint();
        }
	}
	/**
	 * Renders the specified edge object.
	 * @param edgeObject the edge object
	 */
	@SuppressWarnings("unchecked")
	private void renderEdgeObject(Object edgeObject) {
		this.renderEdge((E)edgeObject);
	}
	/**
	 * Renders the specified edge.
	 * @param e the edge to render
	 */
	private void renderEdge(E e) {
		try {
			this.renderEdge(getRenderContext(), getLayout(), e);
			if (this.isDoLabelRendering()) {
				this.renderEdgeLabel(getRenderContext(), getLayout(), e);
			}
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
	private void renderVertices(Object[] vertexArray) {
		try {
			for (int i = 0; i < vertexArray.length; i++) {
				this.renderVertexObject(vertexArray[i]);
			}
		} catch (ConcurrentModificationException cme) {
			 renderContext.getScreenDevice().repaint();
		}
	}
	/**
	 * Renders the specified vertex object.
	 * @param vertexObject the vertex object
	 */
	@SuppressWarnings("unchecked")
	private void renderVertexObject(Object vertexObject) {
		this.renderVertex((V)vertexObject);
	}
	/**
	 * Render the specified vertex.
	 * @param v the vertex to render
	 */
	private void renderVertex(V v) {
		try {
			this.renderVertex(this.getRenderContext(), this.getLayout(), v);
			if (this.isDoLabelRendering()) {
				this.renderVertexLabel(this.getRenderContext(), this.getLayout(), v);
			}
		} catch (ConcurrentModificationException cme) {
			 renderContext.getScreenDevice().repaint();
		}
	}
	// --------------------------------------------------------------------------------------------
	// --- From here, the actual render methods ------------------------------------------- End ---
	// --------------------------------------------------------------------------------------------	

	/**
	 * Returns the current overall scale of the VisualizationViewer.
	 * @return the overall scale
	 */
	public double getOverallScale() {
		
		MutableTransformer layoutTransformer = this.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		MutableTransformer viewTransformer = this.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
		double modelScale = layoutTransformer.getScale();
		double viewScale = viewTransformer.getScale();
		return modelScale * viewScale;
	}
	

	// --------------------------------------------------------------------------------------------
	// --- From here, get / setter methods to handle variables for the rendering -------- Start ---
	// --------------------------------------------------------------------------------------------	
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

	/**
	 * Sets the current edge array.
	 * @param edgeArray the new edge array
	 */
	private void setEdgeArray(Object[] edgeArray) {
		this.edgeArray = edgeArray;
	}
	/**
	 * Returns the current edge array.
	 * @return the edge array
	 */
	private Object[] getEdgeArray() {
		return edgeArray;
	}
		
	/**
	 * Sets the current vertex array.
	 * @param vertexArray the new vertex array
	 */
	private void setVertexArray(Object[] vertexArray) {
		this.vertexArray = vertexArray;
	}
	/**
	 * Returns the current vertex array.
	 * @return the vertex array
	 */
	private Object[] getVertexArray() {
		return vertexArray;
	}
	
	/**
	 * Checks if is do label rendering.
	 * @return true, if is do label rendering
	 */
	private boolean isDoLabelRendering() {
		return doLabelRendering;
	}
	/**
	 * Sets the do label rendering.
	 * @param doLabelRendering the new do label rendering
	 */
	private void setDoLabelRendering(boolean doLabelRendering) {
		this.doLabelRendering = doLabelRendering;
	}
	// --------------------------------------------------------------------------------------------
	// --- From here, get / setter methods to handle variables for the rendering ---------- End ---
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
				System.out.println("[" + this.getClass().getSimpleName() + "] Render Time: " + this.getRenderTimeMillis() + " ms, " + this.getRenderTimeSeconds() + " s, Scale: " + this.getOverallScale());
			}
		}
	}
	
}
