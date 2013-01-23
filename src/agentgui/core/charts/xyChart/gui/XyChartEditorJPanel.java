package agentgui.core.charts.xyChart.gui;

import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.charts.xyChart.XyDataModel;
import agentgui.core.charts.xyChart.XyOntologyModel;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.XyChart;

public class XyChartEditorJPanel extends ChartEditorJPanel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 7040702924414575703L;

	public XyChartEditorJPanel(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		// TODO Auto-generated constructor stub
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

	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.model = new XyDataModel((XyChart) objectInstance);
		this.getChartTab().replaceModel(this.model);
		this.getTableTab().replaceModel(this.model);
		this.getSettingsTab().replaceModel(this.model);
		this.model.addObserver(this);
	}

	@Override
	public Object getOntologyClassInstance() {
		return ((XyOntologyModel)this.model.getOntologyModel()).getXyChart();
	}

}
