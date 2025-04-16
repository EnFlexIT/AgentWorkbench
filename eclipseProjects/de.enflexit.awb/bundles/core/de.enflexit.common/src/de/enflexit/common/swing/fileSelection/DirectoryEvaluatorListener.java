package de.enflexit.common.swing.fileSelection;

/**
 * The listener interface for a {@link DirectoryEvaluator}.
 * 
 * @see DirectoryEvaluator
 * @see DirectoryEvaluator#addDirectoryEvaluatorListener(DirectoryEvaluatorListener)
 * @see DirectoryEvaluator#removeDirectoryEvaluatorListener(DirectoryEvaluatorListener)
 */
public interface DirectoryEvaluatorListener {

	/**
	 * Will be invoked if the file evaluation in the {@link DirectoryEvaluator} is finalized.
	 */
	public void onEvaluationWasFinalized();
}
