package de.enflexit.df.core.processing.ui;

/**
 * The listener interface for receiving radioButtonListSelection events.
 * The class that is interested in processing a radioButtonListSelection
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRadioButtonListSelectionListener</code> method. When
 * the radioButtonListSelection event occurs, that object's appropriate
 * method is invoked.
 *
 * @param <E> the element type
 * @see RadioButtonListSelectionEvent
 */
public interface RadioButtonListSelectionListener<E> {
	
	/**
	 * Implement this method to react on selection changes.
	 * @param selectionEvent the selection event
	 */
	public void selectionChanged(RadioButtonListSelectionEvent<E> selectionEvent);
}
