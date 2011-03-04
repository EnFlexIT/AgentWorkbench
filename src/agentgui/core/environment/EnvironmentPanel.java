package agentgui.core.environment;

import javax.swing.JPanel;

import agentgui.core.application.Project;

public class EnvironmentPanel extends JPanel {

	private static final long serialVersionUID = -5522022346976174783L;

	protected Project currProject = null;
	
	/**
	 * Don't use this constructor !
	 */
	@Deprecated
	public EnvironmentPanel() {
		super();
	}
	/**
	 * This is the default constructor for this class
	 * @param project
	 */
	public EnvironmentPanel(Project project) {
		super();
		this.currProject = project;
	}
	
}
