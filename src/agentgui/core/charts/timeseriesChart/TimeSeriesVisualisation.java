package agentgui.core.charts.timeseriesChart;

import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartEditorJDialog;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartEditorJPanel;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesWidget;
import agentgui.core.ontologies.gui.OntologyClassEditorJDialog;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;
import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.ontologies.gui.OntologyClassWidget;
import agentgui.ontology.TimeSeriesChart;

public class TimeSeriesVisualisation extends OntologyClassVisualisation {

	@Override
	public Class<?> getOntologyClass() {
		return TimeSeriesChart.class;
	}

	@Override
	public Class<? extends OntologyClassWidget> getWidgetClass() {
		return TimeSeriesWidget.class;
	}

	@Override
	public Class<? extends OntologyClassEditorJPanel> getEditorJPanelClass() {
		return TimeSeriesChartEditorJPanel.class;
	}

	@Override
	public Class<? extends OntologyClassEditorJDialog> getEditorJDialogClass() {
		return TimeSeriesChartEditorJDialog.class;
	}

}
