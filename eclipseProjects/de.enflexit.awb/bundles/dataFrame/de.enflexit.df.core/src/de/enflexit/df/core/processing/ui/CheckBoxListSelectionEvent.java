package de.enflexit.df.core.processing.ui;

import java.util.EventObject;

/**
 * This event indicates a selection change in a {@link CheckBoxList}. It indicates
 * which list item was selected, and if it was selected or deselected.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class CheckBoxListSelectionEvent<E> extends EventObject {

	private static final long serialVersionUID = -3377265581276658807L;
	
	private final int index;
	private final E item;
	private final boolean selected;

	public CheckBoxListSelectionEvent(CheckBoxList<E> source, int index, E item, boolean selected) {
		super(source);
		
		this.index = index;
		this.item = item;
		this.selected = selected;
	}

	/**
	 * Gets the index of the affected item.
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the affected item.
	 * @return the item
	 */
	public E getItem() {
		return item;
	}

	/**
	 * Indicates is the item was selected or deselected.
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}
	
	

}
