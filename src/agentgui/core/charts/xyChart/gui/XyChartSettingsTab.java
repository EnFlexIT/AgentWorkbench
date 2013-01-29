package agentgui.core.charts.xyChart.gui;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartSettingsTab;

public class XyChartSettingsTab extends ChartSettingsTab{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1125346705401230033L;

	public XyChartSettingsTab(DataModel model) {
		this.model = model;
		
		this.settings = model.getChartSettings();
		this.settings.addObserver(this);
		
		initialize();
	}

}
