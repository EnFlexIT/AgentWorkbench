package de.enflexit.common.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;

/**
 * Unified keyboard handling for {@link CheckBoxList} and {@link RadioButtonList}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <E> the element type
 */
public class ListKeyboardHandler<E> {

	private final JList<E> list;
	private final Consumer<Integer> toggleAction;

	/**
	 * Instantiates a new list keyboard handler.
	 * @param list the list
	 * @param toggleAction the toggle action
	 */
	public ListKeyboardHandler(JList<E> list, Consumer<Integer> toggleAction) {
		this.list = list;
		this.toggleAction = toggleAction;
		this.registerKeyboardActions();
	}

	/**
	 * Register keyboard actions.
	 */
	private void registerKeyboardActions() {

		InputMap inputMap = list.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = list.getActionMap();

		// --- Map the actions to the keys ---------------- 
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "toggleSelection");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "focusPrevious");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "focusNext");
		
		list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("SPACE"), "space");
		
		// --- Define the actual actions ------------------

		// --- Toggle the focused item --------------------
		actionMap.put("toggleSelection", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				ListKeyboardHandler.this.toggleFocusedItem();
			}
		});

		// --- Move the focus one row up ------------------
		actionMap.put("focusPrevious", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				moveFocus(-1);
			}
		});

		// --- Move the focus one row down ----------------
		actionMap.put("focusNext", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				moveFocus(1);
			}
		});
	}
	
	/**
	 * Toggles the selection state of the list item that is currently  focused.
	 */
	private void toggleFocusedItem() {
		if (this.list.isEnabled()==false) return;

		// --- Invoke the toggle action for the focused element -----  
		int index = list.getLeadSelectionIndex();
		
		if (isValidIndex(index)) {
			toggleAction.accept(index);
		}
	}

	/**
	 * Move focus.
	 * @param direction the direction
	 */
	private void moveFocus(int direction) {

		if (this.list.isEnabled() == false|| this.list.getModel().getSize() == 0) {
			return;
		}

		// --- Move the row focus without changing the selection state -------- 
		int currentIndex = this.list.getLeadSelectionIndex();
		int nextIndex;
		if (currentIndex < 0) {
			nextIndex = direction > 0 ? 0 : this.list.getModel().getSize() - 1;
		} else {
			nextIndex = Math.max(0, Math.min(this.list.getModel().getSize() - 1, currentIndex + direction));
		}
		((DefaultListSelectionModel) list.getSelectionModel()).moveLeadSelectionIndex(nextIndex);

		this.list.ensureIndexIsVisible(nextIndex);
		this.list.repaint();
	}

	/**
	 * Checks if the provided index is valid.
	 * @param index the index
	 * @return true, if is valid index
	 */
	private boolean isValidIndex(int index) {
		return index >= 0 && index < this.list.getModel().getSize();
	}
}

