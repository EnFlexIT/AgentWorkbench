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
package agentgui.core.charts.timeseries;

import jade.util.leap.List;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.Observable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import agentgui.core.charts.ChartTab;

public class TimeSeriesChartTab extends ChartTab{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7901497019520103656L;
	
	private static final String[] ALLOWED_RENDERER_CLASSES = {
		"XYAreaRenderer", 
		"XYLineRenderer", 
		"XYLineAndShapeRenderer", 
		"XYStepRenderer", 
		"XYStepAreaRenderer"
	};
	
	/**
	 * The dataset for the chart
	 */
	private TimeSeriesCollection dataset = null;
	
	private TimeSeriesDataModel model = null;
	
	private String rendererClassName = null;
	
	/**
	 * Returns a panel containing a chart that visualises the passed data.
	 * @param model the model
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model){
		this.model = model;
		setLayout(new BorderLayout());
		this.model.addObserver(this);
		dataset = model.getChartModel();
		add(createChartPanel(dataset));
	}
	
	
	/**
	 * This method creates the chart and its' visualization
	 * @param tsData The dataset
	 * @return Chartpanel containing the chart
	 */
	@Override
	protected ChartPanel createChartPanel(Dataset tsData){
		chart = ChartFactory.createTimeSeriesChart(
				model.getTitle(), 
				model.getxAxisLabel(), 
				model.getyAxisLabel(), 
				(XYDataset) tsData, 
				true, 
				false, 
				false
		);
		chart.setBackgroundPaint(this.getBackground());
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setRenderer(new XYStepRenderer());
		setSeriesColors();
		setSeriesLineWidths();
		
		rendererClassName = plot.getRenderer().getClass().getSimpleName();
		
		return new ChartPanel(chart);
	}
	
	public String getRendererClassName(){
		return this.rendererClassName;
	}
	
	private void setRendererClass(String className){
		if(Arrays.asList(ALLOWED_RENDERER_CLASSES).contains(className)){
			XYItemRenderer renderer = null;
			if(className.equals("XYAreaRenderer")){
				renderer = new XYAreaRenderer();
			}else if(className.equals("XYLineRenderer")){
				renderer = new XYLineAndShapeRenderer(true, false);
			}else if(className.equals("XYLineAndShapeRenderer")){
				renderer = new XYLineAndShapeRenderer(true, true);
			}else if(className.equals("XYStepRenderer")){
				renderer = new XYStepRenderer();
			}else if(className.equals("XYStepAreaRenderer")){
				renderer = new XYStepAreaRenderer();
			};
			chart.getXYPlot().setRenderer(renderer);
		}
	}
	
	public String[] getAllowedRendererClasses(){
		return ALLOWED_RENDERER_CLASSES;
	}
	
	public String getCurrentRendererClassName(){
		String className = null;

		XYItemRenderer renderer = ((XYPlot)chart.getPlot()).getRenderer();
		className = renderer.getClass().getSimpleName();
		
		if(className.equals("XYLineAndShapeRenderer") && !((XYLineAndShapeRenderer)renderer).getBaseShapesVisible()){
			return "XYLineRenderer";
		}else{
			return className;
		}
	}
	
	public Color getSeriesColor(String seriesLabel){
		for(int i=0;i<dataset.getSeriesCount();i++){
			if(dataset.getSeriesKey(i).equals(seriesLabel)){
				XYItemRenderer renderer = (chart.getXYPlot()).getRenderer();
				Color color = (Color) renderer.getSeriesPaint(i);
				return color;
			}
		}
		return null;
	}
	
	public float getLineWidth(String seriesLabel){
		for(int i=0;i<dataset.getSeriesCount();i++){
			if(dataset.getSeriesKey(i).equals(seriesLabel)){
				XYItemRenderer renderer = (chart.getXYPlot()).getRenderer();
				BasicStroke stroke = (BasicStroke)renderer.getSeriesStroke(i);
				if(stroke != null){
					return stroke.getLineWidth();
				}
			}
		}
		return 0;
	}
	
	/**
	 * This method sets the series colors according to those specified in the ontology model
	 */
	private void setSeriesColors(){
		List colors = model.getOntologyModel().getValueAxisColors();
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		for(int i=0; i<dataset.getSeriesCount(); i++){
			if(i < colors.size() && !colors.get(i).equals("")){
				Color newColor = new Color(Integer.parseInt((String) colors.get(i)));
				renderer.setSeriesPaint(i, newColor);
			}
		}
	}
	
	/**
	 * This method sets the series line widths according to those specified in the ontology model
	 */
	private void setSeriesLineWidths(){
		List lineWidths = model.getOntologyModel().getValueAxisLineWidth();
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		for(int i=0; i<dataset.getSeriesCount(); i++){
			if(i < lineWidths.size() && ((Float)lineWidths.get(i)) > 0.0){
				Stroke newStroke = new BasicStroke((Float) lineWidths.get(i));
				renderer.setSeriesStroke(i, newStroke);
			}
		}
	}
	
	/**
	 * This method sets the series labels according to those specified in the ontology model
	 */
	private void setSeriesLabels(){
		List labels = model.getOntologyModel().getValueAxisDescriptions();
		for(int i=0; i<dataset.getSeriesCount(); i++){
			if(i < labels.size()){
				TimeSeries series = dataset.getSeries(i);
				series.setKey((String)labels.get(i));
			}
		}
	}
	
	/**
	 * This method sets the axis labels according to those specified in the ontology model
	 */
	private void setAxisLabels(){
		XYPlot plot = chart.getXYPlot();
		plot.getDomainAxis().setLabel(model.getxAxisLabel());
		plot.getRangeAxis().setLabel(model.getyAxisLabel());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == this.model && (
				(Integer)arg == TimeSeriesDataModel.SETTINGS_CHANGED
				|| (Integer)arg == TimeSeriesDataModel.DATA_ADDED)
		){
			setRendererClass(model.getRendererType());
			chart.setTitle(model.getTitle());
			setAxisLabels();
			setSeriesColors();
			setSeriesLineWidths();
			setSeriesLabels();
		}
	}
	
}
