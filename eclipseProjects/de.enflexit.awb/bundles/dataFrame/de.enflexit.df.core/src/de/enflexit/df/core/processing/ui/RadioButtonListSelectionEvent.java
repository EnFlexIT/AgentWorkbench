package de.enflexit.df.core.processing.ui;

import java.util.EventObject;

/**
 * This event indicates a selection change for a {@link RadioButtonList}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class RadioButtonListSelectionEvent<E> extends EventObject {
	
	private static final long serialVersionUID = 7987762623962566900L;
	
	private final int index;
    private final E item;
	
    /**
     * Instantiates a new radio button list selection event.
     * @param source the source
     * @param index the index
     * @param item the item
     */
    public RadioButtonListSelectionEvent(RadioButtonList<E> source, int index, E item) {
		super(source);
		this.index = index;
		this.item = item;
	}

	/**
	 * Gets the index of the selected item.
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the selected item.
	 * @return the item
	 */
	public E getItem() {
		return item;
	}
    
    
}
