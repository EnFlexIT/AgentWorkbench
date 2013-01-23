package agentgui.core.charts.timeseriesChart.gui;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesOntologyModel;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.TimeSeriesChart;

/**
 * Implementation of OntologyClassEditorJPanel for TimeSeriesChart
 * @author Nils
 *
 */
public class TimeSeriesChartEditorJPanel extends ChartEditorJPanel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 6342520178418229017L;

	public TimeSeriesChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	@Override
	protected TimeSeriesChartTab getChartTab(){
		if(chartTab == null){
			chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesChartTab) chartTab;
	}

	@Override
	protected TimeSeriesTableTab getTableTab(){
		if(tableTab == null){
			tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) this.model);
		}
		return (TimeSeriesTableTab) tableTab;
	}

	@Override
	protected Number parseKey(String key) {
		// TODO This implementation expects time to be in minutes, should be made configurable
		// TODO Move to the model class?
		int minutes = Integer.parseInt(key);
		Long timestamp = ((TimeSeriesDataModel)model).getStartDate().getTime() + minutes * 60000;
		return timestamp;
	}

	@Override
	protected Number parseValue(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.model = new TimeSeriesDataModel((TimeSeriesChart) objectInstance);
		
		this.getChartTab().replaceModel(this.model);
		this.getTableTab().replaceModel(this.model);
		this.getSettingsTab().replaceModel(this.model);
		this.model.addObserver(this);
	}

	@Override
	public Object getOntologyClassInstance() {
		return ((TimeSeriesOntologyModel)this.getModel().getOntologyModel()).getTimeSeriesChart();
	}

}
