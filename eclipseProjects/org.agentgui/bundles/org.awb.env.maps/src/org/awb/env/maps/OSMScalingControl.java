package org.awb.env.maps;

import java.awt.geom.Point2D;
import java.util.List;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;


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
	
	private ZoomLevel zoomLevel;
	
	/**
	 * Sets the current zoom level.
	 * @param zoomLevel the new zoom level
	 */
	public void setZoomLevel(ZoomLevel zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	/**
	 * Returns the current zoom level.
	 * @return the zoom level
	 */
	public ZoomLevel getZoomLevel() {
		if (zoomLevel==null) {
			List<ZoomLevel> zlList = OSMZoomLevels.getInstance().getZoomLevelList();
			zoomLevel = zlList.get(zlList.size()-1);
		}
		return zoomLevel;
	}
	
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
			this.setZoomLevel(zoomLevel);
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
                
        Point2D transformedAtJung = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, at);        
        
        // return the transformers to 1.0
        layoutTransformer.scale(inverseModelScale, inverseModelScale, transformedAtJung);
        viewTransformer.scale(inverseViewScale, inverseViewScale, at);
        super.scale(vv, amount, at);
    }
    
}
