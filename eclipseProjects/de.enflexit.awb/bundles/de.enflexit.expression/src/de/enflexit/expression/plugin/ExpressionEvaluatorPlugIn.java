package de.enflexit.expression.plugin;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.ToolBarSurrounding;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.ToolBarType;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;

import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;

public class ExpressionEvaluatorPlugIn extends PlugIn {
	
	private GraphEnvironmentController graphController;

	public ExpressionEvaluatorPlugIn(Project currProject) {
		super(currProject);
	}
	
	@Override
	public void onPlugIn() {
		this.getGraphController().addCustomToolbarComponentDescription(new CustomToolbarComponentDescription(ToolBarType.EditControl, ToolBarSurrounding.ConfigurationOnly, ExpressionEvaluatorButton.class, null, true));
		super.onPlugIn();
	}

	@Override
	public String getName() {
		return "Expression Evaluator GUI";
	}
	
	/**
	 * Gets the graph controller.
	 * @return the graph controller
	 */
	private GraphEnvironmentController getGraphController() {
		if (graphController==null) {
			graphController = (GraphEnvironmentController) this.project.getEnvironmentController();
		}
		return graphController;
	}

}
