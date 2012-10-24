package agentgui.core.charts.timeseriesChart.gui;

import java.awt.BorderLayout;
import java.awt.Window;
import agentgui.core.charts.gui.ChartDialog;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesChartDialog extends ChartDialog{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2712116890492352158L;
	
	/**
	 * Create the dialog.
	 */
	public TimeSeriesChartDialog(Window owner, TimeSeriesChart chart) {
		
		super(owner, chart);
		this.setModal(true);
		
		setSize(600, 450);
		
		this.model = new TimeSeriesDataModel(chart);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
	}
	
	protected TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesChartTab) chartTab;
	}
	
	protected TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesTableTab) tableTab;
	}
	
	@Override
	protected Number parseKey(String key) {
		// TODO This implementation expects time to be in minutes, should be made configurable
		int minutes = Integer.parseInt(key);
		Long timestamp = ((TimeSeriesDataModel)model).getStartDate().getTime() + minutes * 60000;
		return timestamp;
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	

}
