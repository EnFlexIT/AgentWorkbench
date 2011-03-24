package agentgui.core.plugin;

import agentgui.core.application.Project;

public class TestPlugIn extends PlugIn {

	public TestPlugIn(Project currProject) {
		super(currProject);
	}

	@Override
	public String getName() {
		return "Test-PlugIn";
	}

	@Override
	public void onPlugIn() {
		super.onPlugIn();
		
	}
	
	
}
