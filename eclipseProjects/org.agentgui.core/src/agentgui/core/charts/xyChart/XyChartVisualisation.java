package agentgui.core.charts.xyChart;

import agentgui.core.charts.xyChart.gui.XyChartEditorJPanel;
import agentgui.core.charts.xyChart.gui.XyWidget;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;
import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.ontologies.gui.OntologyClassWidget;
import agentgui.ontology.XyChart;

public class XyChartVisualisation extends OntologyClassVisualisation {

	@Override
	public Class<?> getOntologyClass() {
		return XyChart.class;
	}

	@Override
	public Class<? extends OntologyClassWidget> getWidgetClass() {
		return XyWidget.class;
	}

	@Override
	public Class<? extends OntologyClassEditorJPanel> getEditorJPanelClass() {
		return XyChartEditorJPanel.class;
	}

}
