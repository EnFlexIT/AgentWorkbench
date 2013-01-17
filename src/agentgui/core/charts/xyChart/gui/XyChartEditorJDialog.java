package agentgui.core.charts.xyChart.gui;

import agentgui.core.charts.gui.ChartEditorJDialog;
import agentgui.core.ontologies.gui.DynForm;

public class XyChartEditorJDialog extends ChartEditorJDialog {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 4548924604253959308L;

	public XyChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		this.contentPane = new XyChartEditorJPanel(dynForm, startArgIndex);
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
