package org.awb.env.maps;

import java.awt.geom.Point2D;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * scales to the absolute value passed as an argument.
 * It first resets the scaling transformers, then uses
 * the relative CrossoverScalingControl to achieve the
 * absolute value.
 * 
 * @author Tom Nelson 
 */
public class OSMScalingControl extends CrossoverScalingControl implements ScalingControl {

	private boolean isDebug = true;
	
	/**
	 * Scales the view to the specified {@link ZoomLevel}.
	 *
	 * @param vv the visualization viewer
	 * @param zoomLevel the zoom level
	 * @param at the at
	 */
	public void scale(VisualizationServer<?,?> vv, ZoomLevel zoomLevel, Point2D at) {
		if (zoomLevel!=null) {
			if (isDebug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Scale to " + zoomLevel);
			this.scale(vv, zoomLevel.getJungScaling(), at);
		}
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
        double inverseModelScale = Math.sqrt(this.getCrossover())/modelScale;
        double inverseViewScale = Math.sqrt(this.getCrossover())/viewScale;
        
        BasicGraphGuiVisViewer<?, ?> bvv = (BasicGraphGuiVisViewer<?, ?>) vv;
        TransformerForGraphNodePosition cspTransformer = bvv.getCoordinateSystemPositionTransformer();
        
        Point2D transformedAtJung = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);
        Point2D transformedAtGraph = cspTransformer.inverseTransform(transformedAtJung);
        
        
        // return the transformers to 1.0
        layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAtJung);
        viewTransformer.scale(inverseViewScale, inverseViewScale, at);
        super.scale(vv, amount, at);
    }
    
}
