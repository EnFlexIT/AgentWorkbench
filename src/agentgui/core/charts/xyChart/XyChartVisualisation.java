package agentgui.core.charts.xyChart;

import agentgui.core.charts.xyChart.gui.XyWidget;
import agentgui.core.ontologies.gui.OntologyClassEditorJDialog;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends OntologyClassEditorJDialog> getEditorJDialogClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
