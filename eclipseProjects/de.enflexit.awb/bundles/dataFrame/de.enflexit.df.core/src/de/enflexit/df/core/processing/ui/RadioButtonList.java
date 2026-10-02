package de.enflexit.df.core.processing.ui;

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

/**
 * A custom {@link JList} that uses radio buttons for element selection.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class RadioButtonList<E> extends JList<E> {

	private static final long serialVersionUID = 7067561316565237634L;
	
	private ArrayList<RadioButtonListSelectionListener<E>> selectionListeners;
	
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
	protected void processMouseEvent(MouseEvent e) {
		
		if (e.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
			int index = locationToIndex(e.getPoint());
			
			if (index >= 0) {
				
				// --- No action required if already selected due to radio button logic
				if (isSelectedIndex(index)==false) {
					this.setSelectedIndex(index);
					E item = this.getModel().getElementAt(index);
					fireSelectionChanged(index, item);
				}
				e.consume();
                return;
			}
		}
		super.processMouseEvent(e);
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

			this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
			this.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

			this.setFont(list.getFont());
			this.setOpaque(true);
			
			this.setEnabled(list.isEnabled());

            return this;
		}
		
	}

}
