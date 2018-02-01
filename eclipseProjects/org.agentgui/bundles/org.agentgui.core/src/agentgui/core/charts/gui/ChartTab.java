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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.Series;

import agentgui.core.charts.ChartModel;
import agentgui.core.charts.ChartSettingModel;
import agentgui.core.charts.ChartSettingModel.ChartSettingsUpdateNotification;
import de.enflexit.common.swing.WrappedFlowLayout;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.NoSuchSeriesException;

public abstract class ChartTab extends JPanel implements ActionListener, Observer {

	private static final long serialVersionUID = -7678959452705514151L;
	
	public static final int RENDERER_Area_Renderer = 0;
	public static final int RENDERER_Line_Renderer = 1;
	public static final int RENDERER_Line_And_Shape_Renderer = 2;
	public static final int RENDERER_Step_Renderer = 3;
	public static final int RENDERER_Step_Area_Renderer = 4;
	
	public static final String[] RENDERER_TYPES= {
		"Area Renderer",
		"Line Renderer",
		"Line and Shape Renderer",
		"Step Renderer",
		"Step Area Renderer"
	};
	
	/** Use the step renderer by default. */
	public static final String DEFAULT_RENDERER = RENDERER_TYPES[RENDERER_Line_Renderer];
	
	/** The data model (containing ontology-, chart- and tablemodel) for this chart	 */
	protected DataModel dataModel;
	
	protected ChartPanel chartPanel;
	protected JToolBar jToolBarSeriesVisibility;
	
	protected ChartEditorJPanel parent;
	
	
	/**
	 * Constructor
	 * @param chart The chart to be displayed
	 */
	public ChartTab(JFreeChart chart, ChartEditorJPanel parent) {
		
		this.parent = parent;
		
		// --- Create and configure the chart panel ---
		this.chartPanel = new ChartPanel(chart);
		this.chartPanel.setBackground(Color.WHITE);								// Component background
		this.chartPanel.getChart().getPlot().setBackgroundPaint(Color.WHITE);		// Chart background
		this.chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.BLACK);
		this.chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.BLACK);
		
		// --- Create the tool bar --------------------
		this.rebuildVisibilityToolBar();
		
		// --- Add components to the panel ------------
		this.setLayout(new BorderLayout());
		this.add(this.chartPanel, BorderLayout.CENTER);
		this.add(this.getJToolBarSeriesVisibility(), BorderLayout.SOUTH);
		
