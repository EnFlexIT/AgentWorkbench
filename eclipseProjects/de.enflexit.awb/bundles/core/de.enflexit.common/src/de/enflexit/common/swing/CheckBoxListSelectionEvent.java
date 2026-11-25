package de.enflexit.common.swing;

import java.util.EventObject;
import java.util.List;

/**
 * This event indicates a selection change in a {@link CheckBoxList}. It indicates
 * which list item was selected, and if it was selected or deselected.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class CheckBoxListSelectionEvent<E> extends EventObject {

	private static final long serialVersionUID = -3377265581276658807L;
	
	private CheckBoxList<E> sourceList;
	
	private final int index;
	private final E item;
	private final boolean selected;
	private final boolean isBulkChange;
	

	/**
	 * Instantiates a new check box list selection event.
	 * @param source the source
	 * @param index the index
	 * @param item the item
	 * @param selected the selected
	 * @param isBulkChange the is bulk change
	 */
	public CheckBoxListSelectionEvent(CheckBoxList<E> source, int index, E item, boolean selected, boolean isBulkChange) {
		super(source);
		this.sourceList = source;
		this.index = index;
		this.item = item;
		this.selected = selected;
		this.isBulkChange = isBulkChange;
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
	
	/**
	 * Checks if is bulk change.
	 * @return true, if is bulk change
	 */
	public boolean isBulkChange() {
		return isBulkChange;
	}
	
	/**
	 * Gets the selected items.
	 * @return the selected items
	 */
	public List<E> getSelectedItems() {
		return this.sourceList.getSelectedValuesList();
	}

}
