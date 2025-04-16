package de.enflexit.awb.desktop.project.update;

/**
 * The listener interface for receiving information to close the current dialog or window that 
 * displays the ProjectRepositoryExplorerPanelEvent.
 *
 * @see ProjectRepositoryExplorerPanel
 */
public interface ProjectRepositoryExplorerPanelListener {

	/**
	 * Will be invoked, if the project repository explorer is to be closed.
	 */
	public void closeProjectRepositoryExplorer();
	
	
}
