package de.enflexit.awb.core.ui;

/**
 * The Interface AwbProjectInteractionDialog defines the required methods for a user dialog
 * that handles (create, open, export and delete) projects.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbProjectInteractionDialog {
	
	/**
	 * Sets the dialog visible.
	 * @param b the new visible
	 */
	public void setVisible(boolean b);
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled();

	/**
	 * Closes the dialog.
	 * @return true, if successful
	 */
	public boolean close();

	
	/**
	 * Sets the project name.
	 * @param projectName the new project name
	 */
	public void setProjectName(String projectName);
	/**
	 * Returns the project name.
	 * @return the project name
	 */
	public String getProjectName();

	
	/**
	 * Sets the project folder.
	 * @param projectDirectory the new projects sub-directory
	 */
	public void setProjectDirectory(String projectDirectory);
	/**
	 * Returns the project folder.
	 * @return the project folder
	 */
	public String getProjectDirectory();

	
	/**
	 * Checks if a project is to export before it will be deleted.
	 * @return true, if is export before delete
	 */
	public boolean isExportBeforeDelete();
	
	
}
