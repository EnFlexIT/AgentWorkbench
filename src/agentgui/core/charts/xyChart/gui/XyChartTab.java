package agentgui.core.charts.xyChart.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.xyChart.XyDataModel;

public class XyChartTab extends ChartTab {
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 5373349334098916334L;
	/**
	 * These renderer types can be chosen for rendering the plots. For adding more renderers,
	 * a description must be added to this array, and the corresponding constructor call must 
	 * be added to the setRenderer method.
	 */

	public XyChartTab(XyDataModel model){
		super(ChartFactory.createXYLineChart(
				model.getOntologyModel().getChartSettings().getChartTitle(), 
				model.getOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getOntologyModel().getChartSettings().getYAxisLabel(), 
				(XYDataset) model.getChartModel(), 
				PlotOrientation.VERTICAL, 
				true, false, false
		));
		
		
		this.model = model;
		this.getChart().setBackgroundPaint(this.getBackground());
		
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
	}
}
