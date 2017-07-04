package gasmas.compStat;

import gasmas.ontology.CompStat;
import agentgui.core.ontologies.gui.OntologyClassEditorJPanel;
import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.ontologies.gui.OntologyClassWidget;

public class CompressorStationVisualisation extends OntologyClassVisualisation {

	@Override
	public Class<?> getOntologyClass() {
		return CompStat.class;
	}

	@Override
	public Class<? extends OntologyClassWidget> getWidgetClass() {
		return CompressorStationWidget.class;
	}

	@Override
	public Class<? extends OntologyClassEditorJPanel> getEditorJPanelClass() {
		return CompressorStationEditorPanel.class;
	}

}