//		parent.getDataModel().getChartSettingModel().addObserver(this);
		
	}
	
	private JToolBar getJToolBarSeriesVisibility(){
		if(this.jToolBarSeriesVisibility == null){
			this.jToolBarSeriesVisibility = new JToolBar();
			this.jToolBarSeriesVisibility.setFloatable(false);
			this.jToolBarSeriesVisibility.setLayout(new WrappedFlowLayout(FlowLayout.CENTER));
		}
		return this.jToolBarSeriesVisibility;
	}
	
	/**
	 * Gets the render type.
	 *
	 * @param const_Of_Class_ChartTab_RENDERER a constant of this class
	 * @see ChartTab#RENDERER_Area_Renderer
	 * @see ChartTab#RENDERER_Line_Renderer
	 * @see ChartTab#RENDERER_Line_And_Shape_Renderer
	 * @see ChartTab#RENDERER_Step_Renderer
	 * @see ChartTab#RENDERER_Step_Area_Renderer
	 * 
	 * @return the render type
	 */
	public static String getRenderType(int const_Of_Class_ChartTab_RENDERER){
		return ChartTab.RENDERER_TYPES[const_Of_Class_ChartTab_RENDERER];
	}
	
	/**
	 * Gets the current chartThumb. If this null or forceRefresh is true, it will be initialized/refreshed before. 
	 * @return The chartThumb
	 */
	public BufferedImage getChartThumb(){
		return this.exportAsImage(260, 175, true);
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
			legend = this.chartPanel.getChart().getLegend();
			this.chartPanel.getChart().removeLegend();
		}
		
		BufferedImage thumb = this.chartPanel.getChart().createBufferedImage(width, height, 600, 400, null);
		
		if(hideLegend){
			// If the legend was removed, add it after exporting the image 
			this.chartPanel.getChart().addLegend(legend);
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
			XYPlot plot = (XYPlot) this.chartPanel.getChart().getPlot();
			plot.setRenderer(renderer);
			
			applyColorSettings();
			applyLineWidthsSettings();
		}
		
	}
	
	/**
	 * Applies the color settings from the ontology model
	 */
	public void applyColorSettings(){
		List colors = dataModel.getOntologyModel().getChartSettings().getYAxisColors();
		XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
		for(int i=0; i < colors.size(); i++){
			String colorString = (String) colors.get(i);
			if (colorString!=null && colorString.equals("")==false) {
				Color newColor = new Color(Integer.parseInt((String) colors.get(i)));
				renderer.setSeriesPaint(i, newColor);	
			}
		}	
	}
	
	/**
	 * Applies the line width settings from the ontology model
	 */
	public void applyLineWidthsSettings(){
		List lineWidths = dataModel.getOntologyModel().getChartSettings().getYAxisLineWidth();
		XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
		
		for(int i = 0; i < lineWidths.size(); i++){
			Stroke newStroke = new BasicStroke((Float) lineWidths.get(i));
			renderer.setSeriesStroke(i, newStroke);
		}
		
	}
	
	public void setXAxisLabel(String label){
		this.chartPanel.getChart().getXYPlot().getDomainAxis().setLabel(label);
	}
	
	public void setYAxisLabel(String label){
		this.chartPanel.getChart().getXYPlot().getRangeAxis().setLabel(label);
	}
	
	public void setSeriesColor(int seriesIndex, Color color) throws NoSuchSeriesException{
		if(seriesIndex < dataModel.getSeriesCount()){
			XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
			renderer.setSeriesPaint(seriesIndex, color);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void setSeriesLineWidth(int seriesIndex, float lineWidth) throws NoSuchSeriesException{
		if(seriesIndex < dataModel.getSeriesCount()){
			XYItemRenderer renderer = this.chartPanel.getChart().getXYPlot().getRenderer();
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
		
		if(dataModel.getOntologyModel().getChartSettings().getRendererType() != null){
			// If a renderer type is specified, use that
			setRenderer(dataModel.getOntologyModel().getChartSettings().getRendererType());
		}else{
			// If not, use the default renderer for this chart type
			setRenderer(DEFAULT_RENDERER);
		}
		
		applyColorSettings();
		applyLineWidthsSettings();
		
		this.chartPanel.getChart().getPlot().setBackgroundPaint(Color.WHITE);		// Chart background
		this.chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.BLACK);
		this.chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.BLACK);
		
	}
	
	/**
	 * Returns the chart visualized by this panel
	 * @return The chart visualized by this panel
	 */
	public JFreeChart getChart(){
		return this.chartPanel.getChart();
	}

	/**
	 * Creates a JCheckBox for every series and add it to the JToolBar
	 */
	private void rebuildVisibilityToolBar(){
		
		this.getJToolBarSeriesVisibility().removeAll();

		// Iterate over all series
		int seriesCount = this.getChart().getXYPlot().getDataset().getSeriesCount();
		for(int i=0; i<seriesCount; i++){
			
			// Create JCheckBox for this series
			Series series = parent.getDataModel().getChartModel().getSeries(i);
			JCheckBox seriesCheckBox = new JCheckBox((String)series.getKey());
			seriesCheckBox.addActionListener(this);
			
			// Set state according to current visibility
			boolean currentlyVisible = this.getChart().getXYPlot().getRenderer().getItemVisible(i, 0);
			seriesCheckBox.setSelected(currentlyVisible);
			
			seriesCheckBox.setToolTipText(this.generateToolTipTextForVisibilityCheckBox(seriesCheckBox));
			
			// Add to the JToolBar
			this.getJToolBarSeriesVisibility().add(seriesCheckBox);
		}
		
		this.getJToolBarSeriesVisibility().repaint();
		this.getJToolBarSeriesVisibility().revalidate();
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// Visibility checkbox - show or hide a series
		if(ae.getSource() instanceof JCheckBox){
			
			// Determine which series to hide
			JCheckBox source = (JCheckBox) ae.getSource();
			String seriesLabel = source.getText();

			// Find the series
			XYPlot plot = this.getChart().getXYPlot();
			for(int i=0; i<plot.getSeriesCount(); i++){
				
				// Toggle visibility
				if(plot.getDataset().getSeriesKey(i).equals(seriesLabel)){
					XYItemRenderer renderer = plot.getRenderer();
					boolean currentlyVisible = renderer.getItemVisible(i, 0);
					renderer.setSeriesVisible(i, new Boolean(!currentlyVisible));
				}
			}
			
			// Update the tooltip text to reflect the new state
			source.setToolTipText(this.generateToolTipTextForVisibilityCheckBox(source));
			
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ChartModel){
			// Rebuild the toolbar (all other changes are handled by the ChartPanel component from JFreeChart)
			this.rebuildVisibilityToolBar();
		}else if(o instanceof ChartSettingModel){
			
			try {
				ChartSettingsUpdateNotification notification = (ChartSettingsUpdateNotification) arg;
				int seriesIndex = notification.getSeriesIndex();
				
				switch(notification.getEventType()){
					case TITLE_CHANGED:
						this.chartPanel.getChart().getTitle().setText((String) notification.getNewValue());
						break;
						
					case X_AXIS_LABEL_CHANGED:
						this.getChart().getXYPlot().getDomainAxis().setLabel((String) notification.getNewValue());
						break;
						
					case Y_AXIS_LABEL_CHANGED:
						this.getChart().getXYPlot().getRangeAxis().setLabel((String) notification.getNewValue());
						break;
						
					case RENDERER_CHANGED:
						this.setRenderer((String) notification.getNewValue());
						break;
					
					case SERIES_COLOR_CHANGED:
						this.setSeriesColor(seriesIndex, (Color) notification.getNewValue());
						break;
					
					case SERIES_LINE_WIDTH_CHANGED:
						this.setSeriesLineWidth(seriesIndex, (float) notification.getNewValue());
						break;
						
					default:
						// Nothing to do for other event types
						break;
				}
			} catch (NoSuchSeriesException nosEx) {
				nosEx.printStackTrace();
			}
		}
	}
	
	/**
	 * Generates a ToolTip text for visibility checkboxes based on their current selection state
	 * @param checkBox The checkbox to generate a text for
	 * @return The tooltip text
	 */
	private String generateToolTipTextForVisibilityCheckBox(JCheckBox checkBox){
		if(checkBox.isSelected()){
			return "Hide " + checkBox.getText();
		}else{
			return "Show " + checkBox.getText();
		}
	}
	

}
