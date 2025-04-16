package de.enflexit.expression.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceHelper;
import de.enflexit.expression.ExpressionType;

/**
 * This class implements the lower right part of the expression editor,
 * which contains the expression library.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorLibraryPanel extends JPanel implements TreeSelectionListener, ListSelectionListener{

	private static final long serialVersionUID = -2285298251930084002L;
	
	public static final String EXPRESSION_INSERTED = "ExpressionInserted";
	
	private JSplitPane jSplitPaneMain;
	private JSplitPane jSplitPaneRight;

	private JPanel jPanelLeftMainCategories;
	private JLabel jLabelMainCategories;
	private JScrollPane jScrollPaneMainCategories;
	private JTree jTreeMainCategories;

	private JPanel jPanelCenterSubCategories;
	private JLabel jLabelSubCategories;
	private JScrollPane jScrollPaneSubCategories;
	private DefaultListModel<String> categoriesListModel;
	private JList<String> jListSubCategories;
	
	private JPanel jPanelRightExpressions;
	private JLabel jLabelExpressions;
	private JScrollPane jScrollPaneExpressions;
	private DefaultListModel<String> expressionsListModel;
	private JList<String> jListExpressions;
	
	private TreeMap<String, ArrayList<String>> currentOptions;
	private HashMap<ExpressionEditorTreeNode, ExpressionType> nodeToExpressionTypeHashMap;
	
	private ExpressionContext expressionContext;
	private ExpressionServiceFilter expressionServiceFilter;

	
	/**
	 * Instantiates a new expression editor library panel.
	 */
	public ExpressionEditorLibraryPanel() {
		this.initialize();
	}

	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		
		GridBagLayout gbl_this = new GridBagLayout();
		gbl_this.columnWidths = new int[]{0, 0};
		gbl_this.rowHeights = new int[]{0, 0};
		gbl_this.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_this.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gbl_this);
		
		GridBagConstraints gbc_jSplitPaneMain = new GridBagConstraints();
		gbc_jSplitPaneMain.fill = GridBagConstraints.BOTH;
		gbc_jSplitPaneMain.gridx = 0;
		gbc_jSplitPaneMain.gridy = 0;
		this.add(this.getJSplitPaneMain(), gbc_jSplitPaneMain);
		
	}
	
	private JSplitPane getJSplitPaneMain() {
		if (jSplitPaneMain == null) {
			jSplitPaneMain = new JSplitPane();
			jSplitPaneMain.setLeftComponent(getJPanelLeftMainCategories());
			jSplitPaneMain.setRightComponent(getJSplitPaneRight());
			jSplitPaneMain.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneMain.setOneTouchExpandable(false);
			jSplitPaneMain.setDividerLocation(0.34);
			jSplitPaneMain.setResizeWeight(0.34);
			jSplitPaneMain.setDividerSize(2);
		}
		return jSplitPaneMain;
	}
	private JSplitPane getJSplitPaneRight() {
		if (jSplitPaneRight == null) {
			jSplitPaneRight = new JSplitPane();
			jSplitPaneRight.setLeftComponent(getJPanelCenterSubCategories());
			jSplitPaneRight.setBorder(BorderFactory.createEmptyBorder());
			jSplitPaneRight.setRightComponent(getJPanelRightExpressions());
			jSplitPaneRight.setOneTouchExpandable(false);
			jSplitPaneRight.setDividerLocation(0.3);
			jSplitPaneRight.setResizeWeight(0.3);
			jSplitPaneRight.setDividerSize(2);

		}
		return jSplitPaneRight;
	}
	private JPanel getJPanelLeftMainCategories() {
		if (jPanelLeftMainCategories == null) {
			jPanelLeftMainCategories = new JPanel();
			jPanelLeftMainCategories.setBorder(new EmptyBorder(0, 0, 0, 5));
			
			GridBagLayout gbl_jPanelLeftMainCategories = new GridBagLayout();
			gbl_jPanelLeftMainCategories.columnWidths = new int[]{0, 0};
			gbl_jPanelLeftMainCategories.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelLeftMainCategories.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelLeftMainCategories.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelLeftMainCategories.setLayout(gbl_jPanelLeftMainCategories);
			jPanelLeftMainCategories.setPreferredSize(new Dimension(200,200));
			
			GridBagConstraints gbc_jLabelMainCategories = new GridBagConstraints();
			gbc_jLabelMainCategories.anchor = GridBagConstraints.WEST;
			gbc_jLabelMainCategories.gridx = 0;
			gbc_jLabelMainCategories.gridy = 0;
			jPanelLeftMainCategories.add(getJLabelMainCategories(), gbc_jLabelMainCategories);
			GridBagConstraints gbc_jScrollPaneMainCategories = new GridBagConstraints();
			gbc_jScrollPaneMainCategories.insets = new Insets(5, 0, 0, 0);
			gbc_jScrollPaneMainCategories.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneMainCategories.gridx = 0;
			gbc_jScrollPaneMainCategories.gridy = 1;
			jPanelLeftMainCategories.add(getJScrollPaneMainCategories(), gbc_jScrollPaneMainCategories);
			
		}
		return jPanelLeftMainCategories;
	}
	private JPanel getJPanelCenterSubCategories() {
		if (jPanelCenterSubCategories == null) {
			jPanelCenterSubCategories = new JPanel();
			jPanelCenterSubCategories.setBorder(new EmptyBorder(0, 5, 0, 5));
			GridBagLayout gbl_jPanelCenterSubCategories = new GridBagLayout();
			gbl_jPanelCenterSubCategories.columnWidths = new int[]{0, 0};
			gbl_jPanelCenterSubCategories.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelCenterSubCategories.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelCenterSubCategories.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelCenterSubCategories.setLayout(gbl_jPanelCenterSubCategories);
			jPanelCenterSubCategories.setPreferredSize(new Dimension(200,200));
			GridBagConstraints gbc_jLabelSubCategories = new GridBagConstraints();
			gbc_jLabelSubCategories.anchor = GridBagConstraints.WEST;
			gbc_jLabelSubCategories.gridx = 0;
			gbc_jLabelSubCategories.gridy = 0;
			jPanelCenterSubCategories.add(getJLabelSubCategories(), gbc_jLabelSubCategories);
			GridBagConstraints gbc_jScrollPaneSubCategories = new GridBagConstraints();
			gbc_jScrollPaneSubCategories.insets = new Insets(5, 0, 0, 0);
			gbc_jScrollPaneSubCategories.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneSubCategories.gridx = 0;
			gbc_jScrollPaneSubCategories.gridy = 1;
			jPanelCenterSubCategories.add(getJScrollPaneSubCategories(), gbc_jScrollPaneSubCategories);
		}
		return jPanelCenterSubCategories;
	}
	private JPanel getJPanelRightExpressions() {
		if (jPanelRightExpressions == null) {
			jPanelRightExpressions = new JPanel();
			jPanelRightExpressions.setBorder(new EmptyBorder(0, 5, 0, 0));
			GridBagLayout gbl_jPanelRightExpressions = new GridBagLayout();
			gbl_jPanelRightExpressions.columnWidths = new int[]{0, 0};
			gbl_jPanelRightExpressions.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelRightExpressions.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelRightExpressions.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			jPanelRightExpressions.setLayout(gbl_jPanelRightExpressions);
			GridBagConstraints gbc_jLabelExpressions = new GridBagConstraints();
			gbc_jLabelExpressions.anchor = GridBagConstraints.WEST;
			gbc_jLabelExpressions.gridx = 0;
			gbc_jLabelExpressions.gridy = 0;
			jPanelRightExpressions.add(getJLabelExpressions(), gbc_jLabelExpressions);
			GridBagConstraints gbc_jScrollPaneExpressions = new GridBagConstraints();
			gbc_jScrollPaneExpressions.fill = GridBagConstraints.BOTH;
			gbc_jScrollPaneExpressions.insets = new Insets(5, 0, 0, 0);
			gbc_jScrollPaneExpressions.gridx = 0;
			gbc_jScrollPaneExpressions.gridy = 1;
			jPanelRightExpressions.add(getJScrollPaneExpressions(), gbc_jScrollPaneExpressions);
		}
		return jPanelRightExpressions;
	}
	
	
	private JLabel getJLabelMainCategories() {
		if (jLabelMainCategories == null) {
			jLabelMainCategories = new JLabel("Main Categories");
			jLabelMainCategories.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMainCategories;
	}
	private JScrollPane getJScrollPaneMainCategories() {
		if (jScrollPaneMainCategories == null) {
			jScrollPaneMainCategories = new JScrollPane();
			jScrollPaneMainCategories.setViewportView(this.getJTreeMainCategories());
		}
		return jScrollPaneMainCategories;
	}
	private JTree getJTreeMainCategories() {
		if (jTreeMainCategories == null) {
			jTreeMainCategories = new JTree();
			jTreeMainCategories.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTreeMainCategories.setRootVisible(false);
			jTreeMainCategories.addTreeSelectionListener(this);
		}
		return jTreeMainCategories;
	}
	
	
	private JLabel getJLabelSubCategories() {
		if (jLabelSubCategories == null) {
			jLabelSubCategories = new JLabel("Sub Categories");
			jLabelSubCategories.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSubCategories;
	}
	private JScrollPane getJScrollPaneSubCategories() {
		if (jScrollPaneSubCategories == null) {
			jScrollPaneSubCategories = new JScrollPane();
			jScrollPaneSubCategories.setViewportView(this.getJListSubCategories());
		}
		return jScrollPaneSubCategories;
	}
	private JList<String> getJListSubCategories() {
		if (jListSubCategories == null) {
			jListSubCategories = new JList<String>();
			jListSubCategories.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListSubCategories.addListSelectionListener(this);
		}
		return jListSubCategories;
	}
	
	
	private JLabel getJLabelExpressions() {
		if (jLabelExpressions == null) {
			jLabelExpressions = new JLabel("Expressions");
			jLabelExpressions.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpressions;
	}
	private JScrollPane getJScrollPaneExpressions() {
		if (jScrollPaneExpressions == null) {
			jScrollPaneExpressions = new JScrollPane();
			jScrollPaneExpressions.setViewportView(this.getJListExpressions());
		}
		return jScrollPaneExpressions;
	}
	private JList<String> getJListExpressions() {
		if (jListExpressions == null) {
			jListExpressions = new JList<String>();
			jListExpressions.setFont(new Font("Dialog", Font.PLAIN, 12));
			jListExpressions.addListSelectionListener(this);
			jListExpressions.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount()==2) {
						int index = getJListExpressions().locationToIndex(me.getPoint());
						String clickedExpression = getJListExpressions().getModel().getElementAt(index);
						ExpressionEditorLibraryPanel.this.triggerInsert(clickedExpression);
					}
				}
			});
		}
		return jListExpressions;
	}
	
	/**
	 * Gets the expression types for categories.
	 * @return the expression types for categories
	 */
	private HashMap<ExpressionEditorTreeNode, ExpressionType> getNodeToExpressionTypeHashMap() {
		if (nodeToExpressionTypeHashMap==null) {
			nodeToExpressionTypeHashMap = new HashMap<>();
		}
		return nodeToExpressionTypeHashMap;
	}
	/**
	 * Sets the expression context.
	 * @param context the new expression context
	 */
	public void setExpressionContext(ExpressionContext expressionContext) {
		this.expressionContext = expressionContext;
		this.rebuildLibraryTree();
	}
	
	private void rebuildLibraryTree() {
		// --- Re-initiate / reset local instances ------------------ 
		DefaultMutableTreeNode elRootNode = new DefaultMutableTreeNode("Expression Library");
		this.getNodeToExpressionTypeHashMap().clear();
		
		// --- Get available expression services as sorted list -----
		List<ExpressionService> expressionServices = ExpressionServiceHelper.getAvailableExpressionServicesSorted();
		for (int i=0; i<expressionServices.size(); i++) {
			// --- Get sub-node of service --------------------------
			ExpressionService expressionService = expressionServices.get(i);
			
			if (this.expressionServiceFilter==null || this.expressionServiceFilter.matches(expressionService)) {
				ExpressionEditorTreeNode categoryNode = expressionService.getExpressionEditorNode(this.expressionContext);
				if (categoryNode!=null) {
					elRootNode.add(categoryNode);
					this.addAllNodesToNodeToExpressionTypeHashMap(categoryNode, expressionService.getExpressionType());
				}
			}
			
		}
		this.getJTreeMainCategories().setModel(new DefaultTreeModel(elRootNode));
		
		// --- Expand library tree ----------------------------------
		for (int i=0; i<this.getJTreeMainCategories().getRowCount(); i++) {
			this.getJTreeMainCategories().expandRow(i);
		}
	}
	
	
	
	/**
	 * Gets the library filter.
	 * @return the library filter
	 */
	public ExpressionServiceFilter getLibraryFilter() {
		return expressionServiceFilter;
	}

	/**
	 * Sets the expression service filter.
	 * @param expressionServiceFilter the new expression service filter
	 */
	public void setExpressionServiceFilter(ExpressionServiceFilter expressionServiceFilter) {
		this.expressionServiceFilter = expressionServiceFilter;
		this.rebuildLibraryTree();
	}

	/**
	 *
	 * @param parentNode the parent node
	 * @param expressionType the expression type
	 */
	private void addAllNodesToNodeToExpressionTypeHashMap(ExpressionEditorTreeNode parentNode, ExpressionType expressionType) {
		
		// --- Remind node and its expression type ----------------------------
		this.getNodeToExpressionTypeHashMap().put(parentNode, expressionType);
		// --- Check for sub nodes of the current parent node -----------------
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			ExpressionEditorTreeNode subNode = (ExpressionEditorTreeNode) parentNode.getChildAt(i);
			this.addAllNodesToNodeToExpressionTypeHashMap(subNode, expressionType);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		if (lse.getSource()==this.getJListSubCategories()) {
			String category = this.getJListSubCategories().getSelectedValue();
			this.getJListExpressions().setModel(this.createExpressionsListModel(category));
		}
	}
	/**
	 * Creates the expressions list model.
	 *
	 * @param category the category
	 * @return the default list model
	 */
	private DefaultListModel<String> createExpressionsListModel(String category){
		this.expressionsListModel = new DefaultListModel<>();
		if (category!=null) {
			this.expressionsListModel.addAll(this.currentOptions.get(category));
		}
		return expressionsListModel;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		if (tse.getSource()==this.getJTreeMainCategories()) {
			if (this.getJTreeMainCategories().getLastSelectedPathComponent()!=null) {
				ExpressionEditorTreeNode selecteNode = (ExpressionEditorTreeNode) this.getJTreeMainCategories().getLastSelectedPathComponent();
				this.currentOptions = selecteNode.getExpressionTemplates();
				this.getJListSubCategories().setModel(this.createCategoriesListModel());
				this.getJListSubCategories().setSelectedIndex(0);
			}
		}
	}
	/**
	 * Creates the categories list model.
	 * @return the default list model
	 */
	private DefaultListModel<String> createCategoriesListModel(){
		this.categoriesListModel = new DefaultListModel<>();
		if (this.currentOptions!=null) {
			this.categoriesListModel.addAll(this.currentOptions.keySet());
		}
		return this.categoriesListModel;
	}
	

	/**
	 * Triggers a {@link PropertyChangeEvent} to notify that the selected expression should be inserted.
	 * @param libraryExpression the part to insert into the expression string
	 */
	private void triggerInsert(String libraryExpression) {
		
		ExpressionEditorTreeNode categoryNode = (ExpressionEditorTreeNode) this.getJTreeMainCategories().getLastSelectedPathComponent();
		ExpressionType expressionType = this.getNodeToExpressionTypeHashMap().get(categoryNode);
		ExpressionService eService = ExpressionServiceHelper.getExpressionService(expressionType);

		// --- Get insert string from corresponding expression service --------
		String stringToInsert = eService.getInsertString(libraryExpression);
		if (stringToInsert==null) {
			stringToInsert = "[" + expressionType.getTypePrefix() + "!" + libraryExpression + "]";
		}
		this.firePropertyChange(EXPRESSION_INSERTED, null, stringToInsert);
	}
	
}
