package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 * A custom JList that uses JCheckBoxes for element selection. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class CheckBoxList<E> extends JList<E> {

	private static final long serialVersionUID = 1465821191630421936L;
	
	private ArrayList<CheckBoxListSelectionListener<E>> selectionListeners;
	
	private boolean pauseChangeListeners;
	
	
	/**
	 * Instantiates a new check box list with an empty list model.
	 */
	public CheckBoxList() {
		this(new DefaultListModel<E>(), null);
	}
	
	/**
	 * Instantiates a new check box list with an empty list model, using a custom function to generate the list entries from the items.
	 * @param listEntryStringFunction the list entry string function
	 */
	public CheckBoxList(Function<E, String> listEntryStringFunction) {
		this(new DefaultListModel<E>(), listEntryStringFunction);
	}

	/**
	 * Instantiates a new check box list with the provided list model.
	 * @param listModel the model
	 */
	public CheckBoxList(ListModel<E> listModel) {
		this(listModel, null);
    }
	
	/**
	 * Instantiates a new check box list with the provided list model, using a custom function to generate the list entries from the items.
	 * @param listModel the list model
	 * @param listEntryStringFunction the list entry string function
	 */
	public CheckBoxList(ListModel<E> listModel, Function<E, String> listEntryStringFunction) {
        super(listModel);
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        this.setCellRenderer(new CheckBoxListCellRenderer(listEntryStringFunction));
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#processMouseEvent(java.awt.event.MouseEvent)
	 */
	@Override
    public void processMouseEvent(MouseEvent me) {
		
		if (me.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(me)) {

			int index = this.locationToIndex(me.getPoint());

			if (index >= 0) {

				// --- Check if the click was actually inside the cell --------
				Rectangle bounds = this.getCellBounds(index, index);

				if (bounds != null && bounds.contains(me.getPoint())) {

					// --- Handle the first click only (ignore accidental double clicks
					if (me.getClickCount() == 1) {

						boolean selected = !this.isSelectedIndex(index);

						if (selected) {
							this.addSelectionInterval(index, index);
						} else {
							this.removeSelectionInterval(index, index);
						}

						E item = this.getModel().getElementAt(index);

						this.fireSelectionChangeEvent(index, item, selected);
					}
				}
			}

			// --- Prevent standard click handling for all left clicks --------
			me.consume();
			return;
		}

		super.processMouseEvent(me);
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#processMouseMotionEvent(java.awt.event.MouseEvent)
	 */
	@Override
	protected void processMouseMotionEvent(MouseEvent me) {
		// --- Catch drag events to prevent unwanted movements from affecting the selection behaviour. 
		if (me.getID() == MouseEvent.MOUSE_DRAGGED) {
			me.consume();
	        return;
	    }
		super.processMouseMotionEvent(me);
	}
	
	/**
	 * Gets the selection listeners.
	 * @return the selection listeners
	 */
	private ArrayList<CheckBoxListSelectionListener<E>> getSelectionListeners() {
		if (selectionListeners==null) {
			selectionListeners = new ArrayList<CheckBoxListSelectionListener<E>>();
		}
		return selectionListeners;
	}

	/**
	 * Adds a check box list selection listener.
	 * @param selectionListener the selection listener
	 */
	public void addCheckBoxListSelectionListener(CheckBoxListSelectionListener<E> selectionListener) {
		this.getSelectionListeners().add(selectionListener);
	}

	/**
	 * Removes a check box list selection listener.
	 * @param selectionListener the selection listener
	 */
	public void removeCheckBoxListSelectionListener(CheckBoxListSelectionListener<E> selectionListener) {
		this.getSelectionListeners().remove(selectionListener);
	}

	/**
	 * Checks if is pause change listeners.
	 * @return true, if is pause change listeners
	 */
	public boolean isPauseChangeListeners() {
		return pauseChangeListeners;
	}

	/**
	 * Sets the pause change listeners.
	 * @param pauseChangeListeners the new pause change listeners
	 */
	public void setPauseChangeListeners(boolean pauseChangeListeners) {
		this.pauseChangeListeners = pauseChangeListeners;
	}

	/**
	 * Notifies all listeners about a selection change.
	 * @param index the index
	 * @param item the item
	 * @param selected the selected
	 */
	private void fireSelectionChangeEvent(int index, E item, boolean selected) {
		if (this.isPauseChangeListeners()==false) {
			CheckBoxListSelectionEvent<E> cblse = new CheckBoxListSelectionEvent<E>(this, index, item, selected);
			for (CheckBoxListSelectionListener<E> listener : this.getSelectionListeners()) {
				listener.selectionChanged(cblse);;
			}
		}
	}
	
	/**
	 * Sets the selected items.
	 * @param items the items to select
	 */
	public void setSelectedItems(List<E> items) {
		this.getSelectionModel().clearSelection();
		
		for (int i=0; i<this.getModel().getSize(); i++) {
			if (items.contains(this.getModel().getElementAt(i))) {
				this.getSelectionModel().addSelectionInterval(i, i);
			}
		}
	}
	

	/**
	 * The Class CheckBoxListCellRenderer.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<E> {
	
		private static final long serialVersionUID = 1L;
		
		private final Function<E, String> listEntryStringFunction;
		
		/**
		 * Instantiates a new check box list cell renderer, using a custom string representation defined by textFunction.
		 * @param listEntryStringFunction the text function
		 */
		public CheckBoxListCellRenderer(Function<E, String> listEntryStringFunction) {
			super();
			this.listEntryStringFunction = listEntryStringFunction!=null ? listEntryStringFunction : String::valueOf;
		}


		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		
		    this.setText(this.listEntryStringFunction.apply(value));
		    this.setSelected(isSelected);
		
		    this.setBackground(list.getBackground());
		    this.setForeground(list.getForeground());
		
		    this.setFont(list.getFont());
		    this.setOpaque(true);
		    
		    this.setEnabled(list.isEnabled());
		
		    return this;
		}
		
	}
	
}
