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
package agentgui.envModel.graph.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class BasicGraphGuiVisualizationViewer was basically created to
 * apply and test {@link RenderingHints} for the {@link VisualizationViewer}.
 *
 * @param <V> the value type
 * @param <E> the element type
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGuiVisViewer<V,E> extends VisualizationViewer<V,E> {

	private static final long serialVersionUID = 8187230173732284770L;
	
	private boolean actionOnTop = false;
	
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param layout the layout
	 */
	public BasicGraphGuiVisViewer(Layout<V,E> layout) {
		super(layout);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param layout the layout
	 * @param preferredSize the preferred size
	 */
	public BasicGraphGuiVisViewer(Layout<V,E>layout, Dimension preferredSize) {
		super(layout, preferredSize);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param model the model
	 */
	public BasicGraphGuiVisViewer(VisualizationModel<V,E>model) {
		super(model);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the BasicGraphGui.
	 * @param model the model
	 * @param preferredSize the preferred size
	 */
	public BasicGraphGuiVisViewer(VisualizationModel<V,E> model, Dimension preferredSize) {
		super(model, preferredSize);
		this.initialize();
	}

	/**
	 * This Initializes the VisualizationViewer.
	 */
	private void initialize() {
		
		this.setBackground(Color.WHITE);
		this.setDoubleBuffered(true);
		
		// --- Configure some rendering hints in order to accelerate the visualisation --
		this.renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		this.renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		this.renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		this.renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		this.renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
		this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		
		this.renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		// --- useful and faster, but it makes the image quite unclear --------!!
//		this.renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); 
		
		// ------------------------------------------------------------------------------
		// --- Test area ----------------------------------------------------------------
		// ------------------------------------------------------------------------------
//		this.addPreRenderPaintable(new MapPreRenderer<V, E>(this));
		
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.BasicVisualizationServer#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		
		boolean successfulPainted = false;
		int paintTrials = 0;
		int paintTrialsMax = 3;
		Exception lastException = null;
		
		while (successfulPainted==false) {
			
			try {

				paintTrials++;
				if (this.isActionOnTop()==true || paintTrials>paintTrialsMax) {
					Graphics2D g2d = (Graphics2D)graphics;
					g2d.drawImage(offscreen, null, 0, 0);
					if (paintTrials>paintTrialsMax) break;
					
				} else {
					super.paintComponent(graphics);
					
				}
				successfulPainted = true;
				break;
				
			} catch (Exception ex) {
				String errMessage = "[" + this.getClass().getSimpleName() + "] Error while painting components - Retry ...";
				if (paintTrials>1) {
					errMessage = "\n" + errMessage;
				}
				System.err.print(errMessage);
				successfulPainted = false;
				lastException = ex;
				//ex.printStackTrace();
			}	
		}
		
		
		if (lastException!=null) {
			if (successfulPainted==false) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error while painting components:");
				lastException.printStackTrace();
			} else {
				System.err.println(" Done!");
			}
		}
		
	}
	
	/**
	 * Setter to indicate, if there is an action on the top of the graph visualisation.
	 * @param actionOnTop the new indicator for an action on top
	 */
	public void setActionOnTop(boolean actionOnTop) {
		this.actionOnTop = actionOnTop;
	}
	/**
	 * Checks if there is an action on top of this graph visualisation.
	 * @return true, if there is currently an action on top 
	 */
	public boolean isActionOnTop() {
		return actionOnTop;
	}
	
}
