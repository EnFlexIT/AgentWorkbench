package agentgui.core.charts.timeseriesChart;

import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartEditorJPanel;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesWidget;
import agentgui.ontology.TimeSeriesChart;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;
import de.enflexit.common.ontology.gui.OntologyClassVisualisation;
import de.enflexit.common.ontology.gui.OntologyClassWidget;

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

}
