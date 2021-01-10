package org.awb.env.maps;

import java.awt.geom.Point2D;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbsoluteCrossoverScalingControl;


public class RasterizedCrossoverScalingControl extends AbsoluteCrossoverScalingControl {

	public RasterizedCrossoverScalingControl() {
	}

	/* (non-Javadoc)
	* @see edu.uci.ics.jung.visualization.control.AbsoluteCrossoverScalingControl#scale(edu.uci.ics.jung.visualization.VisualizationViewer, float, java.awt.geom.Point2D)
	*/
	@Override
	public void scale(VisualizationViewer<?, ?> vv, float amount, Point2D at) {
		super.scale(vv, amount, at);
	}
	

}
