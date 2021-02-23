package org.awb.env.maps;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

// TODO: Auto-generated Javadoc
/**
 * scales to the absolute value passed as an argument.
 * It first resets the scaling transformers, then uses
 * the relative CrossoverScalingControl to achieve the
 * absolute value.
 * 
 * @author Tom Nelson 
 */
public class OSMScalingControl extends CrossoverScalingControl implements ScalingControl {

	
	/** The scaling factors. */
	private float scalingFactors[];
	
	/** The zoom level. */
	private int zoomLevel;
	
	/** The refinement scaling factor. */
	private float refinementScalingFactor;
	
	/**   The length of the equator in meters  . */
	static final float EQUATOR_LENGTH_IN_METERS = 40075016.686f; 
	
	/** The size of map tiles in pixels. */
	static final int TILE_SIZE = 256;
	
	/** The Constant MAX_ZOOM. */
	static final int MAX_ZOOM = 0; 

	/** The Constant MIN_ZOOM. */
	static final int MIN_ZOOM = 19; 
	
	/**
	 * Instantiates a new OSM scaling control.
	 *
	 * @param refinementScalingFactor the refinement scaling factor
	 * @param zoomLevel the zoom level
	 */
//	public OSMScalingControl(float refinementScalingFactor, int zoomLevel) {
//		this.refinementScalingFactor = refinementScalingFactor;
//		this.zoomLevel = zoomLevel;
//		calcScalingFactorsByZoomLevel();
//	}
	
//	public OSMScalingControl()
	 /**
     * Point where scale crosses over from view to layout.
     */
    protected double crossover = 1.0;
    
    /**
     * Sets the crossover point to the specified value.
     *
     * @param crossover the new crossover
     */
	public void setCrossover(double crossover) {
	    this.crossover = crossover;
	}
    
    /**
     * Returns the current crossover value.
     *
     * @return the crossover
     */
    public double getCrossover() {
        return crossover;
    }
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.ScalingControl#scale(edu.uci.ics.jung.visualization.VisualizationServer, float, java.awt.geom.Point2D)
	 */
	@Override
    public void scale(VisualizationServer<?,?> vv, float amount, Point2D at) {
  
		MutableTransformer layoutTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        MutableTransformer viewTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
        double modelScale = layoutTransformer.getScale();
        double viewScale = viewTransformer.getScale();
        double inverseModelScale = Math.sqrt(crossover)/modelScale;
        double inverseViewScale = Math.sqrt(crossover)/viewScale;
        
        Point2D transformedAt = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);
        
        // return the transformers to 1.0
        layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);
        viewTransformer.scale(inverseViewScale, inverseViewScale, at);

        System.out.println("Scale to "+ amount);
        superClassScale(vv, amount, transformedAt);
    }
    
    /**
     * Super class scale.
     *
     * @param vv the vv
     * @param amount the amount
     * @param at the at
     */
    public void superClassScale(VisualizationServer<?,?> vv, float amount, Point2D at) {
    	
    	MutableTransformer layoutTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
 	    MutableTransformer viewTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
 	    double modelScale = layoutTransformer.getScale();
 	    double viewScale = viewTransformer.getScale();
 	    double inverseModelScale = Math.sqrt(crossover)/modelScale;
 	    double inverseViewScale = Math.sqrt(crossover)/viewScale;
 	    double scale = modelScale * viewScale;
 	    
 	    Point2D transformedAt = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);
 	    
         if((scale*amount - crossover)*(scale*amount - crossover) < 0.001) {
             // close to the control point, return both transformers to a scale of sqrt crossover value
             layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);
             viewTransformer.scale(inverseViewScale, inverseViewScale, at);
         } else if(scale*amount < crossover) {
             // scale the viewTransformer, return the layoutTransformer to sqrt crossover value
 	        viewTransformer.scale(amount, amount, at);
 	        layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAt);
 	    } else {
             // scale the layoutTransformer, return the viewTransformer to crossover value
 	        layoutTransformer.scale(amount, amount, transformedAt);
 	        viewTransformer.scale(inverseViewScale, inverseViewScale, at);
 	    }
 	    vv.repaint();
    }
    
}
