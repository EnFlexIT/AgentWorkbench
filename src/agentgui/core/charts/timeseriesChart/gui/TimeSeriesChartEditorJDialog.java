package agentgui.core.charts.timeseriesChart.gui;

import agentgui.core.charts.gui.ChartEditorJDialog;
import agentgui.core.ontologies.gui.DynForm;
/**
 * OntologyClassEditorJDialog implementation for time series charts.
 * @author Nils
 */
public class TimeSeriesChartEditorJDialog extends ChartEditorJDialog {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1857036617331377067L;

	public TimeSeriesChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		this.contentPane = new TimeSeriesChartEditorJPanel(dynForm, startArgIndex);
	}

	@Override
	public void setOntologyClassInstance(Object objectInstance) {
		this.contentPane.setOntologyClassInstance(objectInstance);
	}

	@Override
	public Object getOntologyClassInstance() {
		return this.contentPane.getOntologyClassInstance();
	}

}
