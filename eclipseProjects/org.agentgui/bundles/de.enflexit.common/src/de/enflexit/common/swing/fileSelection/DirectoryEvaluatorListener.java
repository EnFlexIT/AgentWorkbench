package de.enflexit.common.swing.fileSelection;

/**
 * The listener interface for receiving directoryEvaluator events.
 * The class that is interested in processing a directoryEvaluator
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDirectoryEvaluatorListener<code> method. When
 * the directoryEvaluator event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DirectoryEvaluatorEvent
 */
public interface DirectoryEvaluatorListener {

	/**
	 * Will be invoked if the evaluation was finalized.
	 */
	public void onEvaluationWasFinalized();
}
