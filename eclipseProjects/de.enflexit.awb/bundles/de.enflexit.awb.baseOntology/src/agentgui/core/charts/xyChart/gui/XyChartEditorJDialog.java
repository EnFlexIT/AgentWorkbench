package agentgui.core.charts.xyChart.gui;

import agentgui.core.charts.gui.ChartEditorJDialog;
import agentgui.core.charts.gui.ChartEditorJPanel;
import de.enflexit.common.ontology.gui.DynForm;

public class XyChartEditorJDialog extends ChartEditorJDialog {

	private static final long serialVersionUID = 4548924604253959308L;

	public XyChartEditorJDialog(DynForm dynForm, int startArgIndex) {
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
			contentPane = new XyChartEditorJPanel(dynForm, startArgIndex);
		}
		return contentPane;
	}

}
