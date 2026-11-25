package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.function.Function;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.Position.Bias;

/**
 * A custom {@link JList} that uses radio buttons for element selection.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class RadioButtonList<E> extends JList<E> {

	private static final long serialVersionUID = 7067561316565237634L;
	
	private ArrayList<RadioButtonListSelectionListener<E>> selectionListeners;
	
	// --- Not actually unused, coupled via consumer pattern (see constructor) 
	@SuppressWarnings("unused")
	private ListKeyboardHandler<E> keyboardHandler;
	
	/**
	 * Instantiates a new radio button list with an empty list model.
	 */
	public RadioButtonList() {
		this(new DefaultListModel<E>(), null);
	}
	
	/**
	 * Instantiates a new radio button list with an empty list model, using a custom function to generate the list entries from the items.
	 * @param listEntryStringFunction the list entry string function
	 */
	public RadioButtonList(Function<E, String> listEntryStringFunction) {
		this(new DefaultListModel<E>(), listEntryStringFunction);
	}
	
	/**
	 * Instantiates a new radio button list with the provided list model.
	 * @param listModel the data model
	 */
	public RadioButtonList(ListModel<E> listModel) {
		this(listModel, null);
	}
	
	/**
	 * Instantiates a new radio button list with the provided list model, using a custom function to generate the list entries from the items.
	 * @param listModel the data model
	 * @param listEntryStringFunction the list entry string function
	 */
	public RadioButtonList(ListModel<E> listModel, Function<E, String> listEntryStringFunction) {
		super(listModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new RadioButtonListCellRenderer(listEntryStringFunction));
		this.keyboardHandler = new ListKeyboardHandler<E>(this, this::selectItem);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JList#getNextMatch(java.lang.String, int, javax.swing.text.Position.Bias)
	 */
	@Override
	public int getNextMatch(String prefix, int startIndex, Bias bias) {
		// --- Disable auto search on keyTyped ------------
		return -1;
	}

	/**
	 * Gets the selection listeners.
	 * @return the selection listeners
	 */
	private ArrayList<RadioButtonListSelectionListener<E>> getSelectionListeners() {
		if (selectionListeners==null) {
			selectionListeners = new ArrayList<RadioButtonListSelectionListener<E>>();
		}
		return selectionListeners;
	}
	
	/**
	 * Adds a selection listener.
	 * @param selectionListener the selection listener
	 */
	public void addSelectionListener(RadioButtonListSelectionListener<E> selectionListener) {
		this.getSelectionListeners().add(selectionListener);
	}
	
	/**
	 * Removes a selection listener.
	 * @param selectionListener the selection listener
	 */
	public void removeSelectionListener(RadioButtonListSelectionListener<E> selectionListener) {
		this.getSelectionListeners().remove(selectionListener);
	}
	
	/**
	 * Notifies registered listeners about a selection change.
	 * @param index the index
	 * @param item the item
	 */
	private void fireSelectionChanged(int index, E item) {
		RadioButtonListSelectionEvent<E> selectionEvent = new RadioButtonListSelectionEvent<E>(this, index, item);
		
		for (RadioButtonListSelectionListener<E> listener : this.getSelectionListeners()) {
			listener.selectionChanged(selectionEvent);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#processMouseEvent(java.awt.event.MouseEvent)
	 */
	@Override
	protected void processMouseEvent(MouseEvent me) {
		
		// --- Ignore mouse interaction if list is disabled -------------------
		if (this.isEnabled()==false) {
			me.consume();
			return;
		}
		
		if (me.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(me)) {
			
			// --- Request focus to enable keyboard controls --------
			this.requestFocusInWindow();

			// --- Select the clicked item --------------------------
			int index = locationToIndex(me.getPoint());
			if (index >= 0) {
				
				// --- No action required if already selected due to radio button logic
				if (isSelectedIndex(index)==false) {
					this.setSelectedIndex(index);
					E item = this.getModel().getElementAt(index);
					fireSelectionChanged(index, item);
				}
				me.consume();
                return;
			}
		}
		super.processMouseEvent(me);
	}
	
	/**
	 * Select item.
	 * @param index the index
	 */
	private void selectItem(int index) {

	    if (isSelectedIndex(index)) {
	        return;
	    }
	    setSelectedIndex(index);
	    E item = getModel().getElementAt(index);
	    this.fireSelectionChanged(index, item);
	}
	
	/**
	 * Sets the selected item.
	 * @param item the item to select
	 */
	public void setSelectedItem(E item) {
		
		if (item==null) {
			this.clearSelection();
			return;
		}
		
		// --- Look for the provided item -----------------
		for (int i=0; i<this.getModel().getSize(); i++) {
			if (this.getModel().getElementAt(i) == item) {
				this.setSelectedIndex(i);
				this.fireSelectionChanged(i, item);
				return;
			}
		}
		
		// --- Item not found -> not in the list ----------
		this.clearSelection();
		
	}
	
	/**
	 * A custom {@link ListCellRenderer} based on {@link JRadioButton}s.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class RadioButtonListCellRenderer extends JRadioButton implements ListCellRenderer<E> {

		private static final long serialVersionUID = -4057418406162655654L;
		
		private final Function<E, String> listEntryStringFunction;

		/**
		 * Instantiates a new radio button list cell renderer, using a custom function to generate the list entries from the items (defaults to String.valueOf() if null is passed).
		 * @param labelStringFunction the label string function
		 */
		public RadioButtonListCellRenderer(Function<E, String> labelStringFunction) {
			super();
			this.listEntryStringFunction = labelStringFunction!=null ? labelStringFunction : String::valueOf;
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
			
			this.setText(this.listEntryStringFunction.apply(value));
			this.setSelected(isSelected);

			 if (cellHasFocus==true) {
	            this.setBackground(list.getSelectionBackground());
	            this.setForeground(list.getSelectionForeground());
	        } else {
	        	this.setBackground(list.getBackground());
	        	this.setForeground(list.getForeground());
	        }

			this.setFont(list.getFont());
			this.setOpaque(true);
			
			this.setEnabled(list.isEnabled());

            return this;
		}
		
	}

}
