package agentgui.core.charts.xyChart;

import agentgui.core.charts.ChartSettings;
import agentgui.ontology.Chart;

/**
 * Settings handler class for XY charts. No additional functionality needed, 
 * this classes only purpose is to provide class symmetry between the different 
 * chart types, to avoid confusion for the developer. 
 * 
 * @author Nils
 *
 */
public class XyChartSettings extends ChartSettings {

	public XyChartSettings(Chart chart) {
		super(chart);
	}

}
