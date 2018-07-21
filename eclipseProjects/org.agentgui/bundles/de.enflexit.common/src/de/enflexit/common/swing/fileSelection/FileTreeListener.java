package de.enflexit.common.swing.fileSelection;

/**
 * The listener interface for receiving fileTree events.
 * The class that is interested in processing a fileTree
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileTreeListener<code> method. When
 * the fileTree event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FileTreeEvent
 */
public interface FileTreeListener {

	/**
	 * Will be invoked if the file selection changed.
	 */
	public void onFileSelectionChanged();
	
}
