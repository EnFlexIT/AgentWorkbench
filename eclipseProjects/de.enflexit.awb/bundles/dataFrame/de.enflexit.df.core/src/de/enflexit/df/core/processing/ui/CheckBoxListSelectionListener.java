package de.enflexit.df.core.processing.ui;

import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionEvent;

/**
 * Listener interface for {@link CheckBoxListSelectionEvent}s. Since in a {@link JCheckBox} there
 * will always be a single item selected or deselected using the checkbox, this will be more
 * convenient than the usual {@link ListSelectionEvent};  
 *
 * @param <E> the element type
 * @see CheckBoxListEvent
 */
public interface CheckBoxListSelectionListener<E> {
	
	/**
	 * Indicates a selection change in the {@link CheckBoxList}.
	 * @param checkboxListSelectionEvent the checkbox list selection event
	 */
	public void selectionChanged(CheckBoxListSelectionEvent<E> checkboxListSelectionEvent);
}
