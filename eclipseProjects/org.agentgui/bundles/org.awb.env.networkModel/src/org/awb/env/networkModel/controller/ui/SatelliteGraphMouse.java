/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package org.awb.env.networkModel.controller.ui;

import java.awt.event.MouseWheelEvent;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalSatelliteGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;

/**
 * The Class SatelliteGraphMouse is used for the satellite visualization of a graph.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SatelliteGraphMouse extends ModalSatelliteGraphMouse {

	private BasicGraphGui basicGraphGui;
	
    /**
     * Instantiates a new satellite graph mouse.
     * @param scalingControl the scaling control
     */
    public SatelliteGraphMouse(BasicGraphGui basicGraphGui) {
        
    	this.basicGraphGui = basicGraphGui;
        this.exchangeScalingControl();
    }
    /**
     * Exchanges the scaling control of the SatelliteGraphMouse with the initially specified one.
     */
    private void exchangeScalingControl() {
    	this.remove(this.scalingPlugin);
    	this.scalingPlugin =  new SatelliteScalingGraphMousePlugin(this.getZoomController().getScalingControl(), 0);
    	this.add(scalingPlugin);
    }
    
    /**
     * Returns the current zoom controller.
     * @return the zoom controller
     */
    protected ZoomController getZoomController() {
    	return this.basicGraphGui.getZoomController();
    }
    
    
    /**
     * The Class SatelliteScalingGraphMousePlugin as an individual alternative for the
     * zoom in or zoom out action - especially if maps are displayed and used.
     * 
     * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
     */
    public class SatelliteScalingGraphMousePlugin extends ScalingGraphMousePlugin {

        /**
         * Instantiates a new satellite scaling graph mouse plugin.
         *
         * @param scaler the scaler
         * @param modifiers the modifiers
         */
        public SatelliteScalingGraphMousePlugin(ScalingControl scaler, int modifiers) {
            super(scaler, modifiers);
        }
        /**
         * Instantiates a new satellite scaling graph mouse plugin.
         *
         * @param scaler the scaler
         * @param modifiers the modifiers
         * @param in the in
         * @param out the out
         */
        public SatelliteScalingGraphMousePlugin(ScalingControl scaler, int modifiers, float in, float out) {
            super(scaler, modifiers, in, out);
        }
        
        /* (non-Javadoc)
         * @see edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin#mouseWheelMoved(java.awt.event.MouseWheelEvent)
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            boolean accepted = checkModifiers(e);
            if (accepted == true) {
            	VisualizationViewer<?, ?> vv = (VisualizationViewer<?, ?>)e.getSource();
                if (vv instanceof SatelliteVisualizationViewer) {
                    int amount = e.getWheelRotation();
                    if (amount>0) {
                    	SatelliteGraphMouse.this.getZoomController().zoomIn();
                    } else if (amount<0) {
                    	SatelliteGraphMouse.this.getZoomController().zoomOut();
                    }
                    e.consume();
                    vv.repaint();
                }
            }
        }

    } // --- end sub class ---
    
}
