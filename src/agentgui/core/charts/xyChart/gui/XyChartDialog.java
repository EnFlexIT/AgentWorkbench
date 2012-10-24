package agentgui.core.charts.xyChart.gui;

import java.awt.BorderLayout;
import java.awt.Window;

import agentgui.core.charts.gui.ChartDialog;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.ontology.XyChart;

public class XyChartDialog extends ChartDialog {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -4780446063163497998L;

	public XyChartDialog(Window owner, XyChart chart) {
		super(owner, chart);
		this.setModal(true);
		
		setSize(600, 450);
		
		this.model = new XyDataModel(chart);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
	}

	@Override
	protected XyChartTab getChartTab() {
		if(chartTab == null){
			chartTab = new XyChartTab((XyDataModel) model);
		}
		return (XyChartTab) chartTab;
	}

	@Override
	protected XyTableTab getTableTab() {
		if(tableTab == null){
			tableTab = new XyTableTab((XyDataModel) model);
		}
		return (XyTableTab) tableTab;
	}

	@Override
	protected Number parseKey(String key) {
		return Float.parseFloat(key);
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

}
