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
package agentgui.core.charts.gui;

import jade.util.leap.List;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.title.LegendTitle;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;

public abstract class ChartTab extends ChartPanel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -7678959452705514151L;
	public static final String[] RENDERER_TYPES= {
		"Area Renderer",
		"Line Renderer",
		"Line and Shape Renderer",
		"Step Renderer",
		"Step Area Renderer"
	};
	
	/**
	 * Use the step renderer by default.
	 */
	public static final String DEFAULT_RENDERER = RENDERER_TYPES[3];
	
	/**
	 * The data model (containing ontology-, chart- and tablemodel) for this chart
	 */
	protected DataModel model;
	
	/**
	 * Gets the current chartThumb. If this null or forceRefresh is true, it will be initialized/refreshed before. 
	 * @param forceRefresh If true, a new chartThumb will be created before returning it.
	 * @return The chartThumb
	 */
	public BufferedImage getChartThumb(){
		return this.exportAsImage(260, 175, true);
	}
	
	/**
	 * Constructor
	 * @param chart The chart to be displayed
	 */
	public ChartTab(JFreeChart chart) {
		super(chart);
		this.setBackground(Color.WHITE);								// Component background
		this.getChart().getPlot().setBackgroundPaint(Color.WHITE);		// Chart background
		
		// Background grid color
		this.getChart().getXYPlot().setDomainGridlinePaint(Color.BLACK);
		this.getChart().getXYPlot().setRangeGridlinePaint(Color.BLACK);
	}
	
	/**
	 * Exports the current chart as an image.
	 * @param width The width of the image
	 * @param height The height of the image
	 * @param hideLegend If true, the image will not contain the legend
	 * @return The image
	 */
	public BufferedImage exportAsImage(int width, int height, boolean hideLegend){
		
		LegendTitle legend = null;
		if(hideLegend){
			// Remove legend while exporting the image
			legend = getChart().getLegend();
			getChart().removeLegend();
		}
		
		BufferedImage thumb = getChart().createBufferedImage(width, height, 600, 400, null);
		
		if(hideLegend){
			// If the legend was removed, add it after exporting the image 
			getChart().addLegend(legend);
		}
		return thumb;
	}
	
	/**
	 * Sets the renderer type for the plot.
	 * @param rendererType The renderer type. Must be one of the strings defined in RENDERER_TYPES.
	 */
	public void setRenderer(String rendererType){
		
		if(! Arrays.asList(RENDERER_TYPES).contains(rendererType)){
			
			// Unsupported renderer type
			System.err.println("Unsupported renderer type: "+rendererType);
			
		}else{
			
			// Create the renderer instance
			XYItemRenderer renderer = null;
			if(rendererType.equals(RENDERER_TYPES[0])){
				renderer = new XYAreaRenderer();
			}else if(rendererType.equals(RENDERER_TYPES[1])){
				renderer = new XYLineAndShapeRenderer(true, false);
			}else if(rendererType.equals(RENDERER_TYPES[2])){
				renderer = new XYLineAndShapeRenderer(true, true);
			}else if(rendererType.equals(RENDERER_TYPES[3])){
				renderer = new XYStepRenderer();
			}else if(rendererType.equals(RENDERER_TYPES[4])){
				renderer = new XYStepAreaRenderer();
			}
			
			// Set it as renderer for all plots
			XYPlot plot = (XYPlot) this.getChart().getPlot();
			plot.setRenderer(renderer);
			
			applyColorSettings();
			applyLineWidthsSettings();
		}
		
	}
	
	/**
	 * Applies the color settings from the ontology model
	 */
	public void applyColorSettings(){
		List colors = model.getOntologyModel().getChartSettings().getYAxisColors();
		XYItemRenderer renderer = getChart().getXYPlot().getRenderer();
		for(int i=0; i < colors.size(); i++){
			Color newColor = new Color(Integer.parseInt((String) colors.get(i)));
			renderer.setSeriesPaint(i, newColor);
		}
	}
	
	/**
	 * Applies the line width settings from the ontology model
	 */
	public void applyLineWidthsSettings(){
		List lineWidths = model.getOntologyModel().getChartSettings().getYAxisLineWidth();
		XYItemRenderer renderer = getChart().getXYPlot().getRenderer();
		
		for(int i = 0; i < lineWidths.size(); i++){
			Stroke newStroke = new BasicStroke((Float) lineWidths.get(i));
			renderer.setSeriesStroke(i, newStroke);
		}
		
	}
	
	public void setXAxisLabel(String label){
		getChart().getXYPlot().getDomainAxis().setLabel(label);
	}
	
	public void setYAxisLabel(String label){
		getChart().getXYPlot().getRangeAxis().setLabel(label);
	}
	
	public void setSeriesColor(int seriesIndex, Color color) throws NoSuchSeriesException{
		if(seriesIndex < model.getSeriesCount()){
			XYItemRenderer renderer = getChart().getXYPlot().getRenderer();
			renderer.setSeriesPaint(seriesIndex, color);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void setSeriesLineWidth(int seriesIndex, float lineWidth) throws NoSuchSeriesException{
		if(seriesIndex < model.getSeriesCount()){
			XYItemRenderer renderer = getChart().getXYPlot().getRenderer();
			renderer.setSeriesStroke(seriesIndex, new BasicStroke(lineWidth));
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public abstract void replaceModel(DataModel newModel);
	
	/**
	 * Applies some settings for chart visualization. If chart type specific settings are required, override this method.
	 */
	protected void applySettings(){
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
		
		this.getChart().getPlot().setBackgroundPaint(Color.WHITE);		// Chart background
		this.getChart().getXYPlot().setDomainGridlinePaint(Color.BLACK);
		this.getChart().getXYPlot().setRangeGridlinePaint(Color.BLACK);
	}

}
