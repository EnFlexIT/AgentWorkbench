package agentgui.core.charts.xyChart;

import agentgui.core.charts.xyChart.gui.XyChartEditorJPanel;
import agentgui.core.charts.xyChart.gui.XyWidget;
import agentgui.ontology.XyChart;
import de.enflexit.common.ontology.gui.OntologyClassEditorJPanel;
import de.enflexit.common.ontology.gui.OntologyClassVisualisation;
import de.enflexit.common.ontology.gui.OntologyClassWidget;

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
