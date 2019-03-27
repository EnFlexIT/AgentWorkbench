package gol.plugin;

import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;

/**
 * The Class GameOfLifePlugIn is just a base implementation of a PlugIn and 
 * does nothing else than printing its name.
 */
public class GameOfLifePlugIn extends PlugIn {

	private String myPlugInName = "Game of Life PlugIn";
	
	/**
	 * Instantiates a new game of life plug in.
	 * @param currProject the current project
	 */
	public GameOfLifePlugIn(Project currProject) {
		super(currProject);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#getName()
	 */
	@Override
	public String getName() {
		return myPlugInName;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#onPlugIn()
	 */
	@Override
	public void onPlugIn() {
		super.onPlugIn();
	}
	
}
