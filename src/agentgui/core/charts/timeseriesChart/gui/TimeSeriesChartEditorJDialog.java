package agentgui.core.charts.timeseriesChart.gui;

import agentgui.core.charts.gui.ChartEditorJDialog;
import agentgui.core.charts.gui.ChartEditorJPanel;
import agentgui.core.ontologies.gui.DynForm;

/**
 * OntologyClassEditorJDialog implementation for time series charts.
 * @author Nils
 */
public class TimeSeriesChartEditorJDialog extends ChartEditorJDialog {

	private static final long serialVersionUID = -1857036617331377067L;

	public TimeSeriesChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
	}

	public void setOntologyClassInstance(Object objectInstance) {
		this.contentPane.setOntologyClassInstance(objectInstance);
	}

	public Object getOntologyClassInstance() {
		return this.contentPane.getOntologyClassInstance();
	}

	@Override
	public ChartEditorJPanel getContentPane() {
		if(contentPane == null){
			contentPane = new TimeSeriesChartEditorJPanel(dynForm, startArgIndex);
		}
		return contentPane;
	}

}
