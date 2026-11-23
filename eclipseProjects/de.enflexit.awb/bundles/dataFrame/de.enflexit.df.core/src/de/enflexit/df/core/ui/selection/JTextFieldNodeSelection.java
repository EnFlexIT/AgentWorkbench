package de.enflexit.df.core.ui.selection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.*;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_DataWorkbook;
import de.enflexit.df.core.ui.DataTreeCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

/**
 * The Class JTextFieldNodeSelection.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JTextFieldNodeSelection extends JTextField {

	private static final long serialVersionUID = -3980525980117609729L;

	private DataController dataController;
	private boolean showInactiveWorkbooks;
	
	private JTree jTreeDataWorkbooks;
	private JWindow popup;
	
	private boolean isAdjusting; // --- avoid filtering while using setText()  ---
	private boolean onlyLeavesSelectable = true;

	
	/**
	 * Instantiates a new JTextFieldNodeSelection.
	 * (Just for the use of the WindowBuilder)
	 */
	@Deprecated
	public JTextFieldNodeSelection() {
		this(null);
	}
	/**
	 * Instantiates a new JTextFieldNodeSelection.
	 * @param dataController the data controller
	 */
	public JTextFieldNodeSelection(DataController dataController) {
		this(dataController, true);
	}
	/**
	 * Instantiates a new JTextFieldNodeSelection.
	 *
	 * @param dataController the data controller
	 * @param showInactiveWorkbooks the indicator to show inactive workbooks or not 
	 */
	public JTextFieldNodeSelection(DataController dataController, boolean showInactiveWorkbooks) {
		super();
		this.dataController = dataController;
		this.showInactiveWorkbooks = showInactiveWorkbooks;
		
		this.applyFilter("");

		// --- Focus Listener ---------------------------------------
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				showPopup();
			}
			@Override
			public void focusLost(FocusEvent e) {
				hidePopup();
			}
		});

		// --- Document Listener ------------------------------------
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}
		});

		this.installKeyBindings();
	}

	/**
	 * Returns the JTree data workbooks.
	 * @return the JTree data workbooks
	 */
	private JTree getJTreeDataWorkbooks() {
		if (jTreeDataWorkbooks == null) {
			jTreeDataWorkbooks = new JTree();
			jTreeDataWorkbooks.setRootVisible(false);
			jTreeDataWorkbooks.setShowsRootHandles(true);
			jTreeDataWorkbooks.setFocusable(false); 
			jTreeDataWorkbooks.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTreeDataWorkbooks.setCellRenderer(new DataTreeCellRenderer());
			jTreeDataWorkbooks.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					TreePath path = jTreeDataWorkbooks.getPathForLocation(me.getX(), me.getY());
					if (path!=null) {
						choose(path);
					}
				}
			});
		}
		return jTreeDataWorkbooks;
	}
	
	/**
	 * Returns the selected tree node.
	 * @return the selected tree node
	 */
	public DefaultMutableTreeNode getSelectedTreeNode() {
		// --- We need the TreeNode from the DataController !! ------
		TreePath selection = this.getJTreeDataWorkbooks().getSelectionPath();
		if (selection!=null) {
			DefaultMutableTreeNode treeNodeLocal = (DefaultMutableTreeNode) this.getJTreeDataWorkbooks().getSelectionPath().getLastPathComponent();
			if (treeNodeLocal!=null) {
				return this.dataController.getDataTreeModel().getTreeNodeOfUserObject(treeNodeLocal.getUserObject());
			}
		}
		return null;
	}
	/**
	 * Sets the selected tree node.
	 * @param treeNodeSelected the new selected tree node
	 */
	public void setSelectedTreeNode(DefaultMutableTreeNode treeNodeSelected) {
		String caption = null;
		if (treeNodeSelected!=null) {
			caption = String.valueOf(treeNodeSelected.getUserObject());
		}
		this.setText(caption);
	}
	
	/**
	 * Sets the only leaves selectable.
	 * @param b the new only leaves selectable
	 */
	public void setOnlyLeavesSelectable(boolean b) {
		this.onlyLeavesSelectable = b;
	}

	// ------------------------------------------------------------------
	// --- Popup --------------------------------------------------------
	// ------------------------------------------------------------------ 
	
	/**
	 * Show popup.
	 */
	private void showPopup() {
		
		if (!isShowing()) return;
		if (popup == null) this.createPopup();
		
		Point screenLocation = this.getLocationOnScreen();
		popup.setBounds(screenLocation.x, screenLocation.y + this.getHeight(), Math.max(this.getWidth(), 200), 250);
		popup.setVisible(true);
	}
	/**
	 * Hides the popup.
	 */
	private void hidePopup() {
		if (popup!=null) {
			popup.setVisible(false);
			this.postActionEvent();
		}
	}
	/**
	 * Checks if the popup is visible.
	 * @return true, if is popup visible
	 */
	private boolean isPopupVisible() {
		return popup!=null && popup.isVisible();
	}
	/**
	 * Creates the popup.
	 */
	private void createPopup() {
		Window owner = SwingUtilities.getWindowAncestor(this);
		popup = new JWindow(owner);
		popup.setFocusableWindowState(false); 
		JScrollPane scroll = new JScrollPane(this.getJTreeDataWorkbooks());
		scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		popup.getContentPane().add(scroll);

		if (owner != null) {
			owner.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentMoved(ComponentEvent e) {
					hidePopup();
				}

				@Override
				public void componentResized(ComponentEvent e) {
					hidePopup();
				}
			});
		}
	}

	// ------------------------------------------------------------------
	// --- Filter according to the text --------------------------------- 
	// ------------------------------------------------------------------
	/**
	 * Text changed.
	 */
	private void textChanged() {
		
		if (this.isAdjusting) return;
		this.applyFilter(getText());
		if (hasFocus() && !isPopupVisible()) {
			this.showPopup();
		}
	}
	
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
		if (this.dataController!=null) {
			this.applyFilter("");
		}
	}
	/**
	 * Returns the original root node.
	 * @return the original root node
	 */
	private DefaultMutableTreeNode getOriginalRootNode() {
		return this.dataController!=null ? this.dataController.getDataTreeModel().getRootNode() : null;
	}
	
	/**
	 * Apply filter.
	 * @param text the text
	 */
	private void applyFilter(String text) {
		
		String f = text.trim().toLowerCase();
		DefaultMutableTreeNode filtered = this.filter(this.getOriginalRootNode(), f);
		if (filtered == null) {
			DefaultMutableTreeNode rootNode = this.getOriginalRootNode();
			if (rootNode!=null) {
				filtered = new DefaultMutableTreeNode(rootNode.getUserObject());
			} else {
				filtered = new DefaultMutableTreeNode();
			}
		}
		this.getJTreeDataWorkbooks().setModel(new DefaultTreeModel(filtered));

		if (f.isEmpty()==false) { 
			for (int i = 0; i < this.getJTreeDataWorkbooks().getRowCount(); i++) {
				this.getJTreeDataWorkbooks().expandRow(i);
			}
			this.selectFirstMatch(f);
		}
	}

	/**
	 * Provides a filtered copy: A node is retained if it matches the criteria (in 
	 * which case all its children are also included) or if at least one of its descendants matches.
	 *
	 * @param node the node
	 * @param filterPhrase the filter phrase
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode filter(DefaultMutableTreeNode node, String filterPhrase) {
		
		if (node==null) return null;

		// --- Show inactive data workbooks? ------------------------
		boolean skipThisClosedDataWorkbookNode = (this.showInactiveWorkbooks==false && node.getUserObject() instanceof DTNO_DataWorkbook dwDTNO && dwDTNO.getDataWorkbook().isOpen()==false);
		if (skipThisClosedDataWorkbookNode==true) return null;
		
		boolean isRootNode = (node==this.getOriginalRootNode());
		if (isRootNode==false && (filterPhrase.isEmpty() || this.matches(node, filterPhrase))) {
			return this.deepCopy(node);
		}

		DefaultMutableTreeNode copy = null;
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode child = this.filter((DefaultMutableTreeNode) node.getChildAt(i), filterPhrase);
			if (child != null) {
				if (copy == null) {
					copy = new DefaultMutableTreeNode(node.getUserObject());
				}
				copy.add(child);
			}
		}
		return copy;
	}

	/**
	 * Checks if the specified node matches the search phrase.
	 *
	 * @param node the node
	 * @param filterPhrase the filter phrase
	 * @return true, if the specified successful
	 */
	private boolean matches(DefaultMutableTreeNode node, String filterPhrase) {
		return String.valueOf(node.getUserObject()).toLowerCase().contains(filterPhrase);
	}
	/**
	 * Deep copy.
	 *
	 * @param node the node
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode deepCopy(DefaultMutableTreeNode node) {
		
		DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.getUserObject());
		for (int i = 0; i < node.getChildCount(); i++) {
			copy.add(this.deepCopy((DefaultMutableTreeNode) node.getChildAt(i)));
		}
		return copy;
	}
	/**
	 * Select first match.
	 * @param f the f
	 */
	private void selectFirstMatch(String f) {
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getJTreeDataWorkbooks().getModel().getRoot();
		Enumeration<TreeNode> e = root.preorderEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
			if (n != root && matches(n, f) && (!onlyLeavesSelectable || n.isLeaf())) {
				TreePath path = new TreePath(n.getPath());
				this.getJTreeDataWorkbooks().setSelectionPath(path);
				this.getJTreeDataWorkbooks().scrollPathToVisible(path);
				return;
			}
		}
	}

	/**
	 * Choose.
	 * @param path the path
	 */
	private void choose(TreePath path) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (onlyLeavesSelectable && !node.isLeaf()) return; 

		isAdjusting = true;
		this.setText(String.valueOf(node.getUserObject()));
		isAdjusting = false;
		this.hidePopup();
		this.postActionEvent();
	}

	
	// --------------------------------------------------------------
	// --- Keyboard-Reactions ---------------------------------------
	// --------------------------------------------------------------
	/**
	 * Install key bindings.
	 */
	private void installKeyBindings() {
		
		InputMap im = getInputMap(WHEN_FOCUSED);
		ActionMap am = getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "treeDown");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "treeUp");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "treeChoose");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "treeHide");

		am.put("treeDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPopupVisible()) {
					showPopup();
					return;
				}
				moveSelection(+1);
			}
		});
		am.put("treeUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveSelection(-1);
			}
		});
		am.put("treeChoose", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath selection = getJTreeDataWorkbooks().getSelectionPath();
				if (isPopupVisible()==true && selection!=null) {
					choose(selection);
				}
			}
		});
		am.put("treeHide", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hidePopup();
			}
		});
	}

	/**
	 * Move selection.
	 * @param delta the delta
	 */
	private void moveSelection(int delta) {
		int rows = this.getJTreeDataWorkbooks().getRowCount();
		if (rows == 0) return;
		int row = this.getJTreeDataWorkbooks().getLeadSelectionRow();
		row = Math.max(0, Math.min(rows - 1, row + delta));
		this.getJTreeDataWorkbooks().setSelectionRow(row);
		this.getJTreeDataWorkbooks().scrollRowToVisible(row);
	}

}
