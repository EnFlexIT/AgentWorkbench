package agentgui.core.charts.timeseriesChart.gui;

import jade.util.leap.List;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.title.LegendTitle;

import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

/**
 * Time series chart visualization component
 * @author Nils
 *
 */
public class TimeSeriesChartTab extends ChartPanel {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1998969136744482400L;
	
	/**
	 * These renderer types can be chosen for rendering the plots. For adding more renderers,
	 * a description must be added to this array, and the corresponding constructor call must 
	 * be added to the setRenderer method.
	 */
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
	
	private TimeSeriesDataModel model;

	/**
	 * Constructor
	 * @param model The data model for this time series
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model){
		super(ChartFactory.createTimeSeriesChart(
				model.getOntologyModel().getGeneralSettings().getChartTitle(), 
				model.getOntologyModel().getGeneralSettings().getXAxisLabel(), 
				model.getOntologyModel().getGeneralSettings().getYAxisLabel(), 
				model.getChartModel(), 
				true, false, false
		));
		
		this.model = model;
		this.getChart().setBackgroundPaint(this.getBackground());
		
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
	}
	
	/**
	 * Creates a thumbnail of the chart
	 * @return The thumbnail
	 */
	public BufferedImage createChartThumb() {
		// Remove legend while creating the thumb
		LegendTitle legend = getChart().getLegend();
		getChart().removeLegend();
		BufferedImage thumb = getChart().createBufferedImage(260, 175, 600, 400, null);
		
		getChart().addLegend(legend);
		return thumb;
	}
	
	/**
	 * Sets the renderer type for the plot.
	 * @param rendererType The renderer type. Must be one of the strings defined in RENDERER_TYPES.
	 */
	void setRenderer(String rendererType){
		
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
	private void applyColorSettings(){
		List colors = model.getOntologyModel().getGeneralSettings().getYAxisColors();
		XYItemRenderer renderer = getChart().getXYPlot().getRenderer();
		for(int i=0; i < colors.size(); i++){
			Color newColor = new Color(Integer.parseInt((String) colors.get(i)));
			renderer.setSeriesPaint(i, newColor);
		}
	}
	
	/**
	 * Applies the line width settings from the ontology model
	 */
	private void applyLineWidthsSettings(){
		List lineWidths = model.getOntologyModel().getGeneralSettings().getYAxisLineWidth();
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
}
