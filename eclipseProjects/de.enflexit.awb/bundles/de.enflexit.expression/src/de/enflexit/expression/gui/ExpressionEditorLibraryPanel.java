package de.enflexit.expression.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.common.ServiceFinder;
import de.enflexit.expression.ExpressionService;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class implements the lower right part of the expression editor,
 * which contains the expression library.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionEditorLibraryPanel extends JPanel implements TreeSelectionListener, ListSelectionListener{

	private static final long serialVersionUID = -2285298251930084002L;
	
	private JLabel jLabelExpressionLybrary;
	private JScrollPane jScrollPaneMainCategories;
	private JLabel jLabelCategories;
	private JScrollPane jScrollPaneSubCategories;
	private JLabel jLabelExpressions;
	private JScrollPane jScrollPaneTemplates;
	private JTree jTreeExpressionTypes;
	private JList<String> jListCategories;
	private JList<String> jListExpressions;
	
	/**
	 * Instantiates a new expression editor library panel.
	 */
	public ExpressionEditorLibraryPanel() {
		super();
		this.initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		GridBagLayout gbl_this = new GridBagLayout();
		gbl_this.columnWidths = new int[]{0, 0, 0, 0};
		gbl_this.rowHeights = new int[]{0, 0, 0};
		gbl_this.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_this.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		this.setLayout(gbl_this);
		GridBagConstraints gbc_jLabelExpressionLybrary = new GridBagConstraints();
		gbc_jLabelExpressionLybrary.anchor = GridBagConstraints.WEST;
		gbc_jLabelExpressionLybrary.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelExpressionLybrary.gridx = 0;
		gbc_jLabelExpressionLybrary.gridy = 0;
		this.add(getJLabelExpressionLybrary(), gbc_jLabelExpressionLybrary);
		GridBagConstraints gbc_jLabelCategories = new GridBagConstraints();
		gbc_jLabelCategories.anchor = GridBagConstraints.WEST;
		gbc_jLabelCategories.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelCategories.gridx = 1;
		gbc_jLabelCategories.gridy = 0;
		this.add(getJLabelCategories(), gbc_jLabelCategories);
		GridBagConstraints gbc_jLabelExpressions = new GridBagConstraints();
		gbc_jLabelExpressions.anchor = GridBagConstraints.WEST;
		gbc_jLabelExpressions.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelExpressions.gridx = 2;
		gbc_jLabelExpressions.gridy = 0;
		this.add(getJLabelExpressions(), gbc_jLabelExpressions);
		GridBagConstraints gbc_jScrollPaneMainCategories = new GridBagConstraints();
		gbc_jScrollPaneMainCategories.insets = new Insets(0, 0, 0, 5);
		gbc_jScrollPaneMainCategories.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneMainCategories.gridx = 0;
		gbc_jScrollPaneMainCategories.gridy = 1;
		this.add(getJScrollPaneMainCategories(), gbc_jScrollPaneMainCategories);
		GridBagConstraints gbc_jScrollPaneSubCategories = new GridBagConstraints();
		gbc_jScrollPaneSubCategories.insets = new Insets(0, 0, 0, 5);
		gbc_jScrollPaneSubCategories.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneSubCategories.gridx = 1;
		gbc_jScrollPaneSubCategories.gridy = 1;
		this.add(getJScrollPaneSubCategories(), gbc_jScrollPaneSubCategories);
		GridBagConstraints gbc_jScrollPaneTemplates = new GridBagConstraints();
		gbc_jScrollPaneTemplates.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTemplates.gridx = 2;
		gbc_jScrollPaneTemplates.gridy = 1;
		this.add(getJScrollPaneTemplates(), gbc_jScrollPaneTemplates);
	}
	private JLabel getJLabelExpressionLybrary() {
		if (jLabelExpressionLybrary == null) {
			jLabelExpressionLybrary = new JLabel("Expression Library");
			jLabelExpressionLybrary.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpressionLybrary;
	}
	private JScrollPane getJScrollPaneMainCategories() {
		if (jScrollPaneMainCategories == null) {
			jScrollPaneMainCategories = new JScrollPane();
			jScrollPaneMainCategories.setViewportView(getJTreeExpressionTypes());
		}
		return jScrollPaneMainCategories;
	}
	private JLabel getJLabelCategories() {
		if (jLabelCategories == null) {
			jLabelCategories = new JLabel("Categories");
			jLabelCategories.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelCategories;
	}
	private JScrollPane getJScrollPaneSubCategories() {
		if (jScrollPaneSubCategories == null) {
			jScrollPaneSubCategories = new JScrollPane();
			jScrollPaneSubCategories.setViewportView(getJListCategories());
		}
		return jScrollPaneSubCategories;
	}
	private JLabel getJLabelExpressions() {
		if (jLabelExpressions == null) {
			jLabelExpressions = new JLabel("Expressions");
			jLabelExpressions.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelExpressions;
	}
	private JScrollPane getJScrollPaneTemplates() {
		if (jScrollPaneTemplates == null) {
			jScrollPaneTemplates = new JScrollPane();
			jScrollPaneTemplates.setViewportView(getJListExpressions());
		}
		return jScrollPaneTemplates;
	}
	private JTree getJTreeExpressionTypes() {
		if (jTreeExpressionTypes == null) {
			jTreeExpressionTypes = new JTree();
			jTreeExpressionTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTreeExpressionTypes.setRootVisible(false);
			jTreeExpressionTypes.setVisibleRowCount(8);
			jTreeExpressionTypes.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Expressions") {
					private static final long serialVersionUID = -8848227480569734907L;

					{
						DefaultMutableTreeNode node_1;
						node_1 = new DefaultMutableTreeNode("Math");
							node_1.add(new DefaultMutableTreeNode("Functions"));
							node_1.add(new DefaultMutableTreeNode("Constants"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("EOM");
							node_1.add(new DefaultMutableTreeNode("Models\t\t"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("Database");
							node_1.add(new DefaultMutableTreeNode("Tables\t"));
						add(node_1);
					}
				}
			));
			jTreeExpressionTypes.addTreeSelectionListener(this);
		}
		return jTreeExpressionTypes;
	}
	private JList<String> getJListCategories() {
		if (jListCategories == null) {
			jListCategories = new JList<String>();
			jListCategories.addListSelectionListener(this);
		}
		return jListCategories;
	}
	private JList<String> getJListExpressions() {
		if (jListExpressions == null) {
			jListExpressions = new JList<String>();
			jListExpressions.addListSelectionListener(this);
		}
		return jListExpressions;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Builds the library tree.
	 * @return the default mutable tree node
	 */
	private DefaultMutableTreeNode buildLibraryTree() {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Expression Library");
		
		// --- Get available expression services ----------
		ServiceFinder.findServices(ExpressionService.class);
		
		return rootNode;
	}

}
